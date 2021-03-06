package com.example.travelmanager.service.auth;

import com.example.travelmanager.enums.UserRoleEnum;
import com.example.travelmanager.payload.EditUserPaylod;
import com.example.travelmanager.payload.ForgetPasswordPayload;
import com.example.travelmanager.payload.LoginPayload;
import com.example.travelmanager.payload.RegisterPayload;
import com.example.travelmanager.payload.ResetPasswordPayload;
import com.example.travelmanager.payload.SetPasswordPayload;
import com.example.travelmanager.response.TokenResponse;


public interface AuthService {

    TokenResponse getToken(LoginPayload loginPayload);

    TokenResponse refershToken(String token);

    void register(RegisterPayload registerPayload);

    int authorize(String tokenString, UserRoleEnum... userRoleEnums);

    int authorize(String tokenString);

    void setPassword(int uid, SetPasswordPayload setPasswordPayload);

    void resetPasswoed(ResetPasswordPayload resetPasswordPayload);

    void editUser(int uid, EditUserPaylod editUserPaylod);

    void forgetPassword(ForgetPasswordPayload forgetPasswordPayload);
}
