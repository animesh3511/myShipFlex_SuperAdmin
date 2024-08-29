package com.example.superadmin.service;

import com.example.superadmin.model.request.CarrierRequest;
import com.example.superadmin.model.request.IndustryRequest;
import org.springframework.data.domain.Pageable;

public interface IMasterService {


    Object saveOrUpdateIndustry(IndustryRequest industryRequest);

    Object getIndustryById(Long industryId);

    Object statusChangeIndustry(Long id) throws Exception;

    Object deleteById(Long id);

    Object getAllIndustry(String name, Pageable pageable);

    Object saveOrUpdateCarrier(CarrierRequest carrierRequest);

    Object getCarrierById(Long carrierId);

    Object statusChangeCarrier(Long carrierId) throws Exception;

    Object deleteCarrierById(Long carrierId);

    Object getAllCarrier(String name, Pageable pageable);
}
