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
    this.path = pPath;
    this.origin = pOrigin;
  }

  /**
   * Search for a specific path (includes children in the search).
   * @param pPath the url to search for.
   * @return The node matching that path or null if not in this branch.
   */
  public final Node search(final String pPath) {
    if (this.path.equals(pPath)) {
      return this;
    } else {
      for (Node node : children) {
        Node temp = node.search(pPath);
        if (temp != null) {
          return temp;
        }
      }
      return null;
    }
  }

  /**
   * Adds a child to our array of children nodes.
   * @param pPath the url our child node represents.
   */
  public final void addChild(final String pPath) {
    Node tempOrigin = this.origin;
    if (tempOrigin == null) {
      tempOrigin = this;
    }
    children.add(new Node(pPath, tempOrigin));
  }
}
