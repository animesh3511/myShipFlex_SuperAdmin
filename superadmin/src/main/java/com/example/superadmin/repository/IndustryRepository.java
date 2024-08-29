package com.example.superadmin.repository;

import com.example.superadmin.model.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndustryRepository extends JpaRepository<Industry, Long> {

    boolean existsByName(String name);

    Object findByName(String name);

    Page<Industry> findIndustryByName(String name, Pageable pageable);

    Page<Industry> findAllByIsDeleted(boolean b, Pageable pageable);
}
