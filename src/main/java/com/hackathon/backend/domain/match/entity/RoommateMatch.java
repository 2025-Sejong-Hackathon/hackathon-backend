package com.hackathon.backend.domain.match.entity;

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
public class RoommateMatch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_pair_id", nullable = false)
    private MatchPair matchPair;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id", nullable = false)
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id", nullable = false)
    private Member member2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoommateMatchStatus status;

    @Column
    private LocalDateTime completedAt;

    @Builder
    public RoommateMatch(MatchPair matchPair, Member member1, Member member2) {
        this.matchPair = matchPair;
        this.member1 = member1;
        this.member2 = member2;
        this.status = RoommateMatchStatus.MATCHED;
    }

    public void complete() {
        this.status = RoommateMatchStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = RoommateMatchStatus.CANCELLED;
    }
}

