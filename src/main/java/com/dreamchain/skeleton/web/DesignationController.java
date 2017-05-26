package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Designation;
import com.dreamchain.skeleton.service.DesignationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
public class DesignationController {

    @Autowired
    DesignationService designationService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(DesignationController.class.getName());

    @RequestMapping("/designation")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "designation";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "designation/designationList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Designation> loadDesignationList() {
        List<Designation> designationList = new ArrayList();
        logger.info("Loading all designation info: >> ");
        designationList = designationService.findAll();
        logger.info("Loading all designation info: << total " + designationList.size());
        return designationList;
    }


    @RequestMapping(value = "designation/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveDesignation(@RequestBody Map<String, String> designationInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new designation: >>");
        objList = designationService.save(designationInfo.get("name"));
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("designation.save.success.msg"));
            successMsg = environment.getProperty("designation.save.success.msg");
        }
        logger.info("creating new designation: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "designation/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteDesignation(@RequestBody Map<String, String> designationInfo) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("designation Company:  >> ");
        validationError = designationService.delete(Long.parseLong(designationInfo.get("id")));
        if (validationError.length() == 0) successMsg = environment.getProperty("designation.delete.success.msg");
        logger.info("designation Company:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "designation/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateDesignation(@RequestBody Map<String, String> designationObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating designation: >>");
        objList = designationService.update(designationObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("designation.update.success.msg"));
            successMsg = environment.getProperty("designation.update.success.msg");
        }
        logger.info("Updating designation:  << " + successMsg + validationError);
        return objList;
    }

}
