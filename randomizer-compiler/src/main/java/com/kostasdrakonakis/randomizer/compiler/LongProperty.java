package com.kostasdrakonakis.randomizer.compiler;

import com.kostasdrakonakis.randomizer.annotations.RandomLong;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class LongProperty extends RandomProperty {
    private long minValue;
    private long maxValue;

    public LongProperty(Element element) {
        super(element);
        minValue = element.getAnnotation(RandomLong.class).minValue();
        maxValue = element.getAnnotation(RandomLong.class).maxValue();
    }

    @Override
    boolean isTypeValid(Elements elements, Types types) {
        return element.asType().getKind().equals(TypeKind.LONG);
    }

    @Override
    String getRandomValue() {
        long generatedLong = minValue + (long) (Math.random() * (maxValue - minValue));
        return "" + generatedLong;
    }
}
