/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.standalone;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author kazim
 */
public class ServerMain {

    public static void main(String[] args) {
        try {
            class TestHandler extends AbstractHandler {

                @Override
                public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                    response.setContentType("text/html;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    baseRequest.setHandled(true);
                    response.getWriter().println("<h1>Hello World</h1>");
                }
            }

            Server server = new Server(Integer.parseInt(args[0]));
            server.setHandler(new TestHandler());
            
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
