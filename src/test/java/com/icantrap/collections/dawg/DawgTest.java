package com.icantrap.collections.dawg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DawgTest {

    private Dawg createDawg(String txtFile) throws IOException {

        try (InputStream inputStream = Dawg.class.getClassLoader().getResourceAsStream(txtFile)) {
            final DawgBuilder dawgBuilder = new DawgBuilder();
            dawgBuilder.add(inputStream);
            return dawgBuilder.build();
        }

    }

    @Test
    void contains() throws IOException {
        Dawg dawg = createDawg("words.txt");
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
    @DisplayName("Searching for a prefix, e.g. 'zoo' should return all suggestions starting with it, including the phrases")
    void suggest() throws IOException {
        Dawg dawg = createDawg("TWL06.txt");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final Set<String> suggestions = dawg.suggest("zoo");
        stopWatch.stop();

        System.out.println("Time to lookup " + suggestions.size() + " suggestions:  " + stopWatch.getTime() + " ms.");

        assertAll(
            () -> assertEquals(111, suggestions.size()),
            () -> assertFalse(suggestions.contains("notZoo".toLowerCase()), "notZoo"),
            () -> assertTrue(suggestions.contains("ZOO".toLowerCase()), "ZOO"),
            () -> assertTrue(suggestions.contains("ZOOPHOBIAS as a phrase".toLowerCase()), "ZOOPHOBIAS as a phrase"),
            () -> assertTrue(suggestions.contains("ZOOPLANKTER".toLowerCase()), "ZOOPLANKTER"),
            () -> assertTrue(suggestions.contains("ZOOSPORES".toLowerCase()), "ZOOSPORES"),
            () -> assertTrue(suggestions.contains("ZOOXANTHELLAE".toLowerCase()), "ZOOXANTHELLAE")
        );
    }

    @Test
    @DisplayName("Searching for prefixes of words in a phrase, e.g. 'ca i bo', should return all suggestions starting with 'ca' and containing 'i' and 'bo'")
    void suggestPhrasePrefixes() throws IOException {
        final Dawg dawg = createDawg("words.txt");

        final Set<String> suggestionsPhrasePrefixes = dawg.suggest("ca bo");

        assertAll(
            () -> assertEquals(1, suggestionsPhrasePrefixes.size()),
            () -> assertTrue(suggestionsPhrasePrefixes.contains("cat in a box".toLowerCase()), "cat in a box")
        );
    }
}
