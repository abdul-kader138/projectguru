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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    public String save(String companyName) throws Exception{
        String validationMsg = "";
        Company company=createObjForSave(companyName);
        validationMsg = checkInput(company);
        Company existingCompany = companyDao.findByCompanyName(company.getName());
        if (existingCompany != null && validationMsg == "") validationMsg = COMPANY_EXISTS;
        if ("".equals(validationMsg)) {
            companyDao.save(company);
        }
        return validationMsg;


    }

    @Override
    public void update(Company user) {

    }

    @Override
    public void delete(long companyId) {

    }


    @Override
    public Company findByCompanyName(String companyName) {
        return null;
    }

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }


    // check for invalid data
    private String checkInput(Company company) {
        String msg = "";
        if (company.getName() == null)
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

    private Company createObjForSave(String name) throws Exception {
        Company company = new Company();
        company.setName(name);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        company.setCreatedBy(UserDetailServiceImpl.userId);
        company.setCreatedOn(date);
        return company;

    }
}