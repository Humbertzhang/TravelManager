package com.example.travelmanager.controller;

import com.alibaba.fastjson.JSON;
import com.example.travelmanager.controller.bean.ResultBean;
import com.example.travelmanager.dao.StatisticsDao;
import com.example.travelmanager.dao.TravelApplicationDao;
import com.example.travelmanager.dao.UserDao;
import com.example.travelmanager.response.statistics.MoneyDate;
import com.example.travelmanager.service.auth.AuthService;
import com.example.travelmanager.service.email.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private StatisticsDao statisticsDao;

    @RequestMapping("/email")
    public HttpEntity email(String to, String content, String subject){
        emailService.sendSimpleMail(to, subject, content);
        throw new RuntimeException("test");
        //return ResultBean.success();
    }

    @RequestMapping("/test")
    public HttpEntity test() {
        var l = statisticsDao.listFoodMoneyDateOfPayment(12);
        System.out.println(l.getClass());
        System.out.println(JSON.toJSONString(l));
        return ResultBean.success();
    }
}
