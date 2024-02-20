package com.example.project.dto.jwt;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {

    private String id;
    private String accessToken;
    private String refreshToken;
}
