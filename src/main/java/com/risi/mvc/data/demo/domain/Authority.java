package com.risi.mvc.data.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "users")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue
    private int id;
    private String authority;
    @ManyToMany //(targetEntity = User.class)
    private Set<User> users = new HashSet<>();

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}