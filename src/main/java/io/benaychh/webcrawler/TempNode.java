/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
   * The self node.
   */
  private Node self;
  /**
   * The path we need to check.
   */
  private final String path;
  /**
   * The Info panel.
   */
  private final InfoPanel ip;
  /**
   * Simple constructor.
   * @param pOrigin The origin node.
   * @param pParent the parent node (so we can add this once we have converted
   * it).
   * @param pPath the url this node represents.
   * @param pInfoPanel the panel to log everything.
   */
  public TempNode(final OriginNode pOrigin, final Node pParent,
      final String pPath, final InfoPanel pInfoPanel) {
    this.origin = pOrigin;
    this.parent = pParent;
    this.path = pPath;
    this.self = null;
    this.ip = pInfoPanel;
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
  
  public final void setNode(final Node pSelf) {
    this.self = pSelf;
  }
  @Override
  public final void run() {
      // this path is in our website original url so we have to crawl it.
      // We do this buy adding it to our executor service.
      if (this.path.contains(origin.getPath())) {
        try {
          Document page = Jsoup.connect(this.path).get();
          Elements links = page.select("a[href]");
          links.stream().forEach((link) -> {
            String stringLink = link.attr("abs:href");
            try {
              if (stringLink.charAt(stringLink.length() - 1) == '/') {
                stringLink = stringLink.substring(0, stringLink.length() - 1);
              }
              if (!stringLink.equals(this.path)) {
                int lastPoundSign = stringLink.indexOf("#");
                if (lastPoundSign == -1) {
                  ip.appendInfoAndLimitLines("Crawling: " + this.path);
                  this.origin.addToQueue(new TempNode(origin, this.self,
                    stringLink, ip));
                } else {
                  this.parent.addChild(stringLink);
                }
              }
            } catch (Exception ex) {
              System.err.println(stringLink);
            }
          });
        } catch (IOException ex) {
          ip.appendInfoAndLimitLines("Content Type is not HTML - " + this.path);
        }
      }
  }
}
