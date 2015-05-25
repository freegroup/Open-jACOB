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

package de.tif.jacob.screen.dialogs.form;

import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.event.IAutosuggestProvider;


/**
 *
 */
public interface IFormDialog extends IDialog   //, IMultipleDataDialog
{
	
  /**
   * Add a header text to the dialog. A header has a label and a right hand side line.<br>
   * <br>
   * Example:<br>
   * label -------------------------------
   * 
   */
  public void addHeader(String label, CellConstraints constraint);

  /**
   * Add a generic Label/Caption to the dialog
   * 
   */
  public void addLabel(String label, CellConstraints constraint);
  

  public void addCheckBox(String name, boolean checked, CellConstraints constraint);
  
	/**
	 * Add a password field to the dialog. The name of the PasswordField represents the returned parameter
	 * if the user press [ok] in the dialog.
	 * 
	 * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
	 * @param value      The start value of the field.
	 * @param constraint The layout constraint of the element.
	 */
	public void addPasswordField(String name, String value, CellConstraints constraint);

	/**
	 * Add a TextField to the dialog. The name of the TextField represents the returned parameter
	 * if the user press [ok] in the dialog.
	 * 
	 * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
	 * @param value      The start value of the field.
	 * @param constraint The layout constraint of the element.
	 */
	public void addTextField(String name, String value, CellConstraints constraint);
	
  /**
   * Add a TextField to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.<br>
   * The user get input support from the IAutosuggestProvider.<br>
   * 
   * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
   * @param value      The start value of the field.
   * @param autosuggest The provider for the textfield autosuggest.
   * @param constraint The layout constraint of the element.
   */
  public void addTextField(String name, String value,IAutosuggestProvider autosuggest, CellConstraints constraint);

  /**
	 * Add a TextField to the dialog. The name of the TextField represents the returned parameter
	 * if the user press [ok] in the dialog. You can set the text field to readonly with the
	 * 'readonly' parameter.
	 * 
	 * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
	 * @param value      The start value of the field.
	 * @param readonly   Toggle the readonly behaviour of the input field.
	 * @param constraint The layout constraint of the element.
	 */
	public void addTextField(String name, String value, boolean readonly , CellConstraints constraint);

	/**
	 * Add a file upload field to the dialog. The name represents the returned parameter
	 * if the user press [ok] in the dialog.
	 * 
	 * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
	 * @param constraint The layout constraint of the element.
	 */
	public void addFileUpload(String name, CellConstraints constraint);
	
	/**
	 * Add a TextArea to the dialog. The name of the TextField represents the returned parameter
	 * if the user press [ok] in the dialog.
	 * 
	 * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
	 * @param value      The start value of the field.
	 * @param constraint The layout constraint of the element.
	 */
	public void addTextArea(String name, String value, CellConstraints constraint);

	/**
	 * Add a TextArea to the dialog. The name of the TextField represents the returned parameter
	 * if the user press [ok] in the dialog.
	 * 
	 * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
	 * @param value      The start value of the field.
	 * @param readonly   Toggle the readonly behaviour of the input field.
	 * @param constraint The layout constraint of the element.
	 */
	public void addTextArea(String name, String value, boolean readonly, CellConstraints constraint);

  /**
   * Add a TextArea to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.
   * 
   * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
   * @param value      The start value of the field.
   * @param codeLanguage required for syntax highlighting. Highlighted code are always readonly
   * @param constraint The layout constraint of the element.
   */
  public void addTextArea(String name, String value,  String codeLanguage, CellConstraints constraint);

  /**
   * Add a TextArea to the dialog. The name of the TextField represents the returned parameter
   * if the user press [ok] in the dialog.
   * 
   * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
   * @param value      The start value of the field.
   * @param readonly   Toggle the readonly behaviour of the input field.
   * @param autowordwrap   Toggle the word wrap behaviour of the input field. Default is [false].
   * @param constraint The layout constraint of the element.
   */
  public void addTextArea(String name, String value, boolean readonly, boolean autowordwrap, CellConstraints constraint);

  /**
	 * Add a ComboBox to the dialog. A ComboBox <b>can't</b> span more than one row. 
	 * 
	 * @param name       The name of the parameter which will be returned, if the user press [ok] in the dialog
	 * @param constraint The layout constraint of the element.
	 */
	public void addComboBox(String name, String[] values, int selectIndex, CellConstraints constraint);

	/**
	 * Add a ComboBox to the dialog. A ComboBox <b>can't</b> span more than one row. 
	 * 
	 * @param buttonCallback The callback which will be called if the user changed the selection in the opend dialog. The dialog will not be closed.
	 * @param name The name of the field/parameter
	 * @param values The values to select
	 * @param selectIndex The pre selected index
	 * @param constraint The position constraint of the element
	 */
	public void addComboBox(IFormActionEmitter buttonCallback, String name, String[] values, int selectIndex, CellConstraints constraint);
	
	/**
   * Add a list box component to the dialog.
   * 
   * @param name
   *          the name of the parameter which will be returned, if the user
   *          press [ok] in the dialog
   * @param values
   *          the list box entries
   * @param selectIndex
   *          the index of the initially selected entry or <code>-1</code>,
   *          if no entry should be initially selected.
   * @param constraint
   *          the layout constraint of the element.
   */
	public void addListBox(String name, String[] values, int selectIndex, CellConstraints constraint);

	/**
	 * Enable/Disable the debug options of the dialog. The debug flag is useful to verify
	 * the form layout.
	 * In the debug modus the form dialog shows the {@link FormLayout} grid.
	 * 
	 * @param debugFlag true to enable the debug feature.
	 */
	public void setDebug(boolean debugFlag);

	/**
   * Sets the label of the cancel button.
   * <p>
   * If you set <i>null</i>, the cancel button will be hidden.
   * 
   * @param cancelButton
   *          The label of the cancel button or <i>null</i> to hide the button.
   */
  public void setCancelButton(String cancelButton);

	/**
	 * Returns the label of the cancel button or <i>null</i>, if the cancel button is hidden.
	 * 
	 * @return Returns the label of the cancel button or null if no button visible
	 */
	public String getCancelButton();

	/**
	 * Adds a submit button to this form dialog.
	 * <p>
	 * Note: This method is equivalent to {@link #addSubmitButton(String, String, boolean)} with <code>emphasize</code> set to <i>false<i>.
	 * 
	 * @param buttonId the button id of the submit button, which will be returned as argument in {@link IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext, String, java.util.Map)}
	 * @param buttonLabel the label of the submit button
	 * @see IFormDialogCallback
	 */
  public void addSubmitButton(String buttonId, String buttonLabel);

  /**
   * Adds a submit button to this form dialog.
   * 
   * @param buttonId the button id of the submit button, which will be returned as argument in {@link IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext, String, java.util.Map)}
   * @param buttonLabel the label of the submit button
   * @param emphasize <i>true</i> to emphasize the button, <i>false</i> if not 
   * @see IFormDialogCallback
   * @since 2.9
   */
  public void addSubmitButton(String buttonId, String buttonLabel, boolean emphasize);
  
	/**
	 * Returns the title of the dialog. The title will be displayed in the top of the 
	 * dialog.
	 * 
	 * @return The title of the dialog.
	 */
  public String getTitle();
	
	/**
	 * Add a Form action button to the dialog. The button does appear <b>in</b> the form dialog and
	 * not in the toolbar button bar.<br>
	 * <br>
	 * The does <b>not</b> close if the user press the button. This is useful if you want make a server
	 * request to update dialog data.<br>
	 * 
	 * @param callback The callback object if the user has clicked the button
	 * @param buttonLabel The label of the button
	 * @param constraint The layout constraint of the element.
	 */
	public void addFormButton(IFormActionEmitter callback, String buttonLabel, CellConstraints constraint);
}
