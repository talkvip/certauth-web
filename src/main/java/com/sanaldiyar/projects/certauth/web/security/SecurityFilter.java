/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kazim
 */
public class SecurityFilter implements Filter {

    private SecurityManager securityManager=SecurityManager.getSecurityManager();

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) baseRequest;
        HttpServletResponse response = (HttpServletResponse) baseResponse;
        
        RequestResponseContext.setRequestResponse(request, response);

        securityManager.checkAndRefleshSecurityToken();

        chain.doFilter(request, response);
        
        RequestResponseContext.clear();
    }

    @Override
    public void destroy() {
    }
}
