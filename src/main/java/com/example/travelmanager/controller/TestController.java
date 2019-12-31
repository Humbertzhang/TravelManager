package com.example.travelmanager.controller;

import com.example.travelmanager.config.Constant;
import com.example.travelmanager.controller.bean.ResultBean;
import com.example.travelmanager.dao.UserDao;
import com.example.travelmanager.entity.User;
import com.example.travelmanager.enums.UserRoleEnum;
import com.example.travelmanager.service.auth.AuthService;
import com.example.travelmanager.service.auth.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthService authService;

    @RequestMapping("/access")
    @ResponseBody
    public HttpEntity access(@RequestHeader(Constant.HEADER_STRING) String auth) {
        authService.authorize(auth, UserRoleEnum.Employee, UserRoleEnum.DepartmentManager);
        return ResultBean.success();
    }
}
