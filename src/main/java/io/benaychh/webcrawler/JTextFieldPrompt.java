/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.benaychh.webcrawler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTextField;

/**
 *
 * @author benhernandez
 */
public class JTextFieldPrompt extends JTextField {
  /**
   * The placeholder text to display.
   */
  private String placeholderText;
  /**
   * Simple constructor.
   * @param pPlaceholderText the text to display when nothing is typed.
   */
  public JTextFieldPrompt(final String pPlaceholderText) {
    this.placeholderText = pPlaceholderText;
  }
  @Override
  public final void paintComponent(final Graphics g) {
    super.paintComponent(g);
    if (this.getText().isEmpty()) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(Color.GRAY);
      g2d.setFont(getFont());
      int x = 10;
      int y = 30;
      g2d.drawString(this.placeholderText, x, y);
    }
  }
}
