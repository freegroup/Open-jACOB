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

package de.tif.jacob.screen.event;

import java.awt.Color;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.impl.html.GuiHtmlElement;

/**
 * 
 * FOR INTERNAL USE AT THE MOMENT
 * 
 * @author Andreas Herz
 */
 public abstract class IDomainEventHandler extends GuiEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IDomainEventHandler.java,v 1.10 2009/09/10 08:33:41 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.10 $";

 
  public interface INavigationPanel
  {
    public INavigationEntry[] getNavigationEntries(IClientContext context, IDomain domain );
  }
  
  
  public static class INavigationEntry
  {
  	public int    emitter;
    public final String anchorId;
    public final String event;
    public final String label;
    public final String eventData;
    public final boolean visible;
    public final IForm  form;
    
    protected String cssColor =null;
    
    public INavigationEntry(IDomain emitter,String anchorId, String label, String event, String eventData)
    {
      if(emitter!=null)
      {
        this.emitter   = emitter.getId();
        this.visible   = emitter.isVisible();
      }
      else
      {
        this.emitter  = 0;
        this.visible  = true;
      }
      this.anchorId   = anchorId;
    	this.event     = event;
      this.label     = label;
      this.eventData = eventData;
      this.form=null;
    }
    
    public INavigationEntry(IForm emitter,String anchorId, String label,String event, String eventData)
    {
      if(emitter!=null)
      {
        this.emitter   = emitter.getId();
        this.visible   = emitter.isVisible();
      }
      else
      {
        this.emitter  = 0;
        this.visible  = true;
      }
      this.anchorId   = anchorId;
      this.event     = event;
      this.label     = label;
      this.eventData = eventData;
      this.form      = emitter;
    }
    
    public String toString()
    {
      return emitter+":"+event+":"+label+":"+eventData;
    }

    public void setColor(Color color)
    {
      if(color==null)
        cssColor = null;
      else
        cssColor = GuiHtmlElement.toCSSString(color);
    }
    
    public String getColor()
    {
      return cssColor;
    }
    
    public String getOnClickAction()
    {
      if(event==null)
        return null;
      return "FireEventData(\""+Integer.toString(hashCode())+"\",\""+event+"\",\""+eventData+"\")";
    }
    
    public String getImageSrc()
    {
      if (this.form == null)
        return null;
      return this.form.getName();
    }
  }
  

	public INavigationPanel getNavigationPanel(IClientContext context,IDomain domain) throws Exception
  {
    return null;
  }

  
  public void onNavigation(IClientContext context, IDomain domain, String navigationId, String navigationData) throws Exception
  {
  }
  
  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param domain The hidden group
   */
  public void onShow(IClientContext context, IDomain domain) throws Exception
  {
    // do nothing by default
  }
  
  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param domain The hidden group
   */
  public void onHide(IClientContext context, IDomain domain) throws Exception
  {
    // do nothing by default
  }}
