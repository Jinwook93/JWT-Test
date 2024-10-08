package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.JwtAuthenticationFilter;
import com.cos.jwt.JwtAuthorizationFilter;
import com.cos.jwt.config.filter.MyFilter1;
import com.cos.jwt.config.filter.MyFilter3;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CorsFilter corsFilter;
	
	private final UserRepository userRepository;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {

    	
    	//http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);		// SecurityContextPersistenceFilte가 실행되기 전에 MyFilter3에 걸려라)
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음
            .and()
            .addFilter(corsFilter)		//CORS 문제 발생시  필터가 허용해줌 
            //Controller에 @CrossOrigin으로 해도 되지만 인증이 필요없는 경우에는 이렇게 해도 되지만, 
            //인증이 필요한 경우, 예를 들어 로그인 기능에는 @CrossOrigin만으로 해결이 되지 않음
            //즉 시큐리티 필터에 등록을 해줘야 해결이 된다
            .formLogin().disable()
            .httpBasic().disable()
           .addFilter(new JwtAuthenticationFilter(authenticationManager))	//AuthenticationManager
           .addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository))	
            .authorizeRequests()
            .requestMatchers("/api/v1/user/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") 
            .requestMatchers("/api/v1/manager/**")
            .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") 
            .requestMatchers("/api/v1/admin/**")
            .access("hasRole('ROLE_ADMIN')") 
            .anyRequest().permitAll(); // 모든 다른 요청 허용

        return http.build();
    }
}
