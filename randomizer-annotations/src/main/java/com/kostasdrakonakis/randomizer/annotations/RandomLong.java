package com.kostasdrakonakis.randomizer.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(value = ElementType.FIELD)
@Documented
public @interface RandomLong {
    long minValue() default 0L;
    long maxValue() default 10000L;
}
