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

package de.tif.jacob.letter;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.InvalidMimetypeException;
import de.tif.jacob.messaging.Message;

/**
 *
 */
public class LetterFactory
{
  public final static String MIMETYPE_TXT  = "text/plain";
  public final static String MIMETYPE_HTML = "text/html";
  public final static String MIMETYPE_RTF  = "application/rtf";
  public final static String MIMETYPE_RTF2 = "text/rtf";
  public final static String MIMETYPE_PDF  = "application/pdf";
  
  private LetterFactory(){};
  
  // Only for error Exception representation
  //
  private static final String[] mimetypes;
  static
  {
    mimetypes = new String[5];
    mimetypes[0]=MIMETYPE_RTF;
    mimetypes[1]=MIMETYPE_PDF;
    mimetypes[2]=MIMETYPE_TXT;
    mimetypes[3]=MIMETYPE_HTML;
    mimetypes[4]=MIMETYPE_RTF2;
  }
  
  public static DataDocumentValue transform(Context context, IDataTableRecord dataRecord, DataDocumentValue template) throws Exception
  {
    // convenient method
    return transform(context, dataRecord, template, template.getName());
  }
  
  public static DataDocumentValue transform(Context context, IDataTableRecord dataRecord, DataDocumentValue template, String documentName) throws Exception
  {
    if (template == null)
      return null;
    String docName = template.getName();
    String mimeType = Message.getMimeType(docName);

    return DataDocumentValue.create(documentName, createInstance(mimeType).transform(context, dataRecord, template.getContent()));
  }
  
  public static ILetter createInstance(String mimeType) throws InvalidMimetypeException
  {
    if(MIMETYPE_RTF.equals(mimeType))
      return new RTFLetter();
    if(MIMETYPE_RTF2.equals(mimeType))
      return new RTFLetter();
    if(MIMETYPE_PDF.equals(mimeType))
      return new PDFGenerator();
    if(MIMETYPE_TXT.equals(mimeType))
      return new TXTLetter();
    if(MIMETYPE_HTML.equals(mimeType))
      return new HTMLLetter();
    
    throw new InvalidMimetypeException("Mimetype ["+mimeType+"] not supported. Valid values are "+mimetypes);
  }
  
  protected static void insertCommonSubstitution(Context context, Map substitutions)
  {
    // use application locale!
    Locale locale = context.getApplicationLocale();
    
    DateFormat date = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
    DateFormat time = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);

    Date now = new Date();
    substitutions.put("db_field(*Date)", date.format(now));
    substitutions.put("db_field(*Time)", time.format(now));
  }
}
