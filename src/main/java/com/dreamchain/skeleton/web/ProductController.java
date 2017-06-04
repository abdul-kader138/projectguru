package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Product;
import com.dreamchain.skeleton.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(ProductController.class.getName());

    @RequestMapping("/product")
    public ModelAndView main()  {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object obj=auth.getDetails();
        Object obj1=auth.getPrincipal();
        ModelAndView model = new ModelAndView();
        String pageName = "product_test";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "product/productList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Product> loadProductList() {

        List<Product> productList = new ArrayList();
        logger.info("Loading all product info: >> ");
        productList = productService.findAll();
        logger.info("Loading all product info: << total " + productList.size());
        return productList;
    }

    @RequestMapping(value = "product/productListByCompany", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Product> loadDepartmentListByCompanyId(@RequestBody String companyId) throws Exception{
        List<Product> productList = new ArrayList();
        logger.info("Loading all product info: >> ");
        productList = productService.findByCompanyName(Long.parseLong(companyId));
        logger.info("Loading all product info: << total " + productList.size());
        return productList;
    }


    @RequestMapping(value = "product/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveProduct(@RequestBody Map<String, Object> productInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new product: >>");
        objList = productService.save(productInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("product.save.success.msg"));
            successMsg = environment.getProperty("product.save.success.msg");
        }
        logger.info("creating new product: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "product/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteProduct(@RequestBody String productId) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete product:  >> ");
        validationError = productService.delete(Long.parseLong(productId));
        if (validationError.length() == 0) successMsg = environment.getProperty("product.delete.success.msg");
        logger.info("Delete product:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "product/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateProduct(@RequestBody Map<String, Object> productObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating product: >>");
        objList = productService.update(productObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("product.update.success.msg"));
            successMsg = environment.getProperty("product.update.success.msg");
        }
        logger.info("Updating product:  << " + successMsg + validationError);
        return objList;
    }
}
