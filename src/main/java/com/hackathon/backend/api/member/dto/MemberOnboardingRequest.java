package com.hackathon.backend.api.member.dto;

import com.hackathon.backend.domain.member.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "회원 추가 정보 입력 요청")
public class MemberOnboardingRequest {

    @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @Schema(description = "생년월일", example = "2000-01-01")
    @NotNull(message = "생년월일은 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    @Schema(description = "흡연 여부", example = "false")
    @NotNull(message = "흡연 여부는 필수입니다.")
    private Boolean isSmoker;

    @Schema(description = "룸메이트 흡연 선호 여부", example = "true")
    @NotNull(message = "룸메이트 흡연 선호 여부는 필수입니다.")
    private Boolean roommateSmokingPref;

    @Schema(description = "룸메이트 음주 선호 여부", example = "false")
    @NotNull(message = "룸메이트 음주 선호 여부는 필수입니다.")
    private Boolean roommateDrinkingPref;

    @Schema(description = "음주 여부", example = "false")
    @NotNull(message = "음주 여부는 필수입니다.")
    private Boolean isDrinker;

    @Schema(description = "추위 민감 여부", example = "true")
    @NotNull(message = "추위 민감 여부는 필수입니다.")
    private Boolean isColdSensitive;

    @Schema(description = "더위 민감 여부", example = "false")
    @NotNull(message = "더위 민감 여부는 필수입니다.")
    private Boolean isHeatSensitive;
}

