package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.Customer;
import com.challenge.Intuit.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

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
        return customerRepository.findByNombresContainingIgnoreCase(name);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> createAllCustomer(List<Customer> customer) {
        return customerRepository.saveAll(customer);
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
    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setNombres(customerDetails.getNombres());
            customer.setApellidos(customerDetails.getApellidos());
            customer.setFechaNacimiento(customerDetails.getFechaNacimiento());
            customer.setCuit(customerDetails.getCuit());
            customer.setDomicilio(customerDetails.getDomicilio());
            customer.setTelefonoCelular(customerDetails.getTelefonoCelular());
            customer.setEmail(customerDetails.getEmail());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
