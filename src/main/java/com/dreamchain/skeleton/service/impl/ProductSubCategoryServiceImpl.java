package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.ProductCategoryDao;
import com.dreamchain.skeleton.dao.ProductSubCategoryDao;
import com.dreamchain.skeleton.model.ProductCategory;
import com.dreamchain.skeleton.model.ProductSubCategory;
import com.dreamchain.skeleton.service.ProductSubCategoryService;
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
public class ProductSubCategoryServiceImpl implements ProductSubCategoryService{

    @Autowired
    ProductSubCategoryDao productSubCategoryDao;
    @Autowired
    ProductCategoryDao productCategoryDao;
    @Autowired
    Environment environment;


    private static String SUBCATEGORY_EXISTS = "This sub-category name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_SUBCATEGORY = "Sub-category not exists";
    private static String BACK_DATED_DATA = "Sub-category data is old.Please try again with updated data";

    @Transactional(readOnly = true)
    public ProductSubCategory get(Long id) {
        return productSubCategoryDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(String productSubCategoryName,String description, long productCategoryId) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        ProductSubCategory newProductSubCategory=new ProductSubCategory();
        ProductSubCategory productSubCategory=createObjForSave(productSubCategoryName,description,productCategoryId);
        validationMsg = checkInput(productSubCategory);
        ProductSubCategory existingProductSubCategory = productSubCategoryDao.findByProductSubCategoryName(productSubCategory.getName());
        if (existingProductSubCategory.getName() != null && validationMsg == "") validationMsg = SUBCATEGORY_EXISTS;
        if ("".equals(validationMsg)) {
            long projectId= productSubCategoryDao.save(productSubCategory);
            newProductSubCategory=productSubCategoryDao.get(projectId);
        }
        obj.put("productSubCategory",newProductSubCategory);
        obj.put("validationError",validationMsg);
        return obj;

    }



    @Transactional
    public Map<String, Object> update(Map<String, String> productSubCategoryObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        ProductSubCategory productSubCategory=new ProductSubCategory();
        ProductSubCategory newObj=new ProductSubCategory();
        productSubCategory.setId(Long.parseLong(productSubCategoryObj.get("id")));
        productSubCategory.setVersion(Long.parseLong(productSubCategoryObj.get("version")));
        productSubCategory.setName(productSubCategoryObj.get("name"));
        productSubCategory.setDescription(productSubCategoryObj.get("description"));
        productSubCategory.setProductCategory(productCategoryDao.get(Long.parseLong(productSubCategoryObj.get("categoryId"))));
        productSubCategory.setProductCategoryId(Long.parseLong(productSubCategoryObj.get("categoryId")));
        validationMsg = checkInput(productSubCategory);
        ProductSubCategory existingProductSubCategory = productSubCategoryDao.get(productSubCategory.getId());
        if (productSubCategory.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (existingProductSubCategory.getName() == null && validationMsg == "") validationMsg = INVALID_SUBCATEGORY;
        if (productSubCategory.getVersion() != existingProductSubCategory.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateProductSubCategoryValue(productSubCategory, existingProductSubCategory);
            productSubCategoryDao.update(newObj);
        }
        obj.put("productSubCategory",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long productSubCategoryId) {
        String validationMsg = "";
        if (productSubCategoryId == 0l) validationMsg = INVALID_INPUT;
        ProductSubCategory productSubCategory = productSubCategoryDao.get(productSubCategoryId);
        if (productSubCategory == null && validationMsg == "") validationMsg = INVALID_SUBCATEGORY;
        if ("".equals(validationMsg)) {
            productSubCategoryDao.delete(productSubCategory);
        }
        return validationMsg;

    }


    public List<ProductSubCategory> findAll() {
        return productSubCategoryDao.findAll();
    }


    // check for invalid data
    private String checkInput(ProductSubCategory productSubCategory) {
        String msg = "";
        if (productSubCategory.getName() == null || productSubCategory.getProductCategory() == null ||
                productSubCategory.getDescription() ==null)
            msg = INVALID_INPUT;

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ProductSubCategory>> constraintViolations = validator.validate(productSubCategory);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    // create ProductSubCategory object for saving

    private ProductSubCategory createObjForSave(String name,String description,Long productCategoryId) throws Exception {
        ProductSubCategory productSubCategory = new ProductSubCategory();
        ProductCategory productCategory = productCategoryDao.get(productCategoryId);;
        productSubCategory.setName(name);
        productSubCategory.setDescription(description);
        productSubCategory.setProductCategory(productCategory);
        productSubCategory.setProductCategoryId(productCategory.getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        productSubCategory.setCreatedBy(UserDetailServiceImpl.userId);
        productSubCategory.setCreatedOn(date);
        return productSubCategory;

    }

    private ProductSubCategory setUpdateProductSubCategoryValue(ProductSubCategory objFromUI,ProductSubCategory existingProductSubCategory) throws ParseException {
        ProductSubCategory productSubCategory = new ProductSubCategory();
        productSubCategory.setId(objFromUI.getId());
        productSubCategory.setVersion(objFromUI.getVersion());
        productSubCategory.setName(objFromUI.getName());
        productSubCategory.setProductCategory(objFromUI.getProductCategory());
        productSubCategory.setDescription(objFromUI.getDescription());
        productSubCategory.setProductCategoryId(objFromUI.getProductCategoryId());
        productSubCategory.setCreatedBy(existingProductSubCategory.getCreatedBy());
        productSubCategory.setCreatedOn(existingProductSubCategory.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        productSubCategory.setUpdatedBy(UserDetailServiceImpl.userId);
        productSubCategory.setUpdatedOn(date);
        return productSubCategory;
    }
}
