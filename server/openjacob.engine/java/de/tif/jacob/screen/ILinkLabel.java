/*
 * Created on 27.09.2010
 *
 */
package de.tif.jacob.screen;

import java.awt.Color;

/**
 * Represents a link inside a longtext UI element.
 * 
 * @since 2.10
 * 
 * @see ILinkParser
 * @see RegExprLinkParser
 * @see ILinkRenderStrategy
 * @see IInFormLongText#setLinkParser(ILinkParser)
 */
public abstract class ILinkLabel
{
  public abstract void setColor(Color color);
  public abstract void setUnderline(boolean flag);
  public abstract void setItalic(boolean flag);
  public abstract void setBold(boolean flag);
  public abstract void setStrike(boolean flag);
}
