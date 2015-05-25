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
 * Created on 22.11.2004
 *
 */
package de.tif.jacob.designer.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public interface UIDBLocalInputFieldModel extends IPropertySource
{
	public JacobModel getJacobModel();
	public UIGroupModel getGroupModel();
	public FieldModel getFieldModel();
	public void       setField(String fieldName);
	public void       setField(int index);
	public String      getName();
	public String      getCaption();
	public String      getDefaultCaption();
	public void       setCaption(String caption) throws Exception;
	public String      getDefaultName();
	public void       setName(String name) throws Exception;
	public void       setLocation(Point p);
	public void       setSize(Dimension size);
  public Rectangle   getConstraint();
  public Rectangle getDefaultCaptionConstraint(Rectangle parentContraint);  
	public Dimension   getSize();
	public void       setGroup(UIGroupModel group);
	public String     getError();
  public CastorGuiElement getCastor();
	public String     getDefaultDbType();
  
	static class  FieldHelper
	{
	  public static void setField(UIDBLocalInputFieldModel obj, int index)
	  {
	    if(index==-1)
		    setField(obj,obj.getGroupModel().getTableAliasModel().getTableModel().NULL_FIELD.getName());
	    else
	      setField(obj,(String)obj.getJacobModel().getTableAliasModel(obj.getGroupModel().getTableAlias()).getFieldNames(obj).get(index));
	    
	  }
	  
	  public static void setField(UIDBLocalInputFieldModel obj, String fieldName)
	  {
		  String save = obj.getFieldModel().getName();

		  if(StringUtil.saveEquals(save,fieldName))
		    return;
		  
		  boolean fitCaption = false;
		  boolean fitName    = false;
		 
		  // TODO: kleiner hack
		  //
		  if(!(obj instanceof UIGroupElementModel))
		    return;
		  
		  UIGroupElementModel groupElement = (UIGroupElementModel)obj;
		  
			// Caption anpassen falls diese nicht ge�ndert worden ist - den
			// Defaultwert noch enth�lt.
			//
			if(obj.getCaption().equals(obj.getDefaultCaption()))
			  fitCaption=true;

			// Falls der Name das Muster aliasnameFieldname01, aliasnameFieldname02, ,,,, hat, wird der Name
			// automatisch angepasst
			//
			if(groupElement.getName().startsWith(groupElement.getDefaultName()))
			  fitName=true;
		  
			groupElement.getCastor().getCastorGuiElementChoice().getLocalInputField().setTableField(fieldName);
		  
			groupElement.firePropertyChange(ObjectModel.PROPERTY_FIELD_CHANGED, save, fieldName);
			
			try
	    {
				if(fitCaption)
				  obj.setCaption(obj.getDefaultCaption());
				
	      if(fitName)
	        groupElement.setName(groupElement.getDefaultName());
	    }
	    catch (Exception e)
	    {
	      // falls ein package nicht umbenannt werden kann, kann der Name nicht angepasst werden
	      //....ignore!
	    }	    
	  }
	  
	  protected static void renameFieldReference(UIDBLocalInputFieldModel obj,FieldModel field, String fromName, String toName)
	  {
	    CastorGuiElement element = obj.getCastor();
      LocalInputField local = element.getCastorGuiElementChoice().getLocalInputField();
      // Wenn die Gruppe auf den selben alias zeigt UND das Feld ein
      // Datenbankfeld ist UND es sich um das gleiche Feld handelt
      // wird dieses ohnen auslösen von Events umbenannt.
      //
      // Anmerkung: obj.getFieldModel() kann man nicht mehr aufrufen, da das Feld bereits umbenannt ist
      //            und dieses jetzt nicht mehr gefunden wird. Ich gehe bei dem LocalInputField dann über die Gruppe um die
      //            Tabelle zu holen. Ein LocalInptField bezieht sich immer auf den selben Alias wie die Gruppe
      if (field.getTableModel() == obj.getGroupModel().getTableAliasModel().getTableModel() && local.getTableField() != null && local.getTableField().equals(fromName))
        local.setTableField(toName);
	  }	
	}

}
