/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.util.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * The CurveQuadTo class demonstrates Cubic & Quad curves implemented 
 * through GeneralPath.
 */
public class CurveQuadTo extends JApplet
{

  private static Color colors[] =
    { Color.blue, Color.green, Color.red };

  public void init()
  {
    setBackground(Color.white);
  }

  public void drawDemo(int w, int h, Graphics2D g2)
  {
    g2.setColor(Color.black);

    // draws the word "QuadCurve2D"
    FontRenderContext frc = g2.getFontRenderContext();
    TextLayout tl = new TextLayout("QuadCurve2D", g2.getFont(), frc);
    float xx = (float) (w * .5 - tl.getBounds().getWidth() / 2);
    tl.draw(g2, xx, tl.getAscent());

    // draws the word "CubicCurve2D"
    tl = new TextLayout("CubicCurve2D", g2.getFont(), frc);
    xx = (float) (w * .5 - tl.getBounds().getWidth() / 2);
    tl.draw(g2, xx, h * .5f);
    g2.setStroke(new BasicStroke(5.0f));

    float yy = 20;

    // draws 3 quad curves and 3 cubic curves.
    for (int i = 0; i < 2; i++)
    {
      for (int j = 0; j < 3; j++)
      {
        Shape shape = null;

        if (i == 0)
        {
          shape = new QuadCurve2D.Float(w * .1f, yy, w * .5f, 50, w * .9f, yy);
        }
        else
        {
          shape = new CubicCurve2D.Float(w * .1f, yy, w * .4f, yy - 15, w * .6f, yy + 15, w * .9f, yy);
        }
        g2.setColor(colors[j]);
        if (j != 2)
          g2.draw(shape);

        if (j == 1)
        {
          g2.setColor(Color.lightGray);

          /*
           * creates an iterator object to iterate the boundary
           * of the curve.
           */
          PathIterator f = shape.getPathIterator(null);

          /*
           * while iteration of the curve is still in process
           * fills rectangles at the endpoints and control
           * points of the curve.
           */
          while (!f.isDone())
          {
            float[] pts = new float[6];
            switch (f.currentSegment(pts))
            {
              case PathIterator.SEG_MOVETO:
              case PathIterator.SEG_LINETO:
                g2.fill(new Rectangle2D.Float(pts[0], pts[1], 5, 5));
                break;
              case PathIterator.SEG_CUBICTO:
              case PathIterator.SEG_QUADTO:
                g2.fill(new Rectangle2D.Float(pts[0], pts[1], 5, 5));
                if (pts[2] != 0)
                {
                  g2.fill(new Rectangle2D.Float(pts[2], pts[3], 5, 5));
                }
                if (pts[4] != 0)
                {
                  g2.fill(new Rectangle2D.Float(pts[4], pts[5], 5, 5));
                }
            }
            f.next();
          }
        }
        else if (j == 2)
        {
          // draws red ellipses along the flattened curve.
          PathIterator p = shape.getPathIterator(null);
          FlatteningPathIterator f = new FlatteningPathIterator(p, 0.1);
          while (!f.isDone())
          {
            float[] pts = new float[6];
            switch (f.currentSegment(pts))
            {
              case PathIterator.SEG_MOVETO:
              case PathIterator.SEG_LINETO:
                g2.fill(new Ellipse2D.Float(pts[0], pts[1], 3, 3));
            }
            f.next();
          }
        }
        yy += h / 6;
      }
      yy = h / 2 + 15;
    }
  }

  public void paint(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    Dimension d = getSize();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setBackground(getBackground());
    g2.clearRect(0, 0, d.width, d.height);
    drawDemo(d.width, d.height, g2);
  }

  public static void main(String argv[])
  {
    final CurveQuadTo demo = new CurveQuadTo();
    demo.init();
    JFrame f = new JFrame("Java 2D Demo - Curves");
    f.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });
    f.getContentPane().add(demo);
    f.pack();
    f.setSize(new Dimension(400, 300));
    f.show();
  }
}
