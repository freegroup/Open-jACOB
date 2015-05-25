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
package de.tif.jacob.util.clazz;

import java.io.ByteArrayOutputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

public class ClassUtil
{
  static public final transient String RCS_ID = "$Id: ClassUtil.java,v 1.2 2010/01/18 08:05:32 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * <p>The package separator character: <code>&#x2e;</code>.</p>
   */
  public static final char PACKAGE_SEPARATOR_CHAR = '.';

  /**
   * <p>The package separator String: <code>&#x2e;</code>.</p>
   */
  public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

  /**
   * <p>The inner class separator character: <code>$</code>.</p>
   */
  public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

  /**
   * <p>The inner class separator String: <code>$</code>.</p>
   */
  public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

  public static String getClassName()
  {
    String className = null;
    try
    {
      // Retrieve call stack
      Exception exception = new Exception();
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      PrintWriter outWriter = new PrintWriter(outStream);
      exception.printStackTrace(outWriter);
      outWriter.flush();

      LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(outStream.toString()));
      lineNumberReader.readLine();// Skip exception header
      lineNumberReader.readLine();// Skip at least one call stack entry
      className = lineNumberReader.readLine().substring(4);
      className = className.substring(0, className.lastIndexOf(".", className.indexOf("(")));
    }
    catch (Exception exception)
    {
      System.err.println("ERROR: Failed to analyse call stack for class name: " + exception);
      exception.printStackTrace();
    }
    return className;
  }

  /**
   * <p>Gets the class name minus the package name from a <code>Class</code>.</p>
   * 
   * @param cls  the class to get the short name for, must not be
   *  <code>null</code>
   * @return the class name without the package name
   * @throws IllegalArgumentException if the class is <code>null</code>
   */
  public static String getShortClassName(Class cls)
  {
    if (cls == null)
    {
      throw new IllegalArgumentException("The class must not be null");
    }
    return getShortClassName(cls.getName());
  }

  /**
   * <p>Gets the class name minus the package name from a String.</p>
   *
   * <p>The string passed in is assumed to be a class name - it is not checked.</p>
   * 
   * @param className  the className to get the short name for,
   *  must not be empty or <code>null</code>
   * @return the class name of the class without the package name
   * @throws IllegalArgumentException if the className is empty
   */
  public static String getShortClassName(String className)
  {
    if (StringUtils.isEmpty(className))
    {
      throw new IllegalArgumentException("The class name must not be empty");
    }
    char[] chars = className.toCharArray();
    int lastDot = 0;
    for (int i = 0; i < chars.length; i++)
    {
      if (chars[i] == PACKAGE_SEPARATOR_CHAR)
      {
        lastDot = i + 1;
      }
      else if (chars[i] == INNER_CLASS_SEPARATOR_CHAR)
      { // handle inner classes
        chars[i] = PACKAGE_SEPARATOR_CHAR;
      }
    }
    return new String(chars, lastDot, chars.length - lastDot);
  }

  /**
   * <p>Gets the package name of a <code>Class</code>.</p>
   * 
   * @param cls  the class to get the package name for,
   *  must not be <code>null</code>
   * @return the package name
   * @throws IllegalArgumentException if the class is <code>null</code>
   */
  public static String getPackageName(Class cls)
  {
    if (cls == null)
    {
      throw new IllegalArgumentException("The class must not be null");
    }
    return getPackageName(cls.getName());
  }

  /**
   * <p>Gets the package name from a <code>String</code>.</p>
   *
   * <p>The string passed in is assumed to be a class name - it is not checked.</p>
   * 
   * @param className  the className to get the package name for,
   *  must not be empty or <code>null</code>
   * @return the package name
   * @throws IllegalArgumentException if the className is empty
   */
  public static String getPackageName(String className)
  {
    if (StringUtils.isEmpty(className))
    {
      throw new IllegalArgumentException("The class name must not be empty");
    }
    int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    if (i == -1)
    {
      return "";
    }
    return className.substring(0, i);
  }

  /**
   * Checks whenever a class has overriden/overload a method
   * 
   * @param cls
   * @param m
   * @return
   */
  public static boolean hasOverriden(Class cls, Method m)
  {
    if (cls == null)
    {
      throw new IllegalArgumentException("The class must not be null");
    }
    return cls == m.getDeclaringClass();
  }

  /**
   *
   */
  public static void main(String[] args)
  {
    System.out.println(getClassName());
  }
}