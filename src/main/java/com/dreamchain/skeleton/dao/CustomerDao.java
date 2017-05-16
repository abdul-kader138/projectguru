package com.dreamchain.skeleton.dao;

import com.dreamchain.skeleton.model.Customer;

import java.util.List;


public interface CustomerDao {
    Customer get(Long id);
    void save(Customer customer);
    void update(Customer customer);
    void delete(Customer customer);
    void remove(Customer customer);
    List<Customer> findAll();
    Customer findByCustomerCode(String customerCode);
    Customer findByNid(String nidNo);
    Customer findByEmailAndPhone(String email,String phone);
    Customer updateCustomerEmailAndPhone(String email,String phone,long id);

}
