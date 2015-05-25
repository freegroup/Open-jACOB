/*
 * Created on 11.07.2007
 *
 */
package test;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawImage extends JPanel{
  Image cover; // The image we'll be displaying, and its size

  static final int COVERWIDTH = 127, COVERHEIGHT = 190;

  /** This constructor loads the cover image */
  public DrawImage() 
  {
    java.net.URL imageurl = this.getClass().getResource("DrawImage.png");
    cover = new javax.swing.ImageIcon(imageurl).getImage();
  }

  // These are basic GraphicsExample methods
  public String getName() {
    return "Composite Effects";
  }

  public int getWidth() {
    return 6 * COVERWIDTH + 70;
  }

  public int getHeight() {
    return COVERHEIGHT + 35;
  }

  /** Draw the example */
  public void paint(Graphics g1) 
  {
    Graphics2D g = (Graphics2D)g1;
  
    // fill the background
    g.setPaint(new Color(175, 175, 175));
    g.fillRect(0, 0, getWidth(), getHeight());

    // Draw the unmodified image
    g.translate(10, 10);
    g.drawImage(cover, 0, 0, this);

    // Set text attributes
    g.setColor(Color.black);
    g.setFont(new Font("SansSerif", Font.BOLD, 12));
    
    // draw some example text
    //
    g.drawString("SRC_OVER", 0, COVERHEIGHT + 15);

  }
  public static void main(String[] a){
      JFrame f = new JFrame();
      f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
      });
      f.setContentPane(new DrawImage());
      f.setSize(700,250);
      f.setVisible(true);
  }
}

