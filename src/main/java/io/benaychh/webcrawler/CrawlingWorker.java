/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

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
   * @param pIp 
   */
  public CrawlingWorker(final InfoPanel pIp, final String pUrl) {
    this.ip = pIp;
    this.url = pUrl;
  }
  @Override
  protected Void doInBackground() throws Exception {
    this.ip.clearLog();
    OriginNode on = new OriginNode(this.url, this.ip);
    ThreadPoolExecutor tpe = on.getThreadPool();
    final int sleepTime = 5000;
    while (tpe.getTaskCount() != tpe.getCompletedTaskCount()) {
      try {
        this.ip.setStatus("Ensuring complete", InfoPanel.Levels.ok);
        Thread.sleep(sleepTime);
      } catch (InterruptedException ex) {
        Logger.getLogger(InfoPanel.class.getName())
            .log(Level.SEVERE, null, ex);
      }
    }
    tpe.shutdown();
    final int timeToFinish = 3600;
    try {
      this.ip.setStatus("Finalizing", InfoPanel.Levels.ok);
      tpe.awaitTermination(timeToFinish, TimeUnit.MILLISECONDS);
    } catch (InterruptedException ex) {
      Logger.getLogger(InfoPanel.class.getName())
          .log(Level.SEVERE, null, ex);
    }
    this.ip.setStatus("Printing Tree", InfoPanel.Levels.ok);
    on.printTree(0, this.ip);
    return null;
  }
}
