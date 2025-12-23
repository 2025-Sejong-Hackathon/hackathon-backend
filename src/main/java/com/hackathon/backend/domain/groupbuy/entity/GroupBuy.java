package com.hackathon.backend.domain.groupbuy.entity;

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
@Table(name = "groupbuys")
public class GroupBuy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer currentCount;

    @Column(nullable = false)
    private Integer targetCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupBuyStatus status;

    @Builder
    public GroupBuy(Category category, Member member, String title, String description, Integer targetCount) {
        this.category = category;
        this.member = member;
        this.title = title;
        this.description = description;
        this.currentCount = 1; // 생성자가 첫 번째 참여자
        this.targetCount = targetCount;
        this.status = GroupBuyStatus.OPEN;
    }

    public void updateGroupBuy(String title, String description, Integer targetCount) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (targetCount != null) {
            this.targetCount = targetCount;
        }
    }

    public void incrementCount() {
        this.currentCount++;
        if (this.currentCount >= this.targetCount) {
            this.status = GroupBuyStatus.CLOSED;
        }
    }

    public void decrementCount() {
        if (this.currentCount > 0) {
            this.currentCount--;
            if (this.status == GroupBuyStatus.CLOSED && this.currentCount < this.targetCount) {
                this.status = GroupBuyStatus.OPEN;
            }
        }
    }

    public void cancel() {
        this.status = GroupBuyStatus.CANCELLED;
    }

    public void reopen() {
        if (this.currentCount < this.targetCount) {
            this.status = GroupBuyStatus.OPEN;
        }
    }
}

