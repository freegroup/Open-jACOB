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

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDocument;

/**
 *
 */
public interface HTTPDocument extends IDocument
{

  void setValue(IClientContext context, IDataTableRecord object) throws Exception;
  void resetCache();

  public DataField getDataField();
  void onUpload(IClientContext context, String docName, byte[] doc) throws Exception;
  void onDelete(IClientContext context) throws Exception;
}
