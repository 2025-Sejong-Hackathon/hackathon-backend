package com.hackathon.backend.api.match.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "AI 서버 추천 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendRequest {

    @Schema(description = "학번", example = "22013565")
    @JsonProperty("student_id")
    private String studentId;

    @Schema(description = "나이", example = "26")
    @JsonProperty("age")
    private Integer age;

    @Schema(description = "성별", example = "MALE")
    @JsonProperty("gender")
    private String gender;

    @Schema(description = "전공", example = "컴퓨터공학과")
    @JsonProperty("major")
    private String major;

    @Schema(description = "흡연 여부", example = "true")
    @JsonProperty("is_smoker")
    private Boolean isSmoker;

    @Schema(description = "룸메이트 흡연 선호", example = "true")
    @JsonProperty("wants_smoker")
    private Boolean wantsSmoker;

    @Schema(description = "음주 여부", example = "true")
    @JsonProperty("is_drinker")
    private Boolean isDrinker;

    @Schema(description = "룸메이트 음주 선호", example = "true")
    @JsonProperty("wants_drinker")
    private Boolean wantsDrinker;

    @Schema(description = "더위 민감도", example = "true")
    @JsonProperty("sensitive_heat")
    private Boolean sensitiveHeat;

    @Schema(description = "추위 민감도", example = "true")
    @JsonProperty("sensitive_cold")
    private Boolean sensitiveCold;

    @Schema(description = "수면 습관", example = "0")
    @JsonProperty("sleep_habit")
    private Integer sleepHabit;

    @Schema(description = "기상 시간", example = "0")
    @JsonProperty("wake_up")
    private Integer wakeUp;

    @Schema(description = "활동 시간", example = "0")
    @JsonProperty("activity_time")
    private Integer activityTime;

    @Schema(description = "즉시 정리", example = "0")
    @JsonProperty("clean_immediate")
    private Integer cleanImmediate;

    @Schema(description = "책상 상태", example = "0")
    @JsonProperty("desk_status")
    private Integer deskStatus;

    @Schema(description = "청소 주기", example = "0")
    @JsonProperty("clean_cycle")
    private Integer cleanCycle;

    @Schema(description = "외출/복귀 연락", example = "0")
    @JsonProperty("out_return")
    private Integer outReturn;

    @Schema(description = "타인 영역 허용", example = "0")
    @JsonProperty("other_seat_tol")
    private Integer otherSeatTol;

    @Schema(description = "전화 소음", example = "0")
    @JsonProperty("phone_noise")
    private Integer phoneNoise;

    @Schema(description = "빛 민감도", example = "0")
    @JsonProperty("light_sensitivity")
    private Integer lightSensitivity;

    @Schema(description = "키보드/마우스 소음", example = "0")
    @JsonProperty("key_mouse_noise")
    private Integer keyMouseNoise;

    @Schema(description = "공간 프라이버시", example = "0")
    @JsonProperty("space_privacy")
    private Integer spacePrivacy;

    @Schema(description = "알람 습관", example = "0")
    @JsonProperty("alarm_habit")
    private Integer alarmHabit;

    @Schema(description = "사회성", example = "0")
    @JsonProperty("social_willingness")
    private Integer socialWillingness;

    @Schema(description = "친구 초대", example = "0")
    @JsonProperty("friend_invite")
    private Integer friendInvite;

    @Schema(description = "기숙사 체류", example = "0")
    @JsonProperty("dorm_stay")
    private Integer dormStay;
}

