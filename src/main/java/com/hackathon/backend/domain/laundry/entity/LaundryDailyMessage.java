package com.hackathon.backend.domain.laundry.entity;

import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "laundry_daily_messages")
public class LaundryDailyMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate messageDate;

    @Column(nullable = false, length = 500)
    private String message;

    @Builder
    public LaundryDailyMessage(LocalDate messageDate, String message) {
        this.messageDate = messageDate;
        this.message = message;
    }

    public void updateMessage(String message) {
        this.message = message;
    }
}

