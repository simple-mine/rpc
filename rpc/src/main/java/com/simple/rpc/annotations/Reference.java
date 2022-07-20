package com.simple.rpc.annotations;

import java.lang.annotation.*;

/**
 * @Author: huangjun
 * @Date: 2022/7/8 10:09
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Reference {
    String name() default "";
}
