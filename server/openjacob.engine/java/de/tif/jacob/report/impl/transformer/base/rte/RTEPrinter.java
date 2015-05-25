/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

package de.tif.jacob.report.impl.transformer.base.rte;

public abstract class RTEPrinter implements IRTEPrinter
{
  private static abstract class Alignment
  {
    private Alignment()
    {
      // to disable instantiation
    }

    protected abstract void append(RTEPrinter printer, String text);
  }

  private static final Alignment LEFT = new Alignment()
  {
    protected void append(RTEPrinter printer, String text)
    {
      printer.appendLeft(text);
    }
  };

  private static final Alignment CENTER = new Alignment()
  {
    protected void append(RTEPrinter printer, String text)
    {
      printer.appendCenter(text);
    }
  };

  private static final Alignment RIGHT = new Alignment()
  {
    protected void append(RTEPrinter printer, String text)
    {
      printer.appendRight(text);
    }
  };

  private Alignment alignment = null;

  public void print(String text)
  {
    if (this.alignment == null)
      markLeftAligned();
    this.alignment.append(this, text);
  }

  public void printLinefeed()
  {
    if (this.alignment == null)
      markLeftAligned();
  }

  public final void markLeftAligned()
  {
    this.alignment = LEFT;
  }

  public final void markCentered()
  {
    this.alignment = CENTER;
  }

  public final void markRightAligned()
  {
    this.alignment = RIGHT;
  }

  public final boolean isLeftAligned()
  {
    return this.alignment == LEFT;
  }

  public final boolean isCentered()
  {
    return this.alignment == CENTER;
  }

  public final boolean isRightAligned()
  {
    return this.alignment == RIGHT;
  }

  protected abstract void appendLeft(String text);

  protected abstract void appendCenter(String text);

  protected abstract void appendRight(String text);
}
