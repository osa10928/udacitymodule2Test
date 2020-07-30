package com.udacity.jwdnd.course1.cloudstorage.config;

import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    HttpSession httpSession;
    UserMapper userMapper;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public AuthenticationSuccessHandler(HttpSession httpSession, UserMapper userMapper) {
        super();
        this.httpSession = httpSession;
        this.userMapper = userMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Integer userid = userMapper.getUser(authentication.getName()).getUserid();
        httpSession.setAttribute("userid", userid);
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/home");
    }

}
