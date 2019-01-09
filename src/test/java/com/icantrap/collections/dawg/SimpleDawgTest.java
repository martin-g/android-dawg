package com.icantrap.collections.dawg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 */
class SimpleDawgTest {

    private Dawg dawg;

    @BeforeEach
    void setup () throws IOException {
        DawgBuilder dawgBuilder = new DawgBuilder ();

        final InputStream inputStream = Dawg.class.getClassLoader().getResourceAsStream("words.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            reader
                .lines()
                .forEach(dawgBuilder::add);
        }

        dawg = dawgBuilder.build();
    }


    @Test
    void contains() {
        assertFalse(dawg.contains("aaa"));
        assertTrue(dawg.contains("blip"));
        assertTrue(dawg.contains("can"));
        assertTrue(dawg.contains("cat"));
        assertTrue(dawg.contains("cate"));
        assertTrue(dawg.contains("cats"));
        assertTrue(dawg.contains("hello"));
        assertTrue(dawg.contains("jello"));
    }

    @Test
    void simple() {
        final Dawg.Result[] subwords = dawg.subwords("cats", null);
        Set<String> words = Dawg.extractWords (subwords);

        System.err.println("=== words:\n" + words);
    }
}
