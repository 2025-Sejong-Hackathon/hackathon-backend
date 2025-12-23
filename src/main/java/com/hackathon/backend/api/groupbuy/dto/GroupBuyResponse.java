package com.hackathon.backend.api.groupbuy.dto;

import com.hackathon.backend.domain.groupbuy.entity.GroupBuy;
import com.hackathon.backend.domain.groupbuy.entity.GroupBuyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "공동구매 응답")
@Getter
@Builder
@AllArgsConstructor
public class GroupBuyResponse {

    @Schema(description = "공동구매 ID")
    private Long id;

    @Schema(description = "카테고리 ID")
    private Long categoryId;

    @Schema(description = "카테고리 이름")
    private String categoryName;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Schema(description = "작성자 이름")
    private String memberName;

    @Schema(description = "작성자 학번")
    private String memberStudentId;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "현재 인원")
    private Integer currentCount;

    @Schema(description = "목표 인원")
    private Integer targetCount;

    @Schema(description = "상태")
    private GroupBuyStatus status;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public static GroupBuyResponse from(GroupBuy groupBuy) {
        return GroupBuyResponse.builder()
                .id(groupBuy.getId())
                .categoryId(groupBuy.getCategory().getId())
                .categoryName(groupBuy.getCategory().getName())
                .memberId(groupBuy.getMember().getId())
                .memberName(groupBuy.getMember().getName())
                .memberStudentId(groupBuy.getMember().getStudentId())
                .title(groupBuy.getTitle())
                .description(groupBuy.getDescription())
                .currentCount(groupBuy.getCurrentCount())
                .targetCount(groupBuy.getTargetCount())
                .status(groupBuy.getStatus())
                .createdAt(groupBuy.getCreatedAt())
                .updatedAt(groupBuy.getUpdatedAt())
                .build();
    }
}

