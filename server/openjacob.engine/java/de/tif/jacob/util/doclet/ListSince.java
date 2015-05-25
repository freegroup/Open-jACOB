/*
 * Created on 14.08.2010
 *
 */
package de.tif.jacob.util.doclet;
import java.text.MessageFormat;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
public class ListSince
{
  private static MessageFormat METHODINFO = new MessageFormat("Method: return type {0}, name = {1};");
  private static MessageFormat FIELDINFO = new MessageFormat("Field: name = {0}, comment = {1}, type = {2};");

  /**
   * A do nothing constructor
   */
  public ListSince()
  {
  }

  /**
   * Entry point for Javadoc to call a doclet.
   * 
   * @param root
   *          Rootdoc provides the entry point to the doclet API.
   * @return Always returns true.
   */
  public static boolean start(RootDoc root)
  {
    // iterate over all classes.
    ClassDoc[] classes = root.classes();
    for (int i = 0; i < classes.length; i++)
    {
      ClassDoc clazz = classes[i];
      // iterate over all methods and print their names.
      MethodDoc[] methods = clazz.methods();
      for (int j = 0; j < methods.length; j++)
      {
        MethodDoc method = methods[j] ;
        Tag[] tags= method.tags() ;
         for (int k = 0; k < tags.length; k++)
        {
          Tag tag = tags[k];
          if("@since".equals(tag.name())==true)
          {
            if(tag.text().startsWith("2.10"))
              System.out.println(tag.text()+"  "+clazz.name()+"."+method.name());
          }
        }
      }

    }
    // No error processing done, simply return true.
    return true;
  }

  private static void out(String msg)
  {
    System.out.println(msg);
  }

  private static String readOptions(String[][] options)
  {
    String tagName = null;
    for (int i = 0; i < options.length; i++)
    {
      String[] opt = options[i];
      if (opt[0].equals("-path"))
      {
        tagName = opt[1];
      }
    }
    return tagName;
  }

  public static int optionLength(String option)
  {
    if (option.equals("-path"))
    {
      return 2;
    }
    return 0;
  }

  public static boolean validOptions(String options[][], DocErrorReporter reporter)
  {
    boolean foundTagOption = false;
    for (int i = 0; i < options.length; i++)
    {
      String[] opt = options[i];
      if (opt[0].equals("-path"))
      {
        if (foundTagOption)
        {
          reporter.printError("Only one -tag option allowed.");
          return false;
        }
        else
        {
          foundTagOption = true;
        }
      }
    }
    if (!foundTagOption)
    {
      reporter.printError("Usage: javadoc -tag mytag -doclet ListTags ...");
    }
    return foundTagOption;
  }
} // SimpleDoclet
