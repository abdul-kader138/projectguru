package com.dreamchain.skeleton.WS;

import com.dreamchain.skeleton.model.Customer;
import com.dreamchain.skeleton.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by LAPTOP DREAM on 11/6/2016.
 */
@PropertySource("classpath:config.properties")
@WebService(portName = "CimsPort", name = "CimsService", serviceName = "CimsService", targetNamespace = "http://WS.skeleton.dreamchain.com/")
public class ApplicationWS {

    @Autowired
    CustomerService customerService;
    @Autowired
    Environment environment;

    @WebMethod(operationName = "getCustomer")
    public Customer getCustomer(long id) {
        Customer customer = customerService.get(id);
        return customer;
    }


    @WebMethod(operationName = "getCustomerList")
    public List<Customer> getCustomerList() {
        return customerService.findAllCustomer();
    }


    @WebMethod(operationName = "createCustomer")
    public String createCustomer(Customer customer) throws Exception {
        String msg = customerService.save(customer);
        if (msg == "") return environment.getProperty("customer.save.success.msg");
        return msg;
    }


}
