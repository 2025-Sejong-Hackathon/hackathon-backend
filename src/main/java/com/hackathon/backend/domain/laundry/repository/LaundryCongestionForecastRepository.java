package com.hackathon.backend.domain.laundry.repository;

import com.hackathon.backend.domain.laundry.entity.GenderZone;
import com.hackathon.backend.domain.laundry.entity.LaundryCongestionForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LaundryCongestionForecastRepository extends JpaRepository<LaundryCongestionForecast, Long> {

    /**
     * 날짜와 성별 존으로 예측 데이터 조회
     */
    List<LaundryCongestionForecast> findByForecastDateAndGenderZoneOrderByHourAsc(LocalDate forecastDate, GenderZone genderZone);

    /**
     * 날짜, 시간, 성별 존으로 조회
     */
    Optional<LaundryCongestionForecast> findByForecastDateAndHourAndGenderZone(LocalDate forecastDate, Integer hour, GenderZone genderZone);

    /**
     * 특정 날짜의 모든 데이터 삭제
     */
    @Query("DELETE FROM LaundryCongestionForecast lcf WHERE lcf.forecastDate = :forecastDate AND lcf.genderZone = :genderZone")
    void deleteByForecastDateAndGenderZone(@Param("forecastDate") LocalDate forecastDate, @Param("genderZone") GenderZone genderZone);
}

