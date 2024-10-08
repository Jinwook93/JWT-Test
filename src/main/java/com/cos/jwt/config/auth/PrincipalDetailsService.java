package com.cos.jwt.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//http://localhost:8081/login 스프링 시큐리티 기본 로그인 요청 주소 => formLogin.disabled로 로그인을 안쓰기로 하였으니 동작이 되지 않는다
//따라서 PrincipalDetailsService를 동작시키는 필터를 만들어야 한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService  loadUserByUsername");
		User userEntity = userRepository.findByUsername(username);
		
		return new PrincipalDetails(userEntity);
	}
	
	
}
