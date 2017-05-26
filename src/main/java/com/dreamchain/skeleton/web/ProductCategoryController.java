package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ProductCategory;
import com.dreamchain.skeleton.service.ProductCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class ProductCategoryController {

    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(CompanyController.class.getName());

    @RequestMapping("/category")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "category";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "category/categoryList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ProductCategory> loadProductCategoryList() {

        List<ProductCategory> productCategoryList = new ArrayList();
        logger.info("Loading all category info: >> ");
        productCategoryList = productCategoryService.findAll();
        logger.info("Loading all category info: << total " + productCategoryList.size());
        return productCategoryList;
    }


    @RequestMapping(value = "category/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveCompany(@RequestBody Map<String, String> productCategoryInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new category: >>");
        objList = productCategoryService.save(productCategoryInfo.get("name"), productCategoryInfo.get("description"));
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("category.save.success.msg"));
            successMsg = environment.getProperty("category.save.success.msg");
        }
        logger.info("creating new category: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "category/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteCompany(@RequestBody Map<String, String> productCategoryInfo) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete category:  >> ");
        validationError = productCategoryService.delete(Long.parseLong(productCategoryInfo.get("id")));
        if (validationError.length() == 0) successMsg = environment.getProperty("category.delete.success.msg");
        logger.info("Delete category:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "category/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateCompany(@RequestBody Map<String, String> productCategoryObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating category: >>");
        objList = productCategoryService.update(productCategoryObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("category.update.success.msg"));
            successMsg = environment.getProperty("category.update.success.msg");
        }
        logger.info("Updating category:  << " + successMsg + validationError);
        return objList;
    }


}
