package com.cos.jwt.config.filter;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;


public class MyFilter1 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletRequest res = (HttpServletRequest) response;
		
		System.out.println("필터1");
		
//		PrintWriter out = response.getWriter();
//		out.print("안녕");					//<----필터가 끝나버림
		
		chain.doFilter(request, response);  //계속 프로세스 과정을 진행해라
		
	}

}







//public class MyFilter1 implements Filter{			수정 전
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		System.out.println("필터1");
//		
////		PrintWriter out = response.getWriter();
////		out.print("안녕");					//<----필터가 끝나버림
//		
//		chain.doFilter(request, response);  //계속 프로세스 과정을 진행해라
//		
//	}
//
//}
