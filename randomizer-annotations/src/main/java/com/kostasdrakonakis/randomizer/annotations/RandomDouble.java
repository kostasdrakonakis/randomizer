package com.kostasdrakonakis.randomizer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(value = ElementType.FIELD)
public @interface RandomDouble {
    double minValue() default 0D;
    double maxValue() default 10000D;
}
