/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author benhernandez
 */
public class OriginNode extends Node {
  /**
   * The pool of threads for TempNode processing.
   */
  ExecutorService es;
  /**
   * The size of our queue.
   */
  private final int queueSize = 5;
  /**
   * Simple constructor.
   * @param pPath the path of the origin.
   */
  public OriginNode(final String pPath) {
    super(pPath, null);
    es = Executors.newFixedThreadPool(queueSize);
  }

  /**
   * Adds a TempNode to our queue.
   * @param tempNode the tempNode to add.
   */
  public synchronized void addToQueue(TempNode tempNode) {
    Node searchResults = search(tempNode.getPath());
    if (searchResults == null) {
      es.execute(tempNode);
    }
  }
}
