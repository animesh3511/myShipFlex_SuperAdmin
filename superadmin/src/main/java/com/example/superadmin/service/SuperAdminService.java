package com.example.superadmin.service;

import com.example.superadmin.model.request.SuperAdminRequest;

public interface SuperAdminService {


    Object signUp(SuperAdminRequest superAdminRequest);

    Object login(String email, String password);


    Object verifyOtpWithLogin(String email, int otp);
}
