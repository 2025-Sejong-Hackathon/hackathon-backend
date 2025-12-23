package com.hackathon.backend.api.member.dto;

import com.hackathon.backend.domain.member.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "회원 정보 수정 요청")
public class MemberUpdateRequest {

    @Schema(description = "전공", example = "컴퓨터공학과")
    private String major;

    @Schema(description = "학년", example = "3")
    private String grade;

    @Schema(description = "이수 학기", example = "5")
    private Integer completedSemester;

    @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
    private Gender gender;

    @Schema(description = "생년월일", example = "2000-01-01")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    @Schema(description = "흡연 여부", example = "false")
    private Boolean isSmoker;

    @Schema(description = "음주 여부", example = "false")
    private Boolean isDrinker;

    @Schema(description = "추위 민감 여부", example = "true")
    private Boolean isColdSensitive;

    @Schema(description = "더위 민감 여부", example = "false")
    private Boolean isHeatSensitive;

    @Schema(description = "룸메이트 흡연 선호", example = "false")
    private Boolean roommateSmokingPref;

    @Schema(description = "룸메이트 음주 선호", example = "true")
    private Boolean roommateDrinkingPref;
}

