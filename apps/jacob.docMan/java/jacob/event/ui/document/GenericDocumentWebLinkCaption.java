/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 08 19:50:50 CEST 2010
 */
package jacob.event.ui.document;

import jacob.common.DocumentUtil;
import jacob.model.Document;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class GenericDocumentWebLinkCaption extends ICaptionEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: GenericDocumentWebLinkCaption.java,v 1.1 2010-09-17 08:42:22 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * Will be called, if the user selects a record or presses the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ICaption element) throws Exception
  {
    IDataTableRecord documentRecord = context.getDataTable(Document.NAME).getSelectedRecord();
    if(documentRecord==null)
      element.setLink((String)null);
    else
      element.setLink(DocumentUtil.getUrl(context, documentRecord));
  }

}
