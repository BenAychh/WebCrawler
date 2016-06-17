/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\"
        + ".([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
    // Check to see if this is a valid url.
    if (url.matches(urlPattern)) {
      // Start the node.
      OriginNode on = new OriginNode(this.url, this.ip);
      ThreadPoolExecutor tpe = on.getThreadPool();
      final int sleepTime = 5000;
      // Sit here until all of our tasks are complete.
      while (tpe.getTaskCount() != tpe.getCompletedTaskCount()) {
        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
          Logger.getLogger(InfoPanel.class.getName())
              .log(Level.SEVERE, null, ex);
        }
      }
      // End the thread pool.
      tpe.shutdown();
      final int timeToFinish = 10;
      // Make sure everything ended ok.
      try {
        this.ip.setStatus("Finalizing", InfoPanel.Levels.ok);
        tpe.awaitTermination(timeToFinish, TimeUnit.SECONDS);
      } catch (InterruptedException ex) {
        Logger.getLogger(InfoPanel.class.getName())
            .log(Level.SEVERE, null, ex);
      }
      this.ip.setStatus("Printing Tree", InfoPanel.Levels.warn);
      PrintWriter pw = new PrintWriter("tree.txt", "UTF-8");
      // Start the tree printing, this is recursive.
      on.printTree(0, pw);
      this.ip.setStatus("Printing Finished", InfoPanel.Levels.ok);
      this.ip.appendLog("File located at: ");
      Path currentRelativePath = Paths.get("");
      String s = currentRelativePath.toAbsolutePath().toString();
      this.ip.appendLog(s + "/tree.txt");
      pw.close();
    } else {
      this.ip.setStatus("Bad URL Format", InfoPanel.Levels.error);
    }
    return null;
  }
}
