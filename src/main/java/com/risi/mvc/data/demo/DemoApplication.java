package com.risi.mvc.data.demo;

import com.risi.mvc.data.demo.domain.Authority;
import com.risi.mvc.data.demo.domain.Customer;
import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.repository.CustomerRepository;
import com.risi.mvc.data.demo.service.UserService;
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
    public CommandLineRunner demo(UserService userService) {
        return (args) -> {

            User john = new User("john", "test123");
            User mary = new User("mary", "test123");
            User susan = new User("susan", "test123");

            Authority employee = new Authority("EMPLOYEE");
            Authority admin = new Authority("ADMIN");
            Authority manager = new Authority("MANAGER");

            john.addAuthority(employee);
            mary.addAuthority(employee);
            mary.addAuthority(manager);
            susan.addAuthority(employee);
            susan.addAuthority(admin);

            userService.save(john);
            userService.save(mary);
            userService.save(susan);
        };
    }
}
