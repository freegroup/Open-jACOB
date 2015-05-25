/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
package de.tif.jacob.designer.preferences;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.util.UIUtils;
/**
 * Plugin preference page.
 * 
 * @author Pascal Essiembre
 * @version $Author: freegroup $ $Revision: 1.1 $ $Date: 2007/05/18 16:13:36 $
 */
public class ResourceBundlePreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  /** Number of pixels per field indentation */
  private final int indentPixels = 20;
  /* Preference fields. */
  private Text keyGroupSeparator;
  private Button alignEqualSigns;
  private Button convertUnicodeToEncoded;
  private Button convertEncodedToUnicode;
  private Button showGeneratedBy;
  private Button supportNL;
  private Button groupKeys;
  private Text groupLevelDeep;
  private Text groupLineBreaks;
  private Button groupAlignEqualSigns;
  private Button wrapLines;
  private Text wrapCharLimit;
  private Button wrapAlignEqualSigns;
  private Text wrapIndentSpaces;
  private Button wrapNewLine;
  private Button newLineTypeForce;
  private Button[] newLineTypes = new Button[3];
  /** Controls with errors in them. */
  private final Map errors = new HashMap();

  /**
   * Constructor.
   */
  public ResourceBundlePreferencePage()
  {
    super();
  }

  /**
   * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
   */
  protected Control createContents(Composite parent)
  {
    IPreferenceStore prefs = getPreferenceStore();
    Composite field = null;
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));
    // Key group separator
    field = createFieldComposite(composite);
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.groupSep"));
    keyGroupSeparator = new Text(field, SWT.BORDER);
    keyGroupSeparator.setText(prefs.getString(ResourceBundlePreferences.KEY_GROUP_SEPARATOR));
    keyGroupSeparator.setTextLimit(2);
    // Convert unicode to encoded?
    field = createFieldComposite(composite);
    convertUnicodeToEncoded = new Button(field, SWT.CHECK);
    convertUnicodeToEncoded.setSelection(prefs.getBoolean(ResourceBundlePreferences.CONVERT_UNICODE_TO_ENCODED));
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.convertUnicode"));
    // Convert encoded to unicode?
    field = createFieldComposite(composite);
    convertEncodedToUnicode = new Button(field, SWT.CHECK);
    convertEncodedToUnicode.setSelection(prefs.getBoolean(ResourceBundlePreferences.CONVERT_ENCODED_TO_UNICODE));
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.convertEncoded"));
    // Show generated by comment?
    field = createFieldComposite(composite);
    showGeneratedBy = new Button(field, SWT.CHECK);
    showGeneratedBy.setSelection(prefs.getBoolean(ResourceBundlePreferences.SHOW_GENERATOR));
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.showGeneratedBy"));
    // Support "NL" localization structure
    field = createFieldComposite(composite);
    supportNL = new Button(field, SWT.CHECK);
    supportNL.setSelection(prefs.getBoolean(ResourceBundlePreferences.SUPPORT_NL));
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.supportNL"));
    // Format group
    Group formatGroup = new Group(composite, SWT.NONE);
    formatGroup.setText(JacobDesigner.getResourceString("prefs.formatOptions"));
    formatGroup.setLayout(new GridLayout(1, false));
    formatGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    // Align equal signs?
    field = createFieldComposite(formatGroup);
    alignEqualSigns = new Button(field, SWT.CHECK);
    alignEqualSigns.setSelection(prefs.getBoolean(ResourceBundlePreferences.ALIGN_EQUAL_SIGNS));
    alignEqualSigns.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        refreshEnabledStatuses();
      }
    });
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.alignEquals"));
    // Group keys?
    field = createFieldComposite(formatGroup);
    groupKeys = new Button(field, SWT.CHECK);
    groupKeys.setSelection(prefs.getBoolean(ResourceBundlePreferences.GROUP_KEYS));
    groupKeys.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        refreshEnabledStatuses();
      }
    });
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.groupKeys"));
    // Group keys by how many level deep?
    field = createFieldComposite(formatGroup, indentPixels);
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.levelDeep"));
    groupLevelDeep = new Text(field, SWT.BORDER);
    groupLevelDeep.setText(prefs.getString(ResourceBundlePreferences.GROUP_LEVEL_DEEP));
    groupLevelDeep.setTextLimit(2);
    setWidthInChars(groupLevelDeep, 2);
    groupLevelDeep.addKeyListener(new IntTextValidatorKeyListener(JacobDesigner.getResourceString("prefs.levelDeep.error")));
    // How many lines between groups?
    field = createFieldComposite(formatGroup, indentPixels);
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.linesBetween"));
    groupLineBreaks = new Text(field, SWT.BORDER);
    groupLineBreaks.setText(prefs.getString(ResourceBundlePreferences.GROUP_LINE_BREAKS));
    groupLineBreaks.setTextLimit(2);
    setWidthInChars(groupLineBreaks, 2);
    groupLineBreaks.addKeyListener(new IntTextValidatorKeyListener(JacobDesigner.getResourceString("prefs.linesBetween.error")));
    // Align equal signs within groups?
    field = createFieldComposite(formatGroup, indentPixels);
    groupAlignEqualSigns = new Button(field, SWT.CHECK);
    groupAlignEqualSigns.setSelection(prefs.getBoolean(ResourceBundlePreferences.GROUP_ALIGN_EQUAL_SIGNS));
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.groupAlignEquals"));
    // Wrap lines?
    field = createFieldComposite(formatGroup);
    wrapLines = new Button(field, SWT.CHECK);
    wrapLines.setSelection(prefs.getBoolean(ResourceBundlePreferences.WRAP_LINES));
    wrapLines.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        refreshEnabledStatuses();
      }
    });
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.wrapLines"));
    // After how many characters should we wrap?
    field = createFieldComposite(formatGroup, indentPixels);
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.wrapLinesChar"));
    wrapCharLimit = new Text(field, SWT.BORDER);
    wrapCharLimit.setText(prefs.getString(ResourceBundlePreferences.WRAP_CHAR_LIMIT));
    wrapCharLimit.setTextLimit(4);
    setWidthInChars(wrapCharLimit, 4);
    wrapCharLimit.addKeyListener(new IntTextValidatorKeyListener(JacobDesigner.getResourceString("prefs.wrapLinesChar.error")));
    // Align wrapped lines with equal signs?
    field = createFieldComposite(formatGroup, indentPixels);
    wrapAlignEqualSigns = new Button(field, SWT.CHECK);
    wrapAlignEqualSigns.setSelection(prefs.getBoolean(ResourceBundlePreferences.WRAP_ALIGN_EQUAL_SIGNS));
    wrapAlignEqualSigns.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        refreshEnabledStatuses();
      }
    });
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.wrapAlignEquals"));
    // How many spaces/tabs to use for indenting?
    field = createFieldComposite(formatGroup, indentPixels);
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.wrapIndent"));
    wrapIndentSpaces = new Text(field, SWT.BORDER);
    wrapIndentSpaces.setText(prefs.getString(ResourceBundlePreferences.WRAP_INDENT_SPACES));
    wrapIndentSpaces.setTextLimit(2);
    setWidthInChars(wrapIndentSpaces, 2);
    wrapIndentSpaces.addKeyListener(new IntTextValidatorKeyListener(JacobDesigner.getResourceString("prefs.wrapIndent.error")));
    // Should we wrap after new line characters
    field = createFieldComposite(formatGroup);
    wrapNewLine = new Button(field, SWT.CHECK);
    wrapNewLine.setSelection(prefs.getBoolean(ResourceBundlePreferences.NEW_LINE_NICE));
    new Label(field, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.newline.nice"));
    // How should new lines appear in properties file
    field = createFieldComposite(formatGroup);
    newLineTypeForce = new Button(field, SWT.CHECK);
    newLineTypeForce.setSelection(prefs.getBoolean(ResourceBundlePreferences.FORCE_NEW_LINE_TYPE));
    newLineTypeForce.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        refreshEnabledStatuses();
      }
    });
    Composite newLineRadioGroup = new Composite(field, SWT.NONE);
    new Label(newLineRadioGroup, SWT.NONE).setText(JacobDesigner.getResourceString("prefs.newline.force"));
    newLineRadioGroup.setLayout(new RowLayout());
    newLineTypes[ResourceBundlePreferences.NEW_LINE_UNIX] = new Button(newLineRadioGroup, SWT.RADIO);
    newLineTypes[ResourceBundlePreferences.NEW_LINE_UNIX].setText("UNIX (\\n)");
    newLineTypes[ResourceBundlePreferences.NEW_LINE_WIN] = new Button(newLineRadioGroup, SWT.RADIO);
    newLineTypes[ResourceBundlePreferences.NEW_LINE_WIN].setText("Windows (\\r\\n)");
    newLineTypes[ResourceBundlePreferences.NEW_LINE_MAC] = new Button(newLineRadioGroup, SWT.RADIO);
    newLineTypes[ResourceBundlePreferences.NEW_LINE_MAC].setText("Mac (\\r)");
    newLineTypes[prefs.getInt(ResourceBundlePreferences.NEW_LINE_TYPE)].setSelection(true);
    refreshEnabledStatuses();
    return composite;
  }

  /**
   * @see org.eclipse.ui.IWorkbenchPreferencePage
   *      #init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench)
  {
    setPreferenceStore(JacobDesigner.getPlugin().getPreferenceStore());
  }

  /**
   * @see org.eclipse.jface.preference.IPreferencePage#performOk()
   */
  public boolean performOk()
  {
    IPreferenceStore prefs = getPreferenceStore();
    prefs.setValue(ResourceBundlePreferences.KEY_GROUP_SEPARATOR, keyGroupSeparator.getText());
    prefs.setValue(ResourceBundlePreferences.CONVERT_ENCODED_TO_UNICODE, convertEncodedToUnicode.getSelection());
    prefs.setValue(ResourceBundlePreferences.CONVERT_UNICODE_TO_ENCODED, convertUnicodeToEncoded.getSelection());
    prefs.setValue(ResourceBundlePreferences.SHOW_GENERATOR, showGeneratedBy.getSelection());
    prefs.setValue(ResourceBundlePreferences.SUPPORT_NL, supportNL.getSelection());
    prefs.setValue(ResourceBundlePreferences.ALIGN_EQUAL_SIGNS, alignEqualSigns.getSelection());
    prefs.setValue(ResourceBundlePreferences.GROUP_KEYS, groupKeys.getSelection());
    prefs.setValue(ResourceBundlePreferences.GROUP_LEVEL_DEEP, groupLevelDeep.getText());
    prefs.setValue(ResourceBundlePreferences.GROUP_LINE_BREAKS, groupLineBreaks.getText());
    prefs.setValue(ResourceBundlePreferences.GROUP_ALIGN_EQUAL_SIGNS, groupAlignEqualSigns.getSelection());
    prefs.setValue(ResourceBundlePreferences.WRAP_LINES, wrapLines.getSelection());
    prefs.setValue(ResourceBundlePreferences.WRAP_CHAR_LIMIT, wrapCharLimit.getText());
    prefs.setValue(ResourceBundlePreferences.WRAP_ALIGN_EQUAL_SIGNS, wrapAlignEqualSigns.getSelection());
    prefs.setValue(ResourceBundlePreferences.WRAP_INDENT_SPACES, wrapIndentSpaces.getText());
    prefs.setValue(ResourceBundlePreferences.NEW_LINE_NICE, wrapNewLine.getSelection());
    prefs.setValue(ResourceBundlePreferences.FORCE_NEW_LINE_TYPE, newLineTypeForce.getSelection());
    for (int i = 0; i < newLineTypes.length; i++)
    {
      if (newLineTypes[i].getSelection())
      {
        prefs.setValue(ResourceBundlePreferences.NEW_LINE_TYPE, i);
      }
    }
    refreshEnabledStatuses();
    return super.performOk();
  }

  /**
   * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
   */
  protected void performDefaults()
  {
    IPreferenceStore prefs = getPreferenceStore();
    keyGroupSeparator.setText(prefs.getDefaultString(ResourceBundlePreferences.KEY_GROUP_SEPARATOR));
    convertEncodedToUnicode.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.CONVERT_ENCODED_TO_UNICODE));
    convertUnicodeToEncoded.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.CONVERT_UNICODE_TO_ENCODED));
    showGeneratedBy.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.SHOW_GENERATOR));
    supportNL.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.SUPPORT_NL));
    alignEqualSigns.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.ALIGN_EQUAL_SIGNS));
    groupKeys.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.GROUP_KEYS));
    groupLevelDeep.setText(prefs.getDefaultString(ResourceBundlePreferences.GROUP_LEVEL_DEEP));
    groupLineBreaks.setText(prefs.getDefaultString(ResourceBundlePreferences.GROUP_LINE_BREAKS));
    groupAlignEqualSigns.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.GROUP_ALIGN_EQUAL_SIGNS));
    wrapLines.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.WRAP_LINES));
    wrapCharLimit.setText(prefs.getDefaultString(ResourceBundlePreferences.WRAP_CHAR_LIMIT));
    wrapAlignEqualSigns.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.WRAP_ALIGN_EQUAL_SIGNS));
    wrapIndentSpaces.setText(prefs.getDefaultString(ResourceBundlePreferences.WRAP_INDENT_SPACES));
    wrapNewLine.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.NEW_LINE_NICE));
    newLineTypeForce.setSelection(prefs.getDefaultBoolean(ResourceBundlePreferences.FORCE_NEW_LINE_TYPE));
    newLineTypes[prefs.getDefaultInt(ResourceBundlePreferences.NEW_LINE_TYPE)].setSelection(true);
    refreshEnabledStatuses();
    super.performDefaults();
  }

  private Composite createFieldComposite(Composite parent)
  {
    return createFieldComposite(parent, 0);
  }

  private Composite createFieldComposite(Composite parent, int indent)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout gridLayout = new GridLayout(2, false);
    gridLayout.marginWidth = indent;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 0;
    composite.setLayout(gridLayout);
    return composite;
  }

  private void refreshEnabledStatuses()
  {
    boolean isGroupKeyEnabled = groupKeys.getSelection();
    boolean isAlignEqualsEnabled = alignEqualSigns.getSelection();
    boolean isWrapEnabled = wrapLines.getSelection();
    boolean isWrapAlignEqualsEnabled = wrapAlignEqualSigns.getSelection();
    boolean isNewLineStyleForced = newLineTypeForce.getSelection();
    groupLevelDeep.setEnabled(isGroupKeyEnabled);
    groupLineBreaks.setEnabled(isGroupKeyEnabled);
    groupAlignEqualSigns.setEnabled(isGroupKeyEnabled && alignEqualSigns.getSelection());
    wrapCharLimit.setEnabled(isWrapEnabled);
    wrapAlignEqualSigns.setEnabled(isWrapEnabled);
    wrapIndentSpaces.setEnabled(isWrapEnabled && !isWrapAlignEqualsEnabled);
    for (int i = 0; i < newLineTypes.length; i++)
    {
      newLineTypes[i].setEnabled(isNewLineStyleForced);
    }
  }
  private class IntTextValidatorKeyListener extends KeyAdapter
  {
    private String errMsg = null;

    /**
     * Constructor.
     * 
     * @param errMsg
     *          error message
     */
    public IntTextValidatorKeyListener(String errMsg)
    {
      super();
      this.errMsg = errMsg;
    }

    /**
     * @see org.eclipse.swt.events.KeyAdapter#keyPressed(
     *      org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent event)
    {
      Text text = (Text) event.widget;
      String value = text.getText();
      event.doit = value.matches("^\\d*$");
      if (event.doit)
      {
        errors.remove(text);
        if (errors.isEmpty())
        {
          setErrorMessage(null);
          setValid(true);
        }
        else
        {
          setErrorMessage((String) errors.values().iterator().next());
        }
      }
      else
      {
        errors.put(text, errMsg);
        setErrorMessage(errMsg);
        setValid(false);
      }
    }
  }

  private void setWidthInChars(Control field, int widthInChars)
  {
    GridData gd = new GridData();
    gd.widthHint = UIUtils.getWidthInChars(field, widthInChars);
    field.setLayoutData(gd);
  }
}