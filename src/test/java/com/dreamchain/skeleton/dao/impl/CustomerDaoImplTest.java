package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.model.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CustomerDaoImplTest extends DaoTest {

    @Autowired
    CustomerDaoImpl customerDaoImpl;

    @Test
    public void all_fields_are_persisted() {
        Customer customer=new Customer();
        customer.setName("test");
        customer.setNid("1234567890123");
        customer.setPhone("12345678901");
        customer.setAddress("ssfhshfhsdgfshgfh");
        customer.setEmail("babu313133@gmail.com");
        customer.setCustomerCode("TE0001");
        customerDaoImpl.save(customer);
        Customer newCustomer=customerDaoImpl.findByNid("1234567890123");


        Assert.assertEquals("test", newCustomer.getName());
        Assert.assertEquals("babu313133@gmail.com", newCustomer.getEmail());
        Assert.assertEquals("TE0001", newCustomer.getCustomerCode());
        Assert.assertEquals("12345678901", newCustomer.getPhone());
        Assert.assertEquals("ssfhshfhsdgfshgfh", newCustomer.getAddress());

        Customer obj=newCustomer;
        obj.setName("Name2");
        obj.setNid("1234567890124");

        customerDaoImpl.remove(newCustomer);
        customerDaoImpl.update(obj);

        newCustomer=customerDaoImpl.findByNid("1234567890124");

        Assert.assertEquals("Name2", newCustomer.getName());
    }


    @Test
    public void loadCustomerLIst(){
        Customer customer=new Customer();
        customer.setName("test");
        customer.setNid("1234567890123");
        customer.setPhone("12345678901");
        customer.setAddress("ssfhshfhsdgfshgfh");
        customer.setEmail("babu313133@gmail.com");
        customer.setCustomerCode("TE0001");
        customerDaoImpl.save(customer);
        List<Customer> lstUser=customerDaoImpl.findAll();
        Assert.assertNotNull(lstUser);
    }

}
