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

package de.tif.jacob.core.data.impl.index.event;

import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import de.tif.jacob.core.Context;

/**
 * Base Lucene event handler implementation.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public abstract class LuceneEventHandler extends IndexEventHandler
{
  static public transient final String RCS_ID = "$Id: LuceneEventHandler.java,v 1.1 2010/09/22 11:22:47 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Returns the analyzer which is either used to build, update the underlying
   * index and to also evaluate queries on the index.
   * <p>
   * By default the Lucene standard analyzer is returned. This might be
   * sufficient to index an English text and document pool. Nevertheless, for
   * different languages other analyzers, e.g.
   * org.apache.lucene.analysis.de.GermanAnalyzer, might be the better
   * selection.
   * 
   * @param locale
   *          the application locale currently in use (see
   *          {@link Context#getApplicationLocale()})
   * 
   * @return the analyzer to be used for the underlying index
   */
  public Analyzer getAnalyzer(Locale locale)
  {
    return new StandardAnalyzer(Version.LUCENE_29);
  }
}
