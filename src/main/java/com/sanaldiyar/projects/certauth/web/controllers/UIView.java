/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 *
 * @author kazim
 */
public class UIView extends InternalResourceView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Determine which request handle to expose to the RequestDispatcher.
        HttpServletRequest requestToExpose = getRequestToExpose(request);

        // Expose the model object as request attributes.
        exposeModelAsRequestAttributes(model, requestToExpose);

        // Expose helpers as request attributes, if any.
        exposeHelpers(requestToExpose);

        // Determine the path for the request dispatcher.
        String dispatcherPath = prepareForRendering(requestToExpose, response);
        
        if(dispatcherPath.endsWith(".text")){
            this.setContentType("text/plain; charset=UTF-8;");
        } else if(dispatcherPath.endsWith(".js")){
            this.setContentType("text/javascript; charset=UTF-8;");
        } else if(dispatcherPath.endsWith(".css")){
            this.setContentType("text/css; charset=UTF-8;");
        } else {
            this.setContentType("text/html; charset=UTF-8;");
        }

        Scanner scanner;
        if (requestToExpose.getServletContext().getRealPath(".") != null) {
            scanner=new Scanner(new File(requestToExpose.getServletContext().getRealPath(".") + dispatcherPath),"UTF-8");
        } else {
            InputStream jspTemplate = this.getClass().getResourceAsStream(dispatcherPath);
            scanner = new Scanner(jspTemplate,"UTF-8");
        }


        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine().trim());
        }
        scanner.close();

        ByteArrayOutputStream outputStream = createTemporaryOutputStream();
        PrintStream printStream=new PrintStream(outputStream, true, "UTF-8");

        String aname, attr;
        int start;
        Enumeration<String> attributeNames = requestToExpose.getAttributeNames();
        for (; attributeNames.hasMoreElements();) {
            aname = attributeNames.nextElement();
            attr = requestToExpose.getAttribute(aname).toString();
            aname = "${" + aname + "}";
            start = sb.indexOf(aname);
            if (start == -1) {
                continue;
            }
            sb = sb.replace(start, start + aname.length(), attr);
        }

        printStream.print(sb.toString());

        printStream.flush();
        writeToResponse(response, outputStream);
    }
}
