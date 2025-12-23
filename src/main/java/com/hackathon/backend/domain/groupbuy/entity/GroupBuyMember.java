package com.hackathon.backend.domain.groupbuy.entity;

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
@Table(name = "groupbuy_members")
public class GroupBuyMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupbuys_id", nullable = false)
    private GroupBuy groupBuy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupBuyMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupBuyMemberStatus status;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Column
    private LocalDateTime leftAt;

    @Builder
    public GroupBuyMember(Member member, GroupBuy groupBuy, Category category, GroupBuyMemberRole role) {
        this.member = member;
        this.groupBuy = groupBuy;
        this.category = category;
        this.role = role != null ? role : GroupBuyMemberRole.MEMBER;
        this.status = GroupBuyMemberStatus.ACTIVE;
        this.joinedAt = LocalDateTime.now();
    }

    public void leave() {
        this.status = GroupBuyMemberStatus.LEFT;
        this.leftAt = LocalDateTime.now();
    }

    public void rejoin() {
        this.status = GroupBuyMemberStatus.ACTIVE;
        this.leftAt = null;
    }
}

