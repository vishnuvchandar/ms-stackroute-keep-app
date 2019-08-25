package com.stackroute.keepnote.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserAuthenticationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserAuthenticationController {

	/*
	 * Autowiring should be implemented for the UserAuthenticationService. (Use
	 * Constructor-based autowiring) Please note that we should not create an object
	 * using the new keyword
	 */
	@Autowired
	private UserAuthenticationService userAuthenticationService;

	public UserAuthenticationController(UserAuthenticationService authicationService) {
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED) - If the user created
	 * successfully. 2. 409(CONFLICT) - If the userId conflicts with any existing
	 * user
	 * 
	 * This handler method should map to the URL "/api/v1/auth/register" using HTTP
	 * POST method
	 */
	@PostMapping(value = "/api/v1/auth/register", produces = "application/json")
	public ResponseEntity<Map<String, Boolean>> register(@RequestBody User user) {
		Map<String, Boolean> map = new HashMap<>();
		try {
			userAuthenticationService.saveUser(user);

		} catch (UserAlreadyExistsException e) {
			map.put("isCreated", false);
			return new ResponseEntity<>(map, HttpStatus.CONFLICT);
		}
		map.put("isCreated", true);
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

	/*
	 * Define a handler method which will authenticate a user by reading the
	 * Serialized user object from request body containing the username and
	 * password. The username and password should be validated before proceeding
	 * ahead with JWT token generation. The user credentials will be validated
	 * against the database entries. The error should be return if validation is not
	 * successful. If credentials are validated successfully, then JWT token will be
	 * generated. The token should be returned back to the caller along with the API
	 * response. This handler method should return any one of the status messages
	 * basis on different situations: 1. 200(OK) - If login is successful 2.
	 * 401(UNAUTHORIZED) - If login is not successful
	 * 
	 * This handler method should map to the URL "/api/v1/auth/login" using HTTP
	 * POST method
	 */
	@PostMapping(value = "/api/v1/auth/login", produces = "application/json")
	public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
		String token;
		Map<String, String> map = new HashMap<>();
		try {
			userAuthenticationService.findByUserIdAndPassword(user.getUserId(), user.getUserPassword());
			token = getToken(user.getUserId(), user.getUserPassword());
			map.put("token", token);
		} catch (UserNotFoundException e) {
			map.put("errorMessage", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			map.put("errorMessage", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}
		return ResponseEntity.ok().body(map);
	}

// Generate JWT token
	public String getToken(String userId, String password) throws Exception {

		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("userId", userId);
		claims.put("password", password);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, "qwertyuiopasdfghjklzxcvbnm123456")
				.compact();

	}
	
	@PostMapping(value="/api/v1/auth/isAuthenticated", produces="application/json")
	public ResponseEntity<Map<String, Boolean>> validate(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		String username = null;
		String pwd = null;
		Claims claims;
		Map<String, Boolean> map = new HashMap<>();
		if (auth != null && StringUtils.hasText("Bearer") && auth.startsWith("Bearer")) {
			String token = auth.substring(7, auth.length());
			claims = Jwts.parser().setSigningKey("qwertyuiopasdfghjklzxcvbnm123456").parseClaimsJws(token)
					.getBody();
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(claims !=null) {
			username = claims.get("userId") != null ? claims.get("userId").toString():"";
			pwd = claims.get("password") != null ? claims.get("password").toString():"";
		}
		try {
			userAuthenticationService.findByUserIdAndPassword(username, pwd);
		} catch (UserNotFoundException e) {
			map.put("isAuthenticated", false);
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}
		map.put("isAuthenticated", true);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

}
