/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.controllers;

import com.sanaldiyar.projects.certauth.web.data.LoginData;
import com.sanaldiyar.projects.certauth.web.security.SecurityManager;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author kazim
 */
@Controller
public class DefaultViewController {

    @Autowired
    ServletContext context;
    
    

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String getIndex(ModelMap map) {
        //SecurityManager.getSecurityManager().setSecurityToken("kazim", new String[]{"admin"});
        return "index.html";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getWelcome(ModelMap map) {
        return "redirect:index.html";
    }

    @RequestMapping(value = "/{script}.js", method = RequestMethod.GET)
    public String getJQuery(@PathVariable(value = "script") String script) {
        return script + ".js";
    }

    @RequestMapping(value = "/{css}.css", method = RequestMethod.GET)
    public String getCSS(@PathVariable(value = "css") String css) {
        return css + ".css";
    }

    
    @RequestMapping(value = "/{content}.text", method = RequestMethod.GET)
    public @ResponseBody
    String getContent(@PathVariable(value = "content") String content) {
        try {
            String path = "/WEB-INF/data/" + content + ".text";
            Scanner scanner;
            if (context.getRealPath(".") != null) {
                scanner = new Scanner(new File(context.getRealPath(".")  + path), "UTF-8");
            } else {
                scanner = new Scanner(this.getClass().getResourceAsStream(path),"UTF-8");
            }
            StringBuilder sb=new StringBuilder();
            
            while(scanner.hasNextLine()){
                sb.append(scanner.nextLine());
            }
            scanner.close();
            return sb.toString();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error at sending content " + content, ex);
        }

        return "Error getting content " + content;
    }
    

    
    @RequestMapping(value="/login.html",method= RequestMethod.POST)
    public @ResponseBody boolean tryLoging(@RequestBody LoginData loginData){
        SecurityManager.getSecurityManager().setSecurityToken(loginData.getUsername(), new String[]{});
        return true;
    }
    
    @RequestMapping(value="/logout.html",method= RequestMethod.GET)
    public @ResponseBody boolean tryLogout(){
        SecurityManager.getSecurityManager().removeAuthenticationToken();
        return true;
    }
    
    @RequestMapping(value="/loginstatus.html",method= RequestMethod.GET)
    public @ResponseBody boolean getLoginStatus(){
        return SecurityManager.getSecurityManager().isAuthenticated();
    }
}
