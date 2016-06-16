/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.io.PrintWriter;
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
  public final String getPath() {
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
      // We can't be searching the children while we are adding them.
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
    synchronized (childrenLocker) {
      Node tempNode = new Node(pPath, tempOrigin);
      boolean unique = true;
      for (Node node : children) {
        if (node.getPath().equals(pPath)) {
          unique = false;
          tempNode = node;
          break;
        }
      }
      if (unique) {
        children.add(tempNode);
      }
      return tempNode;
    }
  }

  @Override
  public final String toString() {
    return this.path;
  }

  /**
   * Prints the tree of nodes.
   * @param spacing the spacing (so we can move things incrementally in).
   * @param pw the printwriter so we can write our text file.
   */
  public final void printTree(final int spacing, final PrintWriter pw) {
    String spacer = "";
    final int spacingAmount = 4;
    for (int i = 0; i < spacing; i++) {
      if (i % spacingAmount == 0) {
        spacer += "│";
      } else {
        spacer += " ";
      }
    }
    spacer += "├";
    for (int i = 0; i < spacingAmount - 1; i++) {
      spacer += "─";
    }
    if (!this.path.isEmpty()) {
      pw.println(spacer + this.toString());
      for (Node child : children) {
        child.printTree(spacing + spacingAmount, pw);
      }
    }
  }
}
