package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.Customer;
import com.challenge.Intuit.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring service component
public class CustomerServiceImpl implements CustomerService {

    // Injects the CustomerRepository dependency. Spring manages the creation and injection of this dependency.
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves all customers from the database.
     *
     * @return A list of all customers.
     */
    @Override
    public List<Customer> getAllCustomers() {
        // Uses the findAll() method provided by JpaRepository.
        return customerRepository.findAll();
    }

    /**
     * Retrieves a customer by their unique ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return An Optional containing the customer if found, otherwise an empty Optional.
     */
    @Override
    public Optional<Customer> getCustomerById(Long id) {
        // Uses the findById() method provided by JpaRepository.
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
        // Uses the custom query method defined in the repository.
        return customerRepository.findByNombresContainingIgnoreCase(name);
    }

    /**
     * Creates a new customer in the database.
     *
     * @param customer The customer object to create.
     * @return The created customer object with the generated ID.
     */
    @Override
    public Customer createCustomer(Customer customer) {
        // Uses the save() method provided by JpaRepository.
        // If the customer object has no ID, it performs an INSERT.
        return customerRepository.save(customer);
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
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
