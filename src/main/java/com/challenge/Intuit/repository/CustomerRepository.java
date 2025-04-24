package com.challenge.Intuit.repository;

import com.challenge.Intuit.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNombresContainingIgnoreCase(String nombres);

}
