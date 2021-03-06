package com.example.travelmanager.service.statistics;

import com.example.travelmanager.config.exception.StatisticsControllerException;
import com.example.travelmanager.dao.DepartmentDao;
import com.example.travelmanager.dao.StatisticsDao;
import com.example.travelmanager.dao.StatisticsRepo;
import com.example.travelmanager.dao.UserDao;
import com.example.travelmanager.enums.UserRoleEnum;
import com.example.travelmanager.response.statistics.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private StatisticsRepo statisticsRepo;

    @Autowired
    private StatisticsDao statisticsDao;

    @Override
    public void checkPermission(Integer userId, Integer departmentId) {
        var userQuery = userDao.findById(userId);
        if(userQuery.isEmpty()) {
            throw StatisticsControllerException.UserNotFoundException;
        }
        var user= userQuery.get();

        // 总经理可以无视department条件直接请求，这里只判断部门经理
        if(user.getRole() == UserRoleEnum.DepartmentManager.getRoleId()) {
            if(departmentId != -1) {
                var departmentQuery = departmentDao.findById(departmentId);
                if(departmentQuery.isEmpty()) {
                    throw StatisticsControllerException.DepartmentNotFoundException;
                }
                var department = departmentQuery.get();

                // 不是这个部门的部门经理
                if(!department.getManagerId().equals(user.getId())) {
                    throw StatisticsControllerException.PermissionDeniedException;
                }
            } else {
                // 部门经理不可以请求全部的
                throw StatisticsControllerException.PermissionDeniedException;
            }
        }

    }

/*

{
    food:{
        "budget": [1,2,3,4,5,6,7,8,9,10,11,12],
        "payment": [1,2,3,4,5,6,7,8,9,10,11,12],
    },
    "vehicle": {
        ""
    },
    "hotel": {

    },
    "other": {

    },
    "all": {

    }
}                       ]

* */


    @Override
    public PayBudgetDiffResponse payBudgetDiff(Integer departmentId, String startTime, String endTime) {


        PayBudgetDiffResponse response = new PayBudgetDiffResponse();
        // food

        // payment
        List<MoneyDatePair> foodPaymentDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "food_payment", "payment_application");
        for(MoneyDatePair pair:foodPaymentDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                // 时间ok
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getFood().getPayment().add(moneyInt);
            }
        }

        // budget
        List<MoneyDatePair> foodBudgetDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "food_budget", "travel_application");
        for (MoneyDatePair pair:foodBudgetDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getFood().getBudget().add(moneyInt);
            }
        }

        List<MoneyDatePair> otherPaymentDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "other_payment", "payment_application");
        for(MoneyDatePair pair:otherPaymentDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getOther().getPayment().add(moneyInt);
            }
        }

        List<MoneyDatePair> otherBudgetDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "other_budget", "travel_application");
        for (MoneyDatePair pair:otherBudgetDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getOther().getBudget().add(moneyInt);
            }
        }


        List<MoneyDatePair> vehiclePaymentDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "vehicle_payment", "payment_application");
        for(MoneyDatePair pair:vehiclePaymentDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getVehicle().getPayment().add(moneyInt);
            }
        }

        List<MoneyDatePair> vehicleBudgetDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "vehicle_budget", "travel_application");
        for (MoneyDatePair pair:vehicleBudgetDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getVehicle().getBudget().add(moneyInt);
            }
        }

        List<MoneyDatePair> hotelPaymentDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "hotel_payment", "payment_application");
        for (MoneyDatePair pair:hotelPaymentDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getHotel().getPayment().add(moneyInt);
            }
        }

        List<MoneyDatePair> hotelBudgetDataList = statisticsRepo.listOneMoneyDatePair(departmentId, "hotel_budget", "travel_application");
        for (MoneyDatePair pair:hotelBudgetDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getHotel().getBudget().add(moneyInt);
            }
        }

        List<MoneyDatePair> allPaymentDataList = statisticsRepo.listAllMoneyDatePair(departmentId, "payment_application");
        for(MoneyDatePair pair:allPaymentDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getAll().getPayment().add(moneyInt);
            }
        }

        List<MoneyDatePair> allBudgetDataList = statisticsRepo.listAllMoneyDatePair(departmentId, "travel_application");
        for(MoneyDatePair pair:allBudgetDataList) {
            if(timeCompare(pair.getDateString(), startTime, endTime)) {
                double money = pair.getMoney();
                int moneyInt = (int) money;
                response.getAll().getBudget().add(moneyInt);
            }
        }

        return response;
    }

    @Override
    public PaymentPercentResponse paymentPercent(Integer userId, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date time;
        try {
            time = formatter.parse(date);
        }
        catch (Exception e) {
            throw StatisticsControllerException.DateStringFormatErrorException;
        }

        PaymentPercentResponse response = statisticsRepo.getMyPayPercent(userId, date);
        return response;
    }

    @Override
    public PaymentPercentResponse paymentPercentDepartment(String date, Integer departmentId) {

        return null;
    }

    @Override
    public PaymentVariationResponse paymentVariation(Integer departmentId, String startTime, String endTime) {
        PaymentVariationResponse response = new PaymentVariationResponse();

        List<MoneyDatePair> dataList = statisticsRepo.listAllMoneyDatePair(departmentId, "payment_application");
        for (MoneyDatePair pair : dataList) {
            if (timeCompare(pair.getDateString(), startTime, endTime)) {
                response.getDataList().add(pair);
            }
        }

        return response;
    }

    @Override
    public List<DepartmentCost> getDepartmentCost(String startTimeStr, String endTimeStr) {
        // get Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = simpleDateFormat.parse(startTimeStr);

            endTime = simpleDateFormat.parse(endTimeStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime); 
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); 
            endTime = calendar.getTime();
        }
        catch (Exception e){
            throw StatisticsControllerException.DateStringFormatErrorException;
        }

        // get data
        List<Object[]> objs = statisticsDao.getDpartmentCost(startTime, endTime);
        List<DepartmentCost> departmentCosts = new ArrayList<DepartmentCost>();
        for(Object[] obj: objs) {
            String departmentName = obj[0].toString();
            Double cost = (Double)obj[1];
            DepartmentCost departmentCost = new DepartmentCost();
            departmentCost.setDepartmentName(departmentName);
            departmentCost.setCost(cost);
            departmentCosts.add(departmentCost);
        }
        return departmentCosts;
    }

    private Boolean timeCompare(String time, String startTime, String endTime) {
        return startTime.compareTo(time) <= 0 && endTime.compareTo(time) >= 0;
    }
}
