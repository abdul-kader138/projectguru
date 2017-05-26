package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.DesignationDao;
import com.dreamchain.skeleton.model.Designation;
import com.dreamchain.skeleton.service.DesignationService;
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
public class DesignationServiceImpl implements DesignationService{

    @Autowired
    DesignationDao designationDao;
    @Autowired
    Environment environment;


    private static String DESIGNATION_EXISTS = "This designation name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_DESIGNATION = "Designation not exists";
    private static String BACK_DATED_DATA = "Designation data is old.Please try again with updated data";
    private static String ASSOCIATED_DESIGNATION = "Designation is tagged with users.First remove tagging and try again";

    @Transactional(readOnly = true)
    public Designation get(Long id) {
        return designationDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(String designationName) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Designation newDesignation=new Designation();
        Designation designation=createObjForSave(designationName);
        validationMsg = checkInput(designation);
        Designation existingDesignation = designationDao.findByDesignationName(designation.getName());
        if (existingDesignation.getName() != null && validationMsg == "") validationMsg = DESIGNATION_EXISTS;
        if ("".equals(validationMsg)) {
            long designationId= designationDao.save(designation);
            newDesignation=designationDao.get(designationId);
        }
        obj.put("designation",newDesignation);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, String> designationObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Designation designation=new Designation();
        Designation newObj=new Designation();
        designation.setId(Long.parseLong(designationObj.get("id")));
        designation.setVersion(Long.parseLong(designationObj.get("version")));
        designation.setName(designationObj.get("name"));
        validationMsg = checkInput(designation);
        Designation existingDesignation = designationDao.get(designation.getId());
        if (designation.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (existingDesignation.getName() == null && validationMsg == "") validationMsg = INVALID_DESIGNATION;
        if (designation.getVersion() != existingDesignation.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateDesignationValue(designation, existingDesignation);
            designationDao.update(newObj);
        }
        obj.put("designation",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long designationId) {
        String validationMsg = "";
        if (designationId == 0l) validationMsg = INVALID_INPUT;
        Designation designation = designationDao.get(designationId);
        if (designation == null && validationMsg == "") validationMsg = INVALID_DESIGNATION;

        //@todo need implement after user implementation
//        List<Object> obj=designationDao.countOfDesignation(designationId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_DESIGNATION;

        if ("".equals(validationMsg)) {
            designationDao.delete(designation);
        }
        return validationMsg;
    }

    @Override
    public List<Designation> findAll() {
        return designationDao.findAll();
    }

    // check for invalid data
    private String checkInput(Designation designation) {
        String msg = "";
        if (designation.getName() == null)
            msg = INVALID_INPUT;

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Designation>> constraintViolations = validator.validate(designation);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    // create company object for saving

    private Designation createObjForSave(String name) throws Exception {
        Designation designation = new Designation();
        designation.setName(name);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        designation.setCreatedBy(UserDetailServiceImpl.userId);
        designation.setCreatedOn(date);
        return designation;

    }

    private Designation setUpdateDesignationValue(Designation objFromUI,Designation existingDesignation) throws ParseException {
        Designation designationObj = new Designation();
        designationObj.setId(objFromUI.getId());
        designationObj.setVersion(objFromUI.getVersion());
        designationObj.setName(objFromUI.getName());
        designationObj.setCreatedBy(existingDesignation.getCreatedBy());
        designationObj.setCreatedOn(existingDesignation.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        designationObj.setUpdatedBy(UserDetailServiceImpl.userId);
        designationObj.setUpdatedOn(date);
        return designationObj;
    }
}
