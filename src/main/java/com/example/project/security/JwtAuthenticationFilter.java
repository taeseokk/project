package com.example.project.security;

import java.io.IOException;

import com.example.project.entity.member.Member;
import com.example.project.repository.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// 1. RefreshToken 없고 AccessToken 유효 -> 인증성공, RefreshToken 재발급 x
// 2. RefreshToken 없고 AccessToken 없거나 유효x -> 인증 실패 403 ERROR
// 3. RefreshToken 있는 경우 -> DB의 RefreshToken과 비교 -> 일치 AccessToken 재발급 and RefreshToken 재발급(RTR 방식) -> 인증 처리하지 않고 실패 처리


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);	// /test URI요청시 다음 필터 호출(현재 필터 넘어감)
            return;	// 다음 필터로 넘어가지 않고 진행을 막기 위해
        }

        //사용자 헤더에서 refreshtoken 추출
        String refresh = jwtService.extractRefreshToken(request)
                .filter(jwtService :: isTokenValid)
                .orElse(null);

        // refersh토큰 존재/access 만료
        if(refresh != null) {
            checkRefreshTokenAndNewAccessToken(response, refresh);
            return;
        }
        // Refresh가 없거나 유효하지 않다면, Access를 검사하고 인증을 처리하는 로직 수행
        // Access가 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // Access가 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if(refresh==null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    // refreshToken을 통해 DB에서 유저를 찾고, AccessToken 재발급
    public void checkRefreshTokenAndNewAccessToken(HttpServletResponse response, String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken).ifPresent(member -> {
            String newRefreshToken = newRefreshToken(member);
            jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(member.getId()),
                    newRefreshToken);
        });
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {

        log.info("checkAccessTokenAndAuthentication : ");
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid)	// filter를 이용하여 token 유형성 검사로 true 찾기
                .ifPresent(accessToken -> jwtService.extractId(accessToken)	// id 추출
                        .ifPresent(id -> memberRepository.findById(id)		// id를 통해 member를 찾고 파라미터로 member 정보를 넘겨서 member 인증 처리
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);

    }

    // [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드]
    // 리프레시 토큰 재발급 후 DB에 재발급한 리프레시 토큰 업데이트 후 Flush
    private String newRefreshToken(Member member) {
        String newRefreshToken = jwtService.createRefreshToken();	// token 생성
        member.setRefreshToken(newRefreshToken);	// 값을 수정할 때 영속성 컨텍스트에 있는 member, null로 저장되어 있는 refreshToken 값을 set을 이용하여 수정하고 save
        memberRepository.saveAndFlush(member);	// save() : 바로 DB에 저장되지 않고 영속성 컨텍스트에 저장 / saveAndFlush : 메소드 실행중에 즉시 data를 flush
        return newRefreshToken;
    }

    public void saveAuthentication(Member member) {

        UserDetails userDetails = User.builder().username(member.getId()).password(member.getPassword())
                .roles(member.getRole().name()).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities())); // 인증 객체인 Authentication 객체 생성

        SecurityContextHolder.getContext().setAuthentication(authentication);
        /*1. securityContextHolder.getContext를 이용하여 securityContext를 꺼낸다
         *2. setAuthentication을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리*/
    }
}