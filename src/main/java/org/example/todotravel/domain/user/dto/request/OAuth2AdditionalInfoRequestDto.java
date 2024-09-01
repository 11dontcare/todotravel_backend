package org.example.todotravel.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
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
public class OAuth2AdditionalInfoRequestDto {
    private String token;

    @Size(max = 8, message = "닉네임은 8자 내로 사용가능합니다.")
    @Pattern(regexp = "^[\\S]+$", message = "닉네임에 공백을 포함할 수 없습니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
