package com.hackathon.backend.api.groupbuy.controller;

import com.hackathon.backend.api.groupbuy.dto.CategoryResponse;
import com.hackathon.backend.api.groupbuy.dto.GroupBuyCreateRequest;
import com.hackathon.backend.api.groupbuy.dto.GroupBuyResponse;
import com.hackathon.backend.api.groupbuy.dto.GroupBuyUpdateRequest;
import com.hackathon.backend.domain.groupbuy.entity.GroupBuy;
import com.hackathon.backend.domain.groupbuy.entity.GroupBuyMember;
import com.hackathon.backend.domain.groupbuy.entity.GroupBuyStatus;
import com.hackathon.backend.domain.groupbuy.service.GroupBuyService;
import com.hackathon.backend.global.response.ApiResponse;
import com.hackathon.backend.global.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "공동구매", description = "공동구매 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groupbuys")
public class GroupBuyController {

    private final GroupBuyService groupBuyService;

    @Operation(
            summary = "공동구매 생성",
            description = """
                    새로운 공동구매 게시글을 작성합니다.
                    
                    - 제목, 설명, 목표 인원, 카테고리를 입력합니다
                    - 작성자는 자동으로 첫 번째 참여자가 됩니다
                    - 채팅방이 자동으로 생성됩니다 (추후 구현)
                    """
    )
    @PostMapping
    public ResponseEntity<ApiResponse<GroupBuyResponse>> createGroupBuy(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @Valid @RequestBody GroupBuyCreateRequest request) {
        log.info("공동구매 생성 요청: memberId={}", memberDetails.getMemberId());

        GroupBuy groupBuy = groupBuyService.createGroupBuy(
                memberDetails.getMemberId(),
                request.getCategoryId(),
                request.getTitle(),
                request.getDescription(),
                request.getTargetCount()
        );

        GroupBuyResponse response = GroupBuyResponse.from(groupBuy);
        return ResponseEntity.ok(ApiResponse.success("공동구매가 생성되었습니다.", response));
    }

    @Operation(
            summary = "공동구매 수정",
            description = "작성한 공동구매 게시글을 수정합니다. (작성자만 가능)"
    )
    @PutMapping("/{groupBuyId}")
    public ResponseEntity<ApiResponse<GroupBuyResponse>> updateGroupBuy(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long groupBuyId,
            @Valid @RequestBody GroupBuyUpdateRequest request) {
        log.info("공동구매 수정 요청: groupBuyId={}, memberId={}", groupBuyId, memberDetails.getMemberId());

        GroupBuy groupBuy = groupBuyService.updateGroupBuy(
                groupBuyId,
                memberDetails.getMemberId(),
                request.getTitle(),
                request.getDescription(),
                request.getTargetCount()
        );

        GroupBuyResponse response = GroupBuyResponse.from(groupBuy);
        return ResponseEntity.ok(ApiResponse.success("공동구매가 수정되었습니다.", response));
    }

    @Operation(
            summary = "공동구매 삭제",
            description = """
                    공동구매 게시글을 삭제합니다. (작성자만 가능)
                    
                    - 참여자가 작성자 본인만 있을 때만 삭제 가능
                    - 참여자가 있으면 취소만 가능
                    """
    )
    @DeleteMapping("/{groupBuyId}")
    public ApiResponse<Void> deleteGroupBuy(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long groupBuyId) {
        log.info("공동구매 삭제 요청: groupBuyId={}, memberId={}", groupBuyId, memberDetails.getMemberId());

        groupBuyService.deleteGroupBuy(groupBuyId, memberDetails.getMemberId());

        return ApiResponse.success();
    }

    @Operation(
            summary = "공동구매 취소",
            description = "공동구매를 취소합니다. (작성자만 가능)"
    )
    @PostMapping("/{groupBuyId}/cancel")
    public ApiResponse<Void> cancelGroupBuy(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long groupBuyId) {
        log.info("공동구매 취소 요청: groupBuyId={}, memberId={}", groupBuyId, memberDetails.getMemberId());

        groupBuyService.cancelGroupBuy(groupBuyId, memberDetails.getMemberId());

        return ApiResponse.success();
    }

    @Operation(
            summary = "공동구매 참여",
            description = """
                    공동구매에 참여합니다.
                    
                    - 채팅방에 자동으로 참여됩니다
                    - 목표 인원 달성 시 자동으로 모집 완료
                    """
    )
    @PostMapping("/{groupBuyId}/join")
    public ApiResponse<Void> joinGroupBuy(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long groupBuyId) {
        log.info("공동구매 참여 요청: groupBuyId={}, memberId={}", groupBuyId, memberDetails.getMemberId());

        groupBuyService.joinGroupBuy(groupBuyId, memberDetails.getMemberId());

        return ApiResponse.success();
    }

    @Operation(
            summary = "공동구매 참여 취소",
            description = """
                    공동구매 참여를 취소합니다.
                    
                    - 작성자는 취소할 수 없음
                    - 채팅방에서 자동으로 나가짐
                    """
    )
    @PostMapping("/{groupBuyId}/leave")
    public ApiResponse<Void> leaveGroupBuy(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable Long groupBuyId) {
        log.info("공동구매 참여 취소 요청: groupBuyId={}, memberId={}", groupBuyId, memberDetails.getMemberId());

        groupBuyService.leaveGroupBuy(groupBuyId, memberDetails.getMemberId());

        return ApiResponse.success();
    }

    @Operation(
            summary = "공동구매 상세 조회",
            description = "공동구매 게시글의 상세 정보를 조회합니다."
    )
    @GetMapping("/{groupBuyId}")
    public ApiResponse<GroupBuyResponse> getGroupBuy(
            @PathVariable Long groupBuyId) {
        log.info("공동구매 상세 조회: groupBuyId={}", groupBuyId);

        GroupBuy groupBuy = groupBuyService.getGroupBuy(groupBuyId);
        GroupBuyResponse response = GroupBuyResponse.from(groupBuy);

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "공동구매 목록 조회",
            description = """
                    공동구매 목록을 조회합니다.
                    
                    - 페이징 지원
                    - 최신순 정렬
                    """
    )
    @GetMapping
    public ApiResponse<Page<GroupBuyResponse>> getGroupBuys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("공동구매 목록 조회: page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<GroupBuy> groupBuys = groupBuyService.getGroupBuys(pageable);
        Page<GroupBuyResponse> response = groupBuys.map(GroupBuyResponse::from);

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "카테고리별 공동구매 목록 조회",
            description = "특정 카테고리의 공동구매 목록을 조회합니다."
    )
    @GetMapping("/category/{categoryId}")
    public ApiResponse<Page<GroupBuyResponse>> getGroupBuysByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("카테고리별 공동구매 목록 조회: categoryId={}", categoryId);

        Pageable pageable = PageRequest.of(page, size);
        Page<GroupBuy> groupBuys = groupBuyService.getGroupBuysByCategory(categoryId, pageable);
        Page<GroupBuyResponse> response = groupBuys.map(GroupBuyResponse::from);

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "상태별 공동구매 목록 조회",
            description = "특정 상태의 공동구매 목록을 조회합니다. (OPEN, CLOSED, CANCELLED)"
    )
    @GetMapping("/status/{status}")
    public ApiResponse<Page<GroupBuyResponse>> getGroupBuysByStatus(
            @PathVariable GroupBuyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("상태별 공동구매 목록 조회: status={}", status);

        Pageable pageable = PageRequest.of(page, size);
        Page<GroupBuy> groupBuys = groupBuyService.getGroupBuysByStatus(status, pageable);
        Page<GroupBuyResponse> response = groupBuys.map(GroupBuyResponse::from);

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "내가 작성한 공동구매 목록 조회",
            description = "내가 작성한 공동구매 게시글 목록을 조회합니다."
    )
    @GetMapping("/my")
    public ApiResponse<List<GroupBuyResponse>> getMyGroupBuys(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("내가 작성한 공동구매 목록 조회: memberId={}", memberDetails.getMemberId());

        List<GroupBuy> groupBuys = groupBuyService.getMyGroupBuys(memberDetails.getMemberId());
        List<GroupBuyResponse> response = groupBuys.stream()
                .map(GroupBuyResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "내가 참여한 공동구매 목록 조회",
            description = "내가 참여 중인 공동구매 게시글 목록을 조회합니다."
    )
    @GetMapping("/joined")
    public ApiResponse<List<GroupBuyResponse>> getJoinedGroupBuys(
            @Parameter(hidden = true) @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("내가 참여한 공동구매 목록 조회: memberId={}", memberDetails.getMemberId());

        List<GroupBuy> groupBuys = groupBuyService.getJoinedGroupBuys(memberDetails.getMemberId());
        List<GroupBuyResponse> response = groupBuys.stream()
                .map(GroupBuyResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "공동구매 참여자 목록 조회",
            description = "특정 공동구매의 참여자 목록을 조회합니다."
    )
    @GetMapping("/{groupBuyId}/members")
    public ApiResponse<List<String>> getGroupBuyMembers(
            @PathVariable Long groupBuyId) {
        log.info("공동구매 참여자 목록 조회: groupBuyId={}", groupBuyId);

        List<GroupBuyMember> members = groupBuyService.getGroupBuyMembers(groupBuyId);
        List<String> response = members.stream()
                .map(gbm -> gbm.getMember().getName())
                .collect(Collectors.toList());

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "카테고리 목록 조회",
            description = "모든 공동구매 카테고리를 조회합니다."
    )
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        log.info("카테고리 목록 조회");

        List<CategoryResponse> response = groupBuyService.getAllCategories().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.success(response);
    }

    @Operation(
            summary = "최상위 카테고리 조회",
            description = "최상위 카테고리(음식, 물품)를 조회합니다."
    )
    @GetMapping("/categories/top")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getTopLevelCategories() {
        log.info("최상위 카테고리 조회");

        List<CategoryResponse> response = groupBuyService.getTopLevelCategories().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "하위 카테고리 조회",
            description = "특정 카테고리의 하위 카테고리를 조회합니다."
    )
    @GetMapping("/categories/{parentId}/sub")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getSubCategories(
            @PathVariable Long parentId) {
        log.info("하위 카테고리 조회: parentId={}", parentId);

        List<CategoryResponse> response = groupBuyService.getSubCategories(parentId).stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

