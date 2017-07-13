package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.*;
import com.dreamchain.skeleton.model.*;
import com.dreamchain.skeleton.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    DepartmentDao departmentDao;
    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    CompanyDao companyDao;
    @Autowired
    ProductDao productDao ;
    @Autowired
    CategoryDao categoryDao ;
    @Autowired
    TeamAllocationDao teamAllocationDao;
    @Autowired
    UserAllocationDao userAllocationDao;
    @Autowired
    Environment environment;

    private static final String CATEGORY_EXISTS = "This category name is already used.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_CATEGORY = "Category not exists";
    private static final String BACK_DATED_DATA = "Category data is old.Please try again with updated data";
    private static final String ASSOCIATED_ALLOCATION = "Category is tagged with allocation.First remove tagging and try again";
    private static final String INVALID_PRIVILEGE_UPDATE = "You have not enough privilege to update client category info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_DELETE = "You have not enough privilege to delete client category info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_CREATE = "You have not enough privilege to create category for client.Please contact with System Admin!!!";
    private static final String CHANGE_REQUEST_ASSOCIATED = "This category already associated with request.So this operation can't happen";



    @Transactional(readOnly = true)
    public Category get(Long id) {
        return categoryDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> categoryObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Category newCategory=new Category();
        Category existingCategory=new Category();
        Category category=createObjForSave(categoryObj);
        validationMsg = checkInput(category);
        if ("".equals(validationMsg)) existingCategory = categoryDao.findByProductName(category.getName(), category.getCompanyId(),category.getDepartmentId(),category.getProductId());
        if (existingCategory.getName() != null && "".equals(validationMsg)) validationMsg = CATEGORY_EXISTS;
        if (!getUserId().getClientId().equals(category.getCompany().getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_CREATE;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            category.setCreatedBy(getUserId().getEmail());
            category.setCreatedOn(date);
            long productId= categoryDao.save(category);
            newCategory=categoryDao.get(productId);
        }
        obj.put("category",newCategory);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> categoryObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        ChangeRequest changeRequest=new ChangeRequest();
        String validationMsg = "";
        Category newObj=new Category();
        Category existingCategory=new Category();
        Category category=createObjForSave(categoryObj);
        validationMsg = checkInput(category);
        if (category.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;

        if ("".equals(validationMsg)) existingCategory = categoryDao.get(category.getId());

        if (existingCategory.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_CATEGORY; // Check for invalid input

        if (!getUserId().getClientId().equals(existingCategory.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_UPDATE; // Stop other Company info update
        long companyId=existingCategory.getCompany().getId();
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByCategoryId(existingCategory.getId());

        if (getUserId().getClientId().equals(existingCategory.getClientId()) && "".equals(validationMsg)
                && category.getCompany().getId() != companyId  && changeRequest.getName() !=null) validationMsg = CHANGE_REQUEST_ASSOCIATED; // stop update company if it's already associated

        if (getUserId().getClientId().equals(existingCategory.getClientId()) && "".equals(validationMsg)
                && category.getProductId() != existingCategory.getProductId()  && changeRequest.getName() !=null) validationMsg = CHANGE_REQUEST_ASSOCIATED; // stop update product if it's already associated

        if (getUserId().getClientId().equals(existingCategory.getClientId()) && "".equals(validationMsg)
                && category.getDepartment().getId() != existingCategory.getDepartment().getId()  && changeRequest.getName() !=null) validationMsg = CHANGE_REQUEST_ASSOCIATED; // stop update department if it's already associated

        if (category.getVersion() != existingCategory.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;

        if ("".equals(validationMsg)) newObj = categoryDao.findByNewName(existingCategory.getName(),category.getName(),category.getCompanyId(),category.getDepartmentId(),category.getProductId()); // check for duplicate name

        if (newObj.getName() != null && "".equals(validationMsg)) validationMsg = CATEGORY_EXISTS;

        if ("".equals(validationMsg)) {
            newObj=setUpdateCategoryValue(category, existingCategory);
            categoryDao.update(newObj);
        }
        obj.put("category",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long categoryId) {
        String validationMsg = "";
        List<Object> obj=new ArrayList<>();
        ChangeRequest changeRequest=new ChangeRequest();
        if (categoryId == 0l) validationMsg = INVALID_INPUT;
        Category category = categoryDao.get(categoryId);
        if ("".equals(category.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_CATEGORY;
        if (!getUserId().getClientId().equals(category.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_DELETE;
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByCategoryId(categoryId);
        if("".equals(validationMsg) && changeRequest.getName() !=null) validationMsg=CHANGE_REQUEST_ASSOCIATED;
        if("".equals(validationMsg))obj=teamAllocationDao.countOfAllocation(categoryId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_ALLOCATION;
        if("".equals(validationMsg) && obj.size() == 0) obj=userAllocationDao.countOfAllocation(categoryId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_ALLOCATION;
        if ("".equals(validationMsg)) {
            categoryDao.delete(category);
        }
        return validationMsg;
    }

    @Transactional(readOnly=true)
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Transactional(readOnly=true)
    public List<Category> findByProductId(long productId) {
        return categoryDao.findByProductId(productId);
    }


    // create Category object for saving

    private Category createObjForSave(Map<String, Object> categoryObj){
        Category category = new Category();
        Department department=departmentDao.get(Long.parseLong((String)categoryObj.get("departmentId")));
        Product product=productDao.get(Long.parseLong((String)categoryObj.get("productId")));
        category.setId(Long.parseLong((String) categoryObj.get("id")));
        category.setVersion(Long.parseLong((String) categoryObj.get("version")));
        category.setCompanyId(Long.parseLong((String) categoryObj.get("companyId")));
        category.setDepartmentId(Long.parseLong((String) categoryObj.get("departmentId")));
        category.setProductId(Long.parseLong((String) categoryObj.get("productId")));
        category.setName(((String)categoryObj.get("name")).trim());
        category.setDescription(((String)categoryObj.get("description")).trim());
        category.setCompany(department.getCompany());
        category.setDepartment(department);
        category.setProduct(product);
        category.setClientId(getUserId().getClientId());
        return category;

    }

    // check for invalid data
    private String checkInput(Category category) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }

    private Category setUpdateCategoryValue(Category objFromUI,Category existingCategory) throws ParseException {
        Category categoryObj = new Category();
        Department department = departmentDao.get(objFromUI.getDepartmentId());
        Product product=productDao.get(objFromUI.getProductId());
        categoryObj.setId(objFromUI.getId());
        categoryObj.setVersion(objFromUI.getVersion());
        categoryObj.setName(objFromUI.getName().trim());
        categoryObj.setDescription(objFromUI.getDescription().trim());
        categoryObj.setCompanyId(objFromUI.getCompanyId());
        categoryObj.setCompany(department.getCompany());
        categoryObj.setDepartmentId(objFromUI.getDepartmentId());
        categoryObj.setDepartment(department);
        categoryObj.setProductId(objFromUI.getProductId());
        categoryObj.setProduct(product);
        categoryObj.setClientId(getUserId().getClientId());
        categoryObj.setCreatedBy(existingCategory.getCreatedBy());
        categoryObj.setCreatedOn(existingCategory.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        categoryObj.setUpdatedBy(getUserId().getEmail());
        categoryObj.setUpdatedOn(date);
        return categoryObj;
    }

    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }


}
