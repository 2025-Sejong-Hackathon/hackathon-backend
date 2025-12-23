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
@Table(name = "gikbti_question")
public class GikbtiQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private GikbtiCategory category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    private Integer weight;

    @Builder
    public GikbtiQuestion(GikbtiCategory category, String text, Boolean isActive, Integer displayOrder, Integer weight) {
        this.category = category;
        this.text = text;
        this.isActive = isActive != null ? isActive : true;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.weight = weight != null ? weight : 1;
    }

    public void updateQuestion(GikbtiCategory category, String text, Boolean isActive, Integer displayOrder, Integer weight) {
        if (category != null) {
            this.category = category;
        }
        if (text != null) {
            this.text = text;
        }
        if (isActive != null) {
            this.isActive = isActive;
        }
        if (displayOrder != null) {
            this.displayOrder = displayOrder;
        }
        if (weight != null) {
            this.weight = weight;
        }
    }
}

