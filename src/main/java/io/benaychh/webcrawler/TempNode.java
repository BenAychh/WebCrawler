/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

/**
 *
 * @author benhernandez
 */
public class TempNode implements Runnable {
  /**
   * The parent node of this node.
   */
  private final Node parent;
  /**
   * The path we need to check.
   */
  private final String path;
  /**
   * Simple constructor.
   * @param pParent the parent node (so we can add this once we have converted
   * it).
   * @param pPath the url this node represents.
   */
  public TempNode(final Node pParent, final String pPath) {
    this.parent = pParent;
    this.path = pPath;
  }
  /**
   * Gets the url represented by this node.
   * @return the url of our node.
   */
  public String getPath() {
    return this.path;
  }
  @Override
  public void run() {
   
  }
}
