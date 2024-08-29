package com.example.superadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SuperAdminRequest {

    private Long superAdminId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
    private String mobNumber;


}
