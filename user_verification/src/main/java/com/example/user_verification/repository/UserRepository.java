package com.example.user_verification.repository;

import com.example.user_verification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndUserIdNotIn(String phoneNumber, List<Long> userId);
}
