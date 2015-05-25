/*
 * Created on Sep 3, 2004
 *
 */
package de.tif.jacob.rule.editor.rules.dialogs.decision._boolean;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

public abstract class TreeObject 
{
    private String name; // Das Label welches von dem TreeElement angezeigt
                            // wird.
    private final TreeParent parent;
    final TreeViewer viewer;
    public abstract Image getImage();

    public TreeObject(TreeViewer viewer, TreeParent parent, String name) 
    {
        this.name = name;
        this.viewer = viewer;
        this.parent = parent;
    }

    public boolean sortingEnabled() 
    {
        return false;
    }

    public int getSortingCategory() 
    {
        return 0;
    }

    public String getName() 
    {
        return name;
    }

    public String getLabel() 
    {
        return getName();
    }

    public String getTooltip() 
    {
        return "Java code attached to table alias [" + getName() + "]";
    }

    public void setName(String name)
    {
    	this.name = name;
    }

    public TreeParent getParent() 
    {
        return parent;
    }

    public String toString() 
    {
        return getName();
    }
}