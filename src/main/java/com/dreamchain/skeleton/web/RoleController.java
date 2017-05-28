package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ProductSubCategory;
import com.dreamchain.skeleton.model.Role;
import com.dreamchain.skeleton.service.ProductSubCategoryService;
import com.dreamchain.skeleton.service.RoleService;
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
public class RoleController {

    @Autowired
    RoleService roleService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(RoleController.class.getName());

    @RequestMapping("/role")
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        String pageName = "role";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "role/roleList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Role> loadRoleList() {

        List<Role> roleList = new ArrayList();
        logger.info("Loading all subcategory info: >> ");
        roleList = roleService.findAll();
        logger.info("Loading all subcategory info: << total " + roleList.size());
        return roleList;
    }


    @RequestMapping(value = "role/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveProductSubCategory(@RequestBody Map<String, String> roleInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new role: >>");
        objList = roleService.save(roleInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("role.save.success.msg"));
            successMsg = environment.getProperty("role.save.success.msg");
        }
        logger.info("creating new role: << " + successMsg + validationError);
        return objList;

    }


    @RequestMapping(value = "role/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteCompany(@RequestBody Map<String, Object> RoleInfo) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete role:  >> ");
        Integer id=(Integer)RoleInfo.get("id");
        validationError = roleService.delete(id.longValue());
        if (validationError.length() == 0) successMsg = environment.getProperty("role.delete.success.msg");
        logger.info("Delete role:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "role/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateCompany(@RequestBody Map<String, String> roleObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating role: >>");
        objList = roleService.update(roleObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("role.update.success.msg"));
            successMsg = environment.getProperty("role.update.success.msg");
        }
        logger.info("Updating role:  << " + successMsg + validationError);
        return objList;
    }
}
