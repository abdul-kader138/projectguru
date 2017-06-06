package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Roles;
import com.dreamchain.skeleton.service.RolesService;
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
public class RolesController {

    @Autowired
    RolesService rolesService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(CompanyController.class.getName());

    @RequestMapping("/roles")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "roles";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "roles/rolesList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Roles> loadProductCategoryList() {

        List<Roles> rolesList = new ArrayList();
        logger.info("Loading all roles info: >> ");
        rolesList = rolesService.findAll();
        logger.info("Loading all roles info: << total " + rolesList.size());
        return rolesList;
    }


    @RequestMapping(value = "roles/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveCompany(@RequestBody Map<String, Object> rolesInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new roles: >>");
        objList = rolesService.save(rolesInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("role.save.success.msg"));
            successMsg = environment.getProperty("role.save.success.msg");
        }
        logger.info("creating new roles: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "roles/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteCompany(@RequestBody String id) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";

        String validationError = "";
        logger.info("Delete roles:  >> ");
        validationError = rolesService.delete(Long.parseLong(id));
        if (validationError.length() == 0) successMsg = environment.getProperty("role.delete.success.msg");
        logger.info("Delete roles:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "roles/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateCompany(@RequestBody Map<String, Object> productCategoryObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating roles: >>");
        objList = rolesService.update(productCategoryObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("role.update.success.msg"));
            successMsg = environment.getProperty("role.update.success.msg");
        }
        logger.info("Updating roles:  << " + successMsg + validationError);
        return objList;
    }


}
