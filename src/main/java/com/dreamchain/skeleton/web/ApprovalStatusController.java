package com.dreamchain.skeleton.web;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PropertySource("classpath:config.properties")
public class ApprovalStatusController {

    @RequestMapping("/approval_details")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "approval_details";
        model.setViewName(pageName);
        return model;

    }
}
