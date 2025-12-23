package com.hackathon.backend.domain.laundry.entity;

import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "laundry_sessions")
public class LaundrySession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laundry_machines_id", nullable = false)
    private LaundryMachine laundryMachine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endAt;

    @Builder
    public LaundrySession(LaundryMachine laundryMachine, Member member, Integer durationSeconds) {
        this.laundryMachine = laundryMachine;
        this.member = member;
        this.status = SessionStatus.RUNNING;
        this.startedAt = LocalDateTime.now();
        this.endAt = LocalDateTime.now().plusSeconds(durationSeconds);
    }

    public void finish() {
        this.status = SessionStatus.FINISHED;
        this.endAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = SessionStatus.CANCELED;
        this.endAt = LocalDateTime.now();
    }

    public Integer getRemainingSeconds() {
        if (status != SessionStatus.RUNNING) {
            return 0;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endAt)) {
            return 0;
        }
        return (int) java.time.Duration.between(now, endAt).getSeconds();
    }
}

