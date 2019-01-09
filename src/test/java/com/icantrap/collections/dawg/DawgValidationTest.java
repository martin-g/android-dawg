// LICENSE: GPLv3. http://www.gnu.org/licenses/gpl-3.0.txt

package com.icantrap.collections.dawg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.time.StopWatch;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DawgValidationTest {
    private static Dawg dawg;

    @BeforeAll
    public static void init() throws IOException {
//    assumeThat (System.getProperty ("RUN_VALIDATION"), is ("on"));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        dawg = Dawg.load(DawgValidationTest.class.getResourceAsStream("/twl06.dat"));
        stopWatch.stop();
        System.out.println("Time to load " + dawg.nodeCount() + " node dawg:  " + stopWatch.getTime() + " ms.");
    }

    @Test
    void containsAllWords() throws IOException {
        try (LineIterator iter = IOUtils.lineIterator(getClass().getResourceAsStream("/TWL06.txt"), StandardCharsets.UTF_8)) {

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            while (iter.hasNext()) {
                String word = iter.next();
                assertTrue(dawg.contains(word), "Missing word (" + word + ")");
            }

            stopWatch.stop();
            System.out.println("Time to query:  " + stopWatch.getTime() + " ms.");

        }
    }

    @Test
    void subwords_noWildcards() {
        Dawg.Result[] subwords = dawg.subwords("PHONE", null);
        Set<String> words = Dawg.extractWords(subwords);

        assertThat(words, hasItem("PHONE"));
        assertThat(words, hasItem("HONE"));
        assertThat(words, hasItem("PONE"));
        assertThat(words, hasItem("NOPE"));
        assertThat(words, hasItem("EON"));
        assertThat(words, hasItem("HON"));
        assertThat(words, hasItem("ONE"));
        assertThat(words, hasItem("EH"));
        assertThat(words, hasItem("PE"));
        assertThat(words, hasItem("OP"));

        assertThat(subwords.length, Matchers.is(31));
    }

    @Test
    void subwords_wildcard() {
        Dawg.Result[] subwords = dawg.subwords("?Q", null);
        Set<String> words = Dawg.extractWords(subwords);

        assertThat(words, hasItem("QI"));
        assertThat(subwords.length, Matchers.is(1));
        assertThat(subwords[0].wildcardPositions[0], is(1));
    }
}
