package com.example.travelmanager.payload;

import javax.validation.constraints.NotBlank;

import lombok.*;

public class ResetPasswordPayload{
    @Getter @Setter
    @NotBlank
    private String token;

    @Getter @Setter
    @NotBlank
    private String newPassword;
}