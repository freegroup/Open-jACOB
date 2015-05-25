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
package de.tif.jacob.util.search;


public class Result implements Comparable
{
    public final String keyword;
    public final int percentage;

    public Result(String keyword, double percentage)
    {
        this.keyword    = keyword;
        this.percentage = (int)percentage;
    }

    public int compareTo(Object o)
    {

        if((((Result)o).percentage - percentage)==0)
        {
            if(((Result)o).keyword.length()< keyword.length())
                return 1;
            else
                return -1;
        }
        return (((Result)o).percentage - percentage);
    }

    public String toString()
    {
        return keyword + "("+percentage+"%)";
    }
}