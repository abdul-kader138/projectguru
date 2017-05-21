package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyDao companyDao;
    @Autowired
    Environment environment;

    private static String COMPANY_EXISTS = "This company name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_COMPANY = "Company not exists";
    private static String BACK_DATED_DATA = "Company data is old.Please try again with updated data";


    @Transactional(readOnly = true)
    public Company get(Long id) {
        return companyDao.get(id);
    }


    @Transactional
    public Map<String,Object> save(String companyName,String address) throws Exception{
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Company newCompany=new Company();
        Company company=createObjForSave(companyName,address);
        validationMsg = checkInput(company);
        Company existingCompany = companyDao.findByCompanyName(company.getName());
        if (existingCompany.getName() != null && validationMsg == "") validationMsg = COMPANY_EXISTS;
        if ("".equals(validationMsg)) {
           long companyId= companyDao.save(company);
            newCompany=companyDao.get(companyId);
        }
        obj.put("company",newCompany);
        obj.put("validationMsg",validationMsg);
        return obj;


    }

    @Override
    public Map<String,Object>  update(Company company) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        validationMsg = checkInput(company);
        Company existingCompany = companyDao.get(company.getId());
        if (existingCompany.getName() == null && validationMsg == "") validationMsg = INVALID_COMPANY;
        if (company.getVersion() != existingCompany.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            Company newObj=setUpdateCompanyValue(company, existingCompany);
            companyDao.update(newObj);
        }
        return obj;
    }



    @Override
    public String delete(Long companyId) {
        String validationMsg = "";
        if (companyId == 0l) validationMsg = INVALID_INPUT;
        Company company = companyDao.get(companyId);
        if (company == null && validationMsg == "") validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) {
            companyDao.delete(company);
        }
        return validationMsg;

    }


    @Override
    public Company findByCompanyName(String companyName) {
        return  companyDao.findByCompanyName(companyName);
    }

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }


    // check for invalid data
    private String checkInput(Company company) {
        String msg = "";
        if (company.getName() == null || company.getAddress() == null)
            msg = INVALID_INPUT;

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Company>> constraintViolations = validator.validate(company);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    // @check for duplicate name

    private String checkForDuplicateName(String name) {
        String msg = "";
        Company duplicateObj = companyDao.findByCompanyName(name);
        if (duplicateObj != null) msg = COMPANY_EXISTS;
        return msg;
    }


    // create company object for saving

    private Company createObjForSave(String name,String address) throws Exception {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        company.setCreatedBy(UserDetailServiceImpl.userId);
        company.setCreatedOn(date);
        return company;

    }

    private Company setUpdateCompanyValue(Company objFromUI,Company existingCompany) throws ParseException {
        Company companyObj = new Company();
        companyObj.setId(objFromUI.getId());
        companyObj.setVersion(objFromUI.getVersion());
        companyObj.setName(objFromUI.getName());
        companyObj.setAddress(objFromUI.getAddress());
        companyObj.setCreatedBy(companyObj.getCreatedBy());
        companyObj.setCreatedOn(companyObj.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        companyObj.setUpdatedBy(UserDetailServiceImpl.userId);
        companyObj.setUpdatedOn(date);
        return companyObj;
    }
}