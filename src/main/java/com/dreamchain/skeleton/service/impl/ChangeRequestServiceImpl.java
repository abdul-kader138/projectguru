package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CategoryDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.TeamAllocationDao;
import com.dreamchain.skeleton.dao.UserAllocationDao;
import com.dreamchain.skeleton.model.Category;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.TeamAllocation;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.ChangeRequestService;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@PropertySource("classpath:config.properties")
public class ChangeRequestServiceImpl implements ChangeRequestService {

    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    UserAllocationDao userAllocationDao;
    @Autowired
    TeamAllocationDao teamAllocationDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    Environment environment;

    private static String CHANGED_REQUEST_EXISTS = "This request name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_COMPANY = "Company not exists";
    private static String BACK_DATED_DATA = "Company data is old.Please try again with updated data";
    private static String ASSOCIATED_COMPANY = "Company is tagged with Department.First remove tagging and try again";
    private static String DOC_PATH = "/resources/images/doc/";
    @Override

    @Transactional(readOnly = true)
    public ChangeRequest get(Long id) {
        return changeRequestDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        Map<String, Object> validationObj = new HashMap<>();
        ChangeRequest changeRequest =new ChangeRequest();
        String validationMsg = "";
        ChangeRequest newChangeRequest = new ChangeRequest();
        ChangeRequest existingChangeRequest = new ChangeRequest();
        validationObj=createObjForSave(request,request.getFile("doc").getOriginalFilename());
        validationMsg=(String) validationObj.get("validationMsg");
        if("".equals(validationMsg)) changeRequest = (ChangeRequest) validationObj.get("changeRequest");
        if("".equals(validationMsg)) validationMsg = checkInput(changeRequest);
        if ("".equals(validationMsg)) existingChangeRequest = changeRequestDao.findByChangeRequestName(changeRequest.getName());
        if (existingChangeRequest.getName() != null && validationMsg == "") validationMsg = CHANGED_REQUEST_EXISTS;
        if ("".equals(validationMsg)) msg = fileSave(request);
        if (msg.get("validationMsg") == "") changeRequest.setDocPath((String) msg.get("path"));
        if ("".equals(validationMsg)) {
            long companyId = changeRequestDao.save(changeRequest);
            newChangeRequest = changeRequestDao.get(companyId);
        }
        obj.put("changeRequest", newChangeRequest);
        obj.put("validationError", validationMsg);
        return obj;
    }

    @Override
    public Map<String, Object> update(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        return null;
    }

    @Override
    public String delete(Long companyId, HttpServletRequest request) {
        return null;
    }

    @Override
    public List<ChangeRequest> findAll() {
        return null;
    }

    // create user object for saving

    private Map createObjForSave(MultipartHttpServletRequest request, String fileName) throws Exception {
        String validationMsg="";
        Map<String,Object> objList=new HashMap<>();
        Category category = categoryDao.get(Long.parseLong(request.getParameter("categoryId")));
        TeamAllocation teamAllocation=teamAllocationDao.findByProductAndCategory(category.getCompanyId(), category.getProductId(), category.getId());
        ChangeRequest changeRequest=new ChangeRequest();
        if (category == null) validationMsg=INVALID_INPUT;
        if (teamAllocation == null) validationMsg=INVALID_INPUT;
        if(validationMsg.length() == 0){
            changeRequest.setName(request.getParameter("name").trim());
            changeRequest.setCategoryName(category.getName());
            changeRequest.setCategoryId(category.getId());
            changeRequest.setCompanyName(category.getCompanyName());
            changeRequest.setCompanyId(category.getCompanyId());
            changeRequest.setProductId(category.getProductId());
            changeRequest.setProductName(category.getProductName());
            changeRequest.setDescription(request.getParameter("description").trim());
            changeRequest.setDocPath(DOC_PATH+fileName.trim());
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            changeRequest.setStatus(environment.getProperty("request.status.open"));
            changeRequest.setWipStatus(environment.getProperty("request.wip.status")+teamAllocation.getCheckedBy());
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            changeRequest.setCreatedBy(getUserId());
            changeRequest.setCreatedOn(date);
        }

        objList.put("validationMsg",validationMsg);
        objList.put("changeRequest",changeRequest);
        return objList;

    }


    private Map fileSave(MultipartHttpServletRequest request) {
        Map<String, String> msg = new HashMap<>();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        MultipartFile multipartFile = request.getFile("doc");
        String fileName = multipartFile.getOriginalFilename();
        Random rand = new Random();
        int n = rand.nextInt(1000) + 1;
        fileName = n + "_" + fileName; // create new name for doc file
        try {
            String filePath = DOC_PATH + fileName;
            String realPathFetch = request.getRealPath( "/");
            inputStream = multipartFile.getInputStream();
            File newFile = new File(realPathFetch + filePath);
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];
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


    private String deleteDoc(String realPathFetch, String fileName) {
        String msg = "";
        try {
            File file = new File(realPathFetch+fileName);
            file.setWritable(true);
            if (file.delete()) msg = "";
            else msg = environment.getProperty("request.file.delete.success.msg");
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }



    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }

    // check for invalid data
    private String checkInput(ChangeRequest changeRequest) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ChangeRequest>> constraintViolations = validator.validate(changeRequest);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }

}
