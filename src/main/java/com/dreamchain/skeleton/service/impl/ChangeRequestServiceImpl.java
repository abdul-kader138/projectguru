package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CategoryDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.model.Category;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.service.ChangeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:config.properties")
public class ChangeRequestServiceImpl implements ChangeRequestService {

    @Autowired
    ChangeRequestDao changeRequestDao;
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

    @Override
    public Map<String, Object> save(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        String validationMsg = "";
        ChangeRequest newChangeRequest = new ChangeRequest();
        ChangeRequest existingChangeRequest = new ChangeRequest();
        ChangeRequest changeRequest = createObjForSave(request);
//        validationMsg = checkInput(changeRequest);
        if ("".equals(validationMsg)) existingChangeRequest = changeRequestDao.findByChangeRequestName(changeRequest.getName());
        if (existingChangeRequest.getName() != null && validationMsg == "") validationMsg = CHANGED_REQUEST_EXISTS;
//        if ("".equals(validationMsg)) msg = fileSave(request);
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

    private ChangeRequest createObjForSave(MultipartHttpServletRequest request) throws Exception {
        Category category = categoryDao.get(Long.parseLong(request.getParameter("categoryId")));
        ChangeRequest changeRequest=new ChangeRequest();
        changeRequest.setName(request.getParameter("name").trim());
        changeRequest.setCategoryName(category.getName());
        changeRequest.setCategoryId(category.getId());
        changeRequest.setCompanyName(category.getCompanyName());
        changeRequest.setCompanyId(category.getCompanyId());
//        changeRequest.setP
//        User user = setEditorInfo(operationName, request);
//        user.setId(Long.parseLong(request.getParameter("id")));
//        user.setVersion(Long.parseLong(request.getParameter("version")));
//        user.setName(request.getParameter("name").trim());
//        user.setPhone(request.getParameter("phone").trim());
//        user.setDesignation(request.getParameter("designation").trim());
//        user.setUserType("client");
//        user.setRoleRight(roleRight);
//        user.setRoleRightsId(roleRight.getId());
//        user.setCompanyId(company.getId());
//        user.setCompany(company);
//        user.setRole(roleName);
//        SimpleDateFormat dateFormat = new SimpleDateFormat();
//        Date date = dateFormat.parse(dateFormat.format(new Date()));
//        user.setCreatedBy(getUserId());
//        user.setCreatedOn(date);
        return changeRequest;

    }
}
