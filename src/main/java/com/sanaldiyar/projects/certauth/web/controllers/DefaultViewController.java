/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sanaldiyar.projects.certauth.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author kazim
 */
@Controller
public class DefaultViewController {
    
    @RequestMapping(value="/index.html",method= RequestMethod.GET)
    public String getIndex(ModelMap map){
        map.addAttribute("name", "World");
        return "index.html";
    }
    
    @RequestMapping(value="/",method= RequestMethod.GET)
    public String getWelcome(ModelMap map){
        return "redirect:index.html";
    }
    
    @RequestMapping(value="/jquery-1.8.2.min.js",method= RequestMethod.GET)
    public String getJQuery(){
        return "jquery-1.8.2.min.js";
    }
    
    @RequestMapping(value="/main.css",method= RequestMethod.GET)
    public String getCSS(){
        return "main.css";
    }
    
}
