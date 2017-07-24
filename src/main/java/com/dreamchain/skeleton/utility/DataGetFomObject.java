package com.dreamchain.skeleton.utility;


import com.dreamchain.skeleton.model.Category;

public class DataGetFomObject {

    public static String companyName(Category category)
    {
        return category.getCompany().getName();
    }

    public static String productName(Category category)
    {
        return category.getProduct().getName();
    }

    public static String categoryName(Category category)
    {
        return category.getName();
    }
}
