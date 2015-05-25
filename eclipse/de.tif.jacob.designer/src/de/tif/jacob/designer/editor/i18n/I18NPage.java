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
package de.tif.jacob.designer.editor.i18n;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.util.UIUtils;
import de.tif.jacob.util.StringUtil;
/**
 * Internationalization page where one can edit all resource bundle entries at
 * once for all supported locales.
 */
public class I18NPage extends ScrolledComposite
{
//  private static final int TEXT_MINIMUM_HEIGHT = 50;
  private JacobModel  jacobModel;
  private I18NKeyTree keyTree;
  private Font boldFont;
  private Text activeTextBox;
  private BidiMap model2TextBoxes = new DualHashBidiMap();
  private boolean lockRefresh = false;
  /**
   * Constructor.
   * 
   * @param parent
   *          parent component.
   * @param style
   *          style to apply to this component
   */
  public I18NPage(Composite parent, int style, JacobModel jacobModel)
  {
    super(parent, style);
    this.jacobModel = jacobModel;
    boldFont = UIUtils.createFont(this, SWT.BOLD, 0);
    // Create screen
    SashForm sashForm = new SashForm(this, SWT.NONE);
    setContent(sashForm);
    setExpandHorizontal(true);
    setExpandVertical(true);
    setMinWidth(400);
//    setMinHeight(bundles.count() * TEXT_MINIMUM_HEIGHT);
    keyTree = new I18NKeyTree(this, sashForm, jacobModel);
    
    createSashRightSide(sashForm);
    sashForm.setWeights(new int[] { 25, 75 });
  }

  /**
   * Creates right side of main sash form.
   * 
   * @param sashForm
   *          parent sash form
   */
  private void createSashRightSide(SashForm sashForm)
  {
    Composite rightComposite = new Composite(sashForm, SWT.BORDER);
    rightComposite.setLayout(new GridLayout(1, false));
    List models = jacobModel.getI18NResourceModels();
    for (int i = 0; i < models.size(); i++)
    {
      I18NResourceModel model =(I18NResourceModel) models.get(i);
      // Label row
      Composite labelComposite = new Composite(rightComposite, SWT.NONE);
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = 2;
      gridLayout.horizontalSpacing = 0;
      gridLayout.verticalSpacing = 0;
      gridLayout.marginWidth = 0;
      gridLayout.marginHeight = 0;
      labelComposite.setLayout(gridLayout);
      labelComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      Label txtLabel = new Label(labelComposite, SWT.NONE);
      txtLabel.setText(model.getLabel() + ":");
      txtLabel.setFont(boldFont);
      Image image = loadCountryIcon(model.getLocale());
      Label imgLabel = new Label(labelComposite, SWT.NONE);
      GridData gridData = new GridData();
      gridData.horizontalAlignment = GridData.END;
      gridData.grabExcessHorizontalSpace = true;
      imgLabel.setLayoutData(gridData);
      imgLabel.setImage(image);
      // Textbox row
      Text textBox = new Text(rightComposite, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
      model2TextBoxes.put(model, textBox);
      gridData = new GridData();
      gridData.verticalAlignment = GridData.FILL;
      gridData.grabExcessVerticalSpace = true;
      gridData.horizontalAlignment = GridData.FILL;
      gridData.grabExcessHorizontalSpace = true;
      textBox.setLayoutData(gridData);
      textBox.addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent event)
        {
          activeTextBox = (Text) event.widget;
        }

        public void focusLost(FocusEvent event)
        {
          activeTextBox = null;
        }
      });
      textBox.addKeyListener(new KeyAdapter()
      {
        
        public void keyPressed(KeyEvent event) 
        {
          if(event.character==SWT.TAB)
          {
            event.doit=false;
            I18NResourceModel model = (I18NResourceModel)model2TextBoxes.getKey(event.getSource());
            List models =  model.getJacobModel().getI18NResourceModels();
            int index = model.getJacobModel().getI18NResourceModels().indexOf(model);
            if((index+1)==models.size())
              index=0;
            else
              index++;
            Text newFocusText = (Text)model2TextBoxes.get(models.get(index));
            newFocusText.forceFocus();
          }
        }
        
        public void keyReleased(KeyEvent event)
        {
          // >revers< lookup from the textbox to the model
          lockRefresh=true;
          try
          {
	          I18NResourceModel model = (I18NResourceModel)model2TextBoxes.getKey(event.getSource());
	          model.setValue(keyTree.getSelectedKey(), ((Text)event.getSource()).getText());
	          keyTree.refreshBranchIcons(keyTree.getSelectedKey());
          }
          finally
          {
	          lockRefresh=false;
          }
        }
      });
    }
     
  }


  /**
   * Refreshes all fields and data linked to this page. This includes resource
   * bundle data, text boxes, and key tree.
   */
  public void refresh()
  {
    if(lockRefresh==true)
      return;
    
    keyTree.refresh(keyTree.getSelectedKey());
  }

  public void select(String key)
  {
    keyTree.select(key);
    selectionChanged(key);
    setFocusToDefault();
  }
  
  public void selectionChanged(String key)
  {
    boolean set=false;
    for (I18NResourceModel model : jacobModel.getI18NResourceModels())
    {
      Text inputBox = (Text)model2TextBoxes.get(model);
      inputBox.setText(StringUtil.toSaveString(model.getValue(key)));
      if(set==false)
      {
        set=true;
        inputBox.setFocus();
        inputBox.setSelection(inputBox.getText().length());
      }
    }
  }
  

  protected void setFocusToDefault()
  {
    I18NResourceModel model = (I18NResourceModel)jacobModel.getI18NResourceModels().get(0);
    if(model!=null)
    {
      Text text =(Text) model2TextBoxes.get(model);
      text.setFocus();
    }
  }


  /**
   * Loads country icon based on locale country.
   * 
   * @param locale
   *          the locale on which to grab the country
   * @return an image, or <code>null</code> if no match could be made
   */
  private Image loadCountryIcon(Locale locale)
  {
    Image flag=null;
    if (locale != null && locale.getCountry() != null)
    {
      String countryCode = locale.getCountry().toLowerCase();
      if(countryCode.length()>0)
        flag = JacobDesigner.getImage("countries/" + countryCode + ".png");
    }
    if(flag==null && locale!=null)
      flag=  JacobDesigner.getImage("countries/" + locale.getLanguage().toLowerCase() + ".png");
    
    return flag;
  }
}
