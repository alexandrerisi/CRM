package com.risi.mvc.data.demo.repository;

import com.risi.mvc.data.demo.domain.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Integer> {

    Authority findByAuthority(String role);
}
