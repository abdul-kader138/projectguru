package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Department;
import com.dreamchain.skeleton.service.DepartmentService;
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
import java.util.*;

@Controller
@PropertySource("classpath:config.properties")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(DepartmentController.class.getName());

    @RequestMapping("/department")
    public ModelAndView main()  {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object obj=auth.getDetails();
        Object obj1=auth.getPrincipal();
        ModelAndView model = new ModelAndView();
        String pageName = "department";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "department/departmentList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Department> loadDepartmentList() {

        List<Department> departmentList = new ArrayList();
        logger.info("Loading all department info: >> ");
        departmentList = departmentService.findAll();
        logger.info("Loading all department info: << total " + departmentList.size());
        return departmentList;
    }


    @RequestMapping(value = "department/departmentListByCompany", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Department> loadDepartmentListByCompanyId(@RequestBody String companyId) throws Exception{
        List<Department> departmentList = new ArrayList();
        logger.info("Loading all department info: >> ");
        departmentList = departmentService.findByCompanyName(Long.parseLong(companyId));
        logger.info("Loading all department info: << total " + departmentList.size());
        return departmentList;
    }

    @RequestMapping(value = "department/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveDepartment(@RequestBody Map<String, Object> departmentObj) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new department: >>");
        objList = departmentService.save(departmentObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("department.save.success.msg"));
            successMsg = environment.getProperty("department.save.success.msg");
        }
        logger.info("creating new department: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "department/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteDepartment(@RequestBody String departmentId) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete department:  >> ");
        validationError = departmentService.delete(Long.parseLong(departmentId));
        if (validationError.length() == 0) successMsg = environment.getProperty("department.delete.success.msg");
        logger.info("Delete department :  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "department/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateDepartment(@RequestBody Map<String, Object> departmentObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating department: >>");
        objList = departmentService.update(departmentObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("department.update.success.msg"));
            successMsg = environment.getProperty("department.update.success.msg");
        }
        logger.info("Updating department:  << " + successMsg + validationError);
        return objList;
    }

}
