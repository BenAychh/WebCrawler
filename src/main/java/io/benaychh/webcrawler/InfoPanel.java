package io.benaychh.webcrawler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

/**
 *
 * @author benhernandez
 */
public class InfoPanel extends JPanel {
  /**
   * The information that our panel is displaying.
   */
  private final JTextArea information;
  /**
   * The information locker.
   */
  private final Object infoLocker;
  /**
   * The logging info that our panel is displaying.
   */
  private final JTextArea logging;
  /**
   * The locker for our logging panel.
   */
  private final Object loggingLocker;
  /**
   * The status label.
   */
  private final JLabel status;
  /**
   * The status locker.
   */
  private final Object statusLocker;
  /**
   * The JLabel border
   */
  private Border border;
  /**
   * The status font size
   */
  private final int fontSize = 20;
  /**
   * The status padding.
   */
  private final int padding = 10;
  /**
   * The status info types.
   */
  public static enum Levels {
    ok, warn, error
  }
  /**
   * The constructor.
   */
  public InfoPanel() {
    border = BorderFactory.createLineBorder(Color.WHITE);
    JPanel textAreaPanel = new JPanel();
    textAreaPanel.setLayout(new GridLayout(0, 2));
    information = new JTextArea();
    JScrollPane scrollInfo = new JScrollPane(information);
    infoLocker = new Object();
    logging = new JTextArea();
    JScrollPane scrollLogging = new JScrollPane(logging);
    loggingLocker = new Object();

    JPanel topAreaPanel = new JPanel();
    topAreaPanel.setLayout(new GridLayout(0, 2));
    JPanel leftTopPanel = new JPanel();
    Border paddingBorder = BorderFactory.createEmptyBorder(
        padding, padding, padding, padding);
    JTextField input = new JTextField();
    input.setPreferredSize(new Dimension(300, 50));
    input.setBorder(paddingBorder);
    JButton button = new JButton("Crawl!");
    button.addActionListener(l -> {
      this.setStatus("Starting Crawl", Levels.ok);
      CrawlingWorker cw = new CrawlingWorker(this, input.getText());
      cw.execute();
    });
    leftTopPanel.add(input);
    leftTopPanel.add(button);
    topAreaPanel.add(leftTopPanel);
    status = new JLabel();
    status.setFont(new Font("Calibri", Font.BOLD, fontSize));
    status.setBorder(paddingBorder);
    status.setBackground(Color.BLACK);
    status.setOpaque(true);
    topAreaPanel.add(status);
    statusLocker = new Object();
    setLayout(new BorderLayout());
    add(topAreaPanel, BorderLayout.PAGE_START);
    textAreaPanel.add(scrollInfo);
    textAreaPanel.add(scrollLogging);
    add(textAreaPanel, BorderLayout.CENTER);
  }
  /**
   * Sets the status code at the top of the panel.
   * @param pMessage the message to set.
   * @param pStatusCode What type of message.
   */
  public final void setStatus(final String pMessage, final Levels level) {
    synchronized (statusLocker) {
      switch (level) {
        case ok:
          status.setForeground(Color.GREEN);
          break;
        case warn:
          status.setForeground(Color.YELLOW);
          break;
        case error:
          status.setForeground(Color.RED);
          break;
      }
      status.setText(pMessage);
    }
  }

  /**
   * Removes the first line in our information text area and appends the new
   * line.
   * @param pMessage the message to append.
   */
  public final void appendInfoAndLimitLines(final String pMessage) {
    synchronized (infoLocker) {
      FontMetrics fm = information.getFontMetrics(information.getFont());
      int lineCount =
          (int) Math.floor(information.getHeight() / fm.getHeight());
      String[] currentText = information.getText().split("\n");
      if (currentText.length >= lineCount) {
        String newCurrentText = "";
        for (int i = 1; i < currentText.length; i++) {
          newCurrentText += currentText[i] + "\n";
        }
        newCurrentText += pMessage;
        information.setText(newCurrentText);
      } else {
        information.setText(String.join("\n", currentText)
          + "\n" + pMessage);
      }
    }
  }

  /**
   * Appends a message to the information pane.
   * @param pMessage the message to append.
   */
  public final void appendInfo(final String pMessage) {
    synchronized (infoLocker) {
      information.setText(information.getText() + "\n" + pMessage);
    }
  }

  /**
   * Appends a message to the information pane.
   * @param pMessage the message to append.
   */
  public final void appendLog(final String pMessage) {
    synchronized (loggingLocker) {
      logging.setText(logging.getText() + "\n" + pMessage);
    }
  }

  public final void clearLog() {
    synchronized (loggingLocker) {
      logging.setText("");
    }
  }

  /**
   * Overwrites the text in the info pane.
   * @param pMessage the message to overwrite with.
   */
  public final void setInfo(final String pMessage) {
    synchronized (infoLocker) {
      information.setText(pMessage);
    }
  }
}