package com.kostasdrakonakis.randomizer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(value = ElementType.FIELD)
public @interface RandomInt {
    int minValue() default 0;
    int maxValue() default 10000;
}
