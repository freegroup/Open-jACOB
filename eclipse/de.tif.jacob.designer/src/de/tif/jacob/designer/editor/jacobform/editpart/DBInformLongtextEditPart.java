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
package de.tif.jacob.designer.editor.jacobform.editpart;


import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import de.tif.jacob.designer.editor.jacobform.figures.HeadingFigure;
import de.tif.jacob.designer.editor.jacobform.figures.InformLongTextFigure;
import de.tif.jacob.designer.model.IStyleProvider;
import de.tif.jacob.designer.model.UICaptionModel;
import de.tif.jacob.designer.model.UIHeadingModel;
import de.tif.jacob.designer.model.UIInformLongTextModel;
import de.tif.jacob.designer.model.UILabelModel;

public class DBInformLongtextEditPart extends DBLocalInputFieldEditPart implements IStyleProvider
{
  public DBInformLongtextEditPart()
  {
    super(InformLongTextFigure.class);
  }

  public void consumeStyle(Map<String, Object> style)
  {
    UIInformLongTextModel model = (UIInformLongTextModel)getModel();

    Integer width = (Integer)style.get("element.width");
    Integer height = (Integer)style.get("element.height");
    Integer x = (Integer)style.get("element.x");
    Integer y = (Integer)style.get("element.y");
    String type = (String)style.get("element.contenttype");
    
    if( x!=null && y !=null)
      model.setLocation(new Point(x,y));
    
    if(type!=null)
      model.setContentType(type);
    
    if(model.getCastorCaption()!=null)
    {
      UICaptionModel caption = model.getCaptionModel();

      Integer captionHeight = (Integer)style.get("caption.height");
      Integer captionWidth = (Integer)style.get("caption.width");
      Integer captionDiffX = (Integer)style.get("caption.diff.x");
      Integer captionDiffY = (Integer)style.get("caption.diff.y");
      String captionFontFamily = (String)style.get("caption.font.family");
      Integer captionSize = (Integer)style.get("caption.font.size");
      String captionFontStyle = (String)style.get("caption.font.style");
      String captionFontWeight = (String)style.get("caption.font.weight");
      Color captionFontColor = (Color)style.get("caption.font.color");
      String captionAlign = (String)style.get("caption.align");
      
      Rectangle rect = caption.getConstraint();
      if(captionHeight!=null)
        rect.height = captionHeight;
      if(captionWidth!=null)
        rect.width = captionWidth;
      if(captionDiffX!=null)
        rect.x = captionDiffX+model.getConstraint().x;
      if(captionDiffY!=null)
        rect.y = captionDiffY+model.getConstraint().y;
      caption.setConstraint(rect);
      
      if(captionFontFamily!=null)
        caption.setFontFamily(captionFontFamily);
      if(captionSize!=null)
        caption.setFontSize(captionSize);
      if(captionFontStyle!=null)
        caption.setFontStyle(captionFontStyle);
      if(captionFontWeight!=null)
        caption.setFontWeight(captionFontWeight);
      if(captionFontColor!=null)
        caption.setColor(captionFontColor);
      if(captionAlign!=null)
        caption.setAlign(captionAlign);
    }

    if(height!=null|| width!=null)
    {
      Rectangle rect = model.getConstraint();
      
      if(height!=null)
        rect.height = height;
      
      if(width!=null)
        rect.width = width;
      
      model.setConstraint(rect);
    }
  }

  public void provideStyle(Map<String, Object> style, boolean withLocation)
  {
    UIInformLongTextModel model = (UIInformLongTextModel)getModel();
    
    style.put("element.width",model.getConstraint().width);
    style.put("element.height",model.getConstraint().height);
    style.put("element.contenttype",model.getContentType());
    
    // Die Prüfung ist ein bischen ungünstig. die Methode getCaptionModel liefert allerdings IMMER eine Caption zurück.
    // Falls keine Caption vorhanden war wird dann dummerweise automatische eine angelegt. Somit prüfe ich hier auf das 
    // CastorElement.
    if(model.getCastorCaption()!=null)
    {
      UICaptionModel caption = model.getCaptionModel();
      style.put("caption.height",caption.getConstraint().height);
      style.put("caption.width",caption.getConstraint().width);
      style.put("caption.diff.x",caption.getConstraint().x-model.getConstraint().x);
      style.put("caption.diff.y",caption.getConstraint().y-model.getConstraint().y);
      style.put("caption.font.family",caption.getFontFamily());
      style.put("caption.font.size",caption.getFontSize());
      style.put("caption.font.style",caption.getFontStyle());
      style.put("caption.font.weight",caption.getFontWeight());
      style.put("caption.font.color.",caption.getColor());
      style.put("caption.align",caption.getAlign());
    }
    
    if(withLocation)
    {
      style.put("element.x",model.getConstraint().x);
      style.put("element.y",model.getConstraint().y);
    }
  }
  
}
