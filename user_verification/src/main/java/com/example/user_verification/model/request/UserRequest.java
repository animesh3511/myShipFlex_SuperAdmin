package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

    private Long userId;
    private String firstName;
    private String lastName;
    private String companyName;
    private String industry;
    private String location;
    private String phoneNumber;

}
