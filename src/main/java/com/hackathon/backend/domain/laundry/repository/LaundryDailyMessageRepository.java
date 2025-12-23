package com.hackathon.backend.domain.laundry.repository;

import com.hackathon.backend.domain.laundry.entity.LaundryDailyMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface LaundryDailyMessageRepository extends JpaRepository<LaundryDailyMessage, Long> {

    Optional<LaundryDailyMessage> findByMessageDate(LocalDate messageDate);
}

