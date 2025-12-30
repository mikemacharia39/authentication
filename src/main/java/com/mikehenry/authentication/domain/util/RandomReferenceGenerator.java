package com.mikehenry.authentication.domain.util;

import org.apache.commons.text.RandomStringGenerator;

public class RandomReferenceGenerator {
    public static String generateReference() {
        final int referenceLength = 10;
        final RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(new char[]{'0', '9'}, new char[]{'A', 'Z'})
                .get();
        return generator.generate(referenceLength);
    }
}
