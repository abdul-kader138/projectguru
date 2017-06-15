package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.User;
import org.apache.commons.logging.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;

import javax.management.modelmbean.ModelMBean;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Controller
public class AuthenticationController {

//	private Log


    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "failed", required = false) String failed,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        HttpSession httpSession=request.getSession();
        String pageName = "main";
        if (error != null) {
            model.addObject("error", "Invalid username or password!");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return model;

    }

    @RequestMapping("/home")
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        ModelAndView model = new ModelAndView();
        String pageName = "main";
        String userName = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        HttpSession httpSession=request.getSession();
        model.setViewName(pageName);
        httpSession.setAttribute("name",user.getName());
        httpSession.setAttribute("email",user.getEmail());
        httpSession.setAttribute("role",user.getRole());
        httpSession.setAttribute("path",user.getImagePath());
        httpSession.setAttribute("userType",user.getUserType());
        return model;

    }





}

