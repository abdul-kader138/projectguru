package com.dreamchain.skeleton.WS;

import com.dreamchain.skeleton.model.Customer;
import com.dreamchain.skeleton.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by LAPTOP DREAM on 11/4/2016.
 */

@WebService
public class CustomerWebService {

    @Autowired(required = true)
    private CustomerService customerService;

    @WebMethod(exclude = true)
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
    @WebMethod(operationName = "getCustomerById")
    public Customer getCustomerById(long id) {
        Customer customer = customerService.get(id);
        return customer;
    }


}
