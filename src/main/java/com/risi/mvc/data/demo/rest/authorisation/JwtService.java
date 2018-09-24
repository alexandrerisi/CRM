package com.risi.mvc.data.demo.rest.authorisation;

import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    @Autowired
    private UserService userService;

    private String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        for (GrantedAuthority authority : user.getAuthorities())
            claims.put(authority.getAuthority(), true);

        return Jwts.builder().setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 7200)) // two hours.
                .signWith(key)
                .compact();
    }

    public String restAuthentication(String username, String password) {
        return generateToken(userService.restAuthentication(username, password));
    }
}
