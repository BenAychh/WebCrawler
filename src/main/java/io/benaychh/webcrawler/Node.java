/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.util.ArrayList;

/**
 *
 * @author benhernandez
 */
public class Node {
  /**
   * The Nodes that are underneath this node.
   */
  private ArrayList<Node> children;
  /**
   * The child locker.
   */
  private final Object childrenLocker;
  /**
   * The Origin node (great great... grandparent node).
   */
  private final Node origin;
  /**
   * The path of this node's url.
   */
  private final String path;

  /**
   * Constructor.
   * @param pPath the path of our url.
   * @param pOrigin the starting node, all other nodes are descendents of
   * this node.
   */
  public Node(final String pPath, final Node pOrigin) {
    children = new ArrayList<>();
    childrenLocker = new Object();
    this.path = pPath;
    this.origin = pOrigin;
  }

  /**
   * Gets the path of the node.
   * @return the path of the node.
   */
  public String getPath() {
    return this.path;
  }

  /**
   * Search for a specific path (includes children in the search).
   * @param pPath the url to search for.
   * @return The node matching that path or null if not in this branch.
   */
  public final synchronized Node search(final String pPath) {
    if (this.path.equals(pPath)) {
      return this;
    } else {
      synchronized (childrenLocker) {
        for (Node node : children) {
          Node temp = node.search(pPath);
          if (temp != null) {
            return temp;
          }
        }
      }
      return null;
    }
  }

  /**
   * Adds a child to our array of children nodes.
   * @param pPath the url our child node represents.
   * @return the child node being added.
   */
  public final Node addChild(final String pPath) {
    Node tempOrigin = this.origin;
    if (tempOrigin == null) {
      tempOrigin = this;
    }
    Node tempNode = new Node(pPath, tempOrigin);
    synchronized (childrenLocker) {
      children.add(tempNode);
    }
    return tempNode;
  }

  @Override
  public final String toString() {
    return this.path;
  }

  public void printTree(int spacing, InfoPanel ip) {
    String spacer = "";
    for (int i = 0; i < spacing; i++) {
      if (i % 4 == 0) {
        spacer += "│";
      } else {
        spacer += " ";
      }
      
    }
    spacer += "├";
    for (int i = 0; i < 3; i++) {
      spacer += "─";
    }
    if (!this.path.isEmpty()) {
      ip.appendLog(spacer + this.toString());
      for (Node child : children) {
        child.printTree(spacing + 4, ip);
      }
    }
  }
}
