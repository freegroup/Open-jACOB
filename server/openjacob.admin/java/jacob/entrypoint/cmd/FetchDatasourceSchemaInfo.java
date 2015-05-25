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
package jacob.entrypoint.cmd;

import jacob.common.AppLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.reconfigure.Reconfigure;
import de.tif.jacob.core.definition.impl.AbstractApplicationProvider;
import de.tif.jacob.core.definition.impl.jad.castor.Applications;
import de.tif.jacob.core.definition.impl.jad.castor.BinaryField;
import de.tif.jacob.core.definition.impl.jad.castor.BooleanField;
import de.tif.jacob.core.definition.impl.jad.castor.Browsers;
import de.tif.jacob.core.definition.impl.jad.castor.CastorApplication;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource;
import de.tif.jacob.core.definition.impl.jad.castor.CastorKey;
import de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelation;
import de.tif.jacob.core.definition.impl.jad.castor.CastorRelationChoice;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTable;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableAlias;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableFieldChoice;
import de.tif.jacob.core.definition.impl.jad.castor.DataSources;
import de.tif.jacob.core.definition.impl.jad.castor.DateField;
import de.tif.jacob.core.definition.impl.jad.castor.DecimalField;
import de.tif.jacob.core.definition.impl.jad.castor.Domains;
import de.tif.jacob.core.definition.impl.jad.castor.DoubleField;
import de.tif.jacob.core.definition.impl.jad.castor.EnumerationField;
import de.tif.jacob.core.definition.impl.jad.castor.FloatField;
import de.tif.jacob.core.definition.impl.jad.castor.Forms;
import de.tif.jacob.core.definition.impl.jad.castor.IntegerField;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.jacob.core.definition.impl.jad.castor.LongField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextField;
import de.tif.jacob.core.definition.impl.jad.castor.Relations;
import de.tif.jacob.core.definition.impl.jad.castor.Relationsets;
import de.tif.jacob.core.definition.impl.jad.castor.TableAliases;
import de.tif.jacob.core.definition.impl.jad.castor.Tables;
import de.tif.jacob.core.definition.impl.jad.castor.TextField;
import de.tif.jacob.core.definition.impl.jad.castor.TimeField;
import de.tif.jacob.core.definition.impl.jad.castor.TimestampField;
import de.tif.jacob.core.definition.impl.jad.castor.types.BinaryFieldSqlTypeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorApplicationEventHandlerLookupMethodType;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextFieldSqlTypeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextFieldSearchModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TimestampFieldResolutionType;
import de.tif.jacob.core.schema.ISchemaColumnDefinition;
import de.tif.jacob.core.schema.ISchemaDefinition;
import de.tif.jacob.core.schema.ISchemaKeyDefinition;
import de.tif.jacob.core.schema.ISchemaRelationDefinition;
import de.tif.jacob.core.schema.ISchemaTableDefinition;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;

/**
 * This is a entry point for the 'admin' application, which extracts schema information from a desired data source. The output will be in JAD format!
 * <p>
 * You can access this entry point with an WebBrowser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=FetchDatasourceSchemaInfo&app=admin&user=USERNAME&pwd=PASSWORD&dataSource=DATASOURCE&newapp=NEWAPP
 * <p>
 * Note: <br>
 * <li>1. Replace USERNAME/PASSWORD in the url with the real username and
 * password of the admin application.
 * <li>2. Replace localhost:8080 with the real server name and port.
 * <li>3. Replace DATASOURCE in the url with the name of the data source schema
 * information should be fetched from.
 * <li>4. Replace NEWAPP in the url with the desired application name of the
 * generated JAD file.
 */
public class FetchDatasourceSchemaInfo implements ICmdEntryPoint
{
  static public transient final String RCS_ID = "$Id: FetchDatasourceSchemaInfo.java,v 1.6 2010/10/13 13:22:37 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.6 $";

  private static final String DEFAULT_ENCODING = "ISO-8859-1";
  private static final String DEFAULT_NEWAPP = "newapp";

	static final Log logger = AppLogger.getLogger();

  /*
   * The main method for the entry point
   *  
   */
  public void enter(CmdEntryPointContext context, Properties properties) throws IOException
  {
    String dataSource = properties.getProperty("dataSource");
    String newapp = properties.getProperty("newapp");
    if (dataSource == null)
    {
      StringBuffer sb = new StringBuffer();
      sb.append("Required parameter missing:\n");
      sb.append("\n\tdataSource:").append(dataSource);
      sb.append("\n\nNO action performed!");
      context.getStream().write(sb.toString().getBytes());
      return;
    }
    if (newapp == null)
      newapp = DEFAULT_NEWAPP;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(64 * 1024);
    try
    {
      // create new JAD document
      //
      Jacob jacob = new Jacob();
      jacob.setVersion("0.1");
      jacob.setEngineVersion(Version.ENGINE.toString());
      jacob.setDefaultApplication(newapp);

      // initialize required elements
      //
      DataSources dataSources = new DataSources();
      jacob.setDataSources(dataSources);
      CastorDataSource jacobDataSource = new CastorDataSource();
      jacobDataSource.setName(dataSource);
      dataSources.addDataSource(jacobDataSource);

      Tables jacobTables = new Tables();
      jacob.setTables(jacobTables);

      TableAliases jacobTableAliases = new TableAliases();
      jacob.setTableAliases(jacobTableAliases);

      Browsers jacobBrowsers = new Browsers();
      jacob.setBrowsers(jacobBrowsers);

      Relations jacobRelations = new Relations();
      jacob.setRelations(jacobRelations);

      Relationsets jacobRelationsets = new Relationsets();
      jacob.setRelationsets(jacobRelationsets);

      Domains jacobDomains = new Domains();
      jacob.setDomains(jacobDomains);

      Forms jacobForms = new Forms();
      jacob.setForms(jacobForms);

      Applications jacobApplications = new Applications();
      jacob.setApplications(jacobApplications);
      CastorApplication jacobApplication = new CastorApplication();
      jacobApplication.setName(newapp);
      jacobApplication.setTitle(newapp + " Application");
      jacobApplication.setEventHandlerLookupMethod(CastorApplicationEventHandlerLookupMethodType.REFERENCE);
      jacobApplications.addApplication(jacobApplication);
      
      // fetch database schema information
      //
      SQLDataSource sqlDataSource = (SQLDataSource) DataSource.get(dataSource);
      Reconfigure reconfigure = sqlDataSource.getReconfigureImpl();
      ISchemaDefinition currentSchemaDefinition = reconfigure.fetchSchemaInformation();

      // sort table definitions
      //
      List tables = new ArrayList();
      Iterator iter = currentSchemaDefinition.getSchemaTableDefinitions();
      while (iter.hasNext())
      {
        tables.add(iter.next());
      }
      Collections.sort(tables, new Comparator()
      {
        public int compare(Object o1, Object o2)
        {
          return ((ISchemaTableDefinition) o1).getDBName().compareTo(((ISchemaTableDefinition) o2).getDBName());
        }
      });

      // create table & alias info
      //
      for (int i = 0; i < tables.size(); i++)
      {
        ISchemaTableDefinition tabledef = (ISchemaTableDefinition) tables.get(i);

        // create the table info
        //
        CastorTable jacobTable = getJacobTable(tabledef, sqlDataSource, jacobRelations, jacobTableAliases);
        jacobTable.setDatasource(dataSource);
        jacobTables.addTable(jacobTable);

        // create an alias which has the same name as the table
        //
        CastorTableAlias jacobTableAlias = new CastorTableAlias();
        jacobTableAlias.setName(tabledef.getDBName().toLowerCase());
        jacobTableAlias.setTable(tabledef.getDBName().toLowerCase());
        jacobTableAliases.addTableAlias(jacobTableAlias);
      }

      AbstractApplicationProvider.printFormated(jacob, outputStream, DEFAULT_ENCODING);
    }
    catch (Exception e)
    {
      if (logger.isWarnEnabled())
        logger.warn("Fetching schema info failed", e);
      
      context.getStream().write(e.toString().getBytes());
      return;
    }

    context.getStream().write(outputStream.toByteArray());
  }

  private static CastorTable getJacobTable(ISchemaTableDefinition tabledef, SQLDataSource dataSource, Relations jacobRelations, TableAliases jacobTableAliases)
  {
    CastorTable jacobTable = new CastorTable();

    jacobTable.setName(tabledef.getDBName().toLowerCase());
    jacobTable.setDbName(tabledef.getDBName());
    jacobTable.setDescription("Fetched by reverse engineering.");

    // handle table fields
    //
    Iterator columnIter = tabledef.getSchemaColumnDefinitions();
    while (columnIter.hasNext())
    {
      ISchemaColumnDefinition columndef = (ISchemaColumnDefinition) columnIter.next();

      CastorTableField jacobTableField = new CastorTableField();
      jacobTableField.setCastorTableFieldChoice(new CastorTableFieldChoice());
      jacobTable.addField(jacobTableField);

      jacobTableField.setName(columndef.getDBName().toLowerCase());
      jacobTableField.setDbName(columndef.getDBName());
      jacobTableField.setLabel(columndef.getDBName().toUpperCase());
      //	    jacobTableField.setLabel(getLabel());
      //	    jacobTableField.setDescription(getDescription());
      jacobTableField.setReadonly(false);
      jacobTableField.setRequired(columndef.isRequired());

      // check for enumeration column first
      // Note: This works currently for Quintus adjustment only
      //
      int sqlType = columndef.getSQLType(dataSource);
      jacobTableField.setDescription("SQL type "+sqlType);
      if (columndef.isEnumeration())
      {
        EnumerationField type = new EnumerationField();

        List enums = columndef.getEnumerationLabels();
        for (int i = 0; i < enums.size(); i++)
        {
          type.addValue((String) enums.get(i));
        }

        String defaultValue = columndef.getDBDefaultValue(dataSource);
        if (defaultValue != null)
        {
          switch (sqlType)
          {
            // Quintus handling
            case Types.BIGINT:
            case Types.SMALLINT:
            case Types.INTEGER:
              defaultValue = (String) enums.get(Integer.parseInt(defaultValue));
              break;

            default:
              if (!new HashSet(enums).contains(defaultValue))
              {
                throw new RuntimeException(tabledef.getDBName() + "." + columndef.getDBName() + ": Invalid enum default value: " + defaultValue);
              }
              break;
          }
          type.setDefault(defaultValue);
        }

        jacobTableField.getCastorTableFieldChoice().setEnumeration(type);
      }
      else
      {
        // evaluate SQL type and map to jACOB field types
        // Note: This does not work for document type and for Oracle also not
        //       for long text and binary fields.
        switch (sqlType)
        {
          case Types.TIMESTAMP:
          {
            TimestampField type = new TimestampField();
            type.setResolution(TimestampFieldResolutionType.SECBASE);
            jacobTableField.getCastorTableFieldChoice().setTimestamp(type);
            break;
          }

          case Types.TIME:
          {
            TimeField type = new TimeField();
            jacobTableField.getCastorTableFieldChoice().setTime(type);
            break;
          }

          case Types.CHAR:
          case Types.VARCHAR:
          {
            TextField type = new TextField();
            type.setCaseSensitive(false);
            type.setDefault(columndef.getDBDefaultValue(dataSource));
            type.setMaxLength(columndef.getSQLSize(dataSource));
            type.setSearchMode(TextFieldSearchModeType.UNBOUND);
            type.setFixedLength(sqlType == Types.CHAR);
            jacobTableField.getCastorTableFieldChoice().setText(type);
            break;
          }

          case Types.BIGINT:
          {
            LongField type = new LongField();
            boolean autoIncrement = columndef.isDBAutoGenerated(dataSource);
            String defaultValue = adjustNumericalDefault(columndef.getDBDefaultValue(dataSource));
            type.setAutoincrement(autoIncrement);
            type.setDbincrement(autoIncrement);
            if (defaultValue != null)
              type.setDefault(Long.parseLong(defaultValue));
            jacobTableField.getCastorTableFieldChoice().setLong(type);
            break;
          }

          case Types.TINYINT:
          case Types.SMALLINT:
          case Types.INTEGER:
          {
            IntegerField type = new IntegerField();
            boolean autoIncrement = columndef.isDBAutoGenerated(dataSource);
            String defaultValue = adjustNumericalDefault(columndef.getDBDefaultValue(dataSource));
            type.setAutoincrement(autoIncrement);
            type.setDbincrement(autoIncrement);
            if (defaultValue != null)
              type.setDefault(Integer.parseInt(defaultValue));
            jacobTableField.getCastorTableFieldChoice().setInteger(type);
            break;
          }

          case Types.REAL:
          case Types.FLOAT:
          {
            FloatField type = new FloatField();
            String defaultValue = adjustNumericalDefault(columndef.getDBDefaultValue(dataSource));
            if (defaultValue != null)
              type.setDefault(Float.parseFloat(defaultValue));
            jacobTableField.getCastorTableFieldChoice().setFloat(type);
            break;
          }

          case Types.DOUBLE:
          {
            DoubleField type = new DoubleField();
            String defaultValue = adjustNumericalDefault(columndef.getDBDefaultValue(dataSource));
            if (defaultValue != null)
              type.setDefault(Double.parseDouble(defaultValue));
            jacobTableField.getCastorTableFieldChoice().setDouble(type);
            break;
          }

          case Types.NUMERIC:
          case Types.DECIMAL:
          {
            int size = columndef.getSQLSize(dataSource);
            short scale = (short) columndef.getSQLDecimalDigits(dataSource);
            String defaultValue = adjustNumericalDefault(columndef.getDBDefaultValue(dataSource));

            // 20090520 Meldung 368: Reverse Engeneering
            // Interpretiere einen Dezimalwert(1,0) als Boolean.
            if (size==1 && scale==0 && (defaultValue==null || "1".equals(defaultValue) || "0".equals(defaultValue)))
            {
              // Boolean-Case
              BooleanField type = new BooleanField();
              if (defaultValue != null)
                type.setDefault("1".equals(defaultValue));
              jacobTableField.getCastorTableFieldChoice().setBoolean(type);
            }
            else
            {
              // Decimal-Case
              DecimalField type = new DecimalField();
              if (defaultValue != null)
                type.setDefault(new BigDecimal(defaultValue));
              type.setScale(scale);
              jacobTableField.getCastorTableFieldChoice().setDecimal(type);
            }
            
            break;
          }

          case Types.DATE:
          {
            DateField type = new DateField();
            jacobTableField.getCastorTableFieldChoice().setDate(type);
            break;
          }

          case Types.BINARY:
          case Types.VARBINARY:
          case Types.LONGVARBINARY:
          case Types.BLOB:
          case Types.OTHER:
            
          // to avoid exceptions 
          case Types.JAVA_OBJECT:
          case Types.DISTINCT:
          case Types.STRUCT:
          case Types.REF:
          {
            BinaryField type = new BinaryField();
            type.setDisableMapping(true);
            if (sqlType== Types.BLOB)
              type.setSqlType(BinaryFieldSqlTypeType.BLOB);
            else if (sqlType== Types.LONGVARBINARY)
              type.setSqlType(BinaryFieldSqlTypeType.LONGVARBINARY);
            jacobTableField.getCastorTableFieldChoice().setBinary(type);
            break;
          }

          case Types.CLOB:
          case Types.LONGVARCHAR:
          {
            LongTextField type = new LongTextField();
            type.setDefault(columndef.getDBDefaultValue(dataSource));
            type.setChangeHeader(false);
            type.setDisableMapping(true);
            if (sqlType== Types.CLOB)
              type.setSqlType(LongTextFieldSqlTypeType.CLOB);
            else if (sqlType== Types.LONGVARCHAR)
              type.setSqlType(LongTextFieldSqlTypeType.LONGVARCHAR);
            jacobTableField.getCastorTableFieldChoice().setLongText(type);
            break;
          }

          default:
          {
            throw new RuntimeException(tabledef.getDBName() + "." + columndef.getDBName() + ": Can not handle SQL type: " + sqlType);
          }
        }
      }
    }

    // ------------------------------------------------------------
    // handle primary key
    //
    ISchemaKeyDefinition primaryKeyDef = tabledef.getSchemaPrimaryKeyDefinition();
    if (null != primaryKeyDef)
    {
      CastorKey jacobPrimaryKey = getJacobKey(primaryKeyDef, "primaryKey");
      jacobTable.setPrimaryKey(jacobPrimaryKey);
    }

    // ------------------------------------------------------------
    // handle relations
    //
    
    Set foreignKeyNames = new HashSet();

    // determine to which tables multiple relations exist
    //
    Set primaryTableNames = new HashSet();
    Set doublePrimaryTableNames = new HashSet();
    Iterator relationIter = tabledef.getSchemaRelationDefinitions();
    while (relationIter.hasNext())
    {
      ISchemaRelationDefinition relationDef = (ISchemaRelationDefinition) relationIter.next();
      
      foreignKeyNames.add(relationDef.getSchemaForeignKeyName());
      
      String primaryTableName = relationDef.getSchemaPrimaryTableName();
      if (!primaryTableNames.add(primaryTableName))
      {
        // already contained
        doublePrimaryTableNames.add(primaryTableName);
      }
    }

    // Map(primaryTableName->Integer)
    Map fromTableMap = new HashMap();
    relationIter = tabledef.getSchemaRelationDefinitions();
    while (relationIter.hasNext())
    {
      ISchemaRelationDefinition relationDef = (ISchemaRelationDefinition) relationIter.next();

      // create foreign key
      //
      CastorKey jacobKey = new CastorKey();
      jacobKey.setName(relationDef.getSchemaForeignKeyName());
      Iterator colnamesIter = relationDef.getSchemaForeignColumnNames();
      while (colnamesIter.hasNext())
      {
        jacobKey.addField(((String) colnamesIter.next()).toLowerCase());
      }
      jacobTable.addForeignKey(jacobKey);

      // create relation
      //
      CastorRelation jacobRelation = new CastorRelation();
      jacobRelation.setCastorRelationChoice(new CastorRelationChoice());
      jacobRelations.addRelation(jacobRelation);

      String toAlias = relationDef.getSchemaTableName().toLowerCase();
      
      // we only get information about 1:N relations
      CastorOneToMany jacobOneToManyRelation = new CastorOneToMany();
      jacobOneToManyRelation.setToAlias(toAlias);
      jacobOneToManyRelation.setToKey(relationDef.getSchemaForeignKeyName());
      jacobRelation.getCastorRelationChoice().setOneToMany(jacobOneToManyRelation);

      // determine relation name
      // Note: relation names must be unique, i.e. if an table has 2 or more
      // relations to another table,
      //       we create additional aliases. E.g. call->callEmployee1;
      // call->callEmployee2;
      // etc.
      String fromAlias;
      boolean newAlias = false;
      if (doublePrimaryTableNames.contains(relationDef.getSchemaPrimaryTableName()))
      {
        Integer counter = (Integer) fromTableMap.get(relationDef.getSchemaPrimaryTableName());
        if (counter == null)
        {
          counter = new Integer(0);
        }
        
        counter = new Integer(counter.intValue() + 1);
        fromAlias = toAlias + StringUtils.capitalise(relationDef.getSchemaPrimaryTableName().toLowerCase()) + counter.toString();
        
        newAlias = true;

        fromTableMap.put(relationDef.getSchemaPrimaryTableName(), counter);
      }
      else
      {
        if (relationDef.getSchemaTableName().equals(relationDef.getSchemaPrimaryTableName()))
        {
          fromAlias = "parent" + StringUtils.capitalise(relationDef.getSchemaPrimaryTableName().toLowerCase());
          newAlias = true;
        }
        else
          fromAlias = relationDef.getSchemaPrimaryTableName().toLowerCase();
      }

      // add new alias?
      //
      if (newAlias)
      {
        CastorTableAlias jacobTableAlias = new CastorTableAlias();
        jacobTableAlias.setName(fromAlias);
        jacobTableAlias.setTable(relationDef.getSchemaPrimaryTableName().toLowerCase());
        jacobTableAliases.addTableAlias(jacobTableAlias);
      }
      
      // set determined from alias name
      jacobOneToManyRelation.setFromAlias(fromAlias);

      jacobRelation.setName(fromAlias + "_" + toAlias);
    }

    // ------------------------------------------------------------
    // handle indices
    //
    Iterator indexIter = tabledef.getSchemaIndexDefinitions();
    while (indexIter.hasNext())
    {
      ISchemaKeyDefinition keyDef = (ISchemaKeyDefinition) indexIter.next();
      
      // skip index, if it is equivalent to an foreign key
      // Note: mySQL creates implicit indices for foreign keys!
      //
      if (foreignKeyNames.contains(keyDef.getDBName()))
        continue;

      CastorKey jacobKey = getJacobKey(keyDef, keyDef.getDBName());
      if (keyDef.isUnique())
      {
        jacobTable.addUniqueIndex(jacobKey);
      }
      else
      {
        jacobTable.addIndex(jacobKey);
      }
    }

    return jacobTable;
  }
  
  /**
   * Adjusts numerical default values as "(0)".
   * @param def
   * @return
   */
  private static String adjustNumericalDefault(String def)
  {
    if (def == null)
      return null;
    if (def.startsWith("(") && def.endsWith(")"))
      return def.substring(1, def.length() - 1);
    return def;
  }

  private static CastorKey getJacobKey(ISchemaKeyDefinition keyDef, String name)
  {
    CastorKey jacobKey = new CastorKey();
    jacobKey.setName(name);
    Iterator colnamesIter = keyDef.getSchemaColumnNames();
    while (colnamesIter.hasNext())
    {
      jacobKey.addField(((String) colnamesIter.next()).toLowerCase());
    }
    return jacobKey;
  }

  /**
   * Returns the mime type for this entry point.
   * 
   * The Web client need this information for the proper display of the returned
   * content.
   */
  public String getMimeType(CmdEntryPointContext context, Properties properties)
  {
    return "text/plain";
  }
}
