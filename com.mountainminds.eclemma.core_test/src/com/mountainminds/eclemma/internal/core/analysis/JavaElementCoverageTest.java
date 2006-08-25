/*
 * $Id$
 */
package com.mountainminds.eclemma.internal.core.analysis;

import junit.framework.TestCase;

/**
 * @author  Marc R. Hoffmann
 * @version $Revision: $
 */
public class JavaElementCoverageTest extends TestCase {
  
  public void testResourceModificationStamp() {
    JavaElementCoverage c = new JavaElementCoverage(null, false, 12345678);
    assertEquals(12345678l, c.getResourceModificationStamp());
  }

  public void testNoLines1() {
    JavaElementCoverage c = new JavaElementCoverage(null, false, 0);
    assertNull(c.getLineCoverage());
    assertNotNull(c.getLineCounter());
  }

  public void testNoLines2() {
    JavaElementCoverage c = new JavaElementCoverage(null, false, 0);
    c.addBlock(17, new int[] { 1, 2, 3 }, true);
    c.addBlock(15, new int[] { 3, 4, 5 }, false);
    assertEquals(2, c.getBlockCounter().getTotalCount());
    assertEquals(1, c.getBlockCounter().getCoveredCount());
    assertEquals(0, c.getLineCounter().getTotalCount());
    assertEquals(0, c.getLineCounter().getCoveredCount());
    assertEquals(32, c.getInstructionCounter().getTotalCount());
    assertEquals(17, c.getInstructionCounter().getCoveredCount());
  }
  
  public void testNoLinesWithParent1() {
    JavaElementCoverage p = new JavaElementCoverage(null, false, 0);
    JavaElementCoverage c1 = new JavaElementCoverage(p, false, 0);
    c1.addBlock(17, new int[] { 1, 2, 3 }, true);
    JavaElementCoverage c2 = new JavaElementCoverage(p, false, 0);
    c2.addBlock(15, new int[] { 3, 4, 5 }, false);

    assertEquals(2, p.getBlockCounter().getTotalCount());
    assertEquals(1, p.getBlockCounter().getCoveredCount());
    assertEquals(0, p.getLineCounter().getTotalCount());
    assertEquals(0, p.getLineCounter().getCoveredCount());
    assertEquals(32, p.getInstructionCounter().getTotalCount());
    assertEquals(17, p.getInstructionCounter().getCoveredCount());
  }

  
  public void testLines1() {
    JavaElementCoverage c = new JavaElementCoverage(null, true, 0);
    c.addBlock(17, new int[] { 1, 2, 3 }, true);
    c.addBlock(15, new int[] { 3, 4, 5 }, false);
    assertEquals(2, c.getBlockCounter().getTotalCount());
    assertEquals(1, c.getBlockCounter().getCoveredCount());
    assertEquals(5, c.getLineCounter().getTotalCount());
    assertEquals(2, c.getLineCounter().getCoveredCount());
    assertEquals(32, c.getInstructionCounter().getTotalCount());
    assertEquals(17, c.getInstructionCounter().getCoveredCount());
  }
  
  public void testLinesWithParent1() {
    JavaElementCoverage p = new JavaElementCoverage(null, false, 0);
    JavaElementCoverage c1 = new JavaElementCoverage(p, true, 0);
    c1.addBlock(17, new int[] { 1, 2, 3 }, true);
    JavaElementCoverage c2 = new JavaElementCoverage(p, true, 0);
    c2.addBlock(15, new int[] { 3, 4, 5 }, false);

    assertEquals(2, p.getBlockCounter().getTotalCount());
    assertEquals(1, p.getBlockCounter().getCoveredCount());
    assertEquals(6, p.getLineCounter().getTotalCount());
    assertEquals(3, p.getLineCounter().getCoveredCount());
    assertEquals(32, p.getInstructionCounter().getTotalCount());
    assertEquals(17, p.getInstructionCounter().getCoveredCount());
  }
  
  public void testLinesWithParent2() {
    JavaElementCoverage p = new JavaElementCoverage(null, true, 0);
    JavaElementCoverage c1 = new JavaElementCoverage(p, true, 0);
    c1.addBlock(17, new int[] { 1, 2, 3 }, true);
    JavaElementCoverage c2 = new JavaElementCoverage(p, true, 0);
    c2.addBlock(15, new int[] { 3, 4, 5 }, false);

    assertEquals(2, p.getBlockCounter().getTotalCount());
    assertEquals(1, p.getBlockCounter().getCoveredCount());
    assertEquals(5, p.getLineCounter().getTotalCount());
    assertEquals(2, p.getLineCounter().getCoveredCount());
    assertEquals(32, p.getInstructionCounter().getTotalCount());
    assertEquals(17, p.getInstructionCounter().getCoveredCount());
  }
  
}
