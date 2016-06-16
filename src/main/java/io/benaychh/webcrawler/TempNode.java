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
public class TempNode {
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
   * Gets the parent node (so we can call addChild).
   * @return the parent node.
   */
  public final Node getParent() {
    return this.parent;
  }

  /**
   * Gets the path this node represents.
   * @return the path of this node.
   */
  public final String getPath() {
    return this.path;
  }
}
