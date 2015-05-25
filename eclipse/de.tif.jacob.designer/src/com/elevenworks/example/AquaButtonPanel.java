package com.elevenworks.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * AquaButtonPanel.java
 *
 * An example of how to use Java2D to create a mac-like Aqua button.
 *
 * @author Jon Lipsky (jon.lipsky@elevenworks.com)
 */
public class AquaButtonPanel extends JPanel
{
	public static float[] BLUR = {0.10f, 0.10f, 0.10f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f};

	private Color buttonColor = Color.BLACK;
	private Color foregroundColor = new Color(0f,0f,0f,.6f);

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int vWidth = getWidth();
		int vHeight = getHeight();

		// Calculate the size of the button
		int vButtonHeight = vHeight;
		int vButtonWidth = vWidth;
		int vArcSize = 24;

		BufferedImage vBuffer = new BufferedImage(vWidth, vHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D bg = vBuffer.createGraphics();
		bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Paint the background of the button
		bg.setColor(Color.WHITE);
		bg.fillRect(0, 0, vWidth, vHeight);

		// Create the gradient paint for the first layer of the button
		Color vGradientStartColor =  buttonColor.darker().darker().darker();
		Color vGradientEndColor = buttonColor.brighter().brighter().brighter();
		Paint vPaint = new GradientPaint(0, 0, vGradientStartColor, vButtonWidth,0, vGradientEndColor, false);
		bg.setPaint(vPaint);

		// Paint the first layer of the button
		bg.fillRoundRect(0,0, vButtonWidth, vButtonHeight, vArcSize, vArcSize);

    
    // Calulate the size of the second layer of the button
    int vButtonGaugeHeight = vButtonHeight;
    int vButtonGaugeWidth = (int)(vButtonWidth/100.0*70);// -130;

    // Create the paint for the second layer of the button
    vPaint = new GradientPaint(0, 0, Color.BLACK, vButtonWidth,0, Color.GREEN, false);

    // Paint the second layer of the button
    bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.9f));
    bg.setPaint(vPaint);
    bg.setClip(new RoundRectangle2D.Float(0,0,vButtonWidth,vButtonGaugeHeight,vArcSize,vArcSize));
//  bg.fillRect(0,0,vButtonGaugeWidth,vButtonGaugeHeight);
    bg.fillRect(0,vButtonHeight-130,vButtonWidth,vButtonHeight);
    
    
		// Calulate the size of the second layer of the button
		int vHighlightInset = 0;
		int vButtonHighlightHeight = vButtonHeight - (vHighlightInset * 2);
		int vButtonHighlightWidth = vButtonWidth - (vHighlightInset * 2);
		int vHighlightArcSize = vArcSize;

		// Create the paint for the second layer of the button
		vGradientStartColor = Color.WHITE;
		vGradientEndColor = buttonColor.brighter();
		vPaint = new GradientPaint(0,vHighlightInset,vGradientStartColor,vHighlightInset+(vButtonHighlightWidth),0, buttonColor.brighter(), false);

		// Paint the second layer of the button
		bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
		bg.setPaint(vPaint);
		bg.setClip(new RoundRectangle2D.Float(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,vArcSize,vArcSize));
		bg.fillRoundRect(vHighlightInset,vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,vHighlightArcSize,vHighlightArcSize);


    // Blur the button
    ConvolveOp vBlurOp = new ConvolveOp(new Kernel(3, 3, BLUR));
    BufferedImage vBlurredBase = vBlurOp.filter(vBuffer, null);

    // Draw our aqua button
		g2.drawImage(vBlurredBase, 0, 0, null);
	}


	// --------------------------------------------------------------------------
	// Utility Methods
	// --------------------------------------------------------------------------

	/**
	 * A main method to test the panel.
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame vFrame = new JFrame(AquaButtonPanel.class.getName());
		vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vFrame.setSize(300, 100);
		vFrame.getContentPane().add(new AquaButtonPanel());
		vFrame.setTitle("Aqua Button");
		vFrame.show();
	}
}
