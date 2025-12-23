package com.hackathon.backend.domain.member.entity;

import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPreferences extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(nullable = false)
    private Boolean roommateSmokingPref;

    @Column(nullable = false)
    private Boolean roommateDrinkingPref;

    @Builder
    public UserPreferences(Member member, Boolean roommateSmokingPref, Boolean roommateDrinkingPref) {
        this.member = member;
        this.roommateSmokingPref = roommateSmokingPref != null ? roommateSmokingPref : false;
        this.roommateDrinkingPref = roommateDrinkingPref != null ? roommateDrinkingPref : false;
    }

    public void updatePreferences(Boolean roommateSmokingPref, Boolean roommateDrinkingPref) {
        if (roommateSmokingPref != null) {
            this.roommateSmokingPref = roommateSmokingPref;
        }
        if (roommateDrinkingPref != null) {
            this.roommateDrinkingPref = roommateDrinkingPref;
        }
    }
}

