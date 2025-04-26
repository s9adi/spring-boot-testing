package com.pokemonreview.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    /*
     * The OncePerRequestFilteris base class for filters that performs the filtering before spring security
     * processes the request. It ensures that the filter is only executed once per request.
     * This is important for performance and to avoid multiple
     * executions of the same filter for a single request.
     * 
     * The doFilterInternal method is called for each request and is where the
     * filtering logic is implemented.
     */

    @Autowired
    private JWTGenerator tokenGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

/*
 * The doFilterInternal method is overridden to implement the filtering logic.
 * It retrieves the JWT token from the request, validates it, and if valid, 
 * sets the authentication in the security context.
 * This allows the application to recognize the user and their roles for authorization purposes.
 * The filter chain is then continued to process the request.
 * If the token is invalid or not present, the filter chain is still continued without setting any authentication.  
 * This allows the request to proceed without authentication, which is useful for public endpoints.
 * The filter chain is responsible for passing the request to the next filter in the chain or to the target resource.
 * The filter chain is typically managed by the Spring Security framework.
 * 
 */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /*
         * Fetched the token from the request head and then validate it if it is valid or not.
         * If it is valid then set the authentication in the security context.
         * The security context is a container for security-related information about the current user.
         * This Userdetails object contains the user's username, password, and granted authorities (roles).
         * The granted authorities are used by Spring Security to determine the user's permissions and access rights.
         */
        String token = getJWTFromRequest(request);
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            String username = tokenGenerator.getUsernameFromJWT(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            /*
             * The UsernamePasswordAuthenticationToken is a Spring Security class that represents an authentication token
             * containing the user's credentials (username and password) and their granted authorities (roles).
             * It is used to authenticate the user and set their security context. Basically , it is used to store the user's
             * authentication information in the security context.
             */
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
        
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // can be done without this line as well
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // setting the current context with the authentication token and authentication details
            // The SecurityContextHolder is a Spring Security class that holds the security context for the current thread.
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
