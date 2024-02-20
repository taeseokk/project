package com.example.project.security;

import com.example.project.service.ResponseService;
import com.example.project.dto.jwt.JwtResponseDto;
import com.example.project.entity.model.SingleResult;
import com.example.project.repository.member.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String id = extractUsername(authentication); // 인증 정보에서 id 추출
        String accessToken = jwtService.createAccessToken(id); // 추출한 id를 이용하여 AccessToken발급
        String refreshToken = jwtService.createRefreshToken(); // refreshToken 발급


        // 응답헤더에 AccessToken과 RefreshToken 값을 실어서 보낸다.
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // ifPresent : 값이 없으면 예외발생하면 넘어감
        memberRepository.findById(id).ifPresent(member -> {
            member.setRefreshToken(refreshToken);
            memberRepository.saveAndFlush(member);
        });

        log.info("로그인에 성공. userID: {}", id);
        log.info("AccessToken 을 발급 AccessToken: {}", accessToken);
        log.info("RefreshToken 을 발급 RefreshToken: {}", refreshToken);

    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    public SingleResult<?> viewJwt(String id, String accessToken, String refreshToken) {
        ResponseService responseService = new ResponseService();
        JwtResponseDto jwt = JwtResponseDto.builder().id(id).accessToken(accessToken).refreshToken(refreshToken)
                .build();
        return responseService.getSingleResult(jwt);
    }

}
/*
 * isPresent() : boolean -> Optional 값을 가지고 있다면 true, 없으면 false ifPresent() :
 * void -> Optional 값에 따라 객체가 있으면 실행, 없으면 넘어간다. isPresent()는 true,false만 체크 /
 * ifPresent는 값을 가지고 있는지 확인 후 예외처리를 통해 값이 넘어감
 */
