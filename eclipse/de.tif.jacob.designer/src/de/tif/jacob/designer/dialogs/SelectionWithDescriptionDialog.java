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
/*
 * Created on 09.06.2005
 *
 */
package de.tif.jacob.designer.dialogs;


import java.util.Arrays;
import java.util.List;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;

/**
 * A list selection dialog with two panes. Duplicated entries will be folded
 * together and are displayed in the lower pane (qualifier).
 * 
 * @since 2.0
 */
public class SelectionWithDescriptionDialog extends AbstractElementListSelectionDialog 
{
    private String fUpperListLabel;
    private String fLowerListLabel;

    private ILabelProvider fQualifierRenderer;

    private Object[] fElements = new Object[0];

    StyledText text;

    /**
     * Creates the two pane element selector.
     * 
     * @param parent
     *            the parent shell.
     * @param elementRenderer
     *            the element renderer.
     * @param qualifierRenderer
     *            the qualifier renderer.
     */
    public SelectionWithDescriptionDialog(Shell parent, ILabelProvider elementRenderer, ILabelProvider qualifierRenderer) 
    {
        super(parent, elementRenderer);
        setSize(150, 10);
        setAllowDuplicates(false);
        fQualifierRenderer = qualifierRenderer;
    }

    /**
     * Sets the upper list label. If the label is <code>null</code> (default),
     * no label is created.
     * 
     * @param label
     */
    public void setUpperListLabel(String label) 
    {
        fUpperListLabel = label;
    }

    /**
     * Sets the lower list label.
     * 
     * @param label
     *            String or <code>null</code>. If the label is
     *            <code>null</code> (default), no label is created.
     */
    public void setLowerListLabel(String label) 
    {
        fLowerListLabel = label;
    }

    protected void computeResult() 
    {
      Object[] results = new Object[] { getSelectedElement() };
      setResult(Arrays.asList(results));
    }
    
    protected Object getSelectedElement() 
    {
	    int index = getSelectionIndex();
	    if (index >= 0)
	        return fElements[index];
	    return null;
    }
    
    /**
     * Sets the elements to be displayed.
     * 
     * @param elements
     *            the elements to be displayed.
     */
    public void setElements(Object[] elements) 
    {
        fElements = elements;
    }

    /*
     * @see Dialog#createDialogArea(Composite)
     */
    public Control createDialogArea(Composite parent) 
    {
        Composite contents = (Composite) super.createDialogArea(parent);
        createMessageArea(contents);
        createFilterText(contents);
        createLabel(contents, fUpperListLabel);
        createFilteredList(contents);
        createLabel(contents, fLowerListLabel);
        
        text = createDescription(contents);
        
        setListElements(fElements);
        List initialSelections = getInitialElementSelections();
        if (!initialSelections.isEmpty()) 
        {
            Object element = initialSelections.get(0);
            setSelection(new Object[] { element });
        }
        return contents;
    }

    /**
     * Creates a filtered list.
     * @param parent the parent composite.
     * @return returns the filtered list widget.
     */
    protected StyledText createDescription(Composite parent) 
    {
      	int flags = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
      	StyledText text = new StyledText (parent, flags);

        GridData data = new GridData();
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.heightHint = 150;
        data.widthHint = 250;
       
        text.setEditable(false);
        text.setLayoutData(data);
        text.setFont(parent.getFont());

        return text;
    }

    /**
     * Creates a label if name was not <code>null</code>.
     * 
     * @param parent
     *            the parent composite.
     * @param name
     *            the name of the label.
     * @return returns a label if a name was given, <code>null</code>
     *         otherwise.
     */
    protected Label createLabel(Composite parent, String name) 
    {
        if (name == null)
            return null;
        Label label = new Label(parent, SWT.NONE);
        label.setText(name);
        label.setFont(parent.getFont());
        return label;
    }



    /**
     * @see AbstractElementListSelectionDialog#handleSelectionChanged()
     */
    protected void handleSelectionChanged() 
    {
        int index = getSelectionIndex();
        if (index < 0)
            return;
        if (getSelectedElement() == null)
            updateLowerListWidget(new Object[] {});
        else
            updateLowerListWidget(getSelectedElement());
        validateCurrentSelection();
    }



    private void updateLowerListWidget(Object element) 
    {
      text.setText(this.fQualifierRenderer.getText(element));
    }
}