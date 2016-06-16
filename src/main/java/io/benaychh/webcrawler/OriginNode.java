/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author benhernandez
 */
public class OriginNode extends Node {
  private boolean firstScan = true;
  /**
   * The pool of threads for TempNode processing.
   */
  private ThreadPoolExecutor tpe;
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
    tpe = new ThreadPoolExecutor(3, 3, 2000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    tpe.execute(new TempNode(this, this, pPath));
  }

  /**
   * Adds a TempNode to our queue.
   * @param tempNode the tempNode to add.
   */
  public synchronized void addToQueue(TempNode tempNode) {
    Node searchResults = search(tempNode.getPath());
    if (searchResults == null ) {
      System.out.println("New Node: " + tempNode.getPath());
      tpe.execute(tempNode);
    } else {
      System.out.println("Already Exists: " + tempNode.getPath());
      tempNode.getParent().addChild(tempNode.getPath());
    }
  }
  
  public final ThreadPoolExecutor getThreadPool() {
    return this.tpe;
  }
}
