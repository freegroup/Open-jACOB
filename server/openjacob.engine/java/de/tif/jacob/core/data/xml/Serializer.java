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

package de.tif.jacob.core.data.xml;

import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.helpers.AttributesImpl;

import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationGraph;
import de.tif.jacob.core.data.internal.IDataAccessorInternal;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.BinaryFieldType;
import de.tif.jacob.util.StringUtil;


/**
 * Beispieloutput mit einem angegebenen RelationSet
 * 
 * <pre>
<faplisbuildingpart>
  <pkey>4</pkey>
  <name>L28c</name>
  <faplisstatus>valid</faplisstatus>
  <building_key>3</building_key>
  <shortname>L28c</shortname>
  <faplisbuilding>
    <pkey>3</pkey>
    <name>L28 Louis London</name>
    <faplisstatus>valid</faplisstatus>
    <sitepart_key>2</sitepart_key>
    <shortname>L28</shortname>
    <faplissitepart>
      <pkey>2</pkey>
      <name>LKW Planung</name>
      <faplisstatus>valid</faplisstatus>
      <site_key>2</site_key>
      <shortname>LKW</shortname>
      <faplissite>
        <pkey>2</pkey>
        <name>059</name>
        <faplisstatus>valid</faplisstatus>
        <shortname>059</shortname>
      </faplissite>
    </faplissitepart>
  </faplisbuilding>
</faplisbuildingpart>
 * </pre>
 *
 * It uses JAXP 1.1 so it will work under JDK 1.4 or JDK 1.2/1.3 with XALAN2 library 
 * (or any XML library JAXP 1.1 compliant). It's also memory-friendly because it doesn't 
 * need DOM.
 * Think about Servlet Engines (Tomcat, ..) and Application servers (Websphere, Weblogic ...).
 * Most of them already include an XML parser/processor and try to add a new one could generate 
 * seal errors.
 * 
 * To avoid this problems we use JAXP 1.1 + SAX to implement a portable XML generation.
 * 
 */
public class Serializer
{
  public final static String ENCODING="ISO-8859-1";
  
	/**
	 * The internal revision control system id.
	 */
	public static transient final String RCS_ID = "$Id: Serializer.java,v 1.3 2009/10/03 09:05:41 freegroup Exp $";
	
	/**
	 * The internal revision control system id in short form.
	 */
	public static transient final String RCS_REV = "$Revision: 1.3 $";

  /**
   * Append the XML representation to the hands over StringBuffer and returns the same StringBuffer.
   * 
   * @param sb
   * @param record
   * @param relationSet
   * @param fillDirection
   * @return
   */
  public static StringBuffer appendXml(StringBuffer sb, IDataRecord record, IRelationSet relationSet, Filldirection fillDirection) throws Exception
  {
     return appendXml(sb,record,relationSet,fillDirection,false);
  }
  
  public static StringBuffer appendXml(StringBuffer sb, IDataRecord record, IRelationSet relationSet, Filldirection fillDirection, boolean skipHistoryField) throws Exception
  {
    ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
    toXml(new StreamResult(stream), (DataRecord) record, relationSet, fillDirection,skipHistoryField);
    sb.append(new String(stream.toByteArray(),ENCODING ));
    return sb;
  }
  
  public static void appendXml(Writer writer, IDataRecord record, IRelationSet relationSet, Filldirection fillDirection) throws Exception
  {
    appendXml(writer, record, relationSet, fillDirection,false);
  }
  
  public static void appendXml(Writer writer, IDataRecord record, IRelationSet relationSet, Filldirection fillDirection, boolean skipHistoryField) throws Exception
  {
    toXml(new StreamResult(writer), (DataRecord) record, relationSet, fillDirection,skipHistoryField);
  }
  
  /**
   * Beispiel Implementierung für einen DataTableRecord mit dem RelationSet 'local'.
   * 
   * @param record
   * @return
   */
  private static void toXml(StreamResult streamResult, DataRecord record, IRelationSet relationSet, Filldirection filldirection, boolean skipHistoryField) throws Exception
	{
		if (record == null)
			throw new NullPointerException("hands over parameter 'record' of type 'DataTableRecord' is null.");

		// create new accessor to avoid any backlash to calling context
		IDataAccessorInternal accessor = (IDataAccessorInternal) record.getAccessor().newAccessor();

		// fetch data of connected records
		QBERelationGraph relationGraph = new QBERelationGraph(relationSet, record.getParent().getTableAlias());
		IDataTableRecord tableRecord = accessor.propagateRecord(relationGraph, filldirection, record);
		if (null == tableRecord)
		{
			// could not fetch data from datasource
			throw new Exception("Could not propagate record: "+record);
		}

		// init XML
		SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

		TransformerHandler hd = tf.newTransformerHandler();
		Transformer serializer = hd.getTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");

		// write NO XML header!!!
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		hd.setResult(streamResult);
		hd.startDocument();

		AttributesImpl atts = new AttributesImpl();

    // and write record (recursive call!)
		recordToXml(hd, atts, relationGraph, filldirection, skipHistoryField, tableRecord);

		hd.endDocument();
	}

  private static void recordToXml(TransformerHandler hd, AttributesImpl atts, QBERelationGraph relationGraph, Filldirection filldirection,boolean skipHistoryField, IDataTableRecord tableRecord) throws Exception
	{
		ITableAlias alias = tableRecord.getTable().getTableAlias();
		ITableDefinition def = alias.getTableDefinition();
		ITableField historyField = def.getHistoryField();
		
		// table record tag.
		hd.startElement("", "", alias.getName(), atts);

		// process record fields
		Iterator iter = def.getTableFields().iterator();
		while (iter.hasNext())
		{
			ITableField field = (ITableField) iter.next();

			// skip binary data
			if (field.getType() instanceof BinaryFieldType)
				continue;

			// skip history field if required
			if(skipHistoryField && historyField!=null && historyField.getName().equals(field.getName()))
			  continue;
			
			atts.clear();
			String value = StringUtil.toSaveString(tableRecord.getStringValue(field.getFieldIndex()));
			hd.startElement("", "", field.getName(), atts);
			hd.characters(value.toCharArray(), 0, value.length());
			hd.endElement("", "", field.getName());
		}

		// process connected records
		if (filldirection.isForward())
		{
			Iterator iter2 = relationGraph.getForwardRelations(alias);
			while (iter2.hasNext())
			{
				QBERelationConstraint relation = (QBERelationConstraint) iter2.next();
				ITableAlias forwardTableAlias = relation.getToTableAlias();
				IDataTable forwardTable = tableRecord.getTable().getAccessor().getTable(forwardTableAlias);
				for (int i = 0; i < forwardTable.recordCount(); i++)
				{
					recordToXml(hd, atts, relationGraph, Filldirection.NONE, skipHistoryField, forwardTable.getRecord(i));
				}
			}
		}
		if (filldirection.isBackward())
		{
			Iterator iter2 = relationGraph.getBackwardRelations(alias);
			while (iter2.hasNext())
			{
				QBERelationConstraint relation = (QBERelationConstraint) iter2.next();
				ITableAlias backwardTableAlias = relation.getFromTableAlias();

				IDataTable backwardTable = tableRecord.getTable().getAccessor().getTable(backwardTableAlias);
				IDataTableRecord backwardTableRecord = backwardTable.getSelectedRecord();
				if (null != backwardTableRecord)
				{
					recordToXml(hd, atts, relationGraph, filldirection,skipHistoryField, backwardTableRecord);
				}
			}
		}

		hd.endElement("", "", alias.getName());
	}
}
