/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
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

        Scanner scanner;
        if (requestToExpose.getServletContext().getRealPath(".") != null) {
            scanner=new Scanner(new File(requestToExpose.getServletContext().getRealPath(".") + dispatcherPath));
        } else {
            InputStream jspTemplate = this.getClass().getResourceAsStream(dispatcherPath);
            scanner = new Scanner(jspTemplate);
        }


        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine().trim());
        }
        scanner.close();

        ByteArrayOutputStream outputStream = createTemporaryOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);

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

        printWriter.write(sb.toString());

        printWriter.flush();
        writeToResponse(response, outputStream);
    }
}
