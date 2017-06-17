package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CategoryDao;
import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.dao.DepartmentDao;
import com.dreamchain.skeleton.dao.ProductDao;
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
    CompanyDao companyDao;
    @Autowired
    ProductDao productDao ;
    @Autowired
    CategoryDao categoryDao ;
    @Autowired
    Environment environment;

    private static String CATEGORY_EXISTS = "This category name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_CATEGORY = "Category not exists";
    private static String BACK_DATED_DATA = "Category data is old.Please try again with updated data";
    private static String ASSOCIATED_REQUEST = "Category is tagged with request.First remove tagging and try again";



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
        if (existingCategory.getName() != null && validationMsg == "") validationMsg = CATEGORY_EXISTS;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            category.setCreatedBy(getUserId());
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
        String validationMsg = "";
        Category newObj=new Category();
        Category existingCategory=new Category();
        Category category=createObjForSave(categoryObj);
        validationMsg = checkInput(category);
        if (category.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingCategory = categoryDao.get(category.getId());
        if (existingCategory.getName() == null && validationMsg == "") validationMsg = INVALID_CATEGORY;
        if (category.getVersion() != existingCategory.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newObj = categoryDao.findByNewName(existingCategory.getName(),category.getName(),category.getCompanyId(),category.getDepartmentId(),category.getProductId());
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
        if (categoryId == 0l) validationMsg = INVALID_INPUT;
        Category category = categoryDao.get(categoryId);
        if (category == null && validationMsg == "") validationMsg = INVALID_CATEGORY;

        //@todo need implement after Request implementation
//        List<Object> obj=departmentDao.countOfDepartment(departmentId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_COMPANY;

        if ("".equals(validationMsg)) {
            categoryDao.delete(category);
        }
        return validationMsg;
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> findByProductId(long productId) {
        return categoryDao.findByProductId(productId);
    }


    // create Category object for saving

    private Category createObjForSave(Map<String, Object> categoryObj){
        Category category = new Category();
        Company company=companyDao.get(Long.parseLong((String) categoryObj.get("companyId")));
        Department department=departmentDao.get(Long.parseLong((String)categoryObj.get("departmentId")));
        Product product=productDao.get(Long.parseLong((String)categoryObj.get("productId")));
        category.setId(Long.parseLong((String) categoryObj.get("id")));
        category.setVersion(Long.parseLong((String) categoryObj.get("version")));
        category.setCompanyId(Long.parseLong((String) categoryObj.get("companyId")));
        category.setDepartmentId(Long.parseLong((String) categoryObj.get("departmentId")));
        category.setProductId(Long.parseLong((String) categoryObj.get("productId")));
        category.setName(((String)categoryObj.get("name")).trim());
        category.setDescription(((String)categoryObj.get("description")).trim());
        category.setCompany(company);
        category.setDepartment(department);
        category.setProduct(product);
        return category;

    }

    // check for invalid data
    private String checkInput(Category category) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }

    private Category setUpdateCategoryValue(Category objFromUI,Category existingCategory) throws ParseException {
        Category categoryObj = new Category();
        Company company = companyDao.get(objFromUI.getCompanyId());
        Department department = departmentDao.get(objFromUI.getDepartmentId());
        Product product=productDao.get(objFromUI.getProductId());
        categoryObj.setId(objFromUI.getId());
        categoryObj.setVersion(objFromUI.getVersion());
        categoryObj.setName(objFromUI.getName().trim());
        categoryObj.setDescription(objFromUI.getDescription().trim());
        categoryObj.setCompanyId(objFromUI.getCompanyId());
        categoryObj.setCompany(company);
        categoryObj.setDepartmentId(objFromUI.getDepartmentId());
        categoryObj.setDepartment(department);
        categoryObj.setProductId(objFromUI.getProductId());
        categoryObj.setProduct(product);
        categoryObj.setCreatedBy(existingCategory.getCreatedBy());
        categoryObj.setCreatedOn(existingCategory.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        categoryObj.setUpdatedBy(getUserId());
        categoryObj.setUpdatedOn(date);
        return categoryObj;
    }

    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }


}
