package com.hackathon.backend.domain.laundry.entity;

import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "laundry_congestion_forecast",
       uniqueConstraints = @UniqueConstraint(columnNames = {"week", "hour", "gender_zone"}))
public class LaundryCongestionForecast extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DayOfWeek week;

    @Column(nullable = false)
    private Integer hour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GenderZone genderZone;

    @Column(nullable = false)
    private Integer congestion;

    @Builder
    public LaundryCongestionForecast(DayOfWeek week, Integer hour, GenderZone genderZone, Integer congestion) {
        this.week = week;
        this.hour = hour;
        this.genderZone = genderZone;
        this.congestion = congestion;
    }

    public void updateCongestion(Integer congestion) {
        if (congestion >= 0 && congestion <= 10) {
            this.congestion = congestion;
        }
    }

    public static boolean isValidHour(Integer hour) {
        return hour >= 0 && hour <= 23;
    }

    public static boolean isValidCongestion(Integer congestion) {
        return congestion >= 0 && congestion <= 10;
    }
}

