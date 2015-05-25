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

package de.tif.jacob.core.definition.impl;

import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.InputFieldDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractBrowserForeignTableField extends AbstractBrowserTableField
{
	private final String foreignBrowserName;
  private final String relationsetName;
  private final Filldirection filldirection;
  private final String relationName;
  private final String tablealiasName;
  
  private IRelationSet relationsetToUse;
  private IBrowserDefinition foreignBrowserToUse;
  private IRelation relationToUse;
  private ITableAlias foreignTableAlias;
  
	/**
	 * @param name
	 * @param tablealiasName
	 * @param tablefieldName
	 * @param label
	 * @param sortorder
	 * @param visible
	 * @param readonly
	 * @param foreignBrowserName
	 * @param relationsetName
	 * @param filldirection
	 * @param relationName
	 */
	public AbstractBrowserForeignTableField(String name, String tablealiasName, String tablefieldName, String label, SortOrder sortorder, boolean visible, boolean readonly,boolean configureable, String foreignBrowserName, String relationsetName, Filldirection filldirection, String relationName)
  {
		super(name, tablefieldName, label, sortorder, visible, readonly, configureable);
    
    if (null == tablealiasName)
      throw new NullPointerException(this + ": tablealiasName is null");
    this.tablealiasName = tablealiasName;

    // plausibility check: the following members/arguments must all be null or
    // all be not null!
    if (null != filldirection || null != foreignBrowserName || null != relationsetName || null != relationName)
    {
      if (null == filldirection)
        throw new NullPointerException(this + ": filldirection is null (at least one of foreignBrowserName/relationsetName/relationName is not null)");
      if (null == foreignBrowserName)
        throw new NullPointerException(this + ": foreignBrowserName is null (at least one of filldirection/relationsetName/relationName is not null)");
      if (null == relationsetName)
        throw new NullPointerException(this + ": relationsetName is null (at least one of filldirection/foreignBrowserName/relationName is not null)");
      if (null == relationName)
        throw new NullPointerException(this + ": relationName is null (at least one of filldirection/foreignBrowserName/relationsetName is not null)");
    }
      
    this.foreignBrowserName = foreignBrowserName;
    this.relationsetName = relationsetName;
    this.relationName = relationName;
    this.filldirection = filldirection;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    // do this in advance, because otherwise createInputFieldDefinition() would
    // throw an Exception
    this.foreignTableAlias = definition.getTableAlias(this.tablealiasName);
    
    if (null != this.relationsetName)
    	this.relationsetToUse = definition.getRelationSet(this.relationsetName);
    if (null != this.relationName)
      this.relationToUse = definition.getRelation(this.relationName);
    if (null != this.foreignBrowserName)
      this.foreignBrowserToUse = definition.getBrowserDefinition(this.foreignBrowserName);
    
    // calls createInputFieldDefinition()!
    super.postProcessing(definition, parent);
	}

	/**
	 * @return Returns the foreignBrowserToUse.
	 */
	public final IBrowserDefinition getForeignBrowserToUse()
	{
		return foreignBrowserToUse;
	}

	/**
	 * @return Returns the relationsetToUse.
	 */
	public final IRelationSet getRelationsetToUse()
	{
		return relationsetToUse;
	}

	/**
	 * @return Returns the relationToUse.
	 */
	public final IRelation getRelationToUse()
	{
		return relationToUse;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractBrowserTableField#createInputFieldDefinition(java.lang.String)
	 */
	protected InputFieldDefinition createInputFieldDefinition(String inputFieldName)
  {
    if (this.filldirection == null || this.foreignBrowserToUse == null || this.relationsetToUse == null)
      // die Attribute relationToUse, browserToUse, relationsSet und
      // filldirection sind nicht gesetzt -> Readonly Textfeld
      return null;//new TextInputFieldDefinition(inputFieldName, null, null, null, null, true, isReadonly(), false, -1, 0, null, this.foreignTableAlias, getTableField());

    return new ForeignInputFieldDefinition(inputFieldName, null, null, null, null, true, isReadonly(), false, -1, 0, null, //
        this.foreignBrowserToUse, (IOneToManyRelation) this.relationToUse, this.relationsetToUse, this.filldirection, getTableField(), null);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractBrowserField#toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField)
	 */
	protected void toJacob(CastorBrowserField jacobBrowserField)
	{
		super.toJacob(jacobBrowserField);
    
    CastorBrowserForeignType jacobBrowserForeign = new CastorBrowserForeignType();
    jacobBrowserForeign.setForeignAlias(getTableAlias().getName());
    
    if (getForeignBrowserToUse() != null)
    	jacobBrowserForeign.setBrowserToUse(getForeignBrowserToUse().getName());
    if (getFilldirection() != null)
      jacobBrowserForeign.setFilldirection(getFilldirection().toJad());
    if (getRelationsetToUse() != null)
      jacobBrowserForeign.setRelationset(getRelationsetToUse().getName());
    if (getRelationToUse() != null)
      jacobBrowserForeign.setRelationToUse(getRelationToUse().getName());
    
    jacobBrowserField.getCastorBrowserFieldChoice().getTableField().setForeign(jacobBrowserForeign);
  }

	/**
	 * @return Returns the filldirection.
	 */
	public final Filldirection getFilldirection()
	{
		return filldirection;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IBrowserTableField#getTableAlias()
	 */
	public final ITableAlias getTableAlias()
	{
    return this.foreignTableAlias;
	}

}
