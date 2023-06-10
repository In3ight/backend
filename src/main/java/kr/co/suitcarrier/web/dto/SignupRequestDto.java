package kr.co.suitcarrier.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.suitcarrier.web.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원가입 요청")
public class SignupRequestDto {

    private String name;
    private String email;
    private String nickname;
    private String password;
    private String contact;

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .password(password)
                .contact(contact)
                .build();
    }

}
