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
   * @param pInfoPanel the panel where we put the information.
   */
  public OriginNode(final String pPath, final InfoPanel pInfoPanel) {
    super(pPath, null);
    this.ip = pInfoPanel;
    tpe = new ThreadPoolExecutor(2, 5, 2000, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>());
    TempNode self = new TempNode(this, this, pPath, this.ip);
    self.setNode(this);
    tpe.execute(self);
  }

  /**
   * Adds a TempNode to our queue.
   * @param tempNode the tempNode to add.
   */
  public final synchronized void addToQueue(final TempNode tempNode) {
    Node searchResults = search(tempNode.getPath());
    if (searchResults == null) {
      tempNode.setNode(tempNode.getParent().addChild(tempNode.getPath()));
      tpe.execute(tempNode);
    } else {
      try {
        tempNode.getParent().addChild(tempNode.getPath());
      } catch (Exception e) {
        System.out.println("Bad Parent");
      }
    }
  }

  /**
   * Gets the threadpool so we can see when it is over.
   * @return the threadpool we are working with.
   */
  public final ThreadPoolExecutor getThreadPool() {
    return this.tpe;
  }
}
