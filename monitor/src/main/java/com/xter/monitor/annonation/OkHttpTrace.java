package com.xter.monitor.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author XTER
 * @Date 2020/9/2 17:00
 * @Description Okhttp切面注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OkHttpTrace {
    String value();
}
