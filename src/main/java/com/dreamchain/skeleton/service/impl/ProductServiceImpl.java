package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.*;
import com.dreamchain.skeleton.model.*;
import com.dreamchain.skeleton.service.ProductService;
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
public class ProductServiceImpl implements ProductService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CompanyDao companyDao;
    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    TeamAllocationDao teamAllocationDao;
    @Autowired
    ProductDao productDao ;
    @Autowired
    Environment environment;

    private static final String PRODUCT_EXISTS = "This product name is already used.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_PRODUCT = "Product not exists";
    private static final String BACK_DATED_DATA = "Product data is old.Please try again with updated data";
    private static final String ASSOCIATED_CATEGORY = "Product is tagged with category.First remove tagging and try again";
    private static final String ASSOCIATED_ALLOCATION = "Product is tagged with allocation.First remove tagging and try again";
    private static final String INVALID_PRIVILEGE_UPDATE = "You have not enough privilege to update client product info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_DELETE = "You have not enough privilege to delete client product info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_CREATE = "You have not enough privilege to create product for client.Please contact with System Admin!!!";



    @Transactional(readOnly = true)
    public Product get(Long id) {
        return productDao.get(id);
    }

    @Transactional
    public Map<String, Object> save( Map<String, Object> productObj) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Product newProduct=new Product();
        Product existingProduct=new Product();
        Product product=createObjForSave(productObj);
        validationMsg = checkInput(product);
        if ("".equals(validationMsg)) existingProduct = productDao.findByProductName(product.getName(), product.getCompany().getId());
        if (existingProduct.getName() != null && "".equals(validationMsg)) validationMsg = PRODUCT_EXISTS;
        if (!getUserId().getClientId().equals(product.getCompany().getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_CREATE;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            product.setCreatedBy(getUserId().getEmail());
            product.setCreatedOn(date);
            long productId= productDao.save(product);
            newProduct=productDao.get(productId);
        }
        obj.put("product",newProduct);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> productObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        List<Object> teamAllocationList=new ArrayList<>();
        Product newObj=new Product();
        Product existingProduct=new Product();
        Product product=createObjForSave(productObj);
        validationMsg = checkInput(product);
        if (product.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingProduct = productDao.get(product.getId());
        if (existingProduct.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_PRODUCT;
        if (!getUserId().getClientId().equals(existingProduct.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_UPDATE;
        if("".equals(validationMsg)) teamAllocationList=teamAllocationDao.countOfAllocationByProduct(existingProduct.getId());
        if (getUserId().getClientId().equals(existingProduct.getClientId()) && "".equals(validationMsg)
                && product.getCompany().getId() != existingProduct.getCompany().getId() && teamAllocationList.size() !=0) validationMsg = ASSOCIATED_ALLOCATION;
        if (product.getVersion() != existingProduct.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newObj = productDao.findByNewName(existingProduct.getName(),product.getName(),product.getCompany().getId());
        if (newObj.getName() != null && "".equals(validationMsg)) validationMsg = PRODUCT_EXISTS;
        if ("".equals(validationMsg)) {
            newObj=setUpdateProductValue(product, existingProduct);
            productDao.update(newObj);
        }
        obj.put("product",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long productId) {
        String validationMsg = "";
        List<Object> obj=new ArrayList<>();
        if (productId == 0l) validationMsg = INVALID_INPUT;
        Product product = productDao.get(productId);
        if ("".equals(product.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRODUCT;
        if (!getUserId().getClientId().equals(product.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_DELETE;
        if("".equals(validationMsg)) obj=categoryDao.countOfProduct(productId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_CATEGORY;
        if("".equals(validationMsg))obj=teamAllocationDao.countOfAllocationByProduct(productId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_ALLOCATION;
        if ("".equals(validationMsg)) {
            productDao.delete(product);
        }
        return validationMsg;
    }

    @Transactional(readOnly=true)
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Transactional(readOnly=true)
    public List<Product> findByCompanyName(long companyId) {
        return productDao.findByCompanyName(companyId);
    }


    // create Product object for saving

    private Product createObjForSave(Map<String, Object> productObj){
        Product product = new Product();
        Company company=companyDao.get(Long.parseLong((String) productObj.get("companyId")));
        product.setId(Long.parseLong((String) productObj.get("id")));
        product.setVersion(Long.parseLong((String) productObj.get("version")));
        product.setName(((String) productObj.get("name")).trim());
        product.setDescription(((String) productObj.get("description")).trim());
        product.setClientId(getUserId().getClientId());
        product.setCompany(company);
        product.setClientId(getUserId().getClientId());
        return product;

    }

    // check for invalid data
    private String checkInput(Product product) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }

    private Product setUpdateProductValue(Product objFromUI,Product existingProduct) throws ParseException {
        Product productObj = new Product();
        Company company = companyDao.get(objFromUI.getCompany().getId());
        productObj.setId(objFromUI.getId());
        productObj.setVersion(objFromUI.getVersion());
        productObj.setName(objFromUI.getName().trim());
        productObj.setDescription(objFromUI.getDescription().trim());
        productObj.setCompany(company);
        productObj.setClientId(getUserId().getClientId());
        productObj.setCreatedBy(existingProduct.getCreatedBy());
        productObj.setCreatedOn(existingProduct.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        productObj.setUpdatedBy(getUserId().getEmail());
        productObj.setUpdatedOn(date);
        return productObj;
    }

    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }


}
