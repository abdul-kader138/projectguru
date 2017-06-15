package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CategoryDao;
import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.dao.DepartmentDao;
import com.dreamchain.skeleton.dao.ProductDao;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.model.Department;
import com.dreamchain.skeleton.model.Product;
import com.dreamchain.skeleton.model.User;
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
    ProductDao productDao ;
    @Autowired
    Environment environment;

    private static String PRODUCT_EXISTS = "This product name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_PRODUCT = "Product not exists";
    private static String BACK_DATED_DATA = "Product data is old.Please try again with updated data";
    private static String ASSOCIATED_PRODUCT = "Product is tagged with category.First remove tagging and try again";


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
        if ("".equals(validationMsg)) existingProduct = productDao.findByProductName(product.getName(), product.getCompanyId());
        if (existingProduct.getName() != null && validationMsg == "") validationMsg = PRODUCT_EXISTS;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            product.setCreatedBy(getUserId());
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
        Product newObj=new Product();
        Product existingProduct=new Product();
        Product product=createObjForSave(productObj);
        validationMsg = checkInput(product);
        if (product.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingProduct = productDao.get(product.getId());
        if (existingProduct.getName() == null && validationMsg == "") validationMsg = INVALID_PRODUCT;
        if (product.getVersion() != existingProduct.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newObj = productDao.findByNewName(existingProduct.getName(),product.getName(),product.getCompanyId());
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
        if (productId == 0l) validationMsg = INVALID_INPUT;
        Product product = productDao.get(productId);
        if (product == null && validationMsg == "") validationMsg = INVALID_PRODUCT;
        List<Object> obj=categoryDao.countOfProduct(productId);
        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_PRODUCT;
        if ("".equals(validationMsg)) {
            productDao.delete(product);
        }
        return validationMsg;
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findByCompanyName(long companyId) {
        return productDao.findByCompanyName(companyId);
    }


    // create Product object for saving

    private Product createObjForSave(Map<String, Object> productObj){
        Product product = new Product();
        Company company=companyDao.get(Long.parseLong((String) productObj.get("companyId")));
        product.setId(Long.parseLong((String) productObj.get("id")));
        product.setVersion(Long.parseLong((String) productObj.get("version")));
        product.setCompanyId(Long.parseLong((String)productObj.get("companyId")));
        product.setName(((String)productObj.get("name")).trim());
        product.setDescription(((String)productObj.get("description")).trim());
        product.setCompany(company);
        return product;

    }

    // check for invalid data
    private String checkInput(Product product) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }

    private Product setUpdateProductValue(Product objFromUI,Product existingProduct) throws ParseException {
        Product productObj = new Product();
        Company company = companyDao.get(objFromUI.getCompanyId());
        productObj.setId(objFromUI.getId());
        productObj.setVersion(objFromUI.getVersion());
        productObj.setName(objFromUI.getName().trim());
        productObj.setDescription(objFromUI.getDescription().trim());
        productObj.setCompanyId(objFromUI.getCompanyId());
        productObj.setCompany(company);
        productObj.setCreatedBy(existingProduct.getCreatedBy());
        productObj.setCreatedOn(existingProduct.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        productObj.setUpdatedBy(getUserId());
        productObj.setUpdatedOn(date);
        return productObj;
    }

    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }


}
