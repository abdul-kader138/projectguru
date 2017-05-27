package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ProductSubCategory;
import com.dreamchain.skeleton.service.ProductSubCategoryService;
import com.dreamchain.skeleton.service.RoleService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class RoleController {

    @Autowired
    RoleService roleService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(RoleController.class.getName());

    @RequestMapping("/role")
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        String pageName = "role";
        model.setViewName(pageName);
        return model;

    }

//    @RequestMapping(value = "subcategory/subcategoryList", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    List<ProductSubCategory> loadProductSubCategoryList() {
//
//        List<ProductSubCategory> productSubCategoryList = new ArrayList();
//        logger.info("Loading all subcategory info: >> ");
//        productSubCategoryList = productSubCategoryService.findAll();
//        logger.info("Loading all subcategory info: << total " + productSubCategoryList.size());
//        return productSubCategoryList;
//    }


    @RequestMapping(value = "role/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveProductSubCategory(@RequestBody Map<String, String> roleInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new role: >>");
        objList = roleService.save(roleInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("role.save.success.msg"));
            successMsg = environment.getProperty("role.save.success.msg");
        }
        logger.info("creating new role: << " + successMsg + validationError);
        return objList;

    }
}
