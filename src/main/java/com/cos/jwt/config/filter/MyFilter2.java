package com.cos.jwt.config.filter;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class MyFilter2 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("필터2");
		
//		PrintWriter out = response.getWriter();
//		out.print("안녕");					//<----필터가 끝나버림
		
		chain.doFilter(request, response);  //계속 프로세스 과정을 진행해라
		
	}

}
