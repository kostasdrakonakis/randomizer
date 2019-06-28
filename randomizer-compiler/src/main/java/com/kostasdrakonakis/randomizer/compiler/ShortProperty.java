package com.kostasdrakonakis.randomizer.compiler;

import java.util.Random;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class ShortProperty extends RandomProperty {

    ShortProperty(Element element) {
        super(element);
    }

    @Override
    boolean isTypeValid(Elements elements, Types types) {
        return element.asType().getKind().equals(TypeKind.SHORT);
    }

    @Override
    String getRandomValue() {
        Random random = new Random();
        return "" + (short) random.nextInt(1 << 16);
    }
}
