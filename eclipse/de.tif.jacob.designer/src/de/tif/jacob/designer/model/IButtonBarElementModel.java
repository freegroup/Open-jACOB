/*
 * Created on 22.10.2010
 *
 */
package de.tif.jacob.designer.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;

public interface IButtonBarElementModel
{

  void addReferrerObject(ReferenceSearchResult result, ObjectModel model);

  void renameRelationsetReference(String from, String to);

  void renameRelationReference(String from, String to);

  void createMissingI18NKey();

  boolean isI18NKeyInUse(String key);

  void renameI18NKey(String fromName, String toName);

  void renameFieldReference(FieldModel field, String fromName, String toName);

  void renameEventHandler(String fromClass, String toClass);

  void setButtonBarModel(UIButtonBarModel object);

  void setSize(Dimension dimension);

  void setLocation(Point point);

  void setGroup(UIGroupModel groupModel);

  CastorGuiElement getCastor();

  String getName();

  void setName(String newName) throws Exception;

  void setJacobModel(JacobModel jacobModel);

  String getDefaultName();

  String getHookClassName();

  UIButtonBarModel getButtonBarModel();
}
