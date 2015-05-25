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
/*
 * Created on 04.04.2005
 *
 */
package jacob.entrypoint.soap;

import de.tif.jacob.soap.SOAPEntryPoint;

/**
 *
 */
public class Monitor extends SOAPEntryPoint
{
  /**
   * Only to test the jACOB ApplicationServer SOAP interface.<br>
   * Usefull for any external monitor tool.
   *  
   * @param value
   * @return
   */
  public String echo(String value)
  {
    return value;
  }
}
