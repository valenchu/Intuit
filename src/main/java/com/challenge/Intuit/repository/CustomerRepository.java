package com.challenge.Intuit.repository;

import com.challenge.Intuit.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNombreContainingIgnoreCase(String nombres);

    @Query("SELECT c FROM Customer c WHERE c.nombre LIKE %:searchTerm%")
    List<Customer> searchByNombres(@Param("searchTerm") String searchTerm);

}
