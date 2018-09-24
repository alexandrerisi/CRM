package com.risi.mvc.data.demo.rest;

import com.risi.mvc.data.demo.domain.Customer;
import com.risi.mvc.data.demo.exception.CustomerNotFoundException;
import com.risi.mvc.data.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestApi {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Collection<Customer> exportCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{email}")
    public Customer getCustomerByEmail(@PathVariable String email) throws CustomerNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        if (!customer.isPresent())
            throw new CustomerNotFoundException("No customer with email -> " + email);
        return customer.get();
    }

    @PostMapping // If full csrf is enabled http post will require csrf token.
    public Customer addCustomer(@RequestBody Customer customer) {
        customer.setId(0);
        return customerService.saveCustomer(customer);
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) throws CustomerNotFoundException {
        Optional<Customer> dbCustomer = customerService.getCustomerById(customer.getId());
        if (dbCustomer.isPresent() && dbCustomer.get().getId() == customer.getId())
            customerService.saveCustomer(customer);
        else
            throw new CustomerNotFoundException("Customer not found. " + customer);
        return customer;
    }

    @DeleteMapping("{email}")
    public Customer deleteCustomer(@PathVariable String email) throws CustomerNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        if (customer.isPresent())
            customerService.deleteCustomer(customer.get().getId());
        else
            throw new CustomerNotFoundException("Customer not found. " + customer);
        return customer.get();
    }
}
