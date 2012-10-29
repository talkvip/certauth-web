/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kazim
 */
public class RequestResponseContext {
    
    private static ThreadLocal<HttpServletRequest> local_request=new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> local_response=new ThreadLocal<HttpServletResponse>();
    
    public static void setRequestResponse(HttpServletRequest request,HttpServletResponse response){
        local_request.set(request);
        local_response.set(response);
    }
    
    public static void clear(){
        local_request.remove();
        local_response.remove();
    }
    
    public static HttpServletRequest getHttpServletRequest(){
        return local_request.get();
    }
    
    public static HttpServletResponse getHttpServletResponse(){
        return local_response.get();
    }
    
}
