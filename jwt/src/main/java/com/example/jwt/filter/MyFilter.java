package com.example.jwt.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(request.getMethod().equals("POST")){
            System.out.println("Post 요청됨");
            String HeaderAuth = request.getHeader("Authorization");
            System.out.println(HeaderAuth);
            System.out.println("필터1");

            if(HeaderAuth.equals("cos")){
                filterChain.doFilter(request,response);
            }else{
                PrintWriter outPrintWriter = response.getWriter();
                outPrintWriter.println("인증 안됨");
            }
        }



    }
}
