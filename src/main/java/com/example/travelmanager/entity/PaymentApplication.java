package com.example.travelmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class PaymentApplication {
    @Id
    @Getter @Setter
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Getter @Setter
    @Column(nullable = false) @NotNull
    private Integer applicantId;

    @Getter @Setter
    @Column(nullable = false) @NotNull
    private Integer departmentId;

    @Getter @Setter
    @Column(nullable = false) @NotNull
    private Date applyTime;

    @Getter @Setter
    @Column(nullable = false) @NotNull
    private Integer status;

    @Getter @Setter
    @Column(nullable = false) @NotNull
    private Integer travelId;

    // "https://www.baidu.com https://www.google.com"
    @Getter @Setter
    private String invoiceURLs;

    @Getter @Setter
    private float hotelPayment;

    @Getter @Setter
    private float vehiclePayment;

    @Getter @Setter
    private float foodPayment;

    @Getter @Setter
    private float otherPayment;

}
