package com.hackathon.backend.domain.member.entity;

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
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String studentId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String major;

    @Column(nullable = false, length = 50)
    private String grade;

    @Column(nullable = false)
    private String completedSemester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10)
    private Gender gender;

    @Column(nullable = true)
    private LocalDate birthDate;

    @Column(nullable = true)
    private Boolean isSmoker;

    @Column(nullable = true)
    private Boolean isDrinker;

    // 추위에 민감
    @Column(nullable = true)
    private Boolean coldSensitive;

    // 더위에 민감
    @Column(nullable = true)
    private Boolean heatSensitive;

    @Column(length = 50)
    private String gikbti;

    @Builder
    public Member(String studentId, String name, String major, String grade, String completedSemester,
                  Gender gender, LocalDate birthDate, Boolean isSmoker, Boolean isDrinker) {
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.grade = grade;
        this.completedSemester = completedSemester != null ? completedSemester : "0";
        this.status = MemberStatus.ACTIVE;
        this.gender = gender;
        this.birthDate = birthDate;
        this.isSmoker = isSmoker != null ? isSmoker : false;
        this.isDrinker = isDrinker != null ? isDrinker : false;
    }

    public void updateInfo(String name, String major, String grade, String completedSemester) {
        if (name != null) {
            this.name = name;
        }
        this.major = major;
        this.grade = grade;
        if (completedSemester != null) {
            this.completedSemester = completedSemester;
        }
    }

    public void updateStatus(MemberStatus status) {
        this.status = status;
    }

    public void updateGikbti(String gikbti) {
        this.gikbti = gikbti;
    }

    public void updatePreferences(Boolean isSmoker, Boolean isDrinker) {
        if (isSmoker != null) {
            this.isSmoker = isSmoker;
        }
        if (isDrinker != null) {
            this.isDrinker = isDrinker;
        }
    }

    public void updateOnboardingInfo(Gender gender, LocalDate birthDate, Boolean isSmoker, Boolean isDrinker,
                                     Boolean coldSensitive, Boolean heatSensitive) {
        if (gender != null) {
            this.gender = gender;
        }
        if (birthDate != null) {
            this.birthDate = birthDate;
        }
        if (isSmoker != null) {
            this.isSmoker = isSmoker;
        }
        if (isDrinker != null) {
            this.isDrinker = isDrinker;
        }
        if (coldSensitive != null) {
            this.coldSensitive = coldSensitive;
        }
        if (heatSensitive != null) {
            this.heatSensitive = heatSensitive;
        }
    }

    public void updateFullInfo(String major, String grade, String completedSemester, Gender gender,
                               LocalDate birthDate, Boolean isSmoker, Boolean isDrinker,
                               Boolean coldSensitive, Boolean heatSensitive) {
        if (major != null) {
            this.major = major;
        }
        if (grade != null) {
            this.grade = grade;
        }
        if (completedSemester != null) {
            this.completedSemester = completedSemester;
        }
        updateOnboardingInfo(gender, birthDate, isSmoker, isDrinker, coldSensitive, heatSensitive);
    }
}

