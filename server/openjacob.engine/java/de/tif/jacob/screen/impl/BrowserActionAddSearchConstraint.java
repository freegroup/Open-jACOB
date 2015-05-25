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
package de.tif.jacob.screen.impl;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.searchbookmark.ISearchConstraint;
import de.tif.jacob.searchbookmark.SearchConstraintManager;
/**
 * Will be fored if hte user clicks on the 'Export to Excel' icon in the browser
 * header bar.
 * 
 * @author Andras Herz
 */
public class BrowserActionAddSearchConstraint extends BrowserAction
{
  public boolean isEnabled(IClientContext context, IBrowser browser)
  {
    return ((HTTPBrowser) browser).getDataInternal().isSearchPerformed();
  }

  public boolean reloadPage()
  {
    return false;
  }

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getIcon()
   */
  public String getIcon()
  {
    return "20x20_bookmark.png";
  }


  public String getLabelId()
  {
    return "BUTTON_COMMON_SEARCHCONSTRAINT";
  }

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "BROWSER_ACTION_TOOLTIP_SEARCHCONSTRAINT";
  }

  /*
   * @see
   * de.tif.jacob.screen.impl.html.BrowserAction#execute(de.tif.jacob.screen
   * .IClientContext, de.tif.jacob.screen.impl.html.Browser, java.lang.String)
   */
  public void execute(IClientContext context, IBrowser b, String value) throws Exception
  {
    final HTTPBrowser browser = (HTTPBrowser) b;
    context.createAskDialog(I18N.getLocalized("LABEL_NAME_OF_CRITERIA",context), new IAskDialogCallback()
    {
      public void onOk(IClientContext context, String value) throws Exception
      {
        String appName = context.getApplication().getName();
        IDataBrowserInternal data = browser.getDataInternal();
        ISearchConstraint searchConstraint = SearchConstraintManager.create(value, appName, context.getDomain().getName(), context.getForm().getName(), context.getApplication().getVersion(), data);
        searchConstraint.setOwner(context.getUser());
        SearchConstraintManager.save(context, searchConstraint);
      }

      public void onCancel(IClientContext context) throws Exception
      {
      }
    }).show();
  }
}
