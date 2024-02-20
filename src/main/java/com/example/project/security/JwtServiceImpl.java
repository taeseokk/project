package com.example.project.security;

import java.util.Date;
import java.util.Optional;

import com.example.project.service.ResponseService;
import com.example.project.dto.jwt.JwtResponseDto;
import com.example.project.entity.model.SingleResult;
import com.example.project.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT; // java 8,11,17 LTS버전에서 지원
import com.auth0.jwt.algorithms.Algorithm;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.access.expiration}")
    private long accessTokenValidityInSeconds;
    @Value("${spring.jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;
    @Value("${spring.jwt.access.header}")
    private String accessHeader;
    @Value("${spring.jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "id";
    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;
    private final ResponseService responseService;

    @Override
    public String createAccessToken(String id) {
        String jwt = JWT.create() // JWT 토큰 생성 빌드 -> create
                .withSubject(ACCESS_TOKEN_SUBJECT) // jwt의 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000)) // jwt 만료시간(설정하지 않았을 경우 무한 지속)
                .withClaim(USERNAME_CLAIM, id) // jwt의 payload부분에서 private 설정(private의 이름과 내용을 작성 / 유저이름을 넣었을 시 이름 기반으로 유저 식별)
                .sign(Algorithm.HMAC512(secret)); // 어떤 해싱 알고리즘으로 해시하는지와 시크릿키를 사용하는지 결정하는 부분

        return jwt;
    }

    @Override
    public String createRefreshToken() {
        // 단순 토큰 자체가 유효한지를 확인하여 새로운 액세스 토큰을 발급하는 용도만 사용할 경우 claim작성하지 않아도 된다.
        String jwt = JWT.create().withSubject(REFRESH_TOKEN_SUBJECT) // jwt의 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000)) // jwt 만료시간(설정하지 않았을 경우 무한지속)
                .sign(Algorithm.HMAC512(secret)); // 어떤 해싱 알고리즘으로 해시하는지와 시크릿키를 사용하는지 결정하는 부분

        return jwt;
    }

    @Override
    public void updateRefreshToken(String id, String refreshToken) {
        memberRepository.findById(id).ifPresentOrElse(member -> member.setRefreshToken(refreshToken),
                () -> new Exception("정보 x"));

    }

    @Override
    public void deleteRefreshToken(String id) {
        memberRepository.findById(id).ifPresentOrElse(member -> memberRepository.deleteById(member.getIdx()),
                // member.setRefreshToken(null);
                () -> new Exception("정보 x"));

    }

    @Override // 로그인 시 AccessToken&RefreshToken을 헤더에 실어서 보내는 메소드
    public SingleResult<JwtResponseDto> sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("헤더 설정");
        JwtResponseDto jwt = JwtResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        log.info(accessToken);
        log.info(refreshToken);
        return responseService.getSingleResult(jwt);

    }

    @Override // AccessToken 재발급 시 헤더에 실어서 보내는 메소드
    public void sendAccessToken(HttpServletResponse response, String accesstoken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accesstoken);
        log.info("재발급 Access : ", accesstoken);

    }

    @Override // AccessToken 헤더 설정
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);

    }

    @Override // RefreshToken 헤더 설정
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);

    }

    @Override
    public boolean isTokenValid(String token) { // 서명 검증
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);   // 알고리즘 사용하여 서명 검증, -> 빌드를 통해 최종 JWT 검증객체(JWT
            return true;													// verifier) 반환 ->v erify 메서드를 통해 서명 확인(토큰 검증하고 유효하지 않으면 예외발생)

        } catch (Exception e) {
            e.getMessage();
            log.error("유효 x 토큰");
            return false;
        }

    }	// 1. JWT.require()로 토큰 유효성을 검사하는 로직이 있는 JWT verifier builder를 반환
    // 2. 반환된 builder를 사용하여 .verify(accessToken)로 token 검증

    // 토큰형식 : Bearer XXXX... 에서 Bearer를 제외하고 순수 토큰만 가져오기 위한 메서드
    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        Optional<String> token = Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));

        return token;
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        Optional<String> token = Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));

        return token;
    }

    @Override
    public Optional<String> extractId(String accessToken) {
        try {
            Optional<String> findId = Optional.ofNullable(JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(accessToken)
                    .getClaim(USERNAME_CLAIM)
                    .asString());
            return findId;
        }catch (Exception e) {
            log.error("액세스 토큰 x");
            return Optional.empty();
        }
    }


}
