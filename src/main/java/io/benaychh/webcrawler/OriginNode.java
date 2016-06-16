/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

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
   * The info panel.
   */
  private final InfoPanel ip;
  /**
   * Simple constructor.
   * @param pPath the path of the origin.
   */
  public OriginNode(final String pPath, InfoPanel pInfoPanel) {
    super(pPath, null);
    this.ip = pInfoPanel;
    tpe = new ThreadPoolExecutor(10, 20, 2000, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>());
    tpe.execute(new TempNode(this, this, pPath, this.ip));
  }

  /**
   * Adds a TempNode to our queue.
   * @param tempNode the tempNode to add.
   */
  public final synchronized void addToQueue(TempNode tempNode) {
    Node searchResults = search(tempNode.getPath());
    if (searchResults == null) {
//      ip.appendInfoAndLimitLines("New Node: " + tempNode.getPath());
      tpe.execute(tempNode);
    } else {
      try {
        tempNode.getParent().addChild(tempNode.getPath());
      } catch (Exception e) {
        this.ip.appendInfoAndLimitLines("Invalid Parent");
      }
    }
  }
  
  public final ThreadPoolExecutor getThreadPool() {
    return this.tpe;
  }
}
