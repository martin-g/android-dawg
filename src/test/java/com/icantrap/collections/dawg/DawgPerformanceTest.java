// LICENSE: GPLv3. http://www.gnu.org/licenses/gpl-3.0.txt

package com.icantrap.collections.dawg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DawgPerformanceTest {

    private Dawg dawg;

    @BeforeEach
    void before() throws IOException {

        try (LineIterator iter = IOUtils.lineIterator(getClass().getResourceAsStream("/TWL06.txt"), StandardCharsets.UTF_8)) {
            DawgBuilder dawgBuilder = new DawgBuilder();

            StopWatch importStopWatch = new StopWatch();
            importStopWatch.start();

            int lines = 0;
            while (iter.hasNext()) {
                dawgBuilder.add(iter.next());
                lines++;
            }

            importStopWatch.stop();
            System.out.println("Time to import " + lines + " words/phrases :  " + importStopWatch.getTime() + " ms.");
            System.out.println("Uncompressed:  " + dawgBuilder.nodeCount() + " nodes");

            StopWatch compressStopWatch = new StopWatch();
            compressStopWatch.start();
            dawg = dawgBuilder.build();
            compressStopWatch.stop();

            System.out.println("Time to compress:  " + compressStopWatch.getTime() + " ms.");
        }

        System.out.println("Compressed:  " + dawg.nodeCount() + " nodes");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dawg.store(baos);
        final int size = baos.toByteArray().length;
        System.out.println("Runtime size:  " + size + " bytes");
    }

    @Test
    void containsAllWords() throws IOException {
        try (LineIterator iter = IOUtils.lineIterator(getClass().getResourceAsStream("/TWL06.txt"), StandardCharsets.UTF_8)) {

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            int i = 0;
            while (iter.hasNext()) {
                String word = iter.next();
                assertTrue(dawg.contains(word), "Missing word (" + word + ")");
                i++;
            }

            stopWatch.stop();
            System.out.println("Time to query all (" + i + ") words :  " + stopWatch.getTime() + " ms.");

        }
    }
}
