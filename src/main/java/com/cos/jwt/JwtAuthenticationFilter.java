package com.cos.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음

// 		/login 요청해서 username,password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작을 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; // SecurityConfig에 IoC 등록해둔거 호출.

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 로그인 시도중");

        // 1. username, password 받아서
        User user;
        try {
//            BufferedReader br = request.getReader();
//            String input = null;
//            while ((input = br.readLine()) !=null) {
//                System.out.println(input);
//            }
            ObjectMapper om = new ObjectMapper(); // JSON 데이터를 파싱해줌.
            user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()); // 토큰생성

            // 2. 정상인지 로그인 시도 해보기. authenticationManager로 로그인 시도!!
            // PrincipalDetailsService 호출 됨. loadUserByUsername() 함수 실행된 후 정상이면 authentication이 리턴됨.
            // DB에 있는 username과 password가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken); // 토큰 넣어서 던짐, Authentication에 로그인한 정보 담김.

            // 3. PrincipalDetails를 세션에 담고(권한 관리를 위해서)
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨:" + principalDetails.getUser().getUsername()); // 로그인 정상적으로 되었다는 뜻.
            // Authentication에 객체가 session 영역에 저장해야하고 그 방법이 return 해주면 됨.
            // return 의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거.
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 단지 권한 처리 때문에 session에 넣어줌.

            // 4. JWT 토큰을 만들어서 응답해주면 됨.

            return authentication;			//세션에 저장됨

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨.
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다는 뜻임.");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA 방식은 아니고 Hash암호방식
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+(JwtProperties.EXPIRATION_TIME))) 
                .withClaim("id", principalDetails.getUser().getId())			
                .withClaim("username", principalDetails.getUser().getUsername())	
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); // HMAC512는 시크릿 키가 있어야 함.=>cos

        response.addHeader(JwtProperties.HEADER_STRING ,JwtProperties.TOKEN_PREFIX +jwtToken);
        
//     // RSA 방식은 아니고 Hash암호방식
//        String jwtToken = JWT.create()
//                .withSubject(principalDetails.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10))) 
//                .withClaim("id", principalDetails.getUser().getId())			
//                .withClaim("username", principalDetails.getUser().getUsername())	
//                .sign(Algorithm.HMAC512("cos")); // HMAC512는 시크릿 키가 있어야 함.
//
//        response.addHeader("Authorization","Bearer "+jwtToken);
   }
}
