package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.service.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyDao companyDao;
    @Autowired
    Environment environment;

    private static String COMPANY_EXISTS = "This company name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_COMPANY = "Company not exists";
    private static String BACK_DATED_DATA = "Company data is old.Please try again with updated data";
    private static String ASSOCIATED_COMPANY = "Company is tagged with projects.First remove tagging and try again";


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
        obj.put("validationError",validationMsg);
        return obj;


    }

    @Transactional
    public Map<String,Object>  update(Map<String, String>  companyObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Company company=new Company();
        Company newObj=new Company();
        company.setId(Long.parseLong(companyObj.get("id")));
        company.setVersion(Long.parseLong(companyObj.get("version")));
        company.setName(companyObj.get("name"));
        company.setAddress(companyObj.get("address"));
        validationMsg = checkInput(company);
        Company existingCompany = companyDao.get(company.getId());
        if (company.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (existingCompany.getName() == null && validationMsg == "") validationMsg = INVALID_COMPANY;
        if (company.getVersion() != existingCompany.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateCompanyValue(company, existingCompany);
            companyDao.update(newObj);
        }
        obj.put("company",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }



    @Transactional
    public String delete(Long companyId) {
        String validationMsg = "";
        if (companyId == 0l) validationMsg = INVALID_INPUT;
        Company company = companyDao.get(companyId);
        if (company == null && validationMsg == "") validationMsg = INVALID_COMPANY;
        List<Object> obj=companyDao.countOfCompany(companyId);
        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_COMPANY;
        if ("".equals(validationMsg)) {
            companyDao.delete(company);
        }
        return validationMsg;
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
        companyObj.setCreatedBy(existingCompany.getCreatedBy());
        companyObj.setCreatedOn(existingCompany.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        companyObj.setUpdatedBy(UserDetailServiceImpl.userId);
        companyObj.setUpdatedOn(date);
        return companyObj;
    }
}