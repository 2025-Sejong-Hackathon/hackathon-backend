package com.hackathon.backend.global.config;

import com.hackathon.backend.domain.laundry.entity.GenderZone;
import com.hackathon.backend.domain.laundry.entity.LaundryMachine;
import com.hackathon.backend.domain.laundry.entity.MachineType;
import com.hackathon.backend.domain.laundry.repository.LaundryMachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 세탁기/건조기 초기 데이터 로더
 */
@Slf4j
@Component
@Order(3)
@RequiredArgsConstructor
public class LaundryMachineDataLoader implements ApplicationRunner {

    private final LaundryMachineRepository laundryMachineRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // 이미 데이터가 있으면 건너뛰기
        if (laundryMachineRepository.count() > 0) {
            log.info("세탁기 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        log.info("세탁기/건조기 초기 데이터 삽입 시작...");

        // 남자 세탁기 6개
        for (int i = 1; i <= 6; i++) {
            LaundryMachine machine = LaundryMachine.builder()
                    .type(MachineType.WASHER)
                    .genderZone(GenderZone.MALE)
                    .machineNumber(i)
                    .build();
            laundryMachineRepository.save(machine);
        }

        // 남자 건조기 6개
        for (int i = 1; i <= 6; i++) {
            LaundryMachine machine = LaundryMachine.builder()
                    .type(MachineType.DRYER)
                    .genderZone(GenderZone.MALE)
                    .machineNumber(i)
                    .build();
            laundryMachineRepository.save(machine);
        }

        // 여자 세탁기 6개
        for (int i = 1; i <= 6; i++) {
            LaundryMachine machine = LaundryMachine.builder()
                    .type(MachineType.WASHER)
                    .genderZone(GenderZone.FEMALE)
                    .machineNumber(i)
                    .build();
            laundryMachineRepository.save(machine);
        }

        // 여자 건조기 6개
        for (int i = 1; i <= 6; i++) {
            LaundryMachine machine = LaundryMachine.builder()
                    .type(MachineType.DRYER)
                    .genderZone(GenderZone.FEMALE)
                    .machineNumber(i)
                    .build();
            laundryMachineRepository.save(machine);
        }

        log.info("세탁기/건조기 초기 데이터 삽입 완료: 총 24대");
    }
}

