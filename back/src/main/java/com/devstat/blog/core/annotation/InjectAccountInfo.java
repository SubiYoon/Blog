package com.devstat.blog.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 현재 사용자에 대한 정보를 Map 에 담음
 * 해당 함수의 첫번쨰 인자가 Map 이어야 함
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectAccountInfo {
}
