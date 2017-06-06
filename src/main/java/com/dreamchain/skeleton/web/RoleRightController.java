package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.RoleRight;
import com.dreamchain.skeleton.service.RoleRightService;
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
public class RoleRightController {

    @Autowired
    RoleRightService roleRightService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(RoleRightController.class.getName());

    @RequestMapping("/role_right")
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        String pageName = "role_right";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "role_right/role_rightList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<RoleRight> loadRoleRightList() {

        List<RoleRight> roleRightList = new ArrayList();
        logger.info("Loading all role_right info: >> ");
        roleRightList = roleRightService.findAll();
        logger.info("Loading all role_right info: << total " + roleRightList.size());
        return roleRightList;
    }


    @RequestMapping(value = "role_right/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveRoleRight(@RequestBody Map<String, Object> roleInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";

        logger.info("creating new role_right: >>");
        objList = roleRightService.save(roleInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("role_right.save.success.msg"));
            successMsg = environment.getProperty("role_right.save.success.msg");
        }
        logger.info("creating new role_right: << " + successMsg + validationError);
        return objList;

    }


    @RequestMapping(value = "role_right/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteRoleRight(@RequestBody String RoleId) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete role_right:  >> ");
        validationError = roleRightService.delete(Long.parseLong(RoleId));
        if (validationError.length() == 0) successMsg = environment.getProperty("role_right.delete.success.msg");
        logger.info("Delete role_right:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "role_right/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateRoleRight(@RequestBody Map<String, Object> roleObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating role: >>");
        objList = roleRightService.update(roleObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("role_right.update.success.msg"));
            successMsg = environment.getProperty("role_right.update.success.msg");
        }
        logger.info("Updating role:  << " + successMsg + validationError);
        return objList;
    }
}
