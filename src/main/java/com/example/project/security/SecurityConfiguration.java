package com.example.project.security;

import com.example.project.repository.member.MemberRepository;

import org.junit.jupiter.api.Order;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;


import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@Configuration // 설정파일을 만들기 위한 or Bean등록하기 위한
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

 //   private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .httpBasic(HttpBasicConfigurer::disable)
//                .formLogin().disable() // spring에서 기본 제공하는 login 페이지 사용
//                .httpBasic().disable() // UI 사용하는 것을 기본값으로 가진 시큐리티 설정(JWT를 사용하기 때문에 disable)
                .csrf(CsrfConfigurer::disable)     // REST API에서는 CSRF 보안이 필요 없음(spring security에서는 csrf()메서드는 기본적으로 CSRF 토큰을 발급해서 클라이언트로부터 요청을 받을 때마다 토큰을 검증하는 방식)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// REST API기반 애플리케이션의 동작 방식을 설정(JWT 토큰으로 인증을 처리하는 경우 세션을 사용하지 않기 때문에 STATELESS)
                .authorizeHttpRequests(authorize ->authorize    // 애플리케이션에 들어오는 요청에 대한 사용 권한을 체크
                .requestMatchers("/member/signup").permitAll() // 해당 경로에 대해서는 모두에게 허용
                .anyRequest().authenticated());    // antMatchers 경로 이외에 기타 요청은 권한 필요

        // security 필터 순서가 logoutfilter 실행 후 로그인 필터가 동작
        // 순서 : LogoutFilter -> customUsernamePasswordFilter -> jwtAuthenticationFilter
        http.addFilterAfter(customUsernamePasswordFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(),CustomUsernamePasswordAuthenticationFilter.class);
    return http.build();
    }

    @Bean
    public PasswordEncoder passwordeEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordeEncoder());
//        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    // jwt 인증 처리를 담당하는 JWT인증 필터
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter jsonUseAuthenticationFilter = new JwtAuthenticationFilter(jwtService, memberRepository);

        return jsonUseAuthenticationFilter;
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, memberRepository);// 변경
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordFilter() {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordFilter = new CustomUsernamePasswordAuthenticationFilter(objectMapper);

        customUsernamePasswordFilter.setAuthenticationManager(authenticationManager());
        customUsernamePasswordFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customUsernamePasswordFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customUsernamePasswordFilter;
    }

}
