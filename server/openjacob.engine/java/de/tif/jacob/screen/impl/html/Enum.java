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

package de.tif.jacob.screen.impl.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.core.definition.guielements.EnumInputFieldDefinition;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.impl.DataField;
import de.tif.jacob.screen.impl.HTTPSingleDataGuiElement;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Enum extends SingleDataGUIElement
{
  static public final transient String RCS_ID = "$Id: Enum.java,v 1.20 2010/10/13 14:21:22 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.20 $";

  protected final static String DELIMITER= "|";
  
  protected final EnumInputFieldDefinition definition;

  // List[Option]
  protected List entries  = new ArrayList();
  
  // Definiert ob im SEARCH-Modus mehr als ein Wert ausgewählt werden kann.
  //
  private boolean multichoice = true;
  
  private ICaption caption;
  
  static class Option
  {
    private final String label;

    private  String i18nLabel;
    
    public  final String value;
    public  final String tooltip;
    public  final Icon   icon;
    public  boolean      enabled = true;
    public  boolean      hasCallback =true;
    
    /**
     * Internal flag to indicate that this option is currently not treated by
     * {@link Enum#enableOption(String, boolean)} or
     * {@link Enum#enableOptions(boolean)}.
     */
    public boolean deleted = false;
    
    public Option(String label, String value, Icon icon, String tooltip, boolean serverCallOnSelect)
    {
      if(value==null)
        throw new NullPointerException("value is a required parameter for the Option");
      this.label = label;
      this.value = value;
      this.tooltip = tooltip;
      this.icon = icon;
      this.hasCallback = serverCallOnSelect;
    }
    
    
    public Option(String label, String value,Icon icon, String tooltip, boolean enabled,boolean serverCallOnSelect)
    {
      this(label, value,icon,tooltip, serverCallOnSelect);
      this.enabled = enabled;
    }
    
    public String getI18NLabel(IClientContext context)
    {
      if (i18nLabel == null)
      {
        if (this.label == null)
          return null;
        
        i18nLabel = I18N.localizeLabel(this.label, context.getApplicationDefinition(), context.getLocale());
      }
      return i18nLabel;
    }

    public int hashCode()
    {
      return value.hashCode();
    }
    
    /**
     * Der I18N Key ist NICHT Bestandteil des Vergleichs. Es wird immer nur der
     * ENUM Wert verglichen!
     */
    public boolean equals(Object obj)
    {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() == obj.getClass())
      {
        Option other = (Option) obj;
        return value.equals(other.value);
      }
      return false;
    }
  }
  
  private static final class EnumDataField extends DataField
  {
    private Object localizedValue;

    private EnumDataField(HTTPSingleDataGuiElement parent, ITableAlias tableAlias, ITableField column, String value)
    {
      super(parent, tableAlias, column, value);
      this.localizedValue = value;
    }

    public void setValue(Object value)
    {
      super.setValue(value);
      this.localizedValue = value;
    }

  }
  
  protected Enum(IApplication app, EnumInputFieldDefinition _enum, boolean addEmptyOption)
  {
    super(app,  _enum.getName(), null,_enum.isVisible(),_enum.isReadOnly(), _enum.getRectangle(),_enum.getLocalTableAlias(), _enum.getLocalTableField(), _enum.getFont() ,_enum.getProperties());
    
    if (null != _enum.getCaption())
      addChild(caption=new Caption(app, _enum.getCaption()));
    definition = _enum;

    dataField = new EnumDataField(this, _enum.getLocalTableAlias(), _enum.getLocalTableField(), "");
      
    // the first one is always the empty entry
    //
    if(definition.getLocalTableField()!=null && definition.getLocalTableField().getType() instanceof EnumerationFieldType)
    {
      EnumerationFieldType enumType = (EnumerationFieldType)definition.getLocalTableField().getType();
      if (addEmptyOption)
        entries.add(new Option("", "", null, null, definition.getCallHookOnSelect()));
      for(int i=0; i<definition.getEnumCount();i++)
      {
        String value   = definition.getEnumEntry(i);
        String i18nKey = enumType.getEnumeratedLabel(value);
        entries.add(new Option(i18nKey, value,null,null, definition.getCallHookOnSelect()));
      }
    }
    else
    {
      if (addEmptyOption)
        entries.add(new Option("", "", null, null, definition.getCallHookOnSelect()));
      for(int i=0; i<definition.getEnumCount();i++)
      {
        String value   = definition.getEnumEntry(i);
        entries.add(new Option(value, value,null,null, definition.getCallHookOnSelect()));
      }
    }
  }

  public void setValue(IClientContext context, IDataTableRecord record) throws Exception
  {
    // This element is not bounded to a database field
    //
    if (getDataField().getField() != null && record != null)
    {
      int fieldIndex = getDataField().getField().getFieldIndex();

      // set the enumeration value
      setValue(record.getStringValue(fieldIndex));

      // set the enumeration label
      ((EnumDataField) getDataField()).localizedValue = record.getStringValue(fieldIndex, context.getLocale());
    }
    else
    {
      super.setValue(context, record);
    }
  }

  public ICaption getCaption()
  {
    return caption;
  }

  public final int getTabIndex()
  {
    return definition.getTabIndex();
  }
  
  
  
  public final String[] getOptions()
  {
    List result = new ArrayList();
    for(int i =0;i<entries.size();i++)
    {
      Option option = (Option)entries.get(i);
      if(option.enabled==true)
        result.add(option.value);
    }
    return (String[])result.toArray(new String[result.size()]);
  }


  /* 
   * @see de.tif.jacob.screen.IComboBox#enableEntry(java.lang.String, boolean)
   */
  public final void enableOption(String entry, boolean enableFlag)
  {
    if(entry==null)
      return;
    
    Iterator iter = entries.iterator();
    while(iter.hasNext())
    {
      Option option = (Option)iter.next();
      if(option.value.equals(entry))
      {
        if (!option.deleted)
          option.enabled = enableFlag;
        return;
      }
    }
  }

  /* 
   * @see de.tif.jacob.screen.IComboBox#enableEntry(java.lang.String, boolean)
   */
  public final void enableOptions(boolean enableFlag)
  {
    Iterator iter = entries.iterator();
    while(iter.hasNext())
    {
      Option option = (Option)iter.next();
      if (!option.deleted)
        option.enabled = enableFlag;
    }
  }

    
  public void setCallbackOptions(String[] options)
  {
    Iterator iter = entries.iterator();
    while(iter.hasNext())
    {
      Option option = (Option)iter.next();
      option.hasCallback=false;
    }
    
    for(int i=0;i<options.length;i++)
    {
      Option option = getOption(options[i]);
      option.hasCallback=true;
    }
  }
  
  
  /**
   * Add the handsover entry to the listbox element
   * 
   * @param entry      The option to add
   */
  public void addOption(String entry)
  {
    addOption(entry, entry, null,null);
  }

  public void addOption(String label, String value)
  {
    addOption(label, value, null,null);
  }

  public void addOption(String value, Icon icon, String tooltip)
  {
    addOption(value,value,icon,tooltip);
  }

  public void addOption(String label, String value, Icon icon, String tooltip)
  {
    if(value==null)
      return;
    entries.add(new Option(label,value,icon,tooltip, definition.getCallHookOnSelect()));
    resetCache();
  }


  public void clearSelection()
  {
    setValue("");
  }


  public boolean hasOption(String value)
  {
    Option optionToTest = new Option(value, value,null,null, definition.getCallHookOnSelect());
    return entries.contains(optionToTest);
  }


  public boolean removeOption(String entry)
  {
    // remove them from the selection
    //
    String[] selection = (String[])ArrayUtils.removeElement(getSelection(),entry);
    setValue("");
    selectOptions(selection);
    
    // remove them from the values
    return entries.remove(new Option(entry, entry,null,null, definition.getCallHookOnSelect()));
  }


  public boolean removeOptions()
  {
    boolean result = entries.size()>0;
    setValue("");
    entries.clear();
    
    return result;
  }

  /**
   * Set the entries of the ListBox element
   * @param entry      The option to add
   */
  public void setOptions(String[] newEntries)
  {
    entries.clear();
    
    if(newEntries==null)
      return;
    
    // vermeiden von doppelten Eintraegen
    //
    for(int i =0;i<newEntries.length;i++)
    {
      Option option = new Option(newEntries[i],newEntries[i],null,null, definition.getCallHookOnSelect());
      if(!entries.contains(option))
        entries.add(option);
    }
    resetCache();
  }
  
  /**
   * Set the entries of the ListBox element
   * @param lables  The labels to show
   * @param values  The values to use
   */
  public void setOptions(String[] labels, String[] values)
  {
    entries.clear();
    
    if(values==null)
      return;
    
    if(labels.length != values.length)
      throw new RuntimeException("Parameter missmatch. Size of labels and values must be the same");
    
    // vermeiden von doppelten Eintraegen
    //
    for(int i =0;i<values.length;i++)
    {
      Option option = new Option(labels[i],values[i],null,null, definition.getCallHookOnSelect());
      if(!entries.contains(option))
        entries.add(option);
    }
    resetCache();
  }
  
  public final String getEventHandlerReference()
  {
    return definition.getEventHandler();
  }
  
  /**
   * @since 2.8.0
   * @param values
   */
  public void selectOptions(String[] values)
  {
    for(int i=0;i<values.length;i++)
      selectOption(values[i]);
    resetCache();    
  }
  
  public void selectOption(String value)
  {
    // unable to select null
    //
    if(value==null)
      return;

    Option option = new Option(value, value,null,null, definition.getCallHookOnSelect());
    // not part of the list items
    //
    if(!entries.contains(option))
      return;

    // already selected
    //
    if(Arrays.asList(getSelection()).contains(option))
      return;
    
    // select them
    //
    if(getValue()!=null && getValue().length()>0)
      setValue(getValue()+DELIMITER+value);
    else
      setValue(value);
    resetCache();
  }
  
  
  /**
   * 
   * @return the selected Strings or new String[0] of non element selected
   */
  public String[] getSelection()
  {
    if(getValue()==null || getValue().length()==0)
      return new String[0];
    return getValue().split("["+DELIMITER+"]");
  }


  public boolean isMultichoice()
  {
    return multichoice;
  }


  public void setMultichoice(boolean multichoice)
  {
    this.multichoice = multichoice;
  }
  
  protected final Option getOption(String value)
  {
    if(value==null)
      return null;
    
    Iterator iter = entries.iterator();
    while(iter.hasNext())
    {
      Option option = (Option)iter.next();
      if(option.value.equals(value))
        return option;
    }
    return null;
  }  
}
