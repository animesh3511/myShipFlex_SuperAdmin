package com.example.superadmin.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarrierRequest {


    private Long carrierId;
    private String name;
    private String description;
    private MultipartFile logo;




}
