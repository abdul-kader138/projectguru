package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.aoptest.BeforeAop;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.UserService;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller

public class SecretController {

	

    @RequestMapping("/tables")
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        ModelAndView model = new ModelAndView();
        String pageName = "tables";
        model.setViewName(pageName);
        return model;

    }
}
