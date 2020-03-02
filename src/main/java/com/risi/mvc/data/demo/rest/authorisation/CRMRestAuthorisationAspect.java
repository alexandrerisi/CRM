package com.risi.mvc.data.demo.rest.authorisation;

import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.exception.InsufficientPermissionException;
import com.risi.mvc.data.demo.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CRMRestAuthorisationAspect {

    private final JwtService jwtService;

    @Pointcut("execution(* com.risi.mvc.data.demo.rest.CustomerRestApi.getToken(..))")
    private void forRestGetToken() {
    }

    @Pointcut("execution(* com.risi.mvc.data.demo.rest.CustomerRestApi.*(..))")
    private void forRestMethods() {
    }

    @Pointcut("execution(* com.risi.mvc.data.demo.rest.CustomerRestApi.get*(..))")
    private void forRestGetMethods() {
    }

    @Pointcut("execution(* com.risi.mvc.data.demo.rest.CustomerRestApi.add*(..))")
    private void forRestAddMethods() {
    }

    @Pointcut("execution(* com.risi.mvc.data.demo.rest.CustomerRestApi.delete*(..))")
    private void forRestDeleteMethods() {
    }

    @Pointcut("execution(* com.risi.mvc.data.demo.rest.CustomerRestApi.update*(..))")
    private void forRestUpdateMethods() {
    }

    @Before("forRestMethods() && !forRestGetToken()")
    @Order(0)
    public void before(JoinPoint joinPoint) throws InvalidTokenException {
        Object[] args = joinPoint.getArgs();
        String token = args[0].toString();
        if (!jwtService.isValidToken(token))
            throw new InvalidTokenException("Invalid Token\n" + token);
    }

    @Before("forRestGetMethods() && !forRestGetToken()")
    @Order(1)
    public void beforeGet(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "EMPLOYEE");
    }

    @Before("forRestAddMethods() && !forRestGetToken()")
    @Order(1)
    public void beforeAdd(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "MANAGER", "ADMIN");
    }

    @Before("forRestUpdateMethods() && !forRestGetToken()")
    public void beforeUpdate(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "MANAGER", "ADMIN");
    }

    @Before("forRestDeleteMethods() && !forRestGetToken()")
    @Order(1)
    public void beforeDelete(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "ADMIN");
    }

    private void isAllowed(JoinPoint joinPoint, String... allowedAuthorities) throws InsufficientPermissionException {
        Object[] args = joinPoint.getArgs();
        String token = args[0].toString();
        User user = jwtService.getUserFromToken(token);

        for (GrantedAuthority authority : user.getAuthorities())
            for (String allowed : allowedAuthorities)
                if (authority.getAuthority().equals(allowed))
                    return;

        throw new InsufficientPermissionException();
    }
}
