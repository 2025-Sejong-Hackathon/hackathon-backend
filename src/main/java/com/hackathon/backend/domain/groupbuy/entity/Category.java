package com.hackathon.backend.domain.groupbuy.entity;

import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer depth;

    @Builder
    public Category(Category parent, String name, Integer depth) {
        this.parent = parent;
        this.name = name;
        this.depth = depth != null ? depth : 0;
    }

    public void updateCategory(String name, Category parent) {
        if (name != null) {
            this.name = name;
        }
        if (parent != null) {
            this.parent = parent;
            this.depth = parent.getDepth() + 1;
        }
    }
}

