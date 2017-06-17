package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.UserAllocation;
import com.dreamchain.skeleton.service.UserAllocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class UserAllocationController {
    @Autowired
    UserAllocationService userAllocationService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(UserAllocationController.class.getName());

    @RequestMapping("/user_allocation")
    public ModelAndView main()  {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView model = new ModelAndView();
        String pageName = "user_allocation";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "user_allocation/user_allocationList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<UserAllocation> loadUserAllocationList() {

        List<UserAllocation> userAllocationList = new ArrayList();
        logger.info("Loading all allocation info: >> ");
        userAllocationList = userAllocationService.findAll();
        logger.info("Loading all allocation info: << total " + userAllocationList.size());
        return userAllocationList;
    }


    @RequestMapping(value = "user_allocation/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveUserAllocation(@RequestBody Map<String, Object> userAllocationInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new allocation: >>");
        objList = userAllocationService.save(userAllocationInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("userAllocation.save.success.msg"));
            successMsg = environment.getProperty("userAllocation.save.success.msg");
        }
        logger.info("creating new allocation: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "user_allocation/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteUserAllocation(@RequestBody String userAllocationId) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete allocation:  >> ");
        validationError = userAllocationService.delete(Long.parseLong(userAllocationId));
        if (validationError.length() == 0) successMsg = environment.getProperty("userAllocation.delete.success.msg");
        logger.info("Delete allocation:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "user_allocation/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateUserAllocation(@RequestBody Map<String, Object> categoryObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating allocation: >>");
        objList = userAllocationService.update(categoryObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("userAllocation.update.success.msg"));
            successMsg = environment.getProperty("userAllocation.update.success.msg");
        }
        logger.info("Updating allocation:  << " + successMsg + validationError);
        return objList;
    }
}
