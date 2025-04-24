package com.challenge.Intuit.controller;

import com.challenge.Intuit.entity.Customer;
import com.challenge.Intuit.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Endpoint to get all customers.
     * Maps to GET requests at /api/clientes.
     *
     * @return A list of all customers.
     */
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Endpoint to get a customer by their ID.
     * Maps to GET requests at /api/clientes/{id}.
     *
     * @param id The ID of the customer from the URL path.
     * @return A ResponseEntity containing the customer if found (200 OK), or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        // Calls the service to find the customer by ID.
        return customerService.getCustomerById(id)
                // If the Optional contains a customer, return 200 OK with the customer body.
                .map(ResponseEntity::ok)
                // If the Optional is empty (customer not found), return 404 Not Found.
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to search for customers by name.
     * Maps to GET requests at /api/clientes/search?name=...
     *
     * @param name The search string from the request parameter.
     * @return A list of customers whose names match the search criteria.
     */
    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String name) {
        return customerService.searchCustomersByName(name);
    }

    /**
     * Endpoint to create a new customer.
     * Maps to POST requests at /api/clientes.
     *
     * @param customer The customer object from the request body (JSON).
     * @return The created customer object. Returns 201 Created status by default due to @ResponseStatus.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Sets the HTTP status code to 201 Created on success.
    public Customer createCustomer(@RequestBody Customer customer) {
        // Calls the service to create the customer.
        return customerService.createCustomer(customer);
    }

    /**
     * Endpoint to update an existing customer.
     * Maps to PUT requests at /api/clientes/{id}.
     *
     * @param id              The ID of the customer to update from the URL path.
     * @param customerDetails The customer object with updated details from the request body (JSON).
     * @return A ResponseEntity containing the updated customer if successful (200 OK), or 404 Not Found if the customer doesn't exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        try {
            // Calls the service to update the customer.
            Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
            // If successful, return 200 OK with the updated customer.
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            // If the service throws a RuntimeException (e.g., customer not found),
            // re-throw it as a ResponseStatusException to return 404 Not Found.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + id);
        }
    }
}
