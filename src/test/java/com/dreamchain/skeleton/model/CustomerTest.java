package com.dreamchain.skeleton.model;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


public class CustomerTest {


    @Test
    public void assert_that_certain_fields_cant_be_null_or_blank() {
        Customer customer = new Customer();
        customer.setName("test");
        customer.setNid("1234567890123");
        customer.setPhone("12345678901");
        customer.setAddress("ssfhshfhsdgfshgfh");
        customer.setEmail("babu313133@gmail.com");
        customer.setCustomerCode("TE0001");

        Map<String, ConstraintViolation<Customer>> violationsMap = validate(customer);
        assertTrue(violationsMap.size() == 0);
    }

    @Test
    public void assert_that_email_has_to_be_an_email() {
        Customer customer = new Customer();
        customer.setName("test");
        customer.setNid("1234567890123");
        customer.setPhone("12345678901");
        customer.setAddress("ssfhshfhsdgfshgfh");
        customer.setEmail("babu@yahoo.com");
        customer.setCustomerCode("TE0001");

        Map<String, ConstraintViolation<Customer>> violationsMap = validate(customer);
        assertTrue(violationsMap.get("email").getMessageTemplate().contains("Email"));
    }


    private <T> Map<String, ConstraintViolation<T>> validate(T user) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Map<String, ConstraintViolation<T>> violations = new HashMap<String, ConstraintViolation<T>>();
        for (ConstraintViolation<T> violation : validator.validate(user)) {
            violations.put(violation.getPropertyPath().toString(), violation);
        }
        return violations;
    }

}
