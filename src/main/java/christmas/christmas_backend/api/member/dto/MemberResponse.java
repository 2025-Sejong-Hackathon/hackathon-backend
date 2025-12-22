package christmas.christmas_backend.api.member.dto;

import christmas.christmas_backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private String completedSemesters;

    @Schema(description = "사용자 상태")
    private String status;

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
                .completedSemesters(member.getCompletedSemesters())
                .status(member.getStatus().name())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}

