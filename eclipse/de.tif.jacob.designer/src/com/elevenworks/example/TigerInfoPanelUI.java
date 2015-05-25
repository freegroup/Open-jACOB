package com.elevenworks.example;

import javax.swing.*;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A UI delegate for a JPanel that makes it look like the info panel
 * used in Apple's iTunes 5.
 * 
 * <p/>
 * Copyright (C) 2005 by Jon Lipsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software d
 * istributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class TigerInfoPanelUI extends BasicPanelUI
{
	private Color backgroundColor1 = new Color(235,247,223);
	private Color backgroundColor2 = new Color(214,219,191);

	private Color borderColor = new Color(86,88,72);
	private Color borderColorAlpha1 = new Color(86,88,72,100);;
	private Color borderColorAlpha2 = new Color(86,88,72,50);;
	private Color borderHighlight = new Color(225,224,224);

	// ------------------------------------------------------------------------------------------------------------------
	//  Custom installation methods
	// ------------------------------------------------------------------------------------------------------------------

	protected void installDefaults(JPanel p)
	{
		p.setOpaque(false);
	}

	// ------------------------------------------------------------------------------------------------------------------
	//  Custom painting methods
	// ------------------------------------------------------------------------------------------------------------------

	public void paint(Graphics g, JComponent c)
	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Insets vInsets = c.getInsets();

		int w = c.getWidth() - (vInsets.left + vInsets.right);
		int h = c.getHeight() - (vInsets.top + vInsets.bottom);

		int x = vInsets.left;
		int y = vInsets.top;
		int arc = 8;

		Shape vButtonShape = new RoundRectangle2D.Double((double) x, (double) y, (double) w, (double) h, (double)arc, (double)arc);
		Shape vOldClip = g.getClip();

		g2d.setClip(vButtonShape);
		g2d.setColor(backgroundColor1);
		g2d.fillRect(x,y,w,h/2);
		g2d.setColor(backgroundColor2);
		g2d.fillRect(x,y+h/2,w,h/2);

		g2d.setClip(vOldClip);
		GradientPaint vPaint = new GradientPaint(x,y,borderColor,x,y+h,borderHighlight);
		g2d.setPaint(vPaint);
		g2d.drawRoundRect(x,y,w,h,arc,arc);

		g2d.clipRect(x,y,w+1,h-arc/4);
		g2d.setColor(borderColorAlpha1);
		g2d.drawRoundRect(x,y+1,w,h-1,arc,arc-1);

		g2d.setClip(vOldClip);
		g2d.setColor(borderColorAlpha2);
		g2d.drawRoundRect(x+1,y+2,w-2,h-3,arc,arc-2);
	}
}
