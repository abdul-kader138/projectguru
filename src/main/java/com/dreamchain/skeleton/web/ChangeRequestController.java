package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.service.ChangeRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class ChangeRequestController {
    @Autowired
    ChangeRequestService changeRequestService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(ChangeRequest.class.getName());

    @RequestMapping("/change_request")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "change_request";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "change_request/change_requestList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ChangeRequest> loadChangeRequestList() {

        List<ChangeRequest> changeRequestList = new ArrayList();
        logger.info("Loading all change request info: >> ");
        changeRequestList = changeRequestService.findAll();
        logger.info("Loading all change request info: << total " + changeRequestList.size());
        return changeRequestList;
    }


    @RequestMapping(value = "change_request/save", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody
    Map saveCompany(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new change request: >>");
        objList = changeRequestService.save(request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("request.save.success.msg"));
            successMsg = environment.getProperty("request.save.success.msg");
        }
        logger.info("creating new change request: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value="/change_request_view")
    public ModelAndView main(@RequestParam("r_id") String requestId)  {
        String pageName = "change_request_view";
        ModelAndView modelAndView = new ModelAndView(pageName);
        Long requestedId=Long.parseLong(requestId);
        ChangeRequest changeRequest=changeRequestService.get(requestedId);
        modelAndView.addObject("changeRequest", changeRequest);
        return modelAndView;

    }

}
