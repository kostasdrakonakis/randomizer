package com.kostasdrakonakis.randomizer.compiler;

import com.kostasdrakonakis.randomizer.annotations.RandomDouble;
import com.kostasdrakonakis.randomizer.annotations.RandomFloat;

import java.util.Random;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class DoubleProperty extends RandomProperty {
    private double minValue;
    private double maxValue;

    public DoubleProperty(Element element) {
        super(element);
        minValue = element.getAnnotation(RandomDouble.class).minValue();
        maxValue = element.getAnnotation(RandomDouble.class).maxValue();
    }

    @Override
    boolean isTypeValid(Elements elements, Types types) {
        return element.asType().getKind().equals(TypeKind.DOUBLE);
    }

    @Override
    String getRandomValue() {
        double generatedDouble = minValue + new Random().nextDouble() * (maxValue - minValue);
        return "" + generatedDouble;
    }
}
