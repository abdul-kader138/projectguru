package com.dreamchain.skeleton.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PropertySource("classpath:config.properties")
public class DeclineController {

    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(DeclineController.class.getName());

    @RequestMapping("/decline")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "decline_request";
        model.setViewName(pageName);
        return model;

    }
}
