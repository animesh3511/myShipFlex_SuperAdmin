package com.example.user_verification.controller;

import com.example.user_verification.model.request.LoginRequest;
import com.example.user_verification.model.request.MsfCompanyRequest;
import com.example.user_verification.model.request.UserRequest;
import com.example.user_verification.model.response.CustomEntityResponse;
import com.example.user_verification.model.response.EntityResponse;
import com.example.user_verification.service.MsfCompanyService;
import com.example.user_verification.serviceimpl.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MsfCompanyController {

    @Autowired
    private MsfCompanyService msfCompanyService;
    @Autowired
    private UserToken userToken;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody MsfCompanyRequest msfCompanyRequest) {

        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.signUp(msfCompanyRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.login(loginRequest.getEmail(), loginRequest.getPassword()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyOtpWithLogin")
    public ResponseEntity<?> verifyOtpWithLogin(@RequestParam String email, @RequestParam int otp) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.verifyOtpWithLogin(email, otp), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByCompanyId")
    public ResponseEntity<?> findByCompanyId() {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.findByCompanyId
                    (userToken.getCompanyFromToken().getCompanyId()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/saveOrUpdateUser")
    public ResponseEntity<?> saveOrUpdateUser(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateUser(userRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/getUserByNameEmailLocation")
    public ResponseEntity<?> getUserByNameEmailLocation(@RequestParam String name, String email, String location) {
        try {
            return new ResponseEntity<>(new EntityResponse(userService.getUserByNameEmailLocation(name, email, location), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }*/

   /* @GetMapping("/findByLocation")
    public ResponseEntity<?> findByLocation(@RequestParam String location) {
        try {
            return new ResponseEntity<>(new EntityResponse(userService.findByLocation(location), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }*/

    //class ends here
}
