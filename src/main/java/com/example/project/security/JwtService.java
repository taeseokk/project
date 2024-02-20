package com.example.project.security;

import com.example.project.dto.jwt.JwtResponseDto;
import com.example.project.entity.model.SingleResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;


public interface JwtService {

    String createAccessToken(String id);	// JWT 토큰 생성

    String createRefreshToken();			// Refresh token 생성

    void updateRefreshToken(String id, String refreshToken); // 유저 회원가입 시 refreshtoken이 발급되기 전이기 때문에, 로그인시 발급받은 refreshToken 저장

    void deleteRefreshToken(String id);		// Refresh token 삭제

    SingleResult<JwtResponseDto> sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);	// 로그인 시 AccessToken & RefreshToken을 헤더에 실어서 보냄

    void sendAccessToken(HttpServletResponse response, String accesstoken);		// AccessToken 재발급 시 헤더에 실어서 보내는 메서드

    void setAccessTokenHeader(HttpServletResponse response, String accessToken); // AccessToken 헤더 설정

    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);	// RefreshToken 헤더 설정

    boolean isTokenValid(String token);	// token 유효성 검사

    Optional<String> extractRefreshToken(HttpServletRequest request);	// 헤더에 담긴 토큰 형식 Bearer 형식을 토큰 값을 가져오기 위해 Bearer 제거

    Optional<String> extractAccessToken(HttpServletRequest request);	// 헤더에서 값을 가져온 후 Bearer를 제거하고 반환

    Optional<String> extractId(String accessToken);

}
//refresh token은 usern