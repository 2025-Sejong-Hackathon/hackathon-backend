package com.hackathon.backend.domain.gikbti.entity;

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
@Table(name = "user_gikbti_answer")
public class UserGikbtiAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gikbti_option_id", nullable = false)
    private GikbtiOption gikbtiOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gikbti_question_id", nullable = false)
    private GikbtiQuestion gikbtiQuestion;

    @Builder
    public UserGikbtiAnswer(Member member, GikbtiOption gikbtiOption, GikbtiQuestion gikbtiQuestion) {
        this.member = member;
        this.gikbtiOption = gikbtiOption;
        this.gikbtiQuestion = gikbtiQuestion;
    }

    public void updateAnswer(GikbtiOption gikbtiOption) {
        this.gikbtiOption = gikbtiOption;
    }
}

