package com.example.superadmin.serviceimpl;

import com.example.superadmin.model.Carrier;
import com.example.superadmin.model.Industry;
import com.example.superadmin.model.request.CarrierRequest;
import com.example.superadmin.model.request.IndustryRequest;
import com.example.superadmin.model.response.PageDto;
import com.example.superadmin.repository.CarrierRepository;
import com.example.superadmin.repository.IndustryRepository;
import com.example.superadmin.service.IMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MasterServiceImpl implements IMasterService {

    @Autowired
    private IndustryRepository industryRepository;
    @Autowired
    private CarrierRepository carrierRepository;
    @Autowired
    private ImageUploadService imageUploadService;

    @Override
    public Object saveOrUpdateIndustry(IndustryRequest industryRequest) {
        if (industryRepository.existsById(industryRequest.getIndustryId())) {
            Industry industry = industryRepository.findById(industryRequest.getIndustryId()).get();
            industry.setIsActive(true);
            industry.setIsDeleted(false);
            saveOrUpdateIndustryDetails(industry, industryRequest);
            return "Updated";
        } else {
            Industry industry = new Industry();
            industry.setIsActive(true);
            industry.setIsDeleted(false);
            saveOrUpdateIndustryDetails(industry, industryRequest);
            return "Created";
        }
    }

    private void saveOrUpdateIndustryDetails(Industry industry, IndustryRequest industryRequest) {
        industry.setIndustryId(industryRequest.getIndustryId());
        industry.setName(industryRequest.getName());
        industry.setDescription(industryRequest.getDescription());
        industryRepository.save(industry);
    }


    @Override
    public Object getIndustryById(Long industryId) {
        if (industryRepository.existsById(industryId)) {
            return industryRepository.findById(industryId);
        } else {
            return "Industry not found for given Id";
        }
    }


    @Override
    public Object statusChangeIndustry(Long id) throws Exception {
        return industryRepository.findById(id)
                .map(industry -> {
                    industry.setIsActive(!industry.getIsActive());
                    industry.setIsDeleted(!industry.getIsDeleted());
                    industryRepository.save(industry);
                    return industry.getIsActive() ? "industry is active" : "industry is deleted";
                })
                .orElseThrow(() -> new Exception("industry not found"));
    }

    @Override
    public Object deleteById(Long id) {
        if (industryRepository.existsById(id)) {
            industryRepository.deleteById(id);
            return "Deleted";
        } else {
            return "Not found";
        }
    }

    @Override
    public Object getAllIndustry(String name, Pageable pageable) {
        List<Industry> industries = new ArrayList<>();
        Page<Industry> page = new PageImpl<>(industries);
        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();
            page = industryRepository.findIndustryByName(name, pageable);
        } else {
            page = industryRepository.findAllByIsDeleted(false, pageable);
        }
        return new PageDto(page.getContent(), page.getTotalPages(), page.getTotalElements(), page.getNumber());
    }

    @Override
    public Object saveOrUpdateCarrier(CarrierRequest carrierRequest) {
        if (carrierRepository.existsById(carrierRequest.getCarrierId())) {
            Carrier carrier = carrierRepository.findById(carrierRequest.getCarrierId()).get();
            carrier.setIsActive(true);
            carrier.setIsDeleted(false);
            saveOrUpdateCarrierDetails(carrier, carrierRequest);
            return "Updated";
        } else {
            Carrier carrier = new Carrier();
            carrier.setIsActive(true);
            carrier.setIsDeleted(false);
            saveOrUpdateCarrierDetails(carrier, carrierRequest);
            return "Created";
        }
    }

    private void saveOrUpdateCarrierDetails(Carrier carrier, CarrierRequest carrierRequest) {
        carrier.setCarrierId(carrierRequest.getCarrierId());
        carrier.setName(carrierRequest.getName());
        carrier.setDescription(carrierRequest.getDescription());
        if (carrierRequest.getLogo() != null) {
            carrier.setLogo(imageUploadService.uploadImage(carrierRequest.getLogo()));
        }
        carrierRepository.save(carrier);
    }

    @Override
    public Object getCarrierById(Long carrierId) {
        if (carrierRepository.existsById(carrierId)) {
            return carrierRepository.findById(carrierId);
        } else {
            return "Not found";
        }
    }

    @Override
    public Object statusChangeCarrier(Long carrierId) throws Exception {
        return carrierRepository.findById(carrierId)
                .map(carrier -> {
                    carrier.setIsActive(!carrier.getIsActive());
                    carrier.setIsDeleted(!carrier.getIsDeleted());
                    carrierRepository.save(carrier);
                    return carrier.getIsActive() ? "carrier is active" : "carrier is deleted";
                })
                .orElseThrow(() -> new Exception("carrier not found"));
    }

    @Override
    public Object deleteCarrierById(Long carrierId) {
        if (carrierRepository.existsById(carrierId)) {
            carrierRepository.deleteById(carrierId);
            return "Deleted";
        } else {
            return "Not found";
        }
    }

    @Override
    public Object getAllCarrier(String name, Pageable pageable) {

        List<Carrier> carriers = new ArrayList<>();
        Page<Carrier> page = new PageImpl<>(carriers);
        if (name != null && !name.isEmpty()) {
            name = name.toLowerCase();
            page = carrierRepository.findCarrierByName(name, pageable);
        } else {
            page = carrierRepository.findAllByIsDeleted(false, pageable);
        }
        return new PageDto(page.getContent(), page.getTotalPages(), page.getTotalElements(), page.getNumber());
    }
    //class ends here
}
