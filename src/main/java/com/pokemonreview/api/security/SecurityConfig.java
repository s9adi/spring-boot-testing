package com.pokemonreview.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

@SuppressWarnings("all")
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    /*
     * CSRF, or Cross-Site Request Forgery, is a web security vulnerability that
     * allows an attacker to trick a user
     * into performing actions on a web application without their knowledge or
     * consent. Spring Security provides
     * built-in protection against CSRF attacks, enabled by default. When CSRF
     * protection is enabled, Spring generates
     * a unique token for each user session. This token must be included in all
     * state-changing requests
     * (e.g., POST, PUT, DELETE). The server validates this token before processing
     * the request,
     * ensuring that it originated from the application itself and not a malicious
     * site.
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                /*
                 * The below line configures how the spring should handle the Authentication
                 * related exceptions.
                 * The JwtAuthEntryPoint class is used to handle the authentication exceptions
                 * and send the appropriate response to the client.
                 * 
                 * .exceptionHandling():
                 * This method initializes an ExceptionHandlingConfigurer, which allows you to
                 * customize how exceptions are handled in the security context.
                 * It provides options to define behaviors for various security-related
                 * exceptions, such as unauthorized access or authentication failures.
                 * 
                 * .authenticationEntryPoint(authEntryPoint):
                 * This method sets a custom AuthenticationEntryPoint to handle authentication
                 * failures. The AuthenticationEntryPoint is a strategy interface
                 * in Spring Security that determines what happens when a user tries to access a
                 * secured resource without being authenticated. For example,
                 * it might return a 401 Unauthorized response or redirect the user to a login
                 * page. In this case, the authEntryPoint object
                 * (likely a custom implementation) is injected and used to define this
                 * behavior.
                 * 
                 * .and():
                 * The and() method is used to return to the parent HttpSecurity configuration
                 * after configuring exception handling. This allows you to chain
                 * additional security configurations, such as session management, authorization
                 * rules, or HTTP basic authentication.
                 * 
                 * Why This Matters:
                 * This configuration is essential for defining a consistent and secure way to
                 * handle authentication failures. By specifying a custom
                 * AuthenticationEntryPoint, the application can provide meaningful feedback to
                 * unauthenticated users, such as an error message or a
                 * redirect to a login page. The use of method chaining
                 * (.exceptionHandling().authenticationEntryPoint().and()) ensures that the
                 * configuration
                 * remains fluent and readable, making it easier to manage complex security
                 * setups.
                 */

                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()

                /*
                 * The session management configuration in Spring Security is used to define
                 * how the application should handle user sessions.
                 * In this case, the session management is set to stateless, meaning that the
                 * server will not store any session information for the user.
                 * This is particularly useful for RESTful APIs, where each request is treated
                 * independently and does not rely on a session state.
                 */

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                
                /*
                 * The userDetailsService method is used to set a custom UserDetailsService
                 * implementation that will be used to load user-specific data.
                 * In this case, the CustomUserDetailsService is injected and set as the
                 * UserDetailsService for the security context.
                 * This allows Spring Security to retrieve user details (such as username,
                 * password, and roles) from the database or any other source
                 * when authenticating users.
                 */


                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()

                /*
                 * The httpBasic() method enables HTTP Basic authentication for the application.
                 * This means that users will be prompted to enter their username and password
                 * when accessing secured resources. The credentials will be sent in the
                 * Authorization header of the HTTP request.
                 * 
                 * Why This Matters:
                 * Enabling HTTP Basic authentication is a simple way to secure RESTful APIs,
                 * especially when combined with HTTPS. It allows clients to authenticate using
                 * standard HTTP headers, making it easy to integrate with various clients and
                 * libraries.
                 */
                .httpBasic(); // ?
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
