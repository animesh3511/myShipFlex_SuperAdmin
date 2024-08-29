package com.example.superadmin.controller;

import com.example.superadmin.model.request.CarrierRequest;
import com.example.superadmin.model.request.IndustryRequest;
import com.example.superadmin.model.response.CustomEntityResponse;
import com.example.superadmin.model.response.EntityResponse;
import com.example.superadmin.service.IMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MasterController {

    @Autowired
    private IMasterService iMasterService;

    @PostMapping("/saveOrUpdateIndustry")
    public ResponseEntity<?> saveOrUpdateIndustry(@RequestBody IndustryRequest industryRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.saveOrUpdateIndustry(industryRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getIndustryById")
    public ResponseEntity<?> getIndustryById(@RequestParam Long industryId) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.getIndustryById(industryId), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/statusChangeIndustry")
    public ResponseEntity<?> statusChangeIndustry(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.statusChangeIndustry(id), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.deleteById(id), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllIndustry")
    public ResponseEntity<?> getAllIndustry(@RequestParam(required = false) String name, @RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                            @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0),
                Optional.ofNullable(pageSize).orElse(50));
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.getAllIndustry(name, pageable), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateCarrier")
    public ResponseEntity<?> saveOrUpdateCarrier(@ModelAttribute CarrierRequest carrierRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.saveOrUpdateCarrier(carrierRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getCarrierById")
    public ResponseEntity<?> getCarrierById(@RequestParam Long carrierId) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.getCarrierById(carrierId), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/statusChangeCarrier")
    public ResponseEntity<?> statusChangeCarrier(@RequestParam Long carrierId) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.statusChangeCarrier(carrierId), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteCarrierById")
    public ResponseEntity<?> deleteCarrierById(@RequestParam Long carrierId) {
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.deleteCarrierById(carrierId), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllCarrier")
    public ResponseEntity<?> getAllCarrier(@RequestParam(required = false) String name, @RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                           @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0),
                Optional.ofNullable(pageSize).orElse(50));
        try {
            return new ResponseEntity<>(new EntityResponse(iMasterService.getAllCarrier(name, pageable), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    //class ends here
}
