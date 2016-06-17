/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.io.IOException;
import org.jsoup.HttpStatusException;
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

  /**
   * Sets the self node.
   * @param pSelf the node realization of this temp node.
   */
  public final void setNode(final Node pSelf) {
    this.self = pSelf;
  }

  @Override
  public final void run() {
      // this path is in our website original url so we have to crawl it.
      // We do this buy adding it to our executor service.
      if (this.path.contains(origin.getPath())) {
        try {
          Document page = Jsoup.connect(this.path).ignoreContentType(true)
              .get();
          Elements links = page.select("a[href]");
          links.stream().forEach((link) -> {
            // Gets the absolute url.
            String stringLink = link.attr("abs:href");
            try {
              // Extra slashes make the benaychh.io different from benaychh.io/
              if (stringLink.charAt(stringLink.length() - 1) == '/') {
                stringLink = stringLink.substring(0, stringLink.length() - 1);
              }
              // Don't want to be recrawling the page we are on (pages can link
              // back to themselves)
              if (!stringLink.equals(this.path)) {
                // Don't need to crawl #anchor links.
                int lastPoundSign = stringLink.indexOf("#");
                if (lastPoundSign == -1) {
                  ip.appendInfoAndLimitLines("Crawling: " + this.path);
                  this.origin.addToQueue(new TempNode(origin, this.self,
                    stringLink, ip));
                } else {
                  // Add the #anchor links to the tree.
                  this.parent.addChild(stringLink);
                }
              }
            } catch (Exception ex) {
              System.err.println(stringLink);
            }
          });
        } catch (HttpStatusException ex) {
          // Repeat the request, 429 is too many requests
          final int tooManyRequests = 429;
          if (ex.getStatusCode() == tooManyRequests) {
            ip.appendInfoAndLimitLines("Too Many Requests, retrying - "
               + this.path);
            // Otherwise our search will keep this from being reparsed.
            this.parent.removeChild(self);
            // Readd this node to the queue to be processed again.
            this.origin.addToQueue(this);
          } else {
            // Other error codes.
            ip.appendInfoAndLimitLines("HTML Error Code - "
                + ex.getStatusCode() + " - " + this.path);
          }
        } catch (IOException ex) {
          ip.appendInfoAndLimitLines(ex.getMessage() + " - " + this.path);
        }
      }
  }
}
