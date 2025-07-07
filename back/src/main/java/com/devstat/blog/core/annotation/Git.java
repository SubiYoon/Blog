
package com.devstat.blog.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Git 연동
 * 반드시 해당 함수 첫번째 인자에는 String 객체로 root폴더 이름이 지정되어 있어야함.
 * ex) /nas/project/blog/ABCD 일경우 해당 폴더에서 git task를 실행.
 * 이 때, ABCD가 해당 함수에 첫번째 인자로 들어가 있어야함.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Git {

    /*
     * 실행할 task + args
     */
    String task() default "";

    /*
     * docs root path
     */
    String path() default "/nas/project/blog";
}
