package com.example.ordercart.security.aspect;

import com.example.ordercart.security.annotation.CheckUserPermission;
import com.example.ordercart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserPermissionAspect {
    private final UserService userService;

    @Around("@annotation(check)")
    public Object around(ProceedingJoinPoint pjp, CheckUserPermission check) throws Throwable {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final boolean loggedIn = userService.isUserLoggedIn();

        // 1) Authentication check
        if (!loggedIn) {
            return handleUnauthorizedReturn(pjp, "User is not authenticated");
        }

        // 2) Optional permission check
        String required = check.value();
        if (!required.isBlank()) {
            boolean hasPerm = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(required::equals);
            if (!hasPerm) {
                return handleUnauthorizedReturn(pjp, "Access denied for permission: " + required);
            }
        }

        // 3) All good → invoke original method
        return pjp.proceed();
    }

    private Object handleUnauthorizedReturn(ProceedingJoinPoint pjp, String message) {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Class<?> retType = sig.getReturnType();

        if (retType.equals(boolean.class) || retType.equals(Boolean.class)) {
            // if the method returns boolean, just return false
            return false;
        }
        // otherwise, throw an exception
        throw new AccessDeniedException(message);
    }
}
