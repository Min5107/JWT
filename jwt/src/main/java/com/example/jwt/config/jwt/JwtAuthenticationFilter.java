package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.MyUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
// login 요청해서 username, password 전송 하면 (post)
// UsernamePasswordAuthenticationFilter가 동작함

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthFilter : 로그인 시도중");

        // 1. username, password 받아서
        try {
//            BufferedReader br = request.getReader();
//
//            String input = null;
//            while ((input = br.readLine()) != null){
//                System.out.println(input);
//            }

            ObjectMapper om = new ObjectMapper(); // Json으로 파싱해줌
            MyUser user = om.readValue(request.getInputStream(), MyUser.class);
            System.out.println(user);

            // 토큰 생성
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

// PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨
// DB에 있는 username과 password가 일치한다
            Authentication authentication =
                    authenticationManager.authenticate(usernamePasswordAuthenticationToken);

// authentication 객체에서 PrincipalDetails 가져오기
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUsername());

// authentication 객체가 session 영역에 저장을 해야하고 그방법이 return 해주면됨
// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        // 2. 정상인지 로그인 시도후 authenticationManger로 로그인 시도 하면
        // PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨
        // 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해)
        // 4. Jwt 토큰을 만들어서 응답

    }


    // attemptAuthentication 실행 후 인즈이 정상적으로 되었으면 successfulAuthentication 함수가 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT토큰을 response 해주면됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        // RSA방식은 아닌 Hash 암호 방식
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000*30)))

                .withClaim("username", principalDetails.getUsername())
                .sign(Algorithm.HMAC512("cos"));

       response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
