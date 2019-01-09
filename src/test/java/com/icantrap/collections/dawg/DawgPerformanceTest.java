// LICENSE: GPLv3. http://www.gnu.org/licenses/gpl-3.0.txt

package com.icantrap.collections.dawg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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

            while (iter.hasNext())
                dawgBuilder.add(iter.next());

            System.out.println("Uncompressed:  " + dawgBuilder.nodeCount() + " nodes");

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            dawg = dawgBuilder.build();
            stopWatch.stop();

            System.out.println("Time to compress:  " + stopWatch.getTime() + " ms.");
        }

        System.out.println("Compressed:  " + dawg.nodeCount() + " nodes");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dawg.store(baos);
        final int size = baos.toByteArray().length;
        System.out.println("Runtime size:  " + size + " bytes");

        dawg.store(new FileOutputStream("/tmp/dawg.dat"));
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
