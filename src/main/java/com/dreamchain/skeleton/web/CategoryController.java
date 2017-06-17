package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Category;
import com.dreamchain.skeleton.service.CategoryService;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(CategoryController.class.getName());

    @RequestMapping("/category")
    public ModelAndView main()  {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView model = new ModelAndView();
        String pageName = "category";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "category/categoryList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Category> loadCategoryList() {

        List<Category> categoryList = new ArrayList();
        logger.info("Loading all category info: >> ");
        categoryList = categoryService.findAll();
        logger.info("Loading all category info: << total " + categoryList.size());
        return categoryList;
    }


    @RequestMapping(value = "category/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveCategory(@RequestBody Map<String, Object> categoryInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new category: >>");
        objList = categoryService.save(categoryInfo);
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
    Map deleteCategory(@RequestBody String categoryId) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete category:  >> ");
        validationError = categoryService.delete(Long.parseLong(categoryId));
        if (validationError.length() == 0) successMsg = environment.getProperty("category.delete.success.msg");
        logger.info("Delete category:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "category/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateCategory(@RequestBody Map<String, Object> categoryObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating category: >>");
        objList = categoryService.update(categoryObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("category.update.success.msg"));
            successMsg = environment.getProperty("category.update.success.msg");
        }
        logger.info("Updating category:  << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "category/categoryListByProduct", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Category> loadDepartmentListByCompanyId(@RequestBody String productId) throws Exception{
        List<Category> productList = new ArrayList();
        logger.info("Loading all category info: >> ");
        productList = categoryService.findByProductId(Long.parseLong(productId));
        logger.info("Loading all category info: << total " + productList.size());
        return productList;
    }
}
