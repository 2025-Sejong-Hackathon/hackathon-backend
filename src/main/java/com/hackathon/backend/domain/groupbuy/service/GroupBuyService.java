package com.hackathon.backend.domain.groupbuy.service;

import com.hackathon.backend.domain.groupbuy.entity.*;
import com.hackathon.backend.domain.groupbuy.repository.CategoryRepository;
import com.hackathon.backend.domain.groupbuy.repository.GroupBuyMemberRepository;
import com.hackathon.backend.domain.groupbuy.repository.GroupBuyRepository;
import com.hackathon.backend.domain.member.entity.Member;
import com.hackathon.backend.domain.member.repository.MemberRepository;
import com.hackathon.backend.global.exception.BusinessException;
import com.hackathon.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupBuyService {

    private final GroupBuyRepository groupBuyRepository;
    private final GroupBuyMemberRepository groupBuyMemberRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public GroupBuy createGroupBuy(Long memberId, Long categoryId, String title, String description, Integer targetCount) {
        log.info("공동구매 생성: memberId={}, categoryId={}", memberId, categoryId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "카테고리를 찾을 수 없습니다."));

        GroupBuy groupBuy = GroupBuy.builder()
                .category(category)
                .member(member)
                .title(title)
                .description(description)
                .targetCount(targetCount)
                .build();
        groupBuyRepository.save(groupBuy);

        GroupBuyMember groupBuyMember = GroupBuyMember.builder()
                .groupBuy(groupBuy)
                .member(member)
                .category(category)
                .role(GroupBuyMemberRole.OWNER)
                .build();
        groupBuyMemberRepository.save(groupBuyMember);

        log.info("공동구매 생성 완료: id={}", groupBuy.getId());
        return groupBuy;
    }

    @Transactional
    public GroupBuy updateGroupBuy(Long groupBuyId, Long memberId, String title, String description, Integer targetCount) {
        log.info("공동구매 수정: id={}, memberId={}", groupBuyId, memberId);

        GroupBuy groupBuy = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공동구매를 찾을 수 없습니다."));

        if (!groupBuy.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "공동구매를 수정할 권한이 없습니다.");
        }

        groupBuy.updateGroupBuy(title, description, targetCount);
        return groupBuy;
    }

    @Transactional
    public void deleteGroupBuy(Long groupBuyId, Long memberId) {
        log.info("공동구매 삭제: id={}, memberId={}", groupBuyId, memberId);

        GroupBuy groupBuy = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공동구매를 찾을 수 없습니다."));

        if (!groupBuy.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "공동구매를 삭제할 권한이 없습니다.");
        }

        long memberCount = groupBuyMemberRepository.countByGroupBuy(groupBuy);
        if (memberCount > 1) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "참여자가 있는 공동구매는 삭제할 수 없습니다. 취소해주세요.");
        }

        groupBuyRepository.delete(groupBuy);
    }

    @Transactional
    public void cancelGroupBuy(Long groupBuyId, Long memberId) {
        log.info("공동구매 취소: id={}, memberId={}", groupBuyId, memberId);

        GroupBuy groupBuy = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공동구매를 찾을 수 없습니다."));

        if (!groupBuy.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "공동구매를 취소할 권한이 없습니다.");
        }

        groupBuy.cancel();
    }

    @Transactional
    public GroupBuyMember joinGroupBuy(Long groupBuyId, Long memberId) {
        log.info("공동구매 참여: groupBuyId={}, memberId={}", groupBuyId, memberId);

        GroupBuy groupBuy = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공동구매를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (groupBuyMemberRepository.existsByGroupBuyAndMember(groupBuy, member)) {
            throw new BusinessException(ErrorCode.CONFLICT, "이미 참여한 공동구매입니다.");
        }

        if (groupBuy.getStatus() == GroupBuyStatus.CLOSED) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "모집이 완료된 공동구매입니다.");
        }

        if (groupBuy.getStatus() == GroupBuyStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "취소된 공동구매입니다.");
        }

        GroupBuyMember groupBuyMember = GroupBuyMember.builder()
                .groupBuy(groupBuy)
                .member(member)
                .category(groupBuy.getCategory())
                .role(GroupBuyMemberRole.MEMBER)
                .build();
        groupBuyMemberRepository.save(groupBuyMember);

        groupBuy.incrementCount();

        log.info("공동구매 참여 완료: groupBuyId={}, memberId={}, currentCount={}",
                groupBuyId, memberId, groupBuy.getCurrentCount());

        return groupBuyMember;
    }

    @Transactional
    public void leaveGroupBuy(Long groupBuyId, Long memberId) {
        log.info("공동구매 참여 취소: groupBuyId={}, memberId={}", groupBuyId, memberId);

        GroupBuy groupBuy = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공동구매를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (groupBuy.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "작성자는 공동구매를 나갈 수 없습니다. 취소 또는 삭제해주세요.");
        }

        GroupBuyMember groupBuyMember = groupBuyMemberRepository.findByGroupBuyAndMember(groupBuy, member)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "참여하지 않은 공동구매입니다."));

        groupBuyMemberRepository.delete(groupBuyMember);
        groupBuy.decrementCount();

        log.info("공동구매 참여 취소 완료: groupBuyId={}, memberId={}, currentCount={}",
                groupBuyId, memberId, groupBuy.getCurrentCount());
    }

    @Transactional
    public GroupBuy getGroupBuy(Long groupBuyId) {
        return groupBuyRepository.findByIdWithDetails(groupBuyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공동구매를 찾을 수 없습니다."));
    }

    @Transactional
    public Page<GroupBuy> getGroupBuys(Pageable pageable) {
        return groupBuyRepository.findAllWithDetailsPageable(pageable);
    }

    @Transactional
    public Page<GroupBuy> getGroupBuysByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
        return groupBuyRepository.findByCategoryWithDetails(category, pageable);
    }

    @Transactional
    public Page<GroupBuy> getGroupBuysByStatus(GroupBuyStatus status, Pageable pageable) {
        return groupBuyRepository.findByStatusWithDetails(status, pageable);
    }

    @Transactional
    public List<GroupBuy> getMyGroupBuys(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return groupBuyRepository.findByMemberWithDetails(member);
    }

    @Transactional
    public List<GroupBuy> getJoinedGroupBuys(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        List<GroupBuyMember> groupBuyMembers = groupBuyMemberRepository.findByMemberWithDetails(member);
        return groupBuyMembers.stream()
                .map(GroupBuyMember::getGroupBuy)
                .toList();
    }

    @Transactional
    public List<GroupBuyMember> getGroupBuyMembers(Long groupBuyId) {
        GroupBuy groupBuy = groupBuyRepository.findById(groupBuyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공동구매를 찾을 수 없습니다."));
        return groupBuyMemberRepository.findByGroupBuyWithMember(groupBuy);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getTopLevelCategories() {
        return categoryRepository.findTopLevelCategories();
    }

    public List<Category> getSubCategories(Long parentId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
        return categoryRepository.findByParent(parent);
    }
}

