package com.kostasdrakonakis.randomizer.compiler;

import com.kostasdrakonakis.randomizer.annotations.RandomFloat;

import java.util.Random;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class FloatProperty extends RandomProperty {
    private float minValue;
    private float maxValue;

    FloatProperty(Element element) {
        super(element);
        minValue = element.getAnnotation(RandomFloat.class).minValue();
        maxValue = element.getAnnotation(RandomFloat.class).maxValue();
    }

    @Override
    boolean isTypeValid(Elements elements, Types types) {
        return element.asType().getKind().equals(TypeKind.FLOAT);
    }

    @Override
    String getRandomValue() {
        float generatedFloat = minValue + new Random().nextFloat() * (maxValue - minValue);
        return "" + generatedFloat;
    }
}
