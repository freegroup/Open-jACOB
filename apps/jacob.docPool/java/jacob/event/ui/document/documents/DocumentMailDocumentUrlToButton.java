/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 06 15:48:24 CEST 2010
 */
package jacob.event.ui.document.documents;

import java.net.URLEncoder;

import jacob.common.AppLogger;
import jacob.common.DocumentUtil;
import jacob.event.ui.document.GenericSendDocumentMailButton;
import jacob.model.Document;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the DocumentMailDocumentUrlToButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DocumentMailDocumentUrlToButton extends GenericSendDocumentMailButton 
{
	static public final transient String RCS_ID = "$Id: DocumentMailDocumentUrlToButton.java,v 1.2 2010-07-16 14:26:14 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

}

