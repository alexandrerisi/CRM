package com.risi.mvc.data.demo.repository;

import com.risi.mvc.data.demo.domain.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Integer> {

    Optional<Authority> findByAuthority(String role);
}
