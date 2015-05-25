/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on Oct 30, 2004
 *
 */
package de.tif.jacob.designer.util;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
/**
 * @author as
 * 
 * Created 23.08.2002 10:59:22
 */
public class Grid {
	private int gx;
	private int gy;
	private int offsetX;
	private int offsetY;
	private int minX, minY;
	private int gx05;
	public Grid() 
	{
		this(10, 10, 0, 0);
	}
	
	public Grid(int gx, int gy) 
	{
		this(gx, gy, 0, 0);
	}
	
	public Grid(int gx, int gy, int offsetX, int offsetY) 
	{
		this(gx, gy, offsetX, offsetY, gx, gy);
	}
	
	public Grid(int gx, int gy, int offsetX, int offsetY, int minX, int minY) 
	{
		this.gx = (gx == 0) ? 1 : gx;
		this.gy = (gy == 0) ? 1 : gy;
		gx05 = this.gx / 2;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.minX = minX;
		this.minY = minY;
	}
	public Dimension snap(Dimension d) 
	{
		d.width = ((d.width + gx05) / gx) * gx;
		if (d.width < minX) {
			d.width = gx;
		}
		d.height = ((d.height + gx05) / gy) * gy;
		if (d.height < minY) {
			d.height = gy;
		}
		return d;
	}
	
	public Point snap(Point p) 
	{
		p.x = ((offsetX + p.x + gx05) / gx) * gx - offsetX;
		p.y = ((offsetY + p.y + gx05) / gy) * gy - offsetY;
		return p;
	}
	
	public Rectangle snap(Rectangle r) 
	{
		Point p = r.getTopLeft();
		snap(p);
		r.setLocation(p);
		//
		Dimension d = r.getSize();
		snap(d);
		r.setSize(d);
		//
		return r;
	}
	
	public int getGx() 
	{
		return gx;
	}
	
	public int getGy() 
	{
		return gy;
	}
}
