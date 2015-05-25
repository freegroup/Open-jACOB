/*
 * Created on 21.06.2007
 *
 */
package de.tif.jacob.screen;

import java.awt.Point;
import java.awt.Rectangle;

import de.tif.jacob.core.definition.guielements.FontDefinition;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.event.IComboBoxEventHandler;
import de.tif.jacob.screen.event.IDynamicImageEventHandler;
import de.tif.jacob.screen.event.ILabelEventHandler;

public interface IMutableGroup extends IGroup
{
  /**
   * Remove all added UI elements from this group.
   *
   */
  public void removeAllChildren();
  
  /**
   * Add a generic Button to the group.
   * 
   * @param context The current working context of jACOB
   * @param name The unique name of the element. It is not possible to add a name twice.
   * @param label The label of the button.
   * @param boundingRect The position and dimension of the button
   * @param callback The callback handler of the element
   * @return The new created UI element.
   */
  public IButton addButtonGeneric(IClientContext context, String name, String label,Rectangle boundingRect, IButtonEventHandler callback);

  public IButton addButtonNewRecord(IClientContext context, String name, String label,Rectangle boundingRect, IActionButtonEventHandler callback);
  public IButton addButtonNewRecord(IClientContext context, String name, String label,Rectangle boundingRect);

  public IButton addButtonSearch(IClientContext context, String name, String label,Rectangle boundingRect,IActionButtonEventHandler callback);
  public IButton addButtonSearch(IClientContext context, String name, String label,Rectangle boundingRect);

  public IButton addButtonUpdateRecord(IClientContext context, String name, String label,Rectangle boundingRect,IActionButtonEventHandler callback);
  public IButton addButtonUpdateRecord(IClientContext context, String name, String label,Rectangle boundingRect);

  public IButton addButtonResetGroup(IClientContext context, String name, String label,Rectangle boundingRect,  IActionButtonEventHandler callback);
  public IButton addButtonResetGroup(IClientContext context, String name, String label,Rectangle boundingRect);
  
  public IText   addTextField(IClientContext context, String field, String name, Rectangle boundingRect );
  public IText   addTextField(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect );
  
  /**
   * @since 2.7.4
   */
  public IInFormLongText addLongTextField(IClientContext context, String field, String name, Rectangle boundingRect, boolean wordwrap);
  /**
   * @since 2.7.4
   */
  public IInFormLongText addLongTextField(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect, boolean wordwrap);
  
  public IComboBox  addComboBox(IClientContext context, String field, String name, Rectangle boundingRect );
  public IComboBox  addComboBox(IClientContext context, String field, String name, Rectangle boundingRect, IComboBoxEventHandler callback );
  public IComboBox  addComboBox(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect );
  public IComboBox  addComboBox(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect, IComboBoxEventHandler callback );

  public IDate  addDate(IClientContext context, String field, String name, Rectangle boundingRect );
  public IDate  addDate(IClientContext context, String tableAlias, String field, int recordIndex, String name, Rectangle boundingRect );

  public IHeading addHeading(IClientContext context, String name, String label, Rectangle boundingRect, FontDefinition font);

  public ILabel addLabel(IClientContext context, String name, String label, Rectangle boundingRect, FontDefinition font);
  public ILabel addLabel(IClientContext context, String name, String label, Rectangle boundingRect, FontDefinition font, ILabelEventHandler callback);

  public IStyledText addStyledText(IClientContext context, String name, String text, Rectangle boundingRect);

  public IStaticImage addStaticImage(IClientContext context, String name, String path, Point location);
  
  /**
   * @param context
   * @param name
   * @param path
   * @param boundingRect
   * @param callback
   * @return
   * @since 2.8.7
   */
  public IDynamicImage addDynamicImage(IClientContext context, String name, String path, Rectangle boundingRect, IDynamicImageEventHandler callback);
}
