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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.*;
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

    private static final String COMPANY_EXISTS = "This company name is already used.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_COMPANY = "Company not exists";
    private static final String BACK_DATED_DATA = "Company data is old.Please try again with updated data";
    private static final String INVALID_PRIVILEGE_UPDATE = "You have not enough privilege to update client company info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_DELETE = "You have not enough privilege to delete client company info.Please contact with System Admin!!!";
    private static final String ASSOCIATED_COMPANY = "Company is tagged with Department.First remove tagging and try again";
    private static final String ASSOCIATED_COMPANY_PRODUCT = "Company is tagged with Product.First remove tagging and try again";
    private static final String LOGO_PATH = "/resources/images/company_logo/";


    @Transactional(readOnly = true)
    public Company get(Long id) {
        return companyDao.get(id);
    }


    @Transactional
    public Map<String, Object> save(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        String validationMsg = "";
        Company newCompany = new Company();
        Company existingCompany = new Company();
        Company company = createObjForSave(request.getParameter("name"), request.getParameter("address"), request.getFile("logo").getOriginalFilename());
        validationMsg = checkInput(company);
        if ("".equals(validationMsg)) existingCompany = companyDao.findByCompanyName(company.getName());
        if (existingCompany.getName() != null && "".equals(validationMsg)) validationMsg = COMPANY_EXISTS;
        if ("".equals(validationMsg)) msg = fileSave(request);
        if ("".equals(msg.get("validationMsg"))) company.setImagePath((String) msg.get("path"));
        if ("".equals(validationMsg)) {
            long companyId = companyDao.save(company);
            newCompany = companyDao.get(companyId);
        }
        obj.put("company", newCompany);
        obj.put("validationError", validationMsg);
        return obj;


    }

    @Transactional
    public Map<String, Object> update(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        String validationMsg = "";
        Company newCompany = new Company();
        Company existingCompany = new Company();
        Company company = createObjForUpdate(request);
        validationMsg = checkInput(company);
        if ("".equals(validationMsg)) existingCompany = companyDao.get(company.getId());
        if (existingCompany.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_COMPANY;
        if (!getUserId().getClientId().equals(existingCompany.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_UPDATE;
        if (company.getVersion() != existingCompany.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newCompany = companyDao.findByNewName(existingCompany.getName(),company.getName());
        if (newCompany.getName() != null && "".equals(validationMsg)) validationMsg = COMPANY_EXISTS;
        String fileName = request.getFile("logo").getOriginalFilename();
        if (!("".equals(fileName))) {
            validationMsg = deleteLogo(request.getRealPath(
                    "/"), existingCompany.getImagePath());
            if ("".equals(validationMsg)) msg = fileSave(request);
            if ("".equals(validationMsg)) existingCompany.setImagePath((String) msg.get("path"));
        }
        if ("".equals(validationMsg)) {
            newCompany = setUpdateCompanyValue(company, existingCompany);
            companyDao.update(newCompany);
        }
        obj.put("company", newCompany);
        obj.put("validationError", validationMsg);
        return obj;
    }


    @Transactional
    public String delete(Long companyId,HttpServletRequest request) {
        String validationMsg = "";
        String fileName = "";
        List<Object> obj=new ArrayList<>();
        if (companyId == 0l) validationMsg = INVALID_INPUT;
        Company company = companyDao.get(companyId);
        if ("".equals(company.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_COMPANY;
        if (!getUserId().getClientId().equals(company.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_DELETE;
        if("".equals(validationMsg)) obj = companyDao.countOfCompany(companyId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_COMPANY;
        if (! "".equals(company.getClientId())) fileName = company.getImagePath();
        if("".equals(validationMsg) && obj.size() == 0) obj=companyDao.countOfCompanyForProduct(companyId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_COMPANY_PRODUCT;
        if ("".equals(validationMsg)) validationMsg = deleteLogo(request.getRealPath("/"),fileName);
        if ("".equals(validationMsg)) {
            companyDao.delete(company);
        }
        return validationMsg;
    }


    @Transactional(readOnly=true)
    public List<Company> findAll() {
        return companyDao.findAll();
    }


    // check for invalid data
    private String checkInput(Company company) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Company>> constraintViolations = validator.validate(company);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }


    // create company object for saving

    private Company createObjForSave(String name, String address, String fileName) throws Exception {
        Company company = new Company();
        company.setName(name.trim());
        company.setAddress(address.trim());
        company.setImagePath(fileName.trim());
        company.setClientId(getUserId().getClientId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        company.setCreatedBy(getUserId().getEmail());
        company.setCreatedOn(date);
        return company;

    }

    private Company setUpdateCompanyValue(Company objFromUI, Company existingCompany) throws ParseException {
        Company companyObj = new Company();
        companyObj.setId(objFromUI.getId());
        companyObj.setVersion(objFromUI.getVersion());
        companyObj.setName(objFromUI.getName().trim());
        companyObj.setAddress(objFromUI.getAddress().trim());
        companyObj.setImagePath(existingCompany.getImagePath());
        companyObj.setCreatedBy(existingCompany.getCreatedBy());
        companyObj.setCreatedOn(existingCompany.getCreatedOn());
        companyObj.setClientId(getUserId().getClientId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        companyObj.setUpdatedBy(getUserId().getEmail());
        companyObj.setUpdatedOn(date);
        return companyObj;
    }


    private Map fileSave(MultipartHttpServletRequest request) {
        Map<String, String> msg = new HashMap<>();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        MultipartFile multipartFile = request.getFile("logo");
        String fileName = multipartFile.getOriginalFilename();
        Random rand = new Random();
        int n = rand.nextInt(1000) + 1;
        fileName = n + "_" + fileName; // create new name for logo file
        try {
            String filePath = LOGO_PATH + fileName;
            String realPathFetch = request.getRealPath( "/");
            inputStream = multipartFile.getInputStream();
            File newFile = new File(realPathFetch + filePath);
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[2048];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            msg.put("validationMsg", "");
            msg.put("path", filePath.trim());
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            msg.put("validationMsg", e.getMessage());
            msg.put("path", "");
        }
        return msg;

    }


    private String deleteLogo(String realPathFetch, String fileName) {
        String msg = "";
        try {
            File file = new File(realPathFetch+fileName);
            file.setWritable(true);
            if (file.delete()) msg = "";
            else msg = environment.getProperty("company.file.delete.success.msg");
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    // create company object for updating

    private Company createObjForUpdate(MultipartHttpServletRequest request) throws Exception {
        Company company = new Company();
        company.setName(request.getParameter("name"));
        company.setAddress(request.getParameter("address"));
        company.setImagePath("test");
        company.setId(Long.parseLong(request.getParameter("id")));
        company.setVersion(Long.parseLong(request.getParameter("version")));
        company.setClientId(getUserId().getClientId());
        return company;

    }


    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }

}