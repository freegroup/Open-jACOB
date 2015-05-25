/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 05 15:25:11 CEST 2010
 */
package jacob.event.ui.document.verwaltung;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.event.ui.document.GenericDeleteDocumentButton;

import org.apache.commons.logging.Log;


/**
 * The event handler for the DocumentDeleteDocumentButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DocumentDeleteDocumentButton extends GenericDeleteDocumentButton 
{
	static public final transient String RCS_ID = "$Id: DocumentDeleteDocumentButton.java,v 1.2 2010-07-16 14:26:13 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
}

