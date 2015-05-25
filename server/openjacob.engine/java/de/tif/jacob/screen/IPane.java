/*
 * Created on 19.10.2007
 *
 * @since 2.8.3
 */
package de.tif.jacob.screen;

import java.math.BigDecimal;

public interface IPane extends IGuiElement
{

  void setBorder(boolean borderEnabledFlag);
  
  /**
   * @param context
   * @since 2.8.0
   */
  void clear(IClientContext context) throws Exception;

  /**
   * Set this pane as the current active tab of the tab container.
   * 
   * @param context
   * @since 2.8.0
   */
  public void setActive(IClientContext context) throws Exception;
  
  /**
   * Returns the related table alias name of the TabPane.
   * 
   * @return the table alias of the TabPane.
   * @throws Exception
   * @since 2.8.0
   */
  public String getPaneTableAlias() throws Exception;

  
  /**
   * Returns the name of the TabPane
   * 
   * @since 2.8.0
   */
  public String getName();
  
  /**
   * Return the parent container of the pane.
   * 
   * @param context
   * @return
   * @throws Exception 
   * @since 2.10
   */
  public IContainer getContainer(IClientContext context) throws Exception;


  /**
   * Sets the value of an input field specified by name.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new value of the input field.
   * @since 2.10
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, String value) throws Exception;

  /**
   * Sets the value given as decimal of an input field specified by name.
   * <p>
   * The decimal value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @since 2.10
   * @param value
   *          The new decimal value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, BigDecimal value) throws Exception;

  /**
   * Sets the value given as double of an input field specified by name.
   * <p>
   * The double value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new double value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, Double value) throws Exception;

  /**
   * Sets the value given as float of an input field specified by name.
   * <p>
   * The float value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   *          The new float value of the input field.
   * @since 2.10
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, Float value) throws Exception;

  /**
   * Sets the value given as date of an input field specified by name.
   * <p>
   * The date value will be converted to the string presentation of the
   * corresponding locale, i.e. the locale of the current user.
   * 
   * @param fieldName
   *          the name of the input field, i.e. this is <b>not</b> the name of
   *          the associated data field.
   * @param value
   * @since 2.10
   *          The new date value of the input field.
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public void setInputFieldValue(String fieldName, java.util.Date value) throws Exception;

  /**
   * Returns the value of the input field specified by name.
   * 
   * @param fieldName
   *          the name of the input field
   * @return the visible value of the input field
   * @since 2.10
   * @throws Exception
   *           if the field does not exist or is not an input field.
   */
  public String getInputFieldValue(String fieldName) throws Exception;
}
