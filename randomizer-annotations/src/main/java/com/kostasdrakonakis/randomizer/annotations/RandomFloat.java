package com.kostasdrakonakis.randomizer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(value = ElementType.FIELD)
public @interface RandomFloat {
    float minValue() default 0F;
    float maxValue() default 10000F;
}
