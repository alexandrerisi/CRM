package com.risi.mvc.data.demo.rest.authorisation;

import com.risi.mvc.data.demo.domain.Authority;
import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    @Autowired
    private UserService userService;

    private String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("country", user.getCountry());
        claims.put("city", user.getCity());
        claims.put("enabled", user.isEnabled());
        claims.put("credentialsNonExpired", user.isCredentialsNonExpired());
        claims.put("accountNonLocked", user.isAccountNonLocked());
        claims.put("accountNonExpired", user.isAccountNonExpired());
        claims.put("id", user.getId());
        claims.put("authorities", user.getAuthorities());

        return Jwts.builder().setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // two hours.
                .signWith(key)
                .compact();
    }

    public String restAuthentication(String username, String password) {
        return generateToken(userService.restAuthentication(username, password));
    }

    User getUserFromToken(String token) {

        Jws<Claims> col = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        Claims claims = col.getBody();
        User user = new User();
        user.setId(claims.get("id", Integer.class));
        user.setUsername(claims.get("sub", String.class));
        user.setCountry(claims.get("country", String.class));
        user.setCity(claims.get("city", String.class));
        user.setEnabled(claims.get("enabled", Boolean.class));
        user.setCredentialsNonExpired(claims.get("credentialsNonExpired", Boolean.class));
        user.setAccountNonLocked(claims.get("accountNonLocked", Boolean.class));
        user.setAccountNonExpired(claims.get("accountNonExpired", Boolean.class));
        List list = (ArrayList) claims.get("authorities");
        for (Object object : list) {
            Authority authority = new Authority();
            authority.setAuthority(((Map) object).get("authority").toString());
            user.addAuthority(authority);
        }
        return user;
    }
}
