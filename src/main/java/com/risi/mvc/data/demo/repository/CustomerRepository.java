package com.risi.mvc.data.demo.repository;

import com.risi.mvc.data.demo.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Collection<Customer> findAllByOrderBySurNameAsc();

    Optional<Customer> findByEmail(String email);
}
