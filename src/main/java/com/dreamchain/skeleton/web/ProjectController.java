package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Project;
import com.dreamchain.skeleton.service.ProjectService;
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
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(ProjectController.class.getName());

    @RequestMapping("/project")
    public ModelAndView main(){
        ModelAndView model = new ModelAndView();
        String pageName = "project";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "project/projectList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Project> loadCompanyList() {

        List<Project> projectList = new ArrayList();
        logger.info("Loading all project info: >> ");
        projectList = projectService.findAll();
        logger.info("Loading all project info: << total " + projectList.size());
        return projectList;
    }


    @RequestMapping(value = "project/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveProject(@RequestBody Map<String, String> projectInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new project: >>");
        objList = projectService.save(projectInfo.get("name"), Long.parseLong(projectInfo.get("companyId")));
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("project.save.success.msg"));
            successMsg = environment.getProperty("project.save.success.msg");
        }
        logger.info("creating new company: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "project/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteCompany(@RequestBody Map<String, Object> projectInfo) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete project:  >> ");
        Integer id=(Integer)projectInfo.get("id");
        validationError = projectService.delete(id.longValue());
        if (validationError.length() == 0) successMsg = environment.getProperty("project.delete.success.msg");
        logger.info("Delete project:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "project/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateCompany(@RequestBody Map<String, String> projectObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating project: >>");
        objList = projectService.update(projectObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("project.update.success.msg"));
            successMsg = environment.getProperty("project.update.success.msg");
        }
        logger.info("Updating project:  << " + successMsg + validationError);
        return objList;
    }



}
