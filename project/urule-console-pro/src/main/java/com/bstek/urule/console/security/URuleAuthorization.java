package com.bstek.urule.console.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface URuleAuthorization {
   String authType();

   String model() default "";

   String code();

   boolean ruleFile() default false;

   boolean ruleDir() default false;
}
