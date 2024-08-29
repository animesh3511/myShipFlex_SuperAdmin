package com.example.superadmin.controller;

import com.example.superadmin.model.request.LoginRequest;
import com.example.superadmin.model.request.SuperAdminRequest;
import com.example.superadmin.model.response.CustomEntityResponse;
import com.example.superadmin.model.response.EntityResponse;
import com.example.superadmin.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping("/superAdminSignup")
    public ResponseEntity<?> signUp(@RequestBody SuperAdminRequest superAdminRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(superAdminService.signUp(superAdminRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/superAdminLogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(superAdminService.login(loginRequest.getEmail(), loginRequest.getPassword()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/superAdminVerifyOtpWithLogin")
    public ResponseEntity<?> verifyOtpWithLogin(@RequestParam String email, @RequestParam int otp) {
        try {
            return new ResponseEntity<>(new EntityResponse(superAdminService.verifyOtpWithLogin(email, otp), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

//class ends here
}
