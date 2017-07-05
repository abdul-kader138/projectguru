package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.service.ApprovalStatusService;
import com.dreamchain.skeleton.service.DeclineRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class DeclineController {

    @Autowired
    Environment environment;

    @Autowired
    DeclineRequestService declineRequestService;

    @Autowired
    ApprovalStatusService approvalStatusService;


    static final Logger logger =
            LoggerFactory.getLogger(DeclineController.class.getName());

    @RequestMapping(value="/decline")
    public ModelAndView main(@RequestParam("a_id") String approvalId,@RequestParam("r_id") String requestId,
                             @RequestParam("ver") String ver)  {
        String pageName = "decline_request";
        ModelAndView modelAndView = new ModelAndView(pageName);
        Long approveId=Long.parseLong(approvalId);
        Long requestedId=Long.parseLong(requestId);
        Long version=Long.parseLong(ver);
        ApprovalStatus approvalStatus=approvalStatusService.get(approveId);
        modelAndView.addObject("approveId", approvalId);
        modelAndView.addObject("requestedId", requestedId);
        modelAndView.addObject("companyName",approvalStatus.getCategory().getCompany().getName());
        modelAndView.addObject("productName",approvalStatus.getCategory().getProduct().getName());
        modelAndView.addObject("categoryName",approvalStatus.getCategory().getName());
        modelAndView.addObject("name",approvalStatus.getRequestName());
        modelAndView.addObject("version",version);
        modelAndView.setViewName(pageName);
        return modelAndView;

    }


    @RequestMapping(value = "decline_request/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveCategory(@RequestBody Map<String, Object> DeclineInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new category: >>");
        objList = declineRequestService.save(DeclineInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("category.save.success.msg"));
            successMsg = environment.getProperty("category.save.success.msg");
        }
        logger.info("creating new category: << " + successMsg + validationError);
        return objList;
    }



}
