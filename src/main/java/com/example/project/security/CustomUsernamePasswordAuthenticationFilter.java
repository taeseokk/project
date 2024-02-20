package com.example.project.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // "/login"으로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private static final String USERNAME_KEY = "id"; // 회원 로그인 시 이메일 요청 JSON Key : "email"
    private static final String PASSWORD_KEY = "password"; // 회원 로그인 시 비밀번호 요청 JSon Key : "password"
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
            DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // "/login" + POST로 온 요청에 매칭된다.

    // Json 데이터를 파싱하거나 Object를 json으로 변환할 때 사용되는 라이브러리
    private final ObjectMapper objectMapper;

    // super()를 통해 부모클래스인 AbstractAuthenticationProcessingFilter의 생성자 파라미터로 login 설정
    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 login + post로 온 요철을 처리하기 위해 설정
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        // 들어온 요청에 contentType이 null이거나 설정한 json이 아닌 경우 예외를 발생시킨다.
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("contenct-type x" + request.getContentType());
        }
		/* StreamUtils를 통해 requestt에서 messageBody(Json) 복사 ->content-body를 통해 단순 텍스트를 얻는 방식
		   inputStream : HTTP 요청 메시지 바디의 내용을 조회
		   Stream은 bytecode이기 때문에 String으로 바꿀 때 어떤 인토딩으로 바꿀건지 설정 */
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        // ObjectMapper.readValue : json파일을 java객체로 역직렬화하여 map으로 변환
        @SuppressWarnings("unchecked")
        Map<String, String> map = objectMapper.readValue(messageBody, Map.class);

        // map의 key로 해당 id와 패스워드 추출
        String id = map.get(USERNAME_KEY);
        String password = map.get(PASSWORD_KEY);

		/* usernamePasswordAuthenticationToken의 파라미터 principal, credentials에 대입
		   authRequest : 인증 처리 객체인 AuthenticationManager가 인증 시 사용할 객체 */
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(id, password);

        // AuthenticationManager는 provicerManager
        return getAuthenticationManager().authenticate(authRequest);
    }

}
