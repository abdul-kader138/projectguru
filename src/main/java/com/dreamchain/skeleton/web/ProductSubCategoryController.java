package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ProductSubCategory;
import com.dreamchain.skeleton.service.ProductSubCategoryService;
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
public class ProductSubCategoryController {

    @Autowired
    ProductSubCategoryService productSubCategoryService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(ProductSubCategoryController.class.getName());

    @RequestMapping("/subcategory")
    public ModelAndView main(){
        ModelAndView model = new ModelAndView();
        String pageName = "subcategory";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "subcategory/subcategoryList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ProductSubCategory> loadProductSubCategoryList() {

        List<ProductSubCategory> productSubCategoryList = new ArrayList();
        logger.info("Loading all subcategory info: >> ");
        productSubCategoryList = productSubCategoryService.findAll();
        logger.info("Loading all subcategory info: << total " + productSubCategoryList.size());
        return productSubCategoryList;
    }


    @RequestMapping(value = "subcategory/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveProductSubCategory(@RequestBody Map<String, String> productSubCategoryInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new subcategory: >>");
        objList = productSubCategoryService.save(productSubCategoryInfo.get("name"),productSubCategoryInfo.get("description"), Long.parseLong(productSubCategoryInfo.get("categoryId")));
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("subcategory.save.success.msg"));
            successMsg = environment.getProperty("subcategory.save.success.msg");
        }
        logger.info("creating new subcategory: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "subcategory/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteProductSubCategory(@RequestBody Map<String, Object> productSubCategoryInfo) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete subcategory:  >> ");
        Integer id=(Integer)productSubCategoryInfo.get("id");
        validationError = productSubCategoryService.delete(id.longValue());
        if (validationError.length() == 0) successMsg = environment.getProperty("subcategory.delete.success.msg");
        logger.info("Delete subcategory:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "subcategory/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateProductSubCategory(@RequestBody Map<String, String> productSubCategoryObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating subcategory: >>");
        objList = productSubCategoryService.update(productSubCategoryObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("subcategory.update.success.msg"));
            successMsg = environment.getProperty("subcategory.update.success.msg");
        }
        logger.info("Updating subcategory:  << " + successMsg + validationError);
        return objList;
    }

}
