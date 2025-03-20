package com.carrotbay.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 어노테이션이 적용될 수 있는 대상을 지정한다.
@Retention(RetentionPolicy.RUNTIME) //  어노테이션의 유효 기간을 지정한다.
public @interface LoginUser {
}
