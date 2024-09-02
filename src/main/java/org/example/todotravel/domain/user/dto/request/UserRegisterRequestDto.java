package org.example.todotravel.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.user.entity.Gender;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto {

    @Size(max = 20, message = "아이디는 20자 내로 설정해주세요.")
    @Pattern(regexp = "^[\\S]+$", message = "아이디에 공백을 포함할 수 없습니다.")
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;

    // 통합 테스트 시 추가 필요
//    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//        message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @Size(max = 8, message = "닉네임은 8자 내로 사용가능합니다.")
    @Pattern(regexp = "^[\\S]+$", message = "닉네임에 공백을 포함할 수 없습니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Size(max = 17, message = "이름은 17자 내로 입력해주세요.")
    @Pattern(regexp = "^[\\S]+$", message = "이름에 공백을 포함할 수 없습니다.")
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "성별은 필수 입력 값입니다.")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "생년월일는 필수 입력 값입니다.")
    private LocalDate birthDate;
}
