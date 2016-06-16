/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

/**
 *
 * @author benhernandez
 */
public class WebCrawlerMain {

  /**
   * @param args the command line arguments
   * @throws java.lang.InterruptedException if executor gets interuppted.
   */
  public static void main(final String[] args) throws InterruptedException {
    JFrame frame = new JFrame("Ben's Web Crawler");
    InfoPanel infoPanel = new InfoPanel();
    frame.setSize(800, 600);
    frame.add(infoPanel);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
