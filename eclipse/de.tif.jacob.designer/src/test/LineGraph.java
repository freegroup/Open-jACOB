/*
 * Created on 10.07.2006
 *
 */
package test;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
 
public class LineGraph
{
    public static void main(String[] args)
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new GraphPanel());
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
 
class GraphPanel extends JPanel
{
    final int  HPAD = 60,  VPAD = 40;
    int[] data;
    Font font;
 
    public GraphPanel()
    {
        data = new int[] {120, 190, 211, 75, 30, 290, 182, 65, 85, 120, 100, 101 };
        font = new Font("lucida sans regular", Font.PLAIN, 16);
        setBackground(Color.white);
    }
 
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(font);
        FontRenderContext frc = g2.getFontRenderContext();
        int w = getWidth();
        int h = getHeight();
        // scales
        float xInc = (w - HPAD - VPAD) / 11f;
        float yInc = (h - 2*VPAD) / 10f;
        int[] dataVals = getDataVals();
        float yScale = dataVals[2] / 10f;
 
        // ordinate
        g2.draw(new Line2D.Double(HPAD, VPAD, HPAD, h - VPAD));
        // tic marks
        float x1 = HPAD, y1 = VPAD, x2 = HPAD - 3, y2;
        for(int j = 0; j < 10; j++)
        {
            g2.draw(new Line2D.Double(x1, y1, x2, y1));
            y1 += yInc;
        }
        // labels
        String text; LineMetrics lm;
        float xs, ys, textWidth, height;
        for(int j = 0; j <= 10; j++)
        {
            text = String.valueOf(dataVals[1] - (int)(j * yScale));
            textWidth = (float)font.getStringBounds(text, frc).getWidth();
            lm = font.getLineMetrics(text, frc);
            height = lm.getAscent();
            xs = HPAD - textWidth - 7;
            ys = VPAD + (j * yInc) + height/2;
            g2.drawString(text, xs, ys);
        }
 
        // abcissa
        g2.draw(new Line2D.Double(HPAD, h - VPAD, w - VPAD, h - VPAD));
        // tic marks
        x1 = HPAD; y1 = h - VPAD; y2 = y1 + 3;
        for(int j = 0; j < 12; j++)
        {
            g2.draw(new Line2D.Double(x1, y1, x1, y2));
            x1 += xInc;
        }
        // labels
        ys = h - VPAD;
        for(int j = 0; j < 12; j++)
        {
            text = String.valueOf(j + 1);
            textWidth = (float)font.getStringBounds(text, frc).getWidth();
            lm = font.getLineMetrics(text, frc);
            height = lm.getHeight();
            xs = HPAD + j * xInc - textWidth/2;
            g2.drawString(text, xs, ys + height);
        }
 
        // plot data
        x1 = HPAD;
        yScale = (float)(h - 2*VPAD) / dataVals[2];
        for(int j = 0; j < data.length; j++)
        {
            y1 = VPAD + (h - 2*VPAD) - (data[j] - dataVals[0]) * yScale;
            if(j > 0)
                g2.draw(new Line2D.Double(x1, y1, x2, y2));
            x2 = x1;
            y2 = y1;
            x1 += xInc;
        }
    }
 
    private int[] getDataVals()
    {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for(int j = 0; j < data.length; j++)
        {
            if(data[j] < min)
                min = data[j];
            if(data[j] > max)
                max = data[j];
        }
        int span = max - min;
        return new int[] { min, max, span };
    }
}

