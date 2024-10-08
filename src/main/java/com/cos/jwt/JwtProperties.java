package com.cos.jwt;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public interface JwtProperties {
	
	String SECRET = "cos"; // 우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 60000*10; // 10분 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

}


