package com.hackathon.backend.domain.chat.entity;

import com.hackathon.backend.domain.match.entity.MatchPair;
import com.hackathon.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_pair_id", nullable = false, unique = true)
    private MatchPair matchPair;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Builder
    public ChatRoom(MatchPair matchPair) {
        this.matchPair = matchPair;
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}

