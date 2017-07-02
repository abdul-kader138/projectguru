package com.dreamchain.skeleton.web;


import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.AuthenticationService;
import com.dreamchain.skeleton.service.ChangeRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


@PropertySource("classpath:config.properties")
@Controller
public class DeveloperWorkStatusController {

    @Autowired
    ChangeRequestService changeRequestService;
    @Autowired
    Environment environment;


    private static final Logger logger =
            LoggerFactory.getLogger(DeveloperWorkStatusController.class.getName());


    @RequestMapping(value = "developer_work_status/developer_work_statusList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ChangeRequest> loadChangeRequestList() {
        List<ChangeRequest> changeRequestList = new ArrayList();
        logger.info("Loading all change request info: >> ");
        changeRequestList = changeRequestService.findAllForDeveloper();
        logger.info("Loading all change request info: << total " + changeRequestList.size());
        return changeRequestList;
    }

}


