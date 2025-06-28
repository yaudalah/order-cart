package com.example.ordercart.security.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckUserPermission {
    /**
     * Optional permission string (e.g. "PRODUCT_ADD", "ORDER_EDIT", etc).
     * Leave blank if you only want to test authentication.
     */
    String value() default "";
}
