package com.hackathon.backend.domain.laundry.repository;

import com.hackathon.backend.domain.laundry.entity.GenderZone;
import com.hackathon.backend.domain.laundry.entity.LaundryMachine;
import com.hackathon.backend.domain.laundry.entity.MachineStatus;
import com.hackathon.backend.domain.laundry.entity.MachineType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LaundryMachineRepository extends JpaRepository<LaundryMachine, Long> {

    /**
     * 타입과 성별 존으로 세탁기 목록 조회
     */
    List<LaundryMachine> findByTypeAndGenderZoneOrderByMachineNumberAsc(MachineType type, GenderZone genderZone);

    /**
     * 성별 존으로 모든 기계 조회
     */
    List<LaundryMachine> findByGenderZone(GenderZone genderZone);

    /**
     * 타입, 성별 존, 기계 번호로 조회
     */
    Optional<LaundryMachine> findByTypeAndGenderZoneAndMachineNumber(MachineType type, GenderZone genderZone, Integer machineNumber);

    /**
     * 상태별 조회
     */
    List<LaundryMachine> findByStatus(MachineStatus status);
}

