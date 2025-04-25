package com.challenge.Intuit.service;


import com.challenge.Intuit.dto.CustomerDto;
import com.challenge.Intuit.entity.Customer;

import java.util.List;
import java.util.Optional;


public interface CustomerService {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    List<Customer> searchCustomersByName(String name);

    List<Customer> createAllCustomer(List<CustomerDto> customerList);

    Customer createCustomer(CustomerDto customer);

    Customer updateCustomer(Long id, CustomerDto customerDetails);

    void deleteCustomer(Long id);

}
