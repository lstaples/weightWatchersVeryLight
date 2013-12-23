package com.wwvl;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


public class NonRedirectingEntryPoint extends LoginUrlAuthenticationEntryPoint {


    public NonRedirectingEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public final void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException )
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
