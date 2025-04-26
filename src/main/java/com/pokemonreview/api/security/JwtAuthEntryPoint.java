package com.pokemonreview.api.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * This method is called when an exception is thrown due to an unauthenticated user trying to access a protected resource.
     * It sends a 401 Unauthorized response to the client with the exception message.
     *
     * @param request       The HttpServletRequest object that contains the request made by the client.
     * @param response      The HttpServletResponse object that contains the response to be sent to the client.
     * @param authException The AuthenticationException that was thrown.
     * @throws IOException      If an input or output error occurs while handling the request or response.
     * @throws ServletException If a servlet error occurs while handling the request or response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
