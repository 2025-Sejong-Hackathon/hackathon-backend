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
@Table(name = "laundry_machines",
       uniqueConstraints = @UniqueConstraint(columnNames = {"type", "gender_zone", "machine_number"}))
public class LaundryMachine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MachineType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GenderZone genderZone;

    @Column(nullable = false)
    private Integer machineNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MachineStatus status;

    @Builder
    public LaundryMachine(MachineType type, GenderZone genderZone, Integer machineNumber) {
        this.type = type;
        this.genderZone = genderZone;
        this.machineNumber = machineNumber;
        this.status = MachineStatus.AVAILABLE;
    }

    public void startUse() {
        this.status = MachineStatus.IN_USE;
    }

    public void finishUse() {
        this.status = MachineStatus.AVAILABLE;
    }

    public void markOutOfOrder() {
        this.status = MachineStatus.OUT_OF_ORDER;
    }

    public void markAvailable() {
        this.status = MachineStatus.AVAILABLE;
    }
}

