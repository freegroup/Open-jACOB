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

package de.tif.jacob.screen.impl.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Vector;

import org.apache.commons.io.CopyUtils;

import de.tif.jacob.core.definition.guielements.DynamicImageDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDynamicImage;
import de.tif.jacob.screen.event.IDynamicImageEventHandler;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.impl.IImageStreamProvider;

/**
 * @author Administrator
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DynamicImage extends GuiHtmlElement implements IDynamicImage, IImageStreamProvider
{
  static public final transient String RCS_ID = "$Id: DynamicImage.java,v 1.2 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private final DynamicImageDefinition definition;
  private String i18nTooltip = null;
  private String tooltip;
  private boolean refreshed;
  private byte[] imageData;
  private String imageId;

  protected DynamicImage(IApplication app, DynamicImageDefinition def)
  {
    super(app, def.getName(), "", def.isVisible(), def.getRectangle(), def.getProperties());
    this.definition = def;
    this.tooltip = definition.getTooltip();
  }

  public void refresh(String imageId)
  {
    resetCache();
    this.imageId = imageId;
  }

  public final void resetCache()
  {
    super.resetCache();
    this.imageData = null;
    this.refreshed = false;
  }
  
  /**
   * return the HTML representation of this object
   */
  public final void calculateHTML(ClientContext context) throws Exception
  {
    if (!isVisible())
      return;

    if (getCache() == null)
    {
      Writer w = newCache();
      String tip = getI18NTooltip(context);
      // Nur wenn das Image clickbar und enabled ist wird dies als Link
      // dargestellt
      //
      if (getEventHandler(context) instanceof IOnClickEventHandler && this.isEnabled())
      {
        w.write("<a href=\"#\" onClick=\"FireEvent('");
        w.write(Integer.toString(getId()));
        w.write("', 'click')\"><img style=\"");
        getCSSStyle(context, w, boundingRect);
        if (tip != null)
        {
          w.write("\" title=\"");
          w.write(tip);
        }
        w.write("\" name=\"");
        w.write(getEtrHashCode());
        w.write("\" id=\"");
        w.write(getEtrHashCode());
        w.write("\" border=0 src=\"image?browser=");
        w.write(context.clientBrowser);
        w.write("&dbImage=");
        w.write(getName());
        if (this.imageId != null)
        {
          w.write("&dbImageId=");
          w.write(this.imageId);
        }
        w.write("&dummy=");
        w.write(Long.toString(System.currentTimeMillis()));
        w.write("\"></a>\n");
      }
      else
      {
        w.write("<img style=\"");
        getCSSStyle(context, w, boundingRect);
        if (!this.isEnabled())
        {
          w.write("filter:progid:DXImageTransform.Microsoft.Alpha(opacity=30);-moz-opacity:0.3;opacity: 0.3;");
          // disabled image hat kein Tooltip
        }
        else if (tip != null)
        {
          w.write("\" title=\"");
          w.write(tip);
        }
        w.write("\" name=\"");
        w.write(getEtrHashCode());
        w.write("\" id=\"");
        w.write(getEtrHashCode());
        w.write("\" border=0 src=\"image?browser=");
        w.write(context.clientBrowser);
        w.write("&dbImage=");
        w.write(getName());
        if (this.imageId != null)
        {
          w.write("&dbImageId=");
          w.write(this.imageId);
        }
        w.write("&dummy=");
        w.write(Long.toString(System.currentTimeMillis()));
        w.write("\">\n");
      }
    }
  }

  public String getImageContentType(IClientContext context, String imageId) throws Exception
  {
    Object obj = getEventHandler(context);
    if (obj instanceof IDynamicImageEventHandler)
    {
      return ((IDynamicImageEventHandler) obj).getImageContentType(context, imageId);
    }
    return null;
  }

  public boolean writeImage(IClientContext context, OutputStream out, String imageId) throws Exception
  {
    // IBIS: Später Mode ohne Caching implementieren
    byte[] data = getImageData(context, imageId);
    if (data == null)
      return false;

    CopyUtils.copy(data, out);
    return true;
  }

  public byte[] getImageData(IClientContext context, String imageId) throws Exception
  {
    if (this.refreshed == false)
    {
      Object obj = getEventHandler(context);
      if (obj instanceof IDynamicImageEventHandler)
      {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
          boolean success = ((IDynamicImageEventHandler) obj).writeImage(context, out, imageId, this.definition.getRectangle().width, this.definition.getRectangle().height);
          out.flush();
          this.imageData = success ? out.toByteArray() : null;
        }
        finally
        {
          out.close();
        }
      }
      this.refreshed = true;
    }
    return this.imageData;
  }

  /** 
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if (!isVisible())
      return;
    writeCache(w);
  }

  protected final void addDataFields(Vector fields)
  {
  }

  public final String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }

  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if (guid != this.getId())
      return false;

    // Event an den Eventhandler weiter leiten
    //
    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the button
    //
    Object obj = getEventHandler(context);
    if (obj instanceof IOnClickEventHandler)
      ((IOnClickEventHandler) obj).onClick(context, this);

    return true;
  }

  public void onGroupDataStatusChanged(IClientContext context, GroupState groupStatus) throws Exception
  {
    super.onGroupDataStatusChanged(context, groupStatus);

    // call the eventhandler if any exists
    // The event handler can override the status (enable/disable) of the button
    //
    Object obj = getEventHandler(context);
    if (obj instanceof IDynamicImageEventHandler)
      ((IDynamicImageEventHandler) obj).onGroupStatusChanged(context, groupStatus, this);
  }

  public void setTooltip(String text)
  {
    this.tooltip = text;
    this.i18nTooltip = null;
    resetCache();
  }

  private String getI18NTooltip(IClientContext context)
  {
    if (i18nTooltip == null)
    {
      if (this.tooltip == null)
        return null;

      i18nTooltip = I18N.localizeLabel(this.tooltip, this.getApplicationDefinition(), context.getLocale());
    }
    return i18nTooltip;
  }
}
