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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.util.StringUtil;

/**
 * Tree for displaying and navigating through resource bundle keys.
 * 
 */
public class I18NKeyTree extends Composite
{
  private static final Image IMAGE_KEY       = JacobDesigner.getImage("i18nKey.png");
  private static final Image IMAGE_KEY_WARN  = JacobDesigner.getImage("i18nKeyWarn.png");
  private static final Image IMAGE_KEY_UNUSED= JacobDesigner.getImage("i18nKeyUnused.png");
  
  private static final Image IMAGE_TREEVIEW   = JacobDesigner.getImage("hierarchicalLayout.gif");
  private static final Image IMAGE_FLATVIEW   = JacobDesigner.getImage("flatLayout.gif");
  
  private Font groupFont;

  private Tree keyTree;
  /** All tree items, keyed by key or group key name. */
  private Map key2treeItem = new HashMap();

//  private boolean keyTreeHierarchical;
  private Text addTextBox;
  private final JacobModel jacobModel;
  private final I18NPage   parent;

  /**
   * Constructor.
   * 
   * @param parent
   *          parent composite
   * @param bundles
   *          all bundles
   */
  public I18NKeyTree(I18NPage parent, Composite comp, JacobModel jacobModel)
  {
    super(comp, SWT.BORDER);
    this.parent = parent;
    this.jacobModel = jacobModel;

    FontData[] fontData = getFont().getFontData();
    for (int i = 0; i < fontData.length; i++)
    {
      fontData[i].setStyle(SWT.BOLD);
    }
    groupFont = new Font(getDisplay(), fontData);
    setLayout(new GridLayout(1, false));
    createTopSection();
    createMiddleSection();
    createBottomSection();
  }

 
  private void createTopSection()
  {
    Composite topComposite = new Composite(this, SWT.NONE);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    gridLayout.horizontalSpacing = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    topComposite.setLayout(gridLayout);
    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.END;
    gridData.verticalAlignment = GridData.CENTER;
    gridData.grabExcessHorizontalSpace = true;
    topComposite.setLayoutData(gridData);

    final Button hierModeButton = new Button(topComposite, SWT.TOGGLE);
    hierModeButton.setImage(IMAGE_TREEVIEW);
    
    final Button flatModeButton = new Button(topComposite, SWT.TOGGLE);
    flatModeButton.setImage(IMAGE_FLATVIEW);
    
    if (jacobModel.getShowHirachical())
    {
      hierModeButton.setSelection(true);
      hierModeButton.setEnabled(false);
    }
    else
    {
      flatModeButton.setSelection(true);
      flatModeButton.setEnabled(false);
    }
    hierModeButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        if (hierModeButton.getSelection())
        {
          flatModeButton.setSelection(false);
          flatModeButton.setEnabled(true);
          hierModeButton.setEnabled(false);
          setKeyTreeHierarchical(true);
        }
      }
    });
    flatModeButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        if (flatModeButton.getSelection())
        {
          hierModeButton.setSelection(false);
          hierModeButton.setEnabled(true);
          flatModeButton.setEnabled(false);
          setKeyTreeHierarchical(false);
        }
      }
    });
  }

  private void createMiddleSection()
  {
    keyTree = new Tree(this, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    refresh();
    GridData gridData = new GridData();
    gridData.verticalAlignment = GridData.FILL;
    gridData.grabExcessVerticalSpace = true;
    gridData.horizontalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    keyTree.setLayoutData(gridData);
    keyTree.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        addTextBox.setText(StringUtil.toSaveString(getSelectedKey()));
        parent.selectionChanged(getSelectedKey());
      }
    });
    keyTree.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent event)
      {
        if (event.character == SWT.DEL)
        {
          deleteKeyOrGroup();
        }
      }
    });
    keyTree.addMouseListener(new MouseAdapter()
    {
      public void mouseDoubleClick(MouseEvent event)
      {
        renameKeyOrGroup();
      }
    });
    
    // Add popup menu
    Menu menu = new Menu(this);
    MenuItem renameItem = new MenuItem(menu, SWT.PUSH);
    renameItem.setText(JacobDesigner.getResourceString("key.rename"));
    renameItem.setImage(JacobDesigner.getImage("rename.png"));
    renameItem.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        renameKeyOrGroup();
      }
    });

    MenuItem deleteItem = new MenuItem(menu, SWT.PUSH);
    deleteItem.setText(JacobDesigner.getResourceString("key.delete"));
    deleteItem.setImage(JacobDesigner.getImage("delete.png"));
    deleteItem.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        deleteKeyOrGroup();
      }
    });

    new MenuItem(menu,SWT.SEPARATOR);
    
    MenuItem importItem = new MenuItem(menu, SWT.PUSH);
    importItem.setText(JacobDesigner.getResourceString("key.import"));
    importItem.setImage(JacobDesigner.getImage("import.png"));
    importItem.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
	        IRunnableWithProgress operation = new IRunnableWithProgress()
	        {
	          public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
	          {
	            jacobModel.createMissingI18NKey();
	          }
	        };
	        new ProgressMonitorDialog(null).run(false, false, operation);
          refresh();
        }
        catch(Exception exc)
        {
          JacobDesigner.showException(exc);
        }
      }
    });

    MenuItem refreshItem = new MenuItem(menu, SWT.PUSH);
    refreshItem.setText(JacobDesigner.getResourceString("key.refresh"));
    refreshItem.setImage(JacobDesigner.getImage("refresh_nav.png"));
    refreshItem.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        refresh();
      }
    });

    MenuItem deleteUnusedItem = new MenuItem(menu, SWT.PUSH);
    deleteUnusedItem.setText(JacobDesigner.getResourceString("key.deleteunused"));
    deleteUnusedItem.setImage(JacobDesigner.getImage("delete.png"));
    deleteUnusedItem.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
	        IRunnableWithProgress operation = new IRunnableWithProgress()
	        {
	          public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
	          {
	            Iterator iter = jacobModel.getI18NKeys().iterator();
	            while(iter.hasNext())
	            {
	              String key = (String)iter.next();
	              if(!jacobModel.isI18NKeyInUse(key))
	                jacobModel.removeI18N(key);
	            }
	          }
	        };
	        new ProgressMonitorDialog(null).run(false, false, operation);
        }
        catch(Exception exc)
        {
          JacobDesigner.showException(exc);
        }
        refresh();
      }
    });

    keyTree.setMenu(menu);
  }

  private void createBottomSection()
  {
    Composite bottomComposite = new Composite(this, SWT.NONE);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    gridLayout.horizontalSpacing = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    bottomComposite.setLayout(gridLayout);
    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.verticalAlignment = GridData.CENTER;
    gridData.grabExcessHorizontalSpace = true;
    bottomComposite.setLayoutData(gridData);
    // Text box
    addTextBox = new Text(bottomComposite, SWT.BORDER);
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = GridData.FILL;
    addTextBox.setLayoutData(gridData);
    addTextBox.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent event)
      {
        if (event.character == SWT.CR)
        {
          addPropertyKey();
        }
      }
    });
    // Add button
    Button addButton = new Button(bottomComposite, SWT.PUSH);
    addButton.setText(JacobDesigner.getResourceString("key.add"));
    addButton.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent event)
      {
        addPropertyKey();
        parent.selectionChanged(getSelectedKey());
        parent.setFocusToDefault();
      }
    });
  }

  public TreeItem getSelectedItem()
  {
    TreeItem item = null;
    if (keyTree.getSelection().length > 0)
    {
      item = keyTree.getSelection()[0];
    }
    return item;
  }

  public String getSelectedKey()
  {
    String key = null;
    TreeItem item = getSelectedItem();
    if (item != null)
    {
      key = (String) item.getData();
    }
    return key;
  }


  public void select(String selectedKey)
  {
    if (selectedKey != null)
    {
      keyTree.setSelection(new TreeItem[] { (TreeItem) key2treeItem.get(selectedKey) });
      keyTree.showSelection();
    }
  }

  public void refresh()
  {
    refresh(null);
  }

  public void refresh(String selectedKey)
  {
    key2treeItem.clear();
    keyTree.removeAll();
    for (Iterator iter = jacobModel.getI18NKeys().iterator(); iter.hasNext();)
    {
      String key = (String) iter.next();
      addGroupKeyItem(key);
    }
    if (selectedKey != null)
    {
      keyTree.setSelection(new TreeItem[] { (TreeItem) key2treeItem.get(selectedKey) });
      keyTree.showSelection();
    }
  }

  private void addGroupKeyItem(String key)
  {
    String   sep              = jacobModel.getSeparator();
    String   escapedSeparator = getEscapedKeyGroupSeparator();
    String[] groups           = key.split(escapedSeparator);
    
    TreeItem treeItem = null;
    StringBuffer group = new StringBuffer();
    // Alle Gruppenelemente fï¿½r diesen einen Key erzeugen falls diese noch nicht vorhanden sind
    //
    for (int i = 0; i < groups.length - 1; i++)
    {
      if (i > 0)
        group.append(sep);

      group.append(groups[i]);
      String groupKey = group.toString();
      TreeItem groupItem = (TreeItem) key2treeItem.get(groupKey);
      // Create new group
      if (groupItem == null)
      {
        // den richtigen Vater bestimmen
        if (treeItem == null)
          groupItem = new TreeItem(keyTree, SWT.NONE);
        else
          groupItem = new TreeItem(treeItem, SWT.NONE);
        
        if(isValueMissing(groupKey))
        {
          groupItem.setImage(IMAGE_KEY_WARN);
          groupItem.setForeground(ColorConstants.red);
        }
        else
        {
          groupItem.setImage(IMAGE_KEY);
        }
        
        groupItem.setText(groups[i]);
        groupItem.setFont(groupFont);
        groupItem.setData(groupKey);
        key2treeItem.put(groupKey, groupItem);
      }
      treeItem = groupItem;
    }
    
    // Das 'Blatt' an den Baum hï¿½ngen
    //
    String keyLeaf = groups[groups.length - 1];
    if (treeItem == null)
      treeItem = new TreeItem(keyTree, SWT.NONE);
    else
      treeItem = new TreeItem(treeItem, SWT.NONE);

    treeItem.setText(keyLeaf);
    treeItem.setData(key);
    if(!jacobModel.isI18NKeyInUse(key))
    {
      treeItem.setImage(IMAGE_KEY_UNUSED);
      treeItem.setForeground(ColorConstants.gray);
    }
    else if (isValueMissing(key))
    {
      treeItem.setImage(IMAGE_KEY_WARN);
      treeItem.setForeground(ColorConstants.red);
    }
    else
      treeItem.setImage(IMAGE_KEY);

    if (group.length() > 0)
      group.append(sep);
    group.append(keyLeaf);
    key2treeItem.put(group.toString(), treeItem);
  }

  /**
   * Gets all keys under a group item. This includes all descendants, not just
   * direct ones.
   * 
   * @param groupItem
   *          item under which to get all keys
   * @return all keys under given group item
   */
  public String[] getAllKeysInGroup(TreeItem groupItem)
  {
    Collection allItems = new ArrayList();
    allItems.add(groupItem);
    findAllItemsInGroup(allItems, groupItem);
    Collection keys = new ArrayList();
    for (Iterator iter = allItems.iterator(); iter.hasNext();)
    {
      TreeItem item = (TreeItem) iter.next();
      if (item.getData() != null)
      {
        keys.add( item.getData());
      }
    }
    return (String[]) keys.toArray(new String[] {});
  }

  /**
   * Store all intems in given group, in given collection.
   * 
   * @param treeItems
   *          all tree items found under group, plus initial content
   * @param groupItem
   *          item under which to get children
   */
  private void findAllItemsInGroup(Collection treeItems, TreeItem groupItem)
  {
    TreeItem[] items = groupItem.getItems();
    treeItems.addAll(Arrays.asList(items));
    for (int i = 0; i < items.length; i++)
    {
      findAllItemsInGroup(treeItems, items[i]);
    }
  }

  /**
   * Refresh all icons associated with the branch the given key is in.
   * 
   * @param key
   */
  private boolean isValueMissing(String groupOrValueKey)
  {
    return false;
    /*
    int testCountLeaf = 0;
    int hitCountLeaf  = 0;
    String groubPrefix = groupOrValueKey+jacobModel.getSeparator();
    Iterator iter = jacobModel.getI18NResourceModels().iterator();
    while (iter.hasNext())
    {
      I18NResourceModel model = (I18NResourceModel) iter.next();
      Iterator keyIter = model.getKeys().iterator();
      while (keyIter.hasNext())
      {
        String key = (String) keyIter.next();
        // es wurde ein Blatt gefunden
        //
        if(key.equals(groupOrValueKey))
        {
	        String value = model.getValue(key);
	        testCountLeaf++;
	        if (value!=null && value.length()>0)
	          hitCountLeaf++;
	        break;
        }
        else if(key.startsWith(groubPrefix))
        {
          if(isValueMissing(key))
          {
            return true;
          }
        }
      }
    }
    return (testCountLeaf != hitCountLeaf);
    */
  }

  /**
   * Renames a key or group of key.
   */
  private void renameKeyOrGroup()
  {
    final String key = getSelectedKey();
    TreeItem item = getSelectedItem();
    if (item.getItemCount() == 0)
    {
      // Rename single item
      WarningInputDialog dialog = new WarningInputDialog(getShell(), "Rename key", "Rename \"" + key + "\" to:", key, new IInputValidator()
      {
        public String isValid(String newText)
        {
          if(!key.equals(newText) && jacobModel.hasI18NKey(newText))
            return "Key already exists and will be overriden.";
          return null;
        }
      });
      
      dialog.open();
      if (dialog.getReturnCode() == Window.OK)
      {
        String newKey = dialog.getValue().toUpperCase();
        if(!newKey.equals(key))
        {
          jacobModel.renameI18NKey(key, newKey);
          refresh(newKey);
        }
      }
    }
    else
    {
      // Rename all keys in group
      String path = (String)item.getData();
      InputDialog dialog = new InputDialog(getShell(), "Rename key group", "Rename key group \"" + path + "\" to (all nested keys wll be renamed):", path, null);
      dialog.open();
      if (dialog.getReturnCode() == Window.OK)
      {
        String newGroup = dialog.getValue().toUpperCase();
        String[] keys = getAllKeysInGroup(getSelectedItem());
        for (int i = 0; i < keys.length; i++)
        {
          jacobModel.renameI18NKey(keys[i], keys[i].replaceFirst("^" + path, newGroup));
        }
        refresh(newGroup);
      }
    }
  }

  /**
   * Deletes a key or group of key.
   */
  private void deleteKeyOrGroup()
  {
    String key = getSelectedKey();
    TreeItem item = getSelectedItem();
    MessageBox msgBox = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
    if (item.getItemCount() == 0)
    {
      // Delete single key
      msgBox.setMessage("Are you sure you want to " + "delete \"" + key + "\"?");
      msgBox.setText("Delete key?");
      if (msgBox.open() == SWT.OK)
      {
        jacobModel.removeI18N(key);
        TreeItem node= (TreeItem)key2treeItem.get(key);
        node.dispose();
        key2treeItem.remove(key);
        keyTree.redraw();
      }
    }
    else
    {
      // Delete group
      msgBox.setMessage("Are you sure you want to " + "delete all keys under \"" + item.getText() + "\"?");
      msgBox.setText("Delete keys?");
      if (msgBox.open() == SWT.OK)
      {
        String[] keys = getAllKeysInGroup(getSelectedItem());
        for (int i = 0; i < keys.length; i++)
        {
          jacobModel.removeI18N(keys[i]);
          TreeItem node= (TreeItem)key2treeItem.get(keys[i]);
          node.dispose();
          key2treeItem.remove(keys[i]);
          keyTree.redraw();
        }
        refresh();
      }
    }
  }


  /**
   * Sets the "keyTreeHierarchical" attribute.
   * 
   * @param keyTreeHierarchical
   *          The keyTreeHierarchical to set.
   */
  public void setKeyTreeHierarchical(boolean keyTreeHierarchical)
  {
    jacobModel.setShowHirachical(keyTreeHierarchical);
    refresh();
    if (keyTree.getItemCount() > 0)
    {
      keyTree.setSelection(new TreeItem[] { keyTree.getItems()[0] });
    }
  }

  /**
   * Gets an escaped key group separator if we are creating groups,
   * <code>null</code> otherwise (flat view).
   * 
   * @return group separator
   */
  private String getEscapedKeyGroupSeparator()
  {
    if (jacobModel.getShowHirachical())
      return "\\Q" + jacobModel.getSeparator() + "\\E";
    else
      return "="; // escape on something we know won't be in a key
  }

  /**
   * Adds a property key to resource bundle, based on content of bottom "add"
   * text box.
   */
  private void addPropertyKey()
  {
    String key = addTextBox.getText();
    if (key != null)
    {
      	key = key.toUpperCase();
      	if(key.startsWith("%"))
      	  key = key.substring(1);
      	addTextBox.setText(key);
      	if(jacobModel.hasI18NKey(key)==false)
      	{
      	  jacobModel.addI18N(key,"");
      	  addGroupKeyItem(key);
      	}
        else
        {
          MessageDialog.openInformation(null,"I18N Editor","Entry ["+key+"] already exists");
        }
        select(key);
        parent.selectionChanged(key);
    }
  }
  
  /**
   * Alle Icons von dem übergebenen Knoten bis zum Elternknoten aktualisieren.
   * @param key the key for which to refrench the branch
   */
  protected void refreshBranchIcons(String mainKey) 
  {
//    System.out.println("refresh item:"+mainKey);
    TreeItem item = (TreeItem) key2treeItem.get(mainKey);
    
    while(item!=null) 
    {
      String key= (String)item.getData();
//      System.out.println("...checking:"+key);
      if (isValueMissing(key)) 
      {
        item.setImage(IMAGE_KEY_WARN);
        item.setForeground(ColorConstants.red);
      }
      else
      {
        item.setImage(IMAGE_KEY);
        item.setForeground(ColorConstants.black);
      }
      item = item.getParentItem();
    }

  }
}
