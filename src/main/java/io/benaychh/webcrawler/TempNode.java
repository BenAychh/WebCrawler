/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author benhernandez
 */
public class TempNode implements Runnable {
  /**
   * The origin Node.
   */
  private final OriginNode origin;
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
   * @param pOrigin The origin node.
   * @param pParent the parent node (so we can add this once we have converted
   * it).
   * @param pPath the url this node represents.
   */
  public TempNode(final OriginNode pOrigin, final Node pParent,
      final String pPath) {
    this.origin = pOrigin;
    this.parent = pParent;
    this.path = pPath;
  }
  /**
   * Gets the url represented by this node.
   * @return the url of our node.
   */
  public final String getPath() {
    return this.path;
  }
  /**
   * Gets the parent node.
   * @return the parent node.
   */
  public final Node getParent() {
    return this.parent;
  }
  @Override
  public final void run() {
    try {
      Node newNode = this.parent.addChild(path);
      // this path is in our website original url so we have to crawl it.
      // We do this buy adding it to our executor service.
      if (this.path.contains(origin.getPath())) {
        System.out.println("Crawling: " + this.path);
        Document page = Jsoup.connect(this.path).get();
        Elements links = page.select("a[href]");
        links.stream().forEach((link) -> {
          this.origin.addToQueue(new TempNode(origin, newNode,
              link.attr("abs:href")));
        });
      }
    } catch (IOException ex) {
      Logger.getLogger(TempNode.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
