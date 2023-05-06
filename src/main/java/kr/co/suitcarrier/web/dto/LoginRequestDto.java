package kr.co.suitcarrier.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "액세스 토큰")
@Getter
public class LoginRequestDto {

    private String email;
    private String password;
    
}
