// LICENSE: GPLv3. http://www.gnu.org/licenses/gpl-3.0.txt

package com.icantrap.collections.dawg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrieValidationTest
{
  private DawgBuilder dawgBuilder;

  @BeforeEach
  void before () throws IOException
  {
//    assumeThat (System.getProperty ("RUN_VALIDATION"), is ("on"));
    try (LineIterator iter = IOUtils.lineIterator (getClass ().getResourceAsStream ("/TWL06.txt"), StandardCharsets.UTF_8)) {
      dawgBuilder = new DawgBuilder();

      while (iter.hasNext())
        dawgBuilder.add(iter.next());

    }

    System.out.println ("Uncompressed:  " + dawgBuilder.nodeCount () + " nodes");

    StopWatch stopWatch = new StopWatch();
    stopWatch.start ();
    dawgBuilder.build ();
    stopWatch.stop ();
    
    System.out.println ("Time to compress:  " + stopWatch.getTime () + " ms.");
    System.out.println ("Compressed:  " + dawgBuilder.nodeCount () + " nodes");
  }

  @Test
  void containsAllWords () throws IOException
  {
    try (LineIterator iter = IOUtils.lineIterator (getClass ().getResourceAsStream ("/TWL06.txt"), StandardCharsets.UTF_8)) {

      StopWatch stopWatch = new StopWatch();
      stopWatch.start();

      while (iter.hasNext()) {
        String word = iter.next();
        assertTrue(dawgBuilder.contains(word), "Missing word (" + word + ")");
      }

      stopWatch.stop();
      System.out.println("Time to query:  " + stopWatch.getTime() + " ms.");

    }
  }
}
