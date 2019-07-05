package com.risi.mvc.data.demo.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "authorities")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private int id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String country = "UK";
    private String city = "Manchester";
    private boolean enabled = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    @ManyToMany(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(Authority role) {
        if (authorities == null)
            authorities = new HashSet<>();
        authorities.add(role);
    }
}
