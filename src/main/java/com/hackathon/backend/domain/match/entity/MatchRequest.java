package com.hackathon.backend.domain.match.entity;

import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchRequest extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchRequestStatus status;

    @Builder
    public MatchRequest(MatchPair matchPair, Member member1, Member member2, Member sender) {
        this.matchPair = matchPair;
        this.member1 = member1;
        this.member2 = member2;
        this.sender = sender;
        this.status = MatchRequestStatus.PENDING;
    }

    public void accept() {
        this.status = MatchRequestStatus.ACCEPTED;
    }

    public void reject() {
        this.status = MatchRequestStatus.REJECTED;
    }
}

