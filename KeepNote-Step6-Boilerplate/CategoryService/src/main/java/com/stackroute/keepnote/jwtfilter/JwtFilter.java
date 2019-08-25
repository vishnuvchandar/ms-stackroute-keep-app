package com.stackroute.keepnote.jwtfilter;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/* This class implements the custom filter by extending org.springframework.web.filter.GenericFilterBean.  
 * Override the doFilter method with ServletRequest, ServletResponse and FilterChain.
 * This is used to authorize the API access for the application.
 */

public class JwtFilter extends GenericFilterBean {

	/*
	 * Override the doFilter method of GenericFilterBean. Retrieve the
	 * "authorization" header from the HttpServletRequest object. Retrieve the
	 * "Bearer" token from "authorization" header. If authorization header is
	 * invalid, throw Exception with message. Parse the JWT token and get claims
	 * from the token using the secret key Set the request attribute with the
	 * retrieved claims Call FilterChain object's doFilter() method
	 */

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, DELETE, OPTIONS, POST, PUT, UPDATE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization");
		httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
		String auth = httpServletRequest.getHeader("Authorization");
		String token;
		if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			if (auth != null && StringUtils.hasText("Bearer") && auth.startsWith("Bearer")) {
				token = auth.substring(7, auth.length());
				Claims claims = Jwts.parser().setSigningKey("qwertyuiopasdfghjklzxcvbnm123456").parseClaimsJws(token)
						.getBody();
				request.setAttribute("claims", claims);
			} else {
				throw new IllegalArgumentException("Error parsing Auth header");
			}
			chain.doFilter(request, response);
		}

	}
}
