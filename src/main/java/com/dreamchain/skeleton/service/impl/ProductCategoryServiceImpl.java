package com.dreamchain.skeleton.service.impl;


import com.dreamchain.skeleton.dao.ProductCategoryDao;
import com.dreamchain.skeleton.model.ProductCategory;
import com.dreamchain.skeleton.service.ProductCategoryService;
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
public class ProductCategoryServiceImpl implements ProductCategoryService{

    @Autowired
    ProductCategoryDao productCategoryDao;
    @Autowired
    Environment environment;


    private static String CATEGORY_EXISTS = "This category name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_CATEGORY = "Category not exists";
    private static String BACK_DATED_DATA = "Category data is old.Please try again with updated data";
    private static String ASSOCIATED_CATEGORY = "Category is tagged with sub-category.First remove tagging and try again";

    @Transactional(readOnly = true)
    public ProductCategory get(Long id) {
        return null;
    }

    @Transactional
    public Map<String, Object> save(String productCategoryName, String description) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        ProductCategory newProductCategory=new ProductCategory();
        ProductCategory productCategory=createObjForSave(productCategoryName,description);
        validationMsg = checkInput(productCategory);
        ProductCategory existingProductCategory = productCategoryDao.findByProductCategoryName(productCategory.getName());
        if (existingProductCategory.getName() != null && validationMsg == "") validationMsg = CATEGORY_EXISTS;
        if ("".equals(validationMsg)) {
            long companyId= productCategoryDao.save(productCategory);
            newProductCategory=productCategoryDao.get(companyId);
        }
        obj.put("productCategory",newProductCategory);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, String> productCategoryObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        ProductCategory productCategory=new ProductCategory();
        ProductCategory newObj=new ProductCategory();
        productCategory.setId(Long.parseLong(productCategoryObj.get("id")));
        productCategory.setVersion(Long.parseLong(productCategoryObj.get("version")));
        productCategory.setName(productCategoryObj.get("name"));
        productCategory.setDescription(productCategoryObj.get("description"));
        validationMsg = checkInput(productCategory);
        ProductCategory existingProductCategory = productCategoryDao.get(productCategory.getId());
        if (productCategory.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (existingProductCategory.getName() == null && validationMsg == "") validationMsg = INVALID_CATEGORY;
        if (productCategory.getVersion() != existingProductCategory.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateProductCategoryValue(productCategory, existingProductCategory);
            productCategoryDao.update(newObj);
        }
        obj.put("productCategory",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long productCategoryId) {
        String validationMsg = "";
        if (productCategoryId == 0l) validationMsg = INVALID_INPUT;
        ProductCategory productCategory = productCategoryDao.get(productCategoryId);
        if (productCategory == null && validationMsg == "") validationMsg = INVALID_CATEGORY;
        List<Object> obj=productCategoryDao.countOfProductCategory(productCategoryId);
        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_CATEGORY;
        if ("".equals(validationMsg)) {
            productCategoryDao.delete(productCategory);
        }
        return validationMsg;
    }

    @Override
    public List<ProductCategory> findAll() {
        return productCategoryDao.findAll();
    }


    // check for invalid data
    private String checkInput(ProductCategory productCategory) {
        String msg = "";
        if (productCategory.getName() == null || productCategory.getDescription() == null)
            msg = INVALID_INPUT;

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ProductCategory>> constraintViolations = validator.validate(productCategory);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    // create company object for saving

    private ProductCategory createObjForSave(String name,String description) throws Exception {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(name);
        productCategory.setDescription(description);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        productCategory.setCreatedBy(UserDetailServiceImpl.userId);
        productCategory.setCreatedOn(date);
        return productCategory;

    }

    private ProductCategory setUpdateProductCategoryValue(ProductCategory objFromUI,ProductCategory existingProductCategory) throws ParseException {
        ProductCategory productCategoryObj = new ProductCategory();
        productCategoryObj.setId(objFromUI.getId());
        productCategoryObj.setVersion(objFromUI.getVersion());
        productCategoryObj.setName(objFromUI.getName());
        productCategoryObj.setDescription(objFromUI.getDescription());
        productCategoryObj.setCreatedBy(existingProductCategory.getCreatedBy());
        productCategoryObj.setCreatedOn(existingProductCategory.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        productCategoryObj.setUpdatedBy(UserDetailServiceImpl.userId);
        productCategoryObj.setUpdatedOn(date);
        return productCategoryObj;
    }
}
