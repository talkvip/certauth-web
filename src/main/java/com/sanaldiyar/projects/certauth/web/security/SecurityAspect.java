/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.security;

import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 *
 * @author kazim
 */
@Component
@Aspect
public class SecurityAspect {

    Logger logger = Logger.getLogger(SecurityAspect.class);
    SecurityManager securityManager = SecurityManager.getSecurityManager();

    @Before("execution(* *.*(..)) && @annotation(needSecurity)")
    public void checkSecurity(JoinPoint joinPoint, NeedSecurity needSecurity) {
        try {
            logger.debug("checkSecurity is called");
            String roles[] = needSecurity.roles();

            if (securityManager.isAuthenticated()) {
                boolean flag = false;
                for (String role : roles) {
                    flag |= securityManager.isInRole(role);
                }
                if (!flag && roles.length > 0) {
                    HttpServletResponse response = RequestResponseContext.getHttpServletResponse();
                    logger.debug("Accessed by user with not sufficient roles");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accessed by user with not sufficient roles");
                }
            } else {
                HttpServletResponse response = RequestResponseContext.getHttpServletResponse();
                logger.debug("Accessed by not authenticated user");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accessed by not authenticated user");
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SecurityAspect.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
