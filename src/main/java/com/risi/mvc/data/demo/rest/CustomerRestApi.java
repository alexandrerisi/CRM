package com.risi.mvc.data.demo.rest;

import com.risi.mvc.data.demo.domain.Customer;
import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.exception.CustomerNotFoundException;
import com.risi.mvc.data.demo.exception.InsufficientPermissionException;
import com.risi.mvc.data.demo.exception.InvalidTokenException;
import com.risi.mvc.data.demo.exception.TokenNotFoundException;
import com.risi.mvc.data.demo.rest.authorisation.JwtService;
import com.risi.mvc.data.demo.service.CustomerService;
import com.risi.mvc.data.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerRestApi {

    private final CustomerService customerService;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping
    public Collection<Customer> getCustomers(@RequestParam String token)
            throws InvalidTokenException, InsufficientPermissionException {

        return customerService.getAllCustomers();
    }

    @GetMapping("{email}")
    public Customer getCustomerByEmail(@RequestParam String token,
                                       @PathVariable String email)
            throws CustomerNotFoundException, InvalidTokenException, InsufficientPermissionException {

        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        if (customer.isEmpty())
            throw new CustomerNotFoundException("No customer with email -> " + email);
        return customer.get();
    }

    @PostMapping // If full csrf is enabled http post will require csrf token.
    public Customer addCustomer(@RequestParam String token, @RequestBody Customer customer)
            throws InvalidTokenException, InsufficientPermissionException {

        customer.setId(0);
        return customerService.saveCustomer(customer);
    }

    @PutMapping
    public Customer updateCustomer(@RequestParam String token, @RequestBody Customer customer)
            throws CustomerNotFoundException, InvalidTokenException, InsufficientPermissionException {

        Optional<Customer> dbCustomer = customerService.getCustomerById(customer.getId());
        if (dbCustomer.isPresent() && dbCustomer.get().getId() == customer.getId())
            customerService.saveCustomer(customer);
        else
            throw new CustomerNotFoundException("Customer not found. " + customer);
        return customer;
    }

    @DeleteMapping("{email}")
    public Customer deleteCustomer(@RequestParam String token,
                                   @PathVariable String email)
            throws CustomerNotFoundException, InvalidTokenException, InsufficientPermissionException {
        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        if (customer.isPresent())
            customerService.deleteCustomer(customer.get().getId());
        else
            throw new CustomerNotFoundException("Customer not found. " + customer);
        return customer.get();
    }

    @GetMapping("{username}/password/{password}")
    public String getToken(@PathVariable String username, @PathVariable String password) {
        return jwtService.restAuthentication(username, password);
    }

    @GetMapping("/verify/{token}")
    public boolean isValidToken(@PathVariable String token) throws InvalidTokenException, TokenNotFoundException {
        if (jwtService.isValidToken(token))
            throw new TokenNotFoundException(token + " does not exist in the system.");
        return true;
    }

    @PutMapping("/delete")
    public void deleteUser(@RequestParam String token, @RequestParam String username)
            throws InvalidTokenException, InsufficientPermissionException, UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        userService.deleteUser(user);
        jwtService.deleteUserToken(username);
    }
}
