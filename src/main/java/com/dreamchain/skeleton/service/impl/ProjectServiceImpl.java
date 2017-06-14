package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.dao.ProjectDao;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.model.Project;
import com.dreamchain.skeleton.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@PropertySource("classpath:config.properties")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectDao projectDao;

    @Autowired
    CompanyDao companyDao;
    @Autowired
    Environment environment;


    private static String PROJECT_EXISTS = "This project name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_PROJECT = "Project not exists";
    private static String BACK_DATED_DATA = "Project data is old.Please try again with updated data";

    @Transactional(readOnly = true)
    public Project get(Long id) {
        return projectDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(String projectName, long companyId) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Project newProject=new Project();
        Project project=createObjForSave(projectName,companyId);
        validationMsg = checkInput(project);
        Project existingProject = projectDao.findByProjectName(project.getName());
        if (existingProject.getName() != null && validationMsg == "") validationMsg = PROJECT_EXISTS;
        if ("".equals(validationMsg)) {
            long projectId= projectDao.save(project);
            newProject=projectDao.get(projectId);
        }
        obj.put("project",newProject);
        obj.put("validationError",validationMsg);
        return obj;

    }



    @Transactional
    public Map<String, Object> update(Map<String, String> projectObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Project project=new Project();
        Project newObj=new Project();
        project.setId(Long.parseLong(projectObj.get("id")));
        project.setVersion(Long.parseLong(projectObj.get("version")));
        project.setName(projectObj.get("name"));
        project.setCompany(companyDao.get(Long.parseLong(projectObj.get("companyId"))));
        project.setCompanyId(Long.parseLong(projectObj.get("companyId")));
        validationMsg = checkInput(project);
        Project existingProject = projectDao.get(project.getId());
        if (project.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (existingProject.getName() == null && validationMsg == "") validationMsg = INVALID_PROJECT;
        if (project.getVersion() != existingProject.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateCompanyValue(project, existingProject);
            projectDao.update(newObj);
        }
        obj.put("project",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long projectId) {
        String validationMsg = "";
        if (projectId == 0l) validationMsg = INVALID_INPUT;
        Project project = projectDao.get(projectId);
        if (project == null && validationMsg == "") validationMsg = INVALID_PROJECT;
        if ("".equals(validationMsg)) {
            projectDao.delete(project);
        }
        return validationMsg;

    }

    @Override
    public List<Project> findAll() {
        return projectDao.findAll();
    }


    // check for invalid data
    private String checkInput(Project project) {
        String msg = "";
        if (project.getName() == null || project.getCompany() == null)
            msg = INVALID_INPUT;

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Project>> constraintViolations = validator.validate(project);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    // create project object for saving

    private Project createObjForSave(String name,Long companyId) throws Exception {
        Project project = new Project();
        Company company = companyDao.get(companyId);
        project.setName(name);
        project.setCompany(company);
        project.setCompanyId(company.getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        project.setCreatedOn(date);
        return project;

    }

    private Project setUpdateCompanyValue(Project objFromUI,Project existingProject) throws ParseException {
        Project projectObj = new Project();
        projectObj.setId(objFromUI.getId());
        projectObj.setVersion(objFromUI.getVersion());
        projectObj.setName(objFromUI.getName());
        projectObj.setCompanyId(objFromUI.getCompanyId());
        projectObj.setCompany(objFromUI.getCompany());
        projectObj.setCreatedBy(existingProject.getCreatedBy());
        projectObj.setCreatedOn(existingProject.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        projectObj.setUpdatedOn(date);
        return projectObj;
    }
}
