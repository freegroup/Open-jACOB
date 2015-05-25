/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 02 20:52:38 CET 2007
 */
package jacob.event.ui.part;

import jacob.backup.castor.Backup;
import jacob.common.AppLogger;
import jacob.model.Part;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringBufferInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.print.attribute.standard.Compression;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.FastStringWriter;


/**
 * The event handler for the PartBackupButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class PartBackupButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: PartBackupButton.java,v 1.1 2007/02/02 22:26:48 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		Backup backup = new Backup();
		IDataAccessor backupAccessor = context.getDataAccessor().newAccessor();
		IDataTable partTable = backupAccessor.getTable(Part.NAME);
		partTable.search();
		for (int i = 0; i < partTable.recordCount(); i++) 
		{
			IDataTableRecord part = partTable.getRecord(i);
			jacob.backup.castor.Part cp = new jacob.backup.castor.Part();
			
			cp.setAssigneeKey(part.getStringValue(Part.assignee_key));
			cp.setCode(part.getStringValue(Part.code));
			cp.setComment(part.getStringValue(Part.comment));
			cp.setHistory(part.getStringValue(Part.history));
			cp.setLastRelease(part.getStringValue(Part.last_release));
			cp.setMandatorId(part.getStringValue(Part.mandator_id));
			cp.setName(part.getStringValue(Part.name));
			cp.setOwnerKey(part.getStringValue(Part.owner_key));
			cp.setPartKey(part.getStringValue(Part.part_key));
			cp.setPartKey2(part.getStringValue(Part.part_key2));
			cp.setPkey(part.getStringValue(Part.pkey));
			if(part.getDocumentValue(Part.resource_image)!=null)
				cp.setResource_image(part.getDocumentValue(Part.resource_image).getContent());
			cp.setState(part.getStringValue(Part.state));
			if(part.getDocumentValue(Part.tool_image)!=null)
				cp.setTool_image(part.getDocumentValue(Part.tool_image).getContent());
			
			backup.addPart(cp);
			
		}
    Document doc = XMLUtils.newDocument();
    Marshaller.marshal(backup, doc);
    
    FastStringWriter writer = new FastStringWriter(1024 * 100);
    org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
    outFormat.setIndenting(true);
    outFormat.setIndent(2);
    outFormat.setLineWidth(200);
    outFormat.setEncoding("ISO-8859-1");
    org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(writer, outFormat);
    
    xmlser.serialize(doc); // replace your_document with reference to
    // document you want to serialize
    String content = new String(writer.toCharArray());

    ByteArrayOutputStream data = new ByteArrayOutputStream();
    ZipOutputStream stream = new ZipOutputStream(data);
    stream.putNextEntry(new ZipEntry("part_backup.xml"));
    stream.write(content.getBytes());
    stream.closeEntry();
    stream.close();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd__HH_mm");
		String filename = "backup_"+format.format(new Date())+".zip"; 
    context.createDocumentDialog(DataDocumentValue.create(filename, data.toByteArray())).show();
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd__HH_mm");
		System.out.println(format.format(new Date()));
	}
}
