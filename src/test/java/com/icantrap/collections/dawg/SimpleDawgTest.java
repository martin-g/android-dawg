package com.icantrap.collections.dawg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 */
class SimpleDawgTest {

    private Dawg dawg;

    @BeforeEach
    void setup() throws IOException {

        try (InputStream inputStream = Dawg.class.getClassLoader().getResourceAsStream("words.txt")) {
            final DawgBuilder dawgBuilder = new DawgBuilder();
            dawgBuilder.add(inputStream);
            dawg = dawgBuilder.build();
        }

    }


    @Test
    void contains() {
        assertFalse(dawg.contains("aaa"));
        assertTrue(dawg.contains("blip"));
        assertTrue(dawg.contains("can"));
        assertTrue(dawg.contains("cat"));
        assertTrue(dawg.contains("cat in a box"));
        assertTrue(dawg.contains("cate"));
        assertTrue(dawg.contains("cats"));
        assertTrue(dawg.contains("hello"));
        assertTrue(dawg.contains("jello"));
    }

    @Test
    void suggest() {
        final Set<String> suggestions = dawg.suggest("ca");

        assertAll(
            () -> assertEquals(5, suggestions.size()),
            () -> assertTrue(suggestions.contains("can"), "can"),
            () -> assertTrue(suggestions.contains("cat"), "cat"),
            () -> assertTrue(suggestions.contains("cat in a box"), "cat in a box"),
            () -> assertTrue(suggestions.contains("cate"), "cate"),
            () -> assertTrue(suggestions.contains("cats"), "cats")
        );
    }
}
