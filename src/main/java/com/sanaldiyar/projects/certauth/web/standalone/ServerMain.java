/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.standalone;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.GzipHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author kazim
 */
public class ServerMain {

    public static void main(String[] args) {
        try {
            StdErrLog log = new StdErrLog();
            log.setLevel(StdErrLog.LEVEL_ALL);
            Log.setLog(log);

            AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

            applicationContext.scan("com.sanaldiyar.projects.certauth.web.controllers", "com.sanaldiyar.projects.certauth.web.standalone");

            DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

            ServletContextHandler contextHandler = new ServletContextHandler();
            contextHandler.addServlet(new ServletHolder(dispatcherServlet), "/*");

            CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
            characterEncodingFilter.setEncoding("UTF-8");
            characterEncodingFilter.setForceEncoding(true);

            contextHandler.addFilter(new FilterHolder(characterEncodingFilter), "/*", null);

            GzipHandler handler=new GzipHandler();
            handler.setHandler(contextHandler);
            
            int port = 60150;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            Server server = new Server(port);
            server.setHandler(handler);



            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
