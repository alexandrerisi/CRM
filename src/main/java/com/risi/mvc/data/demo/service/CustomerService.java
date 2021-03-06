package com.risi.mvc.data.demo.service;

import com.risi.mvc.data.demo.domain.Customer;
import com.risi.mvc.data.demo.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Collection<Customer> getCustomers() {
        Collection<Customer> col = new LinkedHashSet<>();
        Iterable<Customer> it = customerRepository.findAllByOrderBySurNameAsc();
        it.forEach(col::add);
        return col;
    }

    public Optional<Customer> getCustomer(int id) {
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

    public Collection<Customer> getAllCustomers() {
        return customerRepository.findAllByOrderBySurNameAsc();
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> getCustomerById(int id) {
        return customerRepository.findById(id);
    }
}
