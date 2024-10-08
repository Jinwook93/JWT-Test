package com.cos.jwt.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

@RestController
public class RestApiController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
	@GetMapping("home")
	public String home () {
		return "<h1>home</h1>";
	}
	
	@PostMapping("token")
	public String token () {
		return "<h1>token</h1>";
	}
	
	@GetMapping("admin/users")
	public List<User> users(){
		return userRepository.findAll();
	}
	@PostMapping("join")
	public String join (@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return "<h1>token</h1>";
	}
	//user,manager,admin 접근 가능
//	@GetMapping("/api/v1/user")
//	public String user() {
//		return "user";
//	}
	@GetMapping("/api/v1/user")
	public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
//        System.out.println("principal : " + principal.getUser().getId());
//        System.out.println("principal : " + principal.getUser().getUsername());
//        System.out.println("principal : " + principal.getUser().getPassword());

        return "ID : " + principal.getUser().getId() + "\n이름 : " + principal.getUser().getUsername() + "\n패스워드 : " + principal.getUser().getPassword();
    }
	//manager,admin만 접근 가능
	@GetMapping("/api/v1/manager")
	public String manager() {
		return "manager";
	}
	//admin만 접근 가능
	@GetMapping("/api/v1/admin")
	public String admin() {
		return "admin";
	}
	
}
