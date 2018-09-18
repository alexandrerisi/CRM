package com.risi.mvc.data.demo.repository;

import com.risi.mvc.data.demo.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);
}
