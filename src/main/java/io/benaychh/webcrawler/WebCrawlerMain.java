/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author benhernandez
 */
public class WebCrawlerMain {

  /**
   * @param args the command line arguments
   */
  public static void main(final String[] args) throws InterruptedException {
    OriginNode on = new OriginNode("http://localhost:8080/");
    ThreadPoolExecutor tpe = on.getThreadPool();
    while (tpe.getTaskCount() != tpe.getCompletedTaskCount()){
      Thread.sleep(5000);
    }
    tpe.shutdown();
    tpe.awaitTermination(60, TimeUnit.SECONDS);
    on.printTree();
  }
}
