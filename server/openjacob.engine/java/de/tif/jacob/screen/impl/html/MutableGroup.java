/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen.impl.html;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.internal.IDataTableInternal;
import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IMutableGroupDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.actiontypes.ActionTypeClearGroup;
import de.tif.jacob.core.definition.actiontypes.ActionTypeGeneric;
import de.tif.jacob.core.definition.actiontypes.ActionTypeNewRecord;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeUpdateRecord;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.core.definition.guielements.Alignment;
import de.tif.jacob.core.definition.guielements.ButtonDefinition;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.DateInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.DynamicImageDefinition;
import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.core.definition.guielements.HeadingDefinition;
import de.tif.jacob.core.definition.guielements.InFormLongTextInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.LabelDefinition;
import de.tif.jacob.core.definition.guielements.LongTextInputMode;
import de.tif.jacob.core.definition.guielements.StaticImageDefinition;
import de.tif.jacob.core.definition.guielements.StyledTextDefinition;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.impl.AbstractMutableGroupDefinition;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IDate;
import de.tif.jacob.screen.IDynamicImage;
import de.tif.jacob.screen.IHeading;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IMutableGroup;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.IStyledText;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.event.IComboBoxEventHandler;
import de.tif.jacob.screen.event.IDynamicImageEventHandler;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.HTTPForeignField;
import de.tif.jacob.screen.impl.IApplicationFactory;

public class MutableGroup extends Group implements IMutableGroup
{
  static public final transient String RCS_ID = "$Id: MutableGroup.java,v 1.37 2010/08/15 00:40:36 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.37 $";

  private final IMutableGroupDefinition definition;
  private long      changeCount = -1;

  public MutableGroup(IApplication app, IMutableGroupDefinition group)
  {
    super(app, group, null);
    this.definition = group;
  }

  
  /**Ø
   * Return the HTML representation of this object
   * 
   */
  public void calculateHTML(ClientContext context) throws Exception
  {
    context.setGroup(this);
    
    // initial set of the data status
    if(getDataStatus()==UNDEFINED)
      setDataStatus(context,SEARCH);
    
    // update the element if the data has been changed
    // /e.g. an element of a table alias has been selected
    //
    IDataTableInternal table = (IDataTableInternal) context.getDataTable(this.definition.getTableAlias().getName());
    IDataTableRecord currentDataRecord = getSelectedRecord(context);

    if(table.getChangeCount()!= changeCount)
    {
      // Der Record hat sich geï¿½ndert -> Alle GUI Elemente nachladen
      //
      Iterator iter =  getChildren().iterator();
      while(iter.hasNext())
      {
        Object obj=iter.next();
        if(obj instanceof SingleDataGUIElement && !(obj instanceof HTTPForeignField))
        {
          SingleDataGUIElement element =(SingleDataGUIElement)obj;
          // Falls das UI Element den selectedRecord anzeigen soll (index==-1) wird dieser
          // dem Element gleich ï¿½bergeben.
          if(element.getDisplayRecordIndex()==-1)
          {
            element.setValue(context, currentDataRecord);
          }
          // ...ansonsten wird die ganze Tabelle ï¿½bergeben und das Element kann sich den Record selber
          // raus holen. Wird z.B. bei eine MutableGroup verwendet wenn man einen "InformBrowser" selbst aufbaut.
          //
          else
          {
            // Wenn das Element nicht den selectedRecord anzeigt, dann ist es auch nicht 
            // gesagt, dass sich das Element auf den Alias der Gruppe bezieht.
            element.setValue(context, context.getDataTable(element.getDataField().getTableAlias().getName()));
          }
        }
      }
      
      // den Status des Record an die GUI weiterleiten
      // Es kann sein, das ein Record durch einen Hook manipuliert worden ist. Darauf muss nun
      // reagiert werden.
      //
      if(currentDataRecord!=null && currentDataRecord.isUpdated() && getDataStatus()!=UPDATE)
        setDataStatus(context, UPDATE);
      else if(currentDataRecord!=null && currentDataRecord.isNormal() && getDataStatus()!=SELECTED)
        setDataStatus(context, SELECTED);
      else if(currentDataRecord!=null && currentDataRecord.isNormal() && getDataStatus()==SELECTED && changeCount!=table.getChangeCount())
        setDataStatus(context, SELECTED);
      else if(currentDataRecord!=null && currentDataRecord.isNew() && getDataStatus()!=NEW)
        setDataStatus(context,NEW);
      else if(currentDataRecord==null &&  getDataStatus()!=SEARCH)
        setDataStatus(context,SEARCH);
      
      // set new change counter after all children have been informed
      // (nobody knows what they will do which has an influence on the change counter as well :-)
      changeCount = table.getChangeCount();
    }
    
    if(getCache()==null)
    {  
      Writer w = newCache();
      String eventGUID = getEtrHashCode();

      w.write("\n<div isGroup=\"true\" id=\"");
      w.write(eventGUID);
      w.write("\" oncontextmenu=\"");
      w.write(contextMenu.getContextMenuFunction());
      w.write("\" style=\"");
      getCSSStyle(context, w,boundingRect);
      w.write("\">\n");
    }
    super.calculateHTML(context);
  }
  
  /** 
   * ATTENTION: This implementation differs from the super implementation. See the sequenze of the calls
   *            FIRST write me than the childs!!!!
   * @param out
   */
  public void writeHTML(ClientContext context, Writer w) throws IOException
  {
    if(!isVisible())
      return;
    context.setGroup(this);
    writeCache(w);
    super.writeHTML(context, w);
    w.write("</div>\n");
  }

  /**
   * The framework call this method is an event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    ((ClientContext)context).setGroup(this);

    return super.processEvent(context,guid,event,value);
  }
  
  private IButton addButton(IClientContext context, String name, String label, Rectangle boundingRect, IGroupMemberEventHandler callback, ActionType type)
  {
    try
    {
      Dimension dim =toDimension(boundingRect);
      
      ButtonDefinition buttonDef = new ButtonDefinition(name, "", null, label, true, false, null, 0, 0, type, dim, null, -1, null, null);
      Button button = (Button) buttonDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      button.setEventHandlerSetByMutableGroup(callback);
      resetCache();
      
      // propagate the current status of the group to the element
      //
      button.onGroupDataStatusChanged((IClientContext)Context.getCurrent(), getDataStatus());

      return button;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }

  
  public IButton addButtonGeneric(IClientContext context, String name, String label, Rectangle boundingRect, IButtonEventHandler callback)
  {
    return addButton(context, name,label,boundingRect,callback,new ActionTypeGeneric());
  }

  public IButton addButtonNewRecord(IClientContext context, String name, String label, Rectangle boundingRect, IActionButtonEventHandler callback)
  {
    return addButton(context, name,label,boundingRect,callback,new ActionTypeNewRecord());
  }

  public IButton addButtonResetGroup(IClientContext context, String name, String label, Rectangle boundingRect, IActionButtonEventHandler callback)
  {
    return addButton(context, name,label,boundingRect,callback,new ActionTypeClearGroup());
  }

  public IButton addButtonSearch(IClientContext context, String name, String label, Rectangle boundingRect, IActionButtonEventHandler callback)
  {
    return addButton(context, name,label,boundingRect,callback,new ActionTypeSearch(getApplication().getApplicationDefinition().getDefaultRelationSet(),Filldirection.BOTH,false));
  }

  public IButton addButtonUpdateRecord(IClientContext context, String name, String label, Rectangle boundingRect, IActionButtonEventHandler callback)
  {
    return addButton(context, name,label,boundingRect,callback,new ActionTypeUpdateRecord(ActionType.SCOPE_OUTERGROUP));
  }

  public IButton addButtonGeneric(IClientContext context, String name, String label, Rectangle boundingRect)
  {
    return addButton(context, name,label,boundingRect,null,new ActionTypeGeneric());
  }

  public IButton addButtonNewRecord(IClientContext context, String name, String label, Rectangle boundingRect)
  {
    return addButton(context, name,label,boundingRect,null,new ActionTypeNewRecord());
  }

  public IButton addButtonResetGroup(IClientContext context, String name, String label, Rectangle boundingRect)
  {
    return addButton(context, name,label,boundingRect,null,new ActionTypeClearGroup());
  }

  public IButton addButtonSearch(IClientContext context, String name, String label, Rectangle boundingRect)
  {
    return addButton(context, name,label,boundingRect,null,new ActionTypeSearch(getApplication().getApplicationDefinition().getDefaultRelationSet(),Filldirection.BOTH,false));
  }

  public IButton addButtonUpdateRecord(IClientContext context, String name, String label, Rectangle boundingRect)
  {
    return addButton(context, name,label,boundingRect,null,new ActionTypeUpdateRecord(ActionType.SCOPE_OUTERGROUP));
  }


  public String getEventHandlerReference()
  {
    return ((AbstractMutableGroupDefinition)definition).getEventHandler();
  }

  

  public IText addTextField(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect)
  {
    try
    {
      Dimension dim =toDimension(boundingRect);
      
      ITableAlias alias = getApplicationDefinition().getTableAlias(tableAlias);
      TextInputFieldDefinition textDef = new TextInputFieldDefinition(name,"",null, null,dim,true, false, false,0,0,null,alias,alias.getTableDefinition().getTableField(field),null, null);
      Text text = (Text) textDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      text.setDisplayRecordIndex(recordIndex);
      text.setValue(context,context.getDataTable(tableAlias));
      
      // propagate the current status of the group to the element
      //
      text.onGroupDataStatusChanged(context, getDataStatus());

      return text;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }


  public IInFormLongText addLongTextField(IClientContext context, String field, String name, Rectangle boundingRect, boolean wordwrap)
  {
    return addLongTextField(context, this.getGroupTableAlias(), field, -1, name, boundingRect, wordwrap);
  }

  public IInFormLongText addLongTextField(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect, boolean wordwrap)
  {
    try
    {
      Dimension dim = toDimension(boundingRect);

      ITableAlias alias = getApplicationDefinition().getTableAlias(tableAlias);
      InFormLongTextInputFieldDefinition textDef = new InFormLongTextInputFieldDefinition(name, "", null, null, dim, true, false, 0, 0, null, alias, alias.getTableDefinition().getTableField(field), LongTextInputMode.FULLEDIT, wordwrap,null, null);
      InFormLongText text = (InFormLongText) textDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      text.setDisplayRecordIndex(recordIndex);
      text.setValue(context, context.getDataTable(tableAlias));

      // propagate the current status of the group to the element
      //
      text.onGroupDataStatusChanged(context, getDataStatus());

      return text;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }


  public IText addTextField(IClientContext context, String field, String name, Rectangle boundingRect)
  {
    return addTextField(context,this.getGroupTableAlias(),field,-1,name,boundingRect);
  }



  public IComboBox addComboBox(IClientContext context, String tableAlias, String fieldName, int recordIndex, String name, Rectangle boundingRect, IComboBoxEventHandler callback)
  {
    try
    {
      Dimension dim =toDimension(boundingRect);
      
      ITableAlias alias = getApplicationDefinition().getTableAlias(tableAlias);
      ITableField field = alias.getTableDefinition().getTableField(fieldName);
      List values = new ArrayList();
//      List i18ns = new ArrayList();
      if(field.getType() instanceof EnumerationFieldType)
      {
        
        EnumerationFieldType enumType = (EnumerationFieldType)field.getType();
        for(int i=0;i<enumType.enumeratedValueCount();i++)
        {
          values.add(enumType.getEnumeratedValue(i));
//          i18ns.add(enumType.getLabel(i));
        }
      }
      ComboBoxInputFieldDefinition textDef = new ComboBoxInputFieldDefinition(name,"",null, null,(String[])values.toArray(new String[0]),true, false, true, true, dim,true, false, 0,0,null,alias,field,null);
      ComboBox combo = (ComboBox) textDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      combo.setEventHandlerSetByMutableGroup(callback);
      combo.setDisplayRecordIndex(recordIndex);
      combo.setValue(context,context.getDataTable(tableAlias));
      
      // propagate the current status of the group to the element
      //
      combo.onGroupDataStatusChanged(context, getDataStatus());

      return combo;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }


  public IComboBox addComboBox(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect)
  {
    return addComboBox(context, tableAlias,field, recordIndex, name,boundingRect,null);
  }

  public IComboBox addComboBox(IClientContext context, String field, String name, Rectangle boundingRect, IComboBoxEventHandler callback)
  {
    return addComboBox(context, getGroupTableAlias(),field, -1, name,boundingRect,callback);
  }

  public IComboBox addComboBox(IClientContext context, String field, String name, Rectangle boundingRect)
  {
    return addComboBox(context, getGroupTableAlias(),field, -1, name,boundingRect,null);
  }


  public IDate addDate(IClientContext context, String tableAlias, String fieldName, int recordIndex, String name, Rectangle boundingRect)
  {
    try
    {
      Dimension dim =toDimension(boundingRect);
      
      ITableAlias alias = getApplicationDefinition().getTableAlias(tableAlias);
      ITableField field = alias.getTableDefinition().getTableField(fieldName);
      DateInputFieldDefinition dateDef = new DateInputFieldDefinition(name,"",null, null,dim,true, false, 0,0,null,alias,field,null);
      Date date = (Date) dateDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
//      date.setEventHandlerSetByMutableGroup(callback);
      date.setDisplayRecordIndex(recordIndex);
      date.setValue(context,context.getDataTable(tableAlias));
      
      // propagate the current status of the group to the element
      //
      date.onGroupDataStatusChanged(context, getDataStatus());

      return date;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }


  public IDate addDate(IClientContext context, String field, String name, Rectangle boundingRect)
  {
    return addDate(context, this.getGroupTableAlias(), field, -1,name,boundingRect);
  }

  private static IApplicationFactory getApplicationFactory(IClientContext context)
  {
    return ((HTTPClientSession) context.getSession()).getApplicationFactory();
  }

  public IHeading addHeading(IClientContext context, String name, String label, Rectangle boundingRect, FontDefinition font)
  {
    try
    {
      Dimension dim =toDimension(boundingRect);
      
      de.tif.jacob.core.definition.guielements.Caption caption = new Caption(dim,label,null, null,Alignment.LEFT, Alignment.TOP, font,false, null);
      HeadingDefinition headingDef = new HeadingDefinition(name,null,true,  0,0,caption);
      Heading heading = (Heading) headingDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      // propagate the current status of the group to the element
      //
      heading.onGroupDataStatusChanged(context, getDataStatus());

      return heading;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }
  
  public ILabel addLabel(IClientContext context, String name, String label, Rectangle boundingRect, FontDefinition font, ILabelEventHandler callback)
  {
    try
    {
      Dimension dim = toDimension(boundingRect);

      de.tif.jacob.core.definition.guielements.Caption caption = new Caption(dim, label,null, null, Alignment.LEFT, Alignment.TOP, font,false, null);
      LabelDefinition labelDef = new LabelDefinition(name, null, true, 0, 0, caption, null, null,null,-1,null,null);
      Label heading = (Label) labelDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      // propagate the current status of the group to the element
      //
      heading.onGroupDataStatusChanged(context, getDataStatus());
      heading.setEventHandlerSetByMutableGroup(callback);
      
      return heading;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }
  
  public ILabel addLabel(IClientContext context, String name, String label, Rectangle boundingRect, FontDefinition font)
  {
    return addLabel(context, name,label,boundingRect, font,null);
  }
  
  private final static Dimension toDimension(Rectangle rect)
  {
    return new Dimension(rect.x,rect.y,rect.width,rect.height);
  }
  
  public IStyledText addStyledText(IClientContext context, String name, String content, Rectangle boundingRect)
  {
    try
    {
      Dimension dim = toDimension(boundingRect);

      StyledTextDefinition styledTextDef = new StyledTextDefinition(name,"",null,true,true,dim,content);
      StyledText styledText = (StyledText) styledTextDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      // propagate the current status of the group to the element
      //
      styledText.onGroupDataStatusChanged(context, getDataStatus());
      
      return styledText;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }


  public IStaticImage addStaticImage(IClientContext context, String name, String path, Point location)
  {
    try
    {
      Dimension dim = toDimension(new Rectangle(location.x,location.y,-1,-1));

      StaticImageDefinition staticImageDef = new StaticImageDefinition(name,"",null,true,dim,path,null);
      StaticImage image = (StaticImage) staticImageDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      // propagate the current status of the group to the element
      //
      image.onGroupDataStatusChanged(context, getDataStatus());
      
      return image;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }
  
  public IDynamicImage addDynamicImage(IClientContext context, String name, String path, Rectangle boundingRect, IDynamicImageEventHandler callback)
  {
    try
    {
      Dimension dim = toDimension(boundingRect);

      DynamicImageDefinition dynamicImageDef = new DynamicImageDefinition(name, "", null, true, dim, null);
      DynamicImage image = (DynamicImage) dynamicImageDef.createRepresentation(getApplicationFactory(context), getApplication(), this);
      image.setEventHandlerSetByMutableGroup(callback);
      // propagate the current status of the group to the element
      //
      image.onGroupDataStatusChanged(context, getDataStatus());

      return image;
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context, e);
    }
    return null;
  }
  
  /**
   * Methode ist nur vorhanden um legacy Interface declarationen zu befriedigen.
   * Interface Deklaration wird in einem zukï¿½nftigen Release ersatzlos gestrichen.
   */
  public void setBorder(boolean flag)
  {
    // Exception werfen damit man nicht auf die Idee kommt die Methode in einer HtmlGroup zu rufen.
    throw new RuntimeException("Invalid call of MutableGroup.setBorder(boolean..). This method is not supported");
  }
}










