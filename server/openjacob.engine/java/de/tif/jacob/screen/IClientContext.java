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

package de.tif.jacob.screen;

import de.tif.jacob.core.Session;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.i18n.Message;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.dialogs.IExcelDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IMessageDialogCallback;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.IRecordTreeDialog;
import de.tif.jacob.screen.dialogs.IRecordTreeDialogCallback;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.dialogs.IWorkflowEditorCallback;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.impl.HTTPApplication;
import de.tif.jacob.screen.impl.html.SearchBrowser;
import de.tif.jacob.security.IUser;

/**
 * Client context in which an user action has been performed. A proper
 * initialized instance of this class will be provided to all relevant GUI
 * hooks, if a client performs a server request. Application programmers
 * could/should use this abstract class to create user dialog windows in a
 * client independent way.
 * 
 * @author Andreas Herz
 */
public abstract class IClientContext extends SessionContext
{
	/**
	 * The internal revision control system id.
	 */
  static public final transient String RCS_ID = "$Id: IClientContext.java,v 1.24 2010/11/18 11:25:36 freegroup Exp $";
  
	/**
	 * The internal revision control system id in short form.
	 */
  static public final transient String RCS_REV = "$Revision: 1.24 $";

  private final IApplication application;
  
  protected IDomain  domain   = null;
  protected IForm    form     = null;
  protected IGroup   group    = null;
  
  /**
   * Internal method to switch on/off visualization of SQL requests.
   * 
   * @param enable
   *          <code>true</code> to switch on, <code>false</code> to switch
   *          off
   */
  public abstract void setShowSQL(boolean enable);
  
  /**
   * Internal method to check whether visualization of SQL request is switched
   * on or off.
   * 
   * @return <code>true</code> if switch on, <code>false</code> if switch
   *         off
   */
  public abstract boolean getShowSQL();
  
  public abstract IRecordTreeDialog createRecordTreeDialog(IGuiElement anchor, IDataTableRecord record, IRelationSet relationSet, Filldirection filldirection, IRecordTreeDialogCallback callback) throws Exception;

  public abstract IFormDialog createFormDialog(String title, FormLayout layout, IFormDialogCallback callback);

  /**
   * Displays a confirmation message box with Yes and No buttons (comparable to
   * JavaScript's Window.confirm). The callback function which is passed will be
   * called after the user clicks either button (could also be the top-right
   * close button too).
   * 
   * @param question
   *          the question to ask for
   * @param callback
   *          the dialog callback
   * @return the created ok&cancel dialog
   */
  public abstract IOkCancelDialog createOkCancelDialog(String question, IOkCancelDialogCallback callback);

  /**
   * Displays a confirmation message box with Yes and No buttons (comparable to
   * JavaScript's Window.confirm). The callback function which is passed will be
   * called after the user clicks either button (could also be the top-right
   * close button too).
   * 
   * @param question
   *          the question to ask for
   * @param callback
   *          the dialog callback
   * @return the created ok&cancel dialog
   * @deprecated use {@link #createOkCancelDialog(String, IOkCancelDialogCallback)}  and the generated jacob.resource.I18N helper class.
   */
  public abstract IOkCancelDialog createOkCancelDialog(Message question, IOkCancelDialogCallback callback);

  /**
   * Displays a message box with OK and Cancel buttons prompting the user to enter some 
   * text (comparable to JavaScript's Window.prompt). If a callback function is passed 
   * it will be called after the user clicks a button (could also be the top-right close button) 
   * and the text that was entered was passed as parameter to the callback.
   * 
   * @param question
   *          the question to ask for
   * @param callback
   *          the dialog callback
   * @return the created ask dialog
   */
  public final IAskDialog createAskDialog(String question, IAskDialogCallback callback)
  {
    return createAskDialog(question,false,callback);
  }

  /**
   * Displays a message box with OK and Cancel buttons prompting the user to enter some 
   * text (comparable to JavaScript's Window.prompt). If a callback function is passed 
   * it will be called after the user clicks a button (could also be the top-right close button) 
   * and the text that was entered was passed as parameter to the callback.
   *
   * @param question
   *          the question to ask for
   * @param startValue
   *          the start value, i.e. the value which is already filled in
   * @param callback
   *          the dialog callback
   * @return the created ask dialog
   */
  public final IAskDialog createAskDialog(String question, String startValue, IAskDialogCallback callback)
  {
    return createAskDialog(question,false,startValue,callback);
  }
  
  /**
   * Displays a message box with OK and Cancel buttons prompting the user to
   * enter some text (comparable to JavaScript's Window.prompt).The prompt can
   * be a single-line or multi-line textbox. If a callback function is passed*
   * it will be called after the user clicks a button (could also be the
   * top-right close button) and the text that was entered was passed as
   * parameter to the callback.
   * 
   * @param question
   *          the question to ask for
   * @param multiline
   *          <code>true</code> for multiline input, <code>false</code> for
   *          single input
   * @param callback
   *          the dialog callback
   * @return the created ask dialog
   */
  public abstract IAskDialog createAskDialog(String question, boolean multiline, IAskDialogCallback callback);

  /**
   * Displays a message box with OK and Cancel buttons prompting the user to enter some 
   * text (comparable to JavaScript's Window.prompt).The prompt can be a single-line or multi-line textbox.  
   * If a callback function is passed* it will be called after the user clicks a button (could also be the top-right close button) 
   * and the text that was entered was passed as parameter to the callback.
   *
   * @param question
   *          the question to ask for
   * @param multiline
   *          <code>true</code> for multiline input, <code>false</code> for single input 
   * @param startValue
   *          the start value, i.e. the value which is already filled in
   * @param callback
   *          the dialog callback
   * @return the created ask dialog
   */
  public abstract IAskDialog createAskDialog(String question, boolean multiline, String startValue, IAskDialogCallback callback);
  
  /**
   * Creates a file URL dialog.
   * <p>
   * 
   * Either jACOB (application) internal or external web pages could be
   * displayed. <br>
   * External URLs must start with "http://", otherwise the URL is treated as
   * internal and will be completed by means of "http://server:port/jacob/".
   * <p>
   * Examples: <table BORDER="1">
   * <tr>
   * <td>url</td>
   * <td>resulting URL</td>
   * </tr>
   * <tr>
   * <td>http://www.google.com</td>
   * <td>http://www.google.com</td>
   * </tr>
   * <tr>
   * <td>groupinfo.jsp</td>
   * <td>http://www.myserver.de:8080/jacob/groupinfo.jsp</td>
   * </tr>
   * <tr>
   * <td>application/marketplace/1.0/index.html</td>
   * <td>
   * http://www.myserver.de:8080/jacob/application/marketplace/1.0/index.html
   * </td>
   * </tr>
   * </table>
   * 
   * @param url
   *          the URL to the web page to display
   * @return the created URL dialog
   */
  public abstract IUrlDialog createUrlDialog(String url);

  /**
   * Creates a URL Dialog. An jACOB application internal web page can be display.
   * 
  * <p>
   * Examples: <table BORDER="1">
   * <tr>
   * <th>url</th>
   * <th>resulting URL</th>
   * </tr>
   * 
   * <tr>
   * <td>index.html</td>
   * <td>
   * http://www.myserver.de:8080/jacob/application/marketplace/1.0/index.html
   * </td>
   * </tr>
   * <tr>
   * <td>subdir/index.html</td>
   * <td>
   * http://www.myserver.de:8080/jacob/application/marketplace/1.0/subdir/index.html
   * </td>
   * </tr>
   * </table>
   * 
   * @since 2.10
   * @param app
   * @param url
   * @return
   */
  public abstract IUrlDialog createUrlDialog(IApplication app, String url);

  /**
   * Displays a standard read-only message box with an OK button 
   * (comparable to the basic JavaScript Window.alert).
   * 
   * @param message the message to show
   * @return the created message dialog
   */
  public abstract IMessageDialog createMessageDialog(String message);
  
  /**
   * Displays a standard read-only message box with an OK button 
   * (comparable to the basic JavaScript Window.alert) and an extended
   * collapseable area with an extended message.
   * 
   * @param message the message to show
   * @param extendedMessage the extended message to show in the collapseable area
   * @return the created message dialog
   * @since 2.7.2
   */
  public abstract IMessageDialog createMessageDialog(String message, String extendedMessage);


  /**
   * Displays a standard read-only message box with an OK button 
   * (comparable to the basic JavaScript Window.alert).
   * 
   * @param message the message to show
   * @return the created message dialog
   * @since 2.10
   */
  public abstract IMessageDialog createMessageDialog(String message, IMessageDialogCallback callback);
  
  /**
   * Displays a standard read-only message box with an OK button 
   * (comparable to the basic JavaScript Window.alert) and an extended
   * collapseable area with an extended message.
   * 
   * @param message the message to show
   * @param extendedMessage the extended message to show in the collapseable area
   * @return the created message dialog
   * @since 2.10
   */
  public abstract IMessageDialog createMessageDialog(String message, String extendedMessage, IMessageDialogCallback callback);

  /**
   * Transparent messages are the brainchild of Jef Raskin. It's simply a large and 
   * translucent message that's displayed over the contents of your screen. They fade 
   * away when the user takes any action (like typing or moving the mouse). In practice, 
   * the message is both noticeable yet unobtrusive. And because the message is transparent, 
   * you can see what's beneath it.<br>
   * <br>
   * It makes no sense to hands over a message with more than 100 characters.
   * 
   * @param message the message to show.
   * @since 2.7.2
   */
  public abstract void showTransparentMessage(String message);
  
  /**
   * Transparent messages are the brainchild of Jef Raskin. It's simply a large and 
   * translucent message that's displayed over the contents of your screen. They fade 
   * away when the user takes any action (like typing or moving the mouse). In practice, 
   * the message is both noticeable yet unobtrusive. And because the message is transparent, 
   * you can see what's beneath it.<br>
   * <br>
   * It makes no sense to hands over a message with more than 100 characters.
   * 
   * @param message the message to show.
   * @param automaticFadeout flag for automatic fade out of the message.
   * @since 2.8.5
   */
  public abstract void showTransparentMessage(String message, boolean automaticFadeout);

  /**
   * Displays a standard read-only message box with an OK button 
   * (comparable to the basic JavaScript Window.alert).
   * 
   * @param message I18N Message
   * @return the created message dialog
   * @deprecated use {@link #createMessageDialog(String)} and the generated jacob.resource.I18N helper class.
   */
  public abstract IMessageDialog createMessageDialog(Message message);

  public abstract IExcelDialog createExcelDialog();

  /**
   * Transfor the given data browser to an excel document and serve them
   * to the user for download.
   * This method doesn't call any filter or hooks to generate data.
   * 
   * @param browser the browser to export to excel.
   * @since 2.7.4
   */
  public abstract void showExcelDialog(IDataBrowser browser);

  
  public abstract void showTipDialog(IGuiElement anchor, String message);
  public abstract void showTipDialog(String anchorId, String message);

  /**
   * 
   * @param xml
   * @param callback
   * @return
   */
  public abstract void showWorkflowEditor(String  xmlWorkflowDefintion, IWorkflowEditorCallback callback);
  
  /**
   * Creates a file upload dialog.
   * 
   * @param callback the dialog callback
   * @return the created file upload dialog
   */
  public abstract IUploadDialog createUploadDialog(IUploadDialogCallback callback);
  
  /**
   * Creates a file upload dialog with a given discription.
   * 
   * @param title the title of the dialog.
   * @param description the discription to show.
   * @param callback the dialog callback
   * 
   * @return the created file upload dialog
   * @since 2.8.0
   */
  public abstract IUploadDialog createUploadDialog(String title, String description, IUploadDialogCallback callback);

  /**
   * Creates a document dialog to download or display the given document value.
   * 
   * @param document
   *          the document value to create dialog for
   * @return the created document dialog. Call
   *         {@link de.tif.jacob.screen.dialogs.IDialog#show()} to display the
   *         dialog.
   * @see de.tif.jacob.core.data.IDataRecord#getDocumentValue(String)
   */
  public abstract IDocumentDialog createDocumentDialog(DataDocumentValue document);

  /**
   * Creates a document dialog to download or display the given document value.
   * 
   * @param mimeType
   *          the mime type to use or <code>null</code> for default mime type
   * @param document
   *          the document value to create dialog for
   * @return the created document dialog. Call
   *         {@link de.tif.jacob.screen.dialogs.IDialog#show()} to display the
   *         dialog.
   * @see de.tif.jacob.core.data.IDataRecord#getDocumentValue(String)
   */
  public abstract IDocumentDialog createDocumentDialog(String mimeType, DataDocumentValue document);

  /**
   * Creates a document dialog to download or display the given file content.
   * 
   * @param mimeType
   *          the mime type to use or <code>null</code> for default mime type
   * @param fileName
   *          the file name
   * @param content
   *          the file content
   * @return the created document dialog. Call
   *         {@link de.tif.jacob.screen.dialogs.IDialog#show()} to display the
   *         dialog.
   */
  public abstract IDocumentDialog createDocumentDialog(String mimeType, String fileName, byte[] content);

  /**
   * Creates a grid table dialog.
   * 
   * @param anchor the anchor GUI element
   * @return the created grid table dialog
   */
  public abstract IGridTableDialog createGridTableDialog(IGuiElement anchor);

  /**
   * Creates a grid table dialog.
   * 
   * @param anchor the anchor GUI element
   * @param callback the dialog callback
   * @return the created grid table dialog
   */
  public abstract IGridTableDialog createGridTableDialog(IGuiElement anchor, IGridTableDialogCallback callback);

  /**
   * Creates a grid table dialog.
   * 
   * @param anchor the anchor GUI element
   * @return the created grid table dialog
   * @since 2.8.0
   */
  public abstract IGridTableDialog createGridTableDialog(IGuiElement anchor, IDataBrowser data);

  /**
   * Protected constructor.
   * 
   * @param application
   *          the application object
   * @param user
   *          the current user
   */
  protected IClientContext(IApplication application, IUser user)
  {
    super(application.getApplicationDefinition(), user);
    
    this.application = application;
  }
  
  /**
   * Returns the current data browser for the group in which the user action has
   * been performed.
   * 
   * @return the current data browser
   */
  public final IDataBrowser getDataBrowser() throws Exception
  {
    if(getApplication().isInReportMode(this))
      return getApplication().getReportDataBrowser(this);

    return getDataAccessor().getBrowser(getGroup().getBrowser().getDefinition());
  }
  
  /**
   * Set the current data browser for the group in which the action
   * has performed.
   * 
   */
  public final void setDataBrowser(IDataBrowser browser) throws Exception
  {
    if(getApplication().isInReportMode(this))
      getApplication().setReportDataBrowser(this,browser);
    else
      getGroup().getBrowser().setData(this, browser);;
  }
  
  /**
   * Returns the current data table for the group in which the user action
   * has been performed.
   *  
   * @return the current data table
   */
  public final IDataTable getDataTable() throws Exception
  {
    return getDataTable(getGroup().getGroupTableAlias());
  }
 
  /**
   * Returns the search browser of the current group. The current group is the 
   * the group which has fired the last event.
   * 
   * @return The current/visible search browser
   */
  public final IBrowser getGUIBrowser() throws Exception
  {
   return getGroup().getBrowser(); 
  }
  
  /**
   * Completely resets the application object associated with the application
   * window, i.e. this includes the reset off all application's domains.
   * 
   * @see #clearDomain()
   */
  public final void clearApplication() throws Exception
  {
    if(getApplication()!=null)
      getApplication().clear(this);
  }
  
  /**
   * Resets the current domain.
   */
  public final void clearDomain() throws Exception
  {
    if(getDomain()!=null)
      getDomain().clear(this);
  }
 
  /**
   * Resets the current form and the corresponding DataAccessor.
   * 
   */
  public final void clearForm() throws Exception
  {
    if(getForm()!=null)
      getForm().clear(this);
  }
  
  /**
   * Resets a specified form.
   * 
   * @param domainName The parent domain of the form to reset
   * @param formName The name of the form to reset
   */
  public void clearForm(String domainName, String formName) throws Exception
  {
    IDomain domain=(IDomain)getApplication().findByName(domainName);
    IForm   form=(IForm)domain.findByName(formName);
    form.clear(this);
  }

  /**
   * Resets the current group.
   * 
   */
  public abstract void clearGroup() throws Exception;
  
  /**
   * Refetchs the data element from the database of the current group.
   * 
   */
  public void refreshGroup() throws Exception
  {
    if(getGroup()!=null)
    {
    	IDataBrowser dataBrowser = getDataBrowser(getGroup().getBrowser().getName());
    	if (-1 != dataBrowser.getSelectedRecordIndex())
    		dataBrowser.propagateSelections();
    }
  }
 
  /**
   * Returns the selected data table record of the current top level group.<br>
   * <br>
   * The top level group is neither a {@link ITabPane} nor a {@link IStackPane}
   * 
   * @return the selected data table record or <code>null</code> if no
   *         selected record exists
   */
  public IDataTableRecord getSelectedRecord() throws Exception
  {
    IDataTable table = getDataAccessor().getTable(getGroup().getGroupTableAlias());
    return table.getSelectedRecord();
  }

  /**
   * Sets the current form, i.e. switches the form to be displayed. The
   * specified form must be contained in the current domain.
   * 
   * @param formName
   *          the name of the form to display
   * @throws Exception
   *           on any problem
   */
  public void setCurrentForm(String formName) throws Exception
  {
     IForm save = getDomain().getCurrentForm(this);
     IForm f =(IForm)getDomain().findByName(formName);

     if(save==f)
       return;

     getDomain().setCurrentForm(this, f);
     
     // Ab jetzt gilt der DatenAccessor der neuen Form
     //
     this.form = f;
 
     // der Aufruf von context.getGUIBrowser() liefert jetzt nicht die "letzte" aktive Gruppe der Form oder
     // die default Gruppe der Form, sondern die Gruppe in der als letztes ein Button gedrückt wurde.
     // Somit stimmt im Moment nicht die Gruppe/Form Zugehörigkeit. D.H. die Gruppe ist nicht unbedingt
     // ein Kind der jetzt aktiven Form. Wird jetzt überprüft und eventuell korrigiert.
     if(this.group ==null || this.group.getForm()!=this.form)
     {
       this.group = ((SearchBrowser) this.form.getCurrentBrowser()).getRelatedGroup();
     }
   }
  
  /**
   * Sets the current form, i.e. switches the form to be displayed. The
   * specified form must be contained in the given domain.
   * 
   * @param domainName
   *          the name of the domain which contains the form to display
   * @param formName
   *          the name of the form to display
   * @throws Exception
   *           on any problem, e.g. no such domain or form exists
   */
  public void setCurrentForm(String domainName, String formName) throws Exception
  {
    IDomain domain = getDomain(domainName);
    IGuiElement guiform = domain.findByName(formName);
    if (!(guiform instanceof IForm))
      throw new Exception("Domain '" + domainName + "' does not contain a form with name '" + formName + "'");
    IForm form = (IForm) guiform;

    if(this.form==form)
      return;
    
    getApplication().setActiveDomain(this,domain);
    getDomain().setCurrentForm(this,form);
    
    // Danach ist "this.domain" korrekt gesetzt , jedoch war die aktive Form nicht gesetzt.
    // Dies wird jetzt nachgeholt. Unschï¿½nes Bugfixing.
    // Dieser Fehler sieht man nur wenn man den Accessor-Focus auf "form" setzt. Man bekommt
    // nach context.setCurrentForm("anyDomain","anyForm") mit dem Aufruf "context.getDataAccessor()"
    // nicht den Accessor der neuen Form, sonder noch von der alten.
    //
    this.form = form;
    
    // der Aufruf von context.getGUIBrowser() liefert jetzt nicht die "letzte" aktive Gruppe der Form oder
    // die default Gruppe der Form, sondern die Gruppe in der als letztes ein Button gedrückt wurde.
    // Somit stimmt im Moment nicht die Gruppe/Form Zugehörigkeit. D.H. die Gruppe ist nicht unbedingt
    // ein Kind der jetzt aktiven Form. Wird jetzt überprüft und eventuell korrigiert.
    if(this.group ==null || this.group.getForm()!=this.form)
    {
      // nicht jede Form hat einen Browser. Ein Beispiel ist hier die URL Form oder ExternalForm
      if(this.form.getCurrentBrowser()!=null)
        this.group = ((SearchBrowser) this.form.getCurrentBrowser()).getRelatedGroup();
    }
  }
  
  /**
   * Returns the current working domain. This is the domain in which the last
   * user action has been performed.
   * @return the current domain
   */
  public final IDomain getDomain()
  {
    return domain;
  }
  
  /**
   * Returns the domain object specified by name.
   * 
   * @param domainName
   *          the name of the domain object to return
   * @return the desired domain
   * @throws Exception on any problem, e.g. no such domain exists.
   */
  public final IDomain getDomain(String domainName) throws Exception
  {
    IGuiElement domain = getApplication().findByName(domainName);
    if (!(domain instanceof IDomain))
      throw new Exception("No domain with name '" + domainName + "' existing");
    return (IDomain) domain;
  }

  /**
   * Returns the current form.
   * 
   * @return the current form
   */
  public final IForm getForm()
  {
    return form;
  }
  
  /**
   * Returns the current group.
   * @return the current group
   */
  public final IGroup getGroup()
  {
    return group;
  }
  
  /**
   * Returns the application object.
   * 
   * @return the application object
   */
  public final IApplication getApplication()
  {
    return application;
  }
  
  /**
   * @see de.tif.jacob.core.SessionContext#getSession()
   */
  public final Session getSession()
  {
    return application.getSession();
  }


  public void canAbort(boolean flag)
  {
    ((HTTPApplication)getApplication()).canAbort(flag);
  }

  public boolean shouldAbort()
  {
    return ((HTTPApplication)getApplication()).shouldAbort();
  }
  
  /**
   * Return an implementation of ILinkRenderStrategy which transforms a link target (e.g. email address) into a proper link according to the
   * current context.
   * 
   * @return a valid link renderer related to the client cotnext
   * @since 2.10
   */
  public abstract ILinkRenderStrategy createDefaultLinkRenderStrategy();

}
