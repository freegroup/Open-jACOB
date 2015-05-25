/*
 * Created on 17.11.2009
 *
 */
package de.tif.jacob.searchbookmark;

import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.core.exception.UserNotExistingException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.security.IUser;

public interface ISearchConstraint
{

  /**
   * Transforms the constraint to the XML representation of the definition.
   * @return
   * @throws Exception
   */
  public String toXml() throws Exception;

  public String toXmlFormatted() throws Exception;

  public String getGUID();
  
  public void setGUID(String guid);
  
  /**
   * Return the corresponding application for this search constraint
   * 
   * @return
   */
  public IApplicationDefinition getApplication();
  
  /**
   * Returns the name of the application this search constraint belongs to.
   * 
   * @return the application name
   */
  public String getApplicationName();
  
  /**
   * Returns the corresponding Domain for this search constraint. Each search constraint has an anchor domain.
   *  
   * @return IDomain
   */
  public String getAnchorDomain();
  
  public String getAnchorTable();
  
  /**
   * Returns the corresponding Domain for this search constraint. Each search constraint has an anchor domain.
   *  
   * @return IDomain
   */
  public String getAnchorForm();

  /**
   * Returns the display name of the search constraint
   * 
   * @return
   */
  public String getName();
  
  /**
   * Set the display name for the search constraint.
   * @param name
   */
  public void   setName(String name);
  
  /**
   * Set the description for the search constraint
   * @param description
   */
  public void   setDescription(String description);
  
  /**
   * Set the owner of the search constraint.
   * @param user
   */
  public void setOwner(IUser user);
  
  /**
   * set the anchor table of the relation set
   * 
   * @param tableAlias
   */
  public void   setAnchorTable(String tableAlias);

  /**
   * Set the relationset to use for this search constraint
   * @param relationset
   */
  public void  setRelationset(IRelationSet relationset);

  public IRelationSet getRelationset(IClientContext context);
  
  /**
   * Returns the owner of the search constraint.
   * 
   * @return the search constraint owner
   * @throws UserNotExistingException
   *           if the owner does not exist anymore
   * @throws Exception
   *           on any other problem
   */
  public IUser getOwner() throws UserNotExistingException, Exception;
  
  /**
   * Returns the owner ID of the search constraint.
   * 
   * @return the owner ID
   */
  public String getOwnerId();

  
  /**
   * Returns the query constrainst for this report.<br>
   * 
   * @return
   */
  public abstract IDataFieldConstraints getConstraints();
  
  /**
   * Add an evaluation constraint for the report.<br>
   * 
   * @param tableAlias
   * @param fieldName
   * @param constraint
   */
  public abstract void addConstraint(String tableAlias, String fieldName, String constraint, boolean isKeyValue);

}
