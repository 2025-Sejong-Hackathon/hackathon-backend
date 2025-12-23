package com.hackathon.backend.domain.gikbti.entity;

import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "gikbti_option")
public class GikbtiOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gikbti_question_id", nullable = false)
    private GikbtiQuestion gikbtiQuestion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private Boolean isTrue;

    @Column(nullable = false)
    private Integer displayOrder;

    @Builder
    public GikbtiOption(GikbtiQuestion gikbtiQuestion, String text, Boolean isTrue, Integer displayOrder) {
        this.gikbtiQuestion = gikbtiQuestion;
        this.text = text;
        this.isTrue = isTrue != null ? isTrue : false;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public void updateOption(String text, Boolean isTrue, Integer displayOrder) {
        if (text != null) {
            this.text = text;
        }
        if (isTrue != null) {
            this.isTrue = isTrue;
        }
        if (displayOrder != null) {
            this.displayOrder = displayOrder;
        }
    }
}

