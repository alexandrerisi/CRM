package com.risi.mvc.data.demo.rest.authorisation;

import com.risi.mvc.data.demo.domain.User;
import com.risi.mvc.data.demo.exception.InsufficientPermissionException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMRestAuthorisationAspect {

    @Autowired
    private JwtService jwtService;

    @Pointcut("execution(* com.risi.mvc.data.demo.rest.CustomerRestApi.getToken(..))")
    private void forRestGetToken() {
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

    @Before("forRestGetMethods() && !forRestGetToken()")
    public void beforeGet(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "EMPLOYEE");
    }

    @Before("forRestAddMethods() && !forRestGetToken()")
    public void beforeAdd(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "MANAGER", "ADMIN");
    }

    @Before("forRestUpdateMethods() && !forRestGetToken()")
    public void beforeUpdate(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "MANAGER", "ADMIN");
    }

    @Before("forRestDeleteMethods() && !forRestGetToken()")
    public void beforeDelete(JoinPoint joinPoint) throws InsufficientPermissionException {
        isAllowed(joinPoint, "MANAGER", "ADMIN");
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
