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
package de.tif.jacob.util.clazzloader;

/**
 * Interface describing a library source.
 *
 * @created   27. April 2004
 */
public interface ILibrarySource
{
  static public final String RCS_ID = "$Id: ILibrarySource.java,v 1.1 2007/01/19 09:50:36 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.1 $";

    /**
     * Get library location based on library name.
     *
     * @param name  Name of library to be searched
     * @return      Absolute path name of native library
     */
    public String getLibraryLocation( String name );

    /**
     * Get library path this library source references. Each entry including the last must be
     * terminated with a semicolon.
     *
     * @return   Library path this library source references
     */
    public String getLibraryPath();
}
