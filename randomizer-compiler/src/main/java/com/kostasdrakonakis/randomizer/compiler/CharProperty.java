package com.kostasdrakonakis.randomizer.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class CharProperty extends RandomProperty {

    CharProperty(Element element) {
        super(element);
    }

    @Override
    boolean isTypeValid(Elements elements, Types types) {
        return element.asType().getKind().equals(TypeKind.CHAR);
    }

    @Override
    String getRandomValue() {
        return "" + getRandomChar();
    }

    private char getRandomChar () {
        int rnd = (int) (Math.random() * 52);
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);
    }
}
