package com.risi.mvc.data.demo;

import com.risi.mvc.data.demo.domain.Customer;
import com.risi.mvc.data.demo.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            repository.save(new Customer("Alexandre", "Risi", "alexandrerisi@gmail.com"));
            repository.save(new Customer("David", "Adams", "david@luv2code.com"));
            repository.save(new Customer("John", "Doe", "john@luv2code.com"));
            repository.save(new Customer("Ajay", "Rao", "ajay@luv2code.com"));
            repository.save(new Customer("Mary", "Public", "marry@luv2code.com"));
            repository.save(new Customer("Maxwell", "Dixon", "max@luv2code.com"));
        };
    }
}
