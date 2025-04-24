package com.challenge.Intuit.service;


import com.challenge.Intuit.entity.Customer;

import java.util.List;
import java.util.Optional;


public interface CustomerService {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    List<Customer> searchCustomersByName(String name);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customerDetails);

    void deleteCustomer(Long id);

}
