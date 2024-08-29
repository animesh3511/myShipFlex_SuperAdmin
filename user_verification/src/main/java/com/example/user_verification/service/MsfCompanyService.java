package com.example.user_verification.service;

import com.example.user_verification.model.request.MsfCompanyRequest;
import com.example.user_verification.model.request.UserRequest;

public interface MsfCompanyService {

    Object signUp(MsfCompanyRequest userRequest);

    Object login(String email, String password);

    Object verifyOtpWithLogin(String email, int otp) throws Exception;

    Object findByCompanyId(Long companyId);

    Object saveOrUpdateUser(UserRequest userRequest);

    //  Object getUserByNameEmailLocation(String name, String email, String location);

   // Object findByLocation(String location);
}
