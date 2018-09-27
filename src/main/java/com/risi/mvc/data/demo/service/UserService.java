package com.risi.mvc.data.demo.service;

import com.risi.mvc.data.demo.domain.Authority;
import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.repository.AuthorityRepository;
import com.risi.mvc.data.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthorityRepository authorityRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Authority> roles = new HashSet<>();
        for (GrantedAuthority role : user.getAuthorities()) {
            Authority authority = authorityRepo.findByAuthority(role.getAuthority());
            if (authority == null)
                roles.add((Authority) role);
            else
                roles.add(authority);
        }
        user.setAuthorities(roles);
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        if (!user.isPresent())
            throw new UsernameNotFoundException("User " + username + " not found.");
        return user.get();
    }

    public User restAuthentication(String username, String password) {
        User user = getUserByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new UsernameNotFoundException("User " + username + " not found.");
        return user;
    }

    public User getUserByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent())
            return user.get();
        else
            throw new UsernameNotFoundException("User " + username + " not found.");
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }
}
