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
package de.tif.jacob.util.search.levenshtein;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.util.search.ISearch;
import de.tif.jacob.util.search.Result;

/**
 * Simple FuzzySearch mechnism. The FuzzySearch has a Set of words and has some
 * service functions to search in them via a fuzzy algorithm
 *  
 */
public class LevenshteinSearch implements ISearch
{
  /**
   * @return List[Result]
   */
  public List find(Set searchCatalog, String searchStr, int threshold)
  {
    threshold = Integer.MAX_VALUE - threshold;

    List words = new ArrayList();
    int bestHit = Integer.MAX_VALUE;
    int thisHit = Integer.MAX_VALUE;

    try
    {
      Iterator iter = searchCatalog.iterator();

      while (iter.hasNext())
      {
        String origLine = (String) iter.next();

        thisHit =Integer.MAX_VALUE- StringUtils.getLevenshteinDistance(searchStr, origLine);
        if (thisHit > bestHit)
          bestHit = thisHit;
        // Wenn Guete >= Schwellwert: Treffer ausgeben
        //
        if (thisHit >= threshold)
          words.add(new Result(origLine.trim(), thisHit));
      }
    }
    catch (Exception exc)
    {
      System.err.println(exc);
    }
    Collections.sort(words);
    return words;
  }

  /**
   * Only to test this class. <br>
   * Usage: java FuzzySearch filename searchword
   */
  public static void main(String arg[])
  {
    try
    {
      Set catalog = new HashSet();
      LevenshteinSearch search = new LevenshteinSearch();

      catalog.add("Mueller");
      catalog.add("Muller");
      catalog.add("Mueler");
      catalog.add("Muster");

      List words = search.find(catalog, "Müller", 10);
      Iterator iter = words.iterator();
      while (iter.hasNext())
      {
        Result key = (Result) iter.next();
        System.out.println("Matching word: [" + key.keyword + "] " + key.percentage);
      }
    }
    catch (Exception exc)
    {
      System.err.println(exc);
    }
  }
}
