package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.dao.DepartmentDao;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.model.Department;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    CompanyDao companyDao;
    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    Environment environment;

    private static final String DEPARTMENT_EXISTS = "This department name is already used.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_DEPARTMENT = "Department not exists";
    private static final String INVALID_PRIVILEGE_UPDATE = "You have not enough privilege to update client department info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_DELETE = "You have not enough privilege to delete client department info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_CREATE = "You have not enough privilege to create department for client.Please contact with System Admin!!!";
    private static final String BACK_DATED_DATA = "Department data is old.Please try again with updated data";
    private static final String ASSOCIATED_DEPARTMENT = "Department is tagged with product category.First remove tagging and try again";
    private static final String CHANGE_REQUEST_ASSOCIATED = "This department already associated with request.So this operation can't happen";


    @Transactional(readOnly = true)
    public Department get(Long id) {
        return departmentDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> departmentObj) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Department newDepartment=new Department();
        Department existingDepartment=new Department();
        Department department=createObjForSave(departmentObj);
        validationMsg = checkInput(department);
        if ("".equals(validationMsg)) existingDepartment = departmentDao.findByDepartmentName(department.getName(),department.getCompany().getId());
        if (existingDepartment.getName() != null && "".equals(validationMsg)) validationMsg = DEPARTMENT_EXISTS;
        if (!getUserId().getClientId().equals(department.getCompany().getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_CREATE;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            department.setCreatedBy(getUserId().getEmail());
            department.setCreatedOn(date);
            long departmentId= departmentDao.save(department);
            newDepartment=departmentDao.get(departmentId);
        }
        obj.put("department",newDepartment);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> departmentObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Department newObj=new Department();
        Department existingDepartment=new Department();
        ChangeRequest changeRequest=new ChangeRequest();
        Department department=createObjForSave(departmentObj);
        validationMsg = checkInput(department);
        if (department.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingDepartment = departmentDao.get(department.getId());
        if (existingDepartment.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_DEPARTMENT;
        if (!getUserId().getClientId().equals(existingDepartment.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_UPDATE;
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByDepartmentId(existingDepartment.getId());
        if (getUserId().getClientId().equals(existingDepartment.getClientId()) && "".equals(validationMsg)
                && existingDepartment.getCompany().getId() != department.getCompany().getId()  && changeRequest.getName() !=null) validationMsg = CHANGE_REQUEST_ASSOCIATED; // stop update department if it's already associated
        if (department.getVersion() != existingDepartment.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newObj = departmentDao.findByNewName(existingDepartment.getName(),department.getName(),department.getCompany().getId());
        if (newObj.getName() != null && "".equals(validationMsg)) validationMsg = DEPARTMENT_EXISTS;
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
        ChangeRequest changeRequest=new ChangeRequest();
        List<Object> obj=new ArrayList<>();
        if (departmentId == 0l) validationMsg = INVALID_INPUT;
        Department department = departmentDao.get(departmentId);
        if ("".equals(department.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_DEPARTMENT;
        if (!getUserId().getClientId().equals(department.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_DELETE;
        if("".equals(validationMsg)) obj=departmentDao.countOfDepartment(departmentId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_DEPARTMENT;
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByDepartmentId(departmentId);
        if("".equals(validationMsg) && changeRequest.getName() !=null) validationMsg=CHANGE_REQUEST_ASSOCIATED;
        if ("".equals(validationMsg)) {
            departmentDao.delete(department);
        }
        return validationMsg;
    }


    @Transactional(readOnly=true)
    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    @Transactional(readOnly=true)
    public List<Department> findByCompanyName(long companyId) {
       return departmentDao.findByCompanyName(companyId);
    }


    // check for invalid data
    private String checkInput(Department department) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Department>> constraintViolations = validator.validate(department);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }


    // create Department object for saving

    private Department createObjForSave(Map<String, Object> departmentObj){
        Department department = new Department();
        Company company=companyDao.get(Long.parseLong((String)departmentObj.get("companyId")));
        department.setName(((String) departmentObj.get("name")).trim());
        department.setCompany(company);
        department.setId(Long.parseLong((String) departmentObj.get("id")));
        department.setVersion(Long.parseLong((String) departmentObj.get("version")));
        department.setClientId(getUserId().getClientId());
        return department;

    }


    // create Department object for updating

    private Department setUpdateDepartmentValue(Department objFromUI,Department existingDepartment) throws ParseException {
        Department departmentObj = new Department();
        departmentObj.setId(objFromUI.getId());
        departmentObj.setVersion(objFromUI.getVersion());
        departmentObj.setName(objFromUI.getName().trim());
        departmentObj.setCompany(objFromUI.getCompany());
        departmentObj.setCreatedBy(existingDepartment.getCreatedBy());
        departmentObj.setCreatedOn(existingDepartment.getCreatedOn());
        departmentObj.setClientId(getUserId().getClientId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        departmentObj.setUpdatedBy(getUserId().getEmail());
        departmentObj.setUpdatedOn(date);
        return departmentObj;
    }

    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }
}
