package com.pokemonreview.api.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
//import java.security.KeyPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JWTGenerator {
	//private static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	// HS512 is a symmetric algorithm, so we can use the same key for signing and verifying the token.
	// here we are using a symmetric key, so we can use the same key for signing and verifying the token.
	// If we want to use asymmetric key, we can use the following code:
	//private static final Key key = keyPair.getPrivate();

	// key variable here is symmetric key and it is used to sign the JWT token along with verifying the same JWT token
	// that we recieve from the client side. Here we are merely generating the Key that can be used to sign the token and verify the token.
	// The key here is curated to be compatable with the HS512 algorithm. However it does not sign any data.
	// Keys.secretKeyFor(SignatureAlgorithm.HS512) is used to generate a symmetric key for signing the JWT token using HS512 algorithm.
	// By declaring the key variable as static final, we are making sure that the key is only generated once and is reused for all the JWT tokens.
	// This is important for security reasons, as it prevents the key from being exposed to the client side.
	// private identifier prevents it from getting exposed and prevent security vulnerabilities.
	// The key is used to sign the JWT token, and it is also used to verify the JWT token when it is received from the client side.
	

	/*
	 * * This method is used to generate a JWT token using the username and the current date.
	 */
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
		
		String token = Jwts.builder()
				.setSubject(username)
				.setIssuedAt( new Date())
				.setExpiration(expireDate)
				.signWith(key,SignatureAlgorithm.HS512) // here we are using HS512 algorithm to sign the JWT token.
				.compact();
		System.out.println("New token :");
		System.out.println(token);
		return token;
	}




	public String getUsernameFromJWT(String token){
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
		}
	}

}
