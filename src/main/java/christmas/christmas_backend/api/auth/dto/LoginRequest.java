package christmas.christmas_backend.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Schema(description = "학번", example = "22013565")
    @NotBlank(message = "학번은 필수입니다.")
    private String studentId;

    @Schema(description = "비밀번호", example = "password123")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}

