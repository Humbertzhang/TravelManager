package com.example.travelmanager.service.auth;

import com.alibaba.fastjson.JSON;
import com.example.travelmanager.config.Constant;
import com.example.travelmanager.service.auth.AuthException.UserInfo;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AuthHelper {
    public UserInfo Authorize() {
        return new UserInfo();
    }

    public static String generateToken(UserInfo userInfo){  // will use User entry when db designed
        String json = JSON.toJSONString(userInfo);
        return encrypt(json);
    }

    public static String encrypt(String text) {
        try{
            Key aesKey = new SecretKeySpec(Constant.SIGNING_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return parseByte2HexStr(encrypted);
        }
        catch (Exception e){
            System.err.println(e.toString());
            return null;
        }

    }

    public static String decrypt(String text){
        try{
            Key aesKey = new SecretKeySpec(Constant.SIGNING_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(parseHexStr2Byte(text)));
            return decrypted;
        }
        catch (Exception e){
            System.err.println(e.toString());
            return null;
        }
    }

    private static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}