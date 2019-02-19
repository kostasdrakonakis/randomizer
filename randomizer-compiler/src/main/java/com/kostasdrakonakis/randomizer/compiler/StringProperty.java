package com.kostasdrakonakis.randomizer.compiler;

import com.kostasdrakonakis.randomizer.annotations.RandomString;

import java.util.Random;
import java.util.UUID;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class StringProperty extends RandomProperty {

    private static final String QUALIFIER_STRING = "java.lang.String";
    private static final String SEED = "ABCDEFGHJKLMNOPRSTUVYZabcdefghjklmnoprstuvyz";

    private boolean uuid;

    public StringProperty(Element element) {
        super(element);
        uuid = element.getAnnotation(RandomString.class).uuid();
    }

    @Override
    boolean isTypeValid(Elements elements, Types types) {
        TypeMirror elementType = element.asType();
        TypeMirror string = elements.getTypeElement(QUALIFIER_STRING).asType();
        return types.isSameType(elementType, string);
    }

    @Override
    String getRandomValue() {
        if (uuid) {
            return "\"" + UUID.randomUUID().toString() + "\"";
        } else {
            StringBuilder builder = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                int randIndex = random.nextInt(SEED.length());
                builder.append(SEED.charAt(randIndex));
            }
            return "\"" + builder.toString() + "\"";
        }
    }
}
