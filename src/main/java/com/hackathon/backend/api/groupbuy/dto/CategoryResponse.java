package com.hackathon.backend.api.groupbuy.dto;

import com.hackathon.backend.domain.groupbuy.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "카테고리 응답")
@Getter
@Builder
@AllArgsConstructor
public class CategoryResponse {

    @Schema(description = "카테고리 ID")
    private Long id;

    @Schema(description = "부모 카테고리 ID")
    private Long parentId;

    @Schema(description = "카테고리 이름")
    private String name;

    @Schema(description = "깊이")
    private Integer depth;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .name(category.getName())
                .depth(category.getDepth())
                .build();
    }
}

