package com.hackathon.backend.api.member.dto;

import com.hackathon.backend.domain.member.entity.Gender;
import com.hackathon.backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "회원 정보 응답")
@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

    @Schema(description = "회원 ID")
    private Long id;

    @Schema(description = "학번")
    private String studentId;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "학과")
    private String major;

    @Schema(description = "학년")
    private String grade;

    @Schema(description = "이수 학기")
    private String completedSemester;

    @Schema(description = "사용자 상태")
    private String status;

    @Schema(description = "성별")
    private Gender gender;

    @Schema(description = "생년월일")
    private LocalDate birthDate;

    @Schema(description = "흡연 여부")
    private Boolean isSmoker;

    @Schema(description = "음주 여부")
    private Boolean isDrinker;

    @Schema(description = "추위 민감 여부")
    private Boolean isColdSensitive;

    @Schema(description = "더위 민감 여부")
    private Boolean isHeatSensitive;

    @Schema(description = "GIKBTI 타입")
    private String gikbti;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .studentId(member.getStudentId())
                .name(member.getName())
                .major(member.getMajor())
                .grade(member.getGrade())
                .completedSemester(member.getCompletedSemester())
                .status(member.getStatus().name())
                .gender(member.getGender())
                .birthDate(member.getBirthDate())
                .isSmoker(member.getIsSmoker())
                .isDrinker(member.getIsDrinker())
                .isColdSensitive(member.getColdSensitive())
                .isHeatSensitive(member.getHeatSensitive())
                .gikbti(member.getGikbti())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}

