package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;

@Controller
@PropertySource("classpath:config.properties")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    Environment environment;

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "failed", required = false) String failed,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username or password!");
        }
        return model;

    }

    @RequestMapping("/home")
    public ModelAndView main(HttpServletRequest request) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        ModelAndView model = new ModelAndView();
        String pageName = "main";
        String userName = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if(environment.getProperty("user.type.developer").equals(user.getUserType())) model.setViewName("developer_work_list");
        else model.setViewName(pageName);
        authenticationService.setSessionValue(request, user);
        return model;

    }

}

