package com.risi.mvc.data.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "authorities")
@EqualsAndHashCode
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String password;
    private boolean enabled = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    @ManyToMany(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addAuthority(Authority role) {
        authorities.add(role);
    }
}
