package com.challenge.Intuit.service;

import com.challenge.Intuit.dto.CustomerDto;
import com.challenge.Intuit.entity.Customer;
import com.challenge.Intuit.mapper.CustomerMapper;
import com.challenge.Intuit.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Searches for customers whose names contain the given string, ignoring case.
     *
     * @param name The string to search for within customer names.
     * @return A list of customers matching the search criteria.
     */
    @Override
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNombreContainingIgnoreCase(name);
    }

    @Override
    public Customer createCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        try {
            return customerRepository.save(customer);
        }catch (RuntimeException e){
            throw e;
        }
    }

    @Override
    public List<Customer> createAllCustomer(List<CustomerDto> customerDto) {
        List<Customer> customerList = customerMapper.toEntityList(customerDto);
        try {
            return customerRepository.saveAll(customerList);
        }catch (RuntimeException e){
            throw e;
        }
    }

    /**
     * Updates an existing customer identified by ID with new details.
     *
     * @param id              The ID of the customer to update.
     * @param customerDetails The customer object containing the updated details.
     * @return The updated customer object.
     * @throws RuntimeException if the customer with the given ID is not found.
     */
    @Override
    public Customer updateCustomer(Long id, CustomerDto customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customerMapper.updateCustomerFromDto(customerDetails, customer);
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
