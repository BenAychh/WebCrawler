/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.io.PrintWriter;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author benhernandez
 */
public class CrawlingWorker extends SwingWorker<Void, Void> {
  /**
   * The info panel to write our status to.
   */
  private final InfoPanel ip;
  /**
   * The page to crawl.
   */
  private final String url;
  /**
   * Simple constructor.
   * @param pIp the Info Panel to write results to.
   * @param pUrl the url to fetch.
   */
  public CrawlingWorker(final InfoPanel pIp, final String pUrl) {
    this.ip = pIp;
    this.url = pUrl;
  }
  @Override
  protected final Void doInBackground() throws Exception {
    this.ip.clearLog();
    // URL validation taken from http://www.santhoshreddymandadi.com/java/best-url-and-email-validation-using.html
    String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
    if (url.matches(urlPattern)) {
      OriginNode on = new OriginNode(this.url, this.ip);
      ThreadPoolExecutor tpe = on.getThreadPool();
      final int sleepTime = 5000;
      while (tpe.getTaskCount() != tpe.getCompletedTaskCount()) {
        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
          Logger.getLogger(InfoPanel.class.getName())
              .log(Level.SEVERE, null, ex);
        }
      }
      tpe.shutdown();
      final int timeToFinish = 60;
      try {
        this.ip.setStatus("Finalizing", InfoPanel.Levels.ok);
        tpe.awaitTermination(timeToFinish, TimeUnit.SECONDS);
      } catch (InterruptedException ex) {
        Logger.getLogger(InfoPanel.class.getName())
            .log(Level.SEVERE, null, ex);
      }
      this.ip.setStatus("Printing Tree", InfoPanel.Levels.warn);
      PrintWriter pw = new PrintWriter("tree", "UTF-8");
      on.printTree(0, pw);
      this.ip.setStatus("Printing Finished", InfoPanel.Levels.ok);
    } else {
      this.ip.setStatus("Bad URL Format", InfoPanel.Levels.error);
    }
    return null;
  }
}
