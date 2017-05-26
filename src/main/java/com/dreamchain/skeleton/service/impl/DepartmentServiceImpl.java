package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.DepartmentDao;
import com.dreamchain.skeleton.model.Department;
import com.dreamchain.skeleton.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentDao departmentDao;
    @Autowired
    Environment environment;

    private static String DEPARTMENT_EXISTS = "This department name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_DEPARTMENT = "Department not exists";
    private static String BACK_DATED_DATA = "Department data is old.Please try again with updated data";
    private static String ASSOCIATED_DEPARTMENT = "Department is tagged with users.First remove tagging and try again";


    @Transactional(readOnly = true)
    public Department get(Long id) {
        return departmentDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(String departmentName) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Department newDepartment=new Department();
        Department department=createObjForSave(departmentName);
        validationMsg = checkInput(department);
        Department existingDepartment = departmentDao.findByDepartmentName(department.getName());
        if (existingDepartment.getName() != null && validationMsg == "") validationMsg = DEPARTMENT_EXISTS;
        if ("".equals(validationMsg)) {
            long departmentId= departmentDao.save(department);
            newDepartment=departmentDao.get(departmentId);
        }
        obj.put("department",newDepartment);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, String> companyObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Department department=new Department();
        Department newObj=new Department();
        department.setId(Long.parseLong(companyObj.get("id")));
        department.setVersion(Long.parseLong(companyObj.get("version")));
        department.setName(companyObj.get("name"));
        validationMsg = checkInput(department);
        Department existingDepartment = departmentDao.get(department.getId());
        if (department.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (existingDepartment.getName() == null && validationMsg == "") validationMsg = INVALID_DEPARTMENT;
        if (department.getVersion() != existingDepartment.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateDepartmentValue(department, existingDepartment);
            departmentDao.update(newObj);
        }
        obj.put("department",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long departmentId) {
        String validationMsg = "";
        if (departmentId == 0l) validationMsg = INVALID_INPUT;
        Department department = departmentDao.get(departmentId);
        if (department == null && validationMsg == "") validationMsg = INVALID_DEPARTMENT;

        //@todo need implement after user implementation
//        List<Object> obj=departmentDao.countOfDepartment(departmentId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_COMPANY;

        if ("".equals(validationMsg)) {
            departmentDao.delete(department);
        }
        return validationMsg;
    }

    @Override
    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    // check for invalid data
    private String checkInput(Department department) {
        String msg = "";
        if (department.getName() == null)
            msg = INVALID_INPUT;

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Department>> constraintViolations = validator.validate(department);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    // create company object for saving

    private Department createObjForSave(String name) throws Exception {
        Department department = new Department();
        department.setName(name);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        department.setCreatedBy(UserDetailServiceImpl.userId);
        department.setCreatedOn(date);
        return department;

    }

    private Department setUpdateDepartmentValue(Department objFromUI,Department existingDepartment) throws ParseException {
        Department departmentObj = new Department();
        departmentObj.setId(objFromUI.getId());
        departmentObj.setVersion(objFromUI.getVersion());
        departmentObj.setName(objFromUI.getName());
        departmentObj.setCreatedBy(existingDepartment.getCreatedBy());
        departmentObj.setCreatedOn(existingDepartment.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        departmentObj.setUpdatedBy(UserDetailServiceImpl.userId);
        departmentObj.setUpdatedOn(date);
        return departmentObj;
    }
}
