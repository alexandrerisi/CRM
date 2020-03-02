package com.risi.mvc.data.demo.rest.authorisation;

import com.risi.mvc.data.demo.domain.Authority;
import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final UserService userService;
    private final TokenCacheService tokenCacheService;

    private synchronized String generateToken(User user) {

        var claims = new HashMap<String, Object>();
        claims.put("country", user.getCountry());
        claims.put("city", user.getCity());
        claims.put("enabled", user.isEnabled());
        claims.put("credentialsNonExpired", user.isCredentialsNonExpired());
        claims.put("accountNonLocked", user.isAccountNonLocked());
        claims.put("accountNonExpired", user.isAccountNonExpired());
        claims.put("id", user.getId());
        claims.put("authorities", user.getAuthorities());

        var token = Jwts.builder().setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // two hours.
                .signWith(key)
                .compact();
        tokenCacheService.addToken(token, user.getUsername());

        return token;
    }

    public String restAuthentication(String username, String password) {
        return generateToken(userService.restAuthentication(username, password));
    }

    User getUserFromToken(String token) {

        Jws<Claims> col = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        Claims claims = col.getBody();
        var user = new User();
        user.setId(claims.get("id", Integer.class));
        user.setUsername(claims.get("sub", String.class));
        user.setCountry(claims.get("country", String.class));
        user.setCity(claims.get("city", String.class));
        user.setEnabled(claims.get("enabled", Boolean.class));
        user.setCredentialsNonExpired(claims.get("credentialsNonExpired", Boolean.class));
        user.setAccountNonLocked(claims.get("accountNonLocked", Boolean.class));
        user.setAccountNonExpired(claims.get("accountNonExpired", Boolean.class));
        var list = (ArrayList) claims.get("authorities");
        for (Object object : list) {
            var authority = new Authority();
            authority.setAuthority(((Map) object).get("authority").toString());
            user.addAuthority(authority);
        }
        return user;
    }

    public boolean isValidToken(String token) {
        return tokenCacheService.isValidToken(token);
    }

    public void deleteUserToken(String username) {
        tokenCacheService.deleteToken(username);
    }
}
