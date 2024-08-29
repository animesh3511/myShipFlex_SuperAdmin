package com.example.superadmin.repository;

import com.example.superadmin.model.Carrier;
import com.example.superadmin.model.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Page<Carrier> findCarrierByName(String name, Pageable pageable);

    Page<Carrier> findAllByIsDeleted(boolean b, Pageable pageable);
}
