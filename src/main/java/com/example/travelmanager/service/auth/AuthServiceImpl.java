package com.example.travelmanager.service.auth;

import com.alibaba.fastjson.JSON;
import com.example.travelmanager.config.Constant;
import com.example.travelmanager.config.exception.AuthControllerException;
import com.example.travelmanager.config.exception.ForbiddenException;
import com.example.travelmanager.config.exception.UnauthorizedException;
import com.example.travelmanager.dao.DepartmentDao;
import com.example.travelmanager.dao.UserDao;
import com.example.travelmanager.entity.User;
import com.example.travelmanager.enums.UserRoleEnum;
import com.example.travelmanager.payload.EditUserPaylod;
import com.example.travelmanager.payload.RegisterPayload;
import com.example.travelmanager.payload.ResetPasswordPayload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import response.TokenResponse;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public int authorize(String tokenString) {
        Token token = decryptToken(tokenString);
        Date now = new Date();
        if (token == null || now.after(token.getExpire())) {
            throw new UnauthorizedException();
        }
        return token.getId();
    }

    @Override
    public int authorize(String tokenString, UserRoleEnum... userRoleEnums) {
        Token token = decryptToken(tokenString);
        Date now = new Date();
        if (token == null || now.after(token.getExpire())) {
            throw new UnauthorizedException();
        }
        UserInfo userInfo = token.getUserInfo();
        int roleId = userInfo.getRole();
        for (UserRoleEnum role: userRoleEnums) {
            if (roleId == role.getRoleId()) {
                return token.getId();
            }
        }
        throw new ForbiddenException();
    }

    @Override
    public void register(RegisterPayload registerPayload) {

        if (userDao.findByWorkId(registerPayload.getWorkId()) != null) {
            throw AuthControllerException.WorkIdNotExistException;
        }
        User user = new User();
        user.setName(registerPayload.getName());
        user.setEmail(registerPayload.getEmail());
        user.setTelephone(registerPayload.getTelephone());
        user.setPassword(registerPayload.getPassword());
        user.setWorkId(registerPayload.getWorkId());
        user.setRole(UserRoleEnum.Employee.getRoleId());
        user.setStatus(false);
        userDao.save(user);
    }

    @Override
    public TokenResponse generateTokenResponse(User user) {
        UserInfo userInfo = new UserInfo(user);
        Integer departmetnId = user.getDepartmentId();
        userInfo.setDepartmentName("未知部门");

        if (departmetnId != null) {
            var query = departmentDao.findById(user.getDepartmentId());
            if (! query.isEmpty()) {
                userInfo.setDepartmentName(query.get().getName());
            }
        }

        Token token = new Token();
        token.setId(user.getId());
        token.setUserInfo(userInfo);
        
        Date expire = new Date(new Date().getTime() + Constant.ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
        token.setExpire(expire);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setExpire(expire);
        tokenResponse.setToken(generateToken(token));
        tokenResponse.setUserInfo(userInfo);

        return tokenResponse;
    }

    @Override
    public void resetPassword(int uid, ResetPasswordPayload resetPasswordPayload){
        User user = userDao.findById(uid).get();
        if (user.validPassword(resetPasswordPayload.getOldPassword())) {
            user.setPassword(resetPasswordPayload.getNewPassword());
        }
        else {
            throw AuthControllerException.OldPasswordErrorException;
        }
        userDao.save(user);
    }

    @Override
    public void editUser(int uid, EditUserPaylod editUserPaylod) {
        User user = userDao.findById(uid).get();
        String email = editUserPaylod.getEmail();
        String phone = editUserPaylod.getTelephone();
        String name = editUserPaylod.getName();

        if (name != null && !(email.isEmpty() || email.isBlank())){
            user.setEmail(email);
        }
        if (phone != null && !(phone.isEmpty() || phone.isBlank())){
            user.setTelephone(phone);
        }
        if (name != null && !(name.isEmpty() || name.isBlank())){
            user.setName(name);
        }
        userDao.save(user);
    }

    private static Token decryptToken(String tokenStr) {
        String text = decrypt(tokenStr);
        if (text == null) {
            return null;
        }
        return JSON.parseObject(text, Token.class);
    }

    private static String generateToken(Token token){
        String json = JSON.toJSONString(token);
        return encrypt(json);
    }

    private static String encrypt(String text) {
        try{
            Base64.Encoder encoder = Base64.getEncoder();
            Key aesKey = new SecretKeySpec(Constant.SIGNING_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return encoder.encodeToString(encrypted);
        }
        catch (Exception e){
            return null;
        }

    }

    private static String decrypt(String encodeText){
        try{
            Base64.Decoder decoder = Base64.getDecoder();
            Key aesKey = new SecretKeySpec(Constant.SIGNING_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            return new String(cipher.doFinal(decoder.decode(encodeText)));
        }
        catch (Exception e){
            return null;
        }
    }
}
