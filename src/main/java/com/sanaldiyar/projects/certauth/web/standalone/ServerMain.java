/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.standalone;

import com.sanaldiyar.projects.certauth.web.security.SecurityFilter;
import java.util.EnumSet;
import java.util.Properties;
import javax.servlet.DispatcherType;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.GzipHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
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
            PropertyConfigurator.configure(ServerMain.class.getResourceAsStream("/WEB-INF/log4j.properties"));

            Slf4jLog log = new Slf4jLog();
            log.setDebugEnabled(true);
            Log.setLog(log);

            AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

            applicationContext.scan("com.sanaldiyar.projects.certauth.web");

            DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

            ServletContextHandler contextHandler = new ServletContextHandler();
            contextHandler.addServlet(new ServletHolder(dispatcherServlet), "/*");

            CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
            characterEncodingFilter.setEncoding("UTF-8");
            characterEncodingFilter.setForceEncoding(true);

            contextHandler.addFilter(new FilterHolder(characterEncodingFilter), "/*", null);

            SecurityFilter securityFilter = new SecurityFilter();
            contextHandler.addFilter(new FilterHolder(securityFilter), "/*", null);

            GzipHandler handler = new GzipHandler();
            handler.setHandler(contextHandler);


            int port = 60150;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            ThreadPool threadPool = new ExecutorThreadPool(5, 10, 1000 * 60 * 5);

            Server server = new Server(port);
            server.setSendDateHeader(true);
            server.setHandler(handler);
            server.setThreadPool(threadPool);

            server.start();
            server.join();
        } catch (Exception ex) {
        }

    }
}
