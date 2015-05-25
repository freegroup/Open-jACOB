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
package de.tif.jacob.designer.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.commons.collections.ListUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.impl.jad.castor.ClearGroup;
import de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord;
import de.tif.jacob.core.definition.impl.jad.castor.Generic;
import de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm;
import de.tif.jacob.core.definition.impl.jad.castor.NewRecord;
import de.tif.jacob.core.definition.impl.jad.castor.RecordSelected;
import de.tif.jacob.core.definition.impl.jad.castor.Search;
import de.tif.jacob.core.definition.impl.jad.castor.SearchUpdateRecord;
import de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceReconfigureType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDomainDataScopeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorExternalFormTargetType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontFamilyType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontStyleType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFontWeightType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorResizeMode;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.TextInputModeType;
import de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.properties.plugin.IPropertyPlugin;
import de.tif.jacob.properties.plugin.PropertyPluginManager;
import de.tif.jacob.util.clazz.ClassUtil;

/*
 */
public abstract class ObjectModel implements IPropertySource, TreeSelectionObjectModelProvider
{
  // The jacobBase.jar must have at least this version if you wnat midify the related project with this designer 
  //
  private final static Version version = new Version(2,7,0);

  static public final int DEFAULT_CAPTION_WIDTH   = 70;
  static public final int DEFAULT_CAPTION_HEIGHT  = 20;
  static public final int DEFAULT_CAPTION_SPACING = 5;

  static public final int DEFAULT_BUTTON_WIDTH   = 70;
  static public final int DEFAULT_BUTTON_HEIGHT  = 20;
  
  static public final int DEFAULT_ELEMENT_WIDTH   = 200;
  static public final int DEFAULT_ELEMENT_HEIGHT  = 20;
  static public final int DEFAULT_ELEMENT_SPACING = 5;
  static public final int DEFAULT_ELEMENT_FIRST_Y = 25;

  static public final int DEFAULT_GROUP_SPACING   = 5;
  static public final int DEFAULT_GROUP_HEIGHT    = 350;
  static public final int DEFAULT_GROUP_WIDTH     = 650;

  public static final String PROPERTY_I18NVALUE_CHANGED     = "i18n value changed";
	public static final String PROPERTY_I18NKEY_CHANGED       = "i18n key changed";
	public static final String PROPERTY_I18NKEY_CREATED       = "i18n key added";
	public static final String PROPERTY_I18NKEY_REMOVE        = "i18n key removed";
	public static final String PROPERTY_I18NBUNDLE_DELETED    = "i18n bundle deleted";
	public static final String PROPERTY_I18NBUNDLE_CREATED    = "i18n bundle created";
	public static final String PROPERTY_I18NBUNDLE_CHANGED    = "i18n bundle changed";
  public static final String PROPERTY_MAX_LENGTH_CHANGED    = "MAX_LENGTH";
  public static final String PROPERTY_SEARCH_MODE_CHANGED   = "SEARCH_MODE";
  public static final String PROPERTY_EDIT_MODE_CHANGED     = "Edit MODE";
  public static final String PROPERTY_DEFAULT_CHANGED       = "DEFAULT";
  public static final String PROPERTY_CASESENSITIVE_CHANGED = "CASE_SENSITIVE";
  public static final String PROPERTY_FIX_LENGTH_CHANGED    = "fixe length changed";
	public static final String PROPERTY_RELATIONSET_CHANGED   = "relationset changed";
	public static final String PROPERTY_TESTRELATIONSET_CHANGED = "test relationset changed";
	public static final String PROPERTY_TESTRESOURCEBUNDLE_CHANGED = "test resourcebundle changed";
	public static final String PROPERTY_JACOBMODEL_CHANGED    = "one element of the jacob model changed";
	public static final String PROPERTY_JACOBMODEL_CLOSED     = "the jacob model closed";
	public static final String PROPERTY_FILLDIRECTION_CHANGED = "filldirection changed";
	public static final String PROPERTY_CONDITION_CHANGED     = "condition changed";
	public static final String PROPERTY_NAME_CHANGED          = "name changed";
	public static final String PROPERTY_TABINDEX_CHANGED      = "tabindex changed";
	public static final String PROPERTY_EVENTHANDLER_CHANGED  = "eventhandler changed";
  public static final String PROPERTY_LABEL_CHANGED         = "label changed";
  public static final String PROPERTY_BORDER_CHANGED        = "border changed";
  public static final String PROPERTY_EMPHASIZE_CHANGED     = "emphasize changed";
  public static final String PROPERTY_HIDE_BROWSER_CHANGED  = "border changed";
  public static final String PROPERTY_HAS_GRID_CHANGED      = "has grid changed";
  public static final String PROPERTY_HAS_TITLE_CHANGED     = "has title changed";
  public static final String PROPERTY_HAS_BACKGROUND_CHANGED= "has background changed";
  public static final String PROPERTY_HAS_LEGEND_X_CHANGED  = "has legend x changed";
  public static final String PROPERTY_HAS_LEGEND_Y_CHANGED  = "has legend y changed";
  public static final String PROPERTY_ALIGN_CHANGED         = "alignment changed";
	public static final String PROPERTY_CAPTION_CHANGED       = "caption changed";
  public static final String PROPERTY_CONSTRAINT_CHANGED    = "constraint";
  public static final String PROPERTY_CONTENTYPE_CHANGED    = "content type";
	public static final String PROPERTY_ALIAS_DELETED         = "alias deleted";
	public static final String PROPERTY_ALIAS_CREATED         = "alias created";
	public static final String PROPERTY_RELATION_CREATED      = "relation created";
	public static final String PROPERTY_ACTIVITYDIAGRAM_CREATED ="activity diagram created";
	public static final String PROPERTY_ACTIVITYDIAGRAM_DELETED ="activity diagram deleted";
	public static final String PROPERTY_RELATION_DELETED      = "relation deleted";
	public static final String PROPERTY_DOMAIN_CREATED        = "domain created";
	public static final String PROPERTY_DOMAIN_DELETED        = "domain deleted";
	public static final String PROPERTY_RELATIONSET_CREATED   = "relationset created";
  public static final String PROPERTY_RELATIONSET_DELETED   = "relationset deleted";
	public static final String PROPERTY_FORM_DELETED          = "form deleted";
	public static final String PROPERTY_FORM_CREATED          = "form created";
	public static final String PROPERTY_BROWSER_DELETED       = "browser deleted";
	public static final String PROPERTY_BROWSER_CREATED       = "browser created";
	public static final String PROPERTY_DATASOURCE_CREATED    = "datasource created";
	public static final String PROPERTY_DATASOURCE_DELETED    = "datasource deleted";
	public static final String PROPERTY_SYSTEMJOB_CREATED     = "system scheduler created";
	public static final String PROPERTY_SYSTEMJOB_DELETED     = "system scheduler deleted";
	public static final String PROPERTY_USERJOB_CREATED       = "user scheduler created";
	public static final String PROPERTY_USERJOB_DELETED       = "user scheduler deleted";
	public static final String PROPERTY_SOAPENTRY_CREATED     = "SOAP entry created";
	public static final String PROPERTY_SOAPENTRY_DELETED     = "SOAP entry deleted";
	public static final String PROPERTY_CMDENTRY_CREATED      = "CMD entry created";
	public static final String PROPERTY_CMDENTRY_DELETED      = "CMD entry deleted";
	public static final String PROPERTY_GUIENTRY_CREATED      = "GUI entry created";
	public static final String PROPERTY_GUIENTRY_DELETED      = "GUI entry deleted";
	public static final String PROPERTY_USERROLE_CHANGED      = "role entry changed";
	public static final String PROPERTY_USERROLE_CREATED      = "role entry created";
	public static final String PROPERTY_USERROLE_DELETED      = "role entry deleted";
	public static final String PROPERTY_USERROLE_ASSIGNED     = "role assigned";
	public static final String PROPERTY_USERROLE_UNASSIGNED   = "role unassigned";
	public static final String PROPERTY_PREFERENCES_CHANGED   = "jacob preferences changed";
  public static final String PROPERTY_GUIDE_ADDED           = "guide added";
  public static final String PROPERTY_GUIDE_REMOVED         = "guide removed";
  public static final String PROPERTY_ELEMENT_ADDED         = "element added";
  public static final String PROPERTY_VISIBILITY_CHANGED    = "visibility changed";
	public static final String PROPERTY_ELEMENT_CHANGED       = "element changed";
	public static final String PROPERTY_ELEMENT_REMOVED       = "element removed";
	public static final String PROPERTY_ELEMENT_ASSIGNED      = "this element is assigned to container";
	public static final String PROPERTY_ELEMENT_UNASSIGNED    = "this element is removed from a container";
	public static final String PROPERTY_ACTION_CHANGED        = "action changed";
  public static final String PROPERTY_FONT_CHANGED          = "font changed";
  public static final String PROPERTY_COLOR_CHANGED         = "color changed";
  public static final String PROPERTY_SAFEMODE_CHANGED      = "safemode changed";
  public static final String PROPERTY_CONFIRMATION_CHANGED  = "user confimation changed";
	public static final String PROPERTY_FOREIGNTABLE_CHANGED  = "foreigntable changed";
	public static final String PROPERTY_TABLE_CHANGED         = "table changed";
	public static final String PROPERTY_TABLE_DELETED         = "table deleted";
	public static final String PROPERTY_TABLE_CREATED         = "table created";
	public static final String PROPERTY_BROWSER_CHANGED       = "browser changed";
	public static final String PROPERTY_FIELDS_CHANGED        = "fields changed";
  public static final String PROPERTY_KEY_DELETED           = "KEY_DELETED";
  public static final String PROPERTY_HISTORY_FIELD_CHANGED = "HISTORY_FIELD";
  public static final String PROPERTY_REQUIRED_CHANGED      = "REQUIRED";
  public static final String PROPERTY_READONLY_CHANGED      = "READONLY";
  public static final String PROPERTY_HISTORY_CHANGED       = "HISTORY";
  public static final String PROPERTY_MULTISELECT_CHANGED   = "MULTISELECT";
  public static final String PROPERTY_REPRESENTATIVE_FIELD  = "REPRESENTATIVE_FIELD";
  public static final String PROPERTY_DATASOURCE_CHANGED    = "DATASOURCE";
  public static final String PROPERTY_DBNAME_CHANGED        = "DBNAME";
  public static final String PROPERTY_FIELD_ADDED           = "FIELD_ADDED";
  public static final String PROPERTY_FIELD_DELETED         = "FIELD_DELETED";
  public static final String PROPERTY_FIELD_CHANGED         = "FIELD_CHANGED";
  public static final String PROPERTY_FIELD_TYPE_CHANGED    = "FIELD_TYPE_CHANGED";
  public static final String PROPERTY_KEY_ADDED             = "KEY_ADDED";
  public static final String PROPERTY_KEY_CHANGED           = "KEY_CHANGED";
  public static final String PROPERTY_DESCRIPTION_CHANGED   = "description changed";
	public static final String PROPERTY_DISPLAYFIELD_CHANGED  = "foreing display field changed";
	public static final String PROPERTY_ENUMS_CHANGED         = "enum values changed";
	public static final String PROPERTY_BROWSERCOLUMN_CHANGED = "browser column changed";
	public static final String PROPERTY_BROWSERMODE_CHANGED   = "browser mode changed";
	public static final String PROPERTY_TABLEALIAS_CHANGED    = "tableAlias changed";
  public static final String PROPERTY_FORM_LINKED           = "FORM linked";
  public static final String PROPERTY_FORM_UNLINKED         = "FORM unlinked";
	
	public static final String ID_PROPERTY_CONDITION       = "condition";
	public static final String ID_PROPERTY_TABLEALIAS      = "tableAlias";
	public static final String ID_PROPERTY_FIELD           = "field";
	public static final String ID_PROPERTY_DELETE_MODE     = "deletemode";
	public static final String ID_PROPERTY_WORDWRAP        = "wordwrap";
  public static final String ID_PROPERTY_EDITMODE        = "editmode";
  public static final String ID_PROPERTY_CONTENTTYPE  = "contentype";
	public static final String ID_PROPERTY_TEXTMODE        = "textmode";
	public static final String ID_PROPERTY_KEY             = "key";
  public static final String ID_PROPERTY_BROWSER         = "browser";  
  public static final String ID_PROPERTY_RESIZE_MODE      = "resize mode";  
	public static final String ID_PROPERTY_ASCOMBOBOX      = "asComboBox";  
	public static final String ID_PROPERTY_BORDER          = "border";  
	public static final String ID_PROPERTY_EMPHASIZE       = "emphasize";  
	public static final String ID_PROPERTY_HIDE_EMPTY_BROWSER = "hide_empty_browser";  
	public static final String ID_PROPERTY_HIDE_TAB_STRIP  = "hide_tab_strip";  
	public static final String ID_PROPERTY_CAPTION         = "caption";  
  public static final String ID_PROPERTY_SEARCH_NULL     = "search null";  
  public static final String ID_PROPERTY_SEARCH_NOT_NULL = "search not null";  
	public static final String ID_PROPERTY_ELLIPSIS        = "ellipsis";  
	public static final String ID_PROPERTY_COLOR           = "font-color";  
	public static final String ID_PROPERTY_INPUT_HINT      = "input_hint";  
	public static final String ID_PROPERTY_FOREINGTABLE    = "foreignTable";
	public static final String ID_PROPERTY_FOREINGFIELD    = "foreignField";
	public static final String ID_PROPERTY_ACTION          = "action";
	public static final String ID_PROPERTY_HAS_GRID        = "grid";
	public static final String ID_PROPERTY_HAS_TITLE       = "title";
	public static final String ID_PROPERTY_HAS_LEGEND_X    = "label x";
	public static final String ID_PROPERTY_HAS_LEGEND_Y    = "label y";
	public static final String ID_PROPERTY_HAS_BACKGROUND  = "background";
	public static final String ID_PROPERTY_ALIGN           = "align";
	public static final String ID_PROPERTY_RECONFIGURE     = "reconfigure";
	public static final String ID_PROPERTY_SAFEMODE        = "safemode";
	public static final String ID_PROPERTY_USERCONFIRM     = "user confirm";
	public static final String ID_PROPERTY_NAME            = "name";
  public static final String ID_PROPERTY_VISIBLE         = "visible";
	public static final String ID_PROPERTY_TABINDEX        = "tab index";
	public static final String ID_PROPERTY_DESCRIPTION     = "description";
	public static final String ID_PROPERTY_DBNAME          = "db-name";
  public static final String ID_PROPERTY_LABEL           = "label";
  public static final String ID_PROPERTY_DBLABEL_DEFAULT_VALUE  = "dblabel default value";
	public static final String ID_PROPERTY_TOOLTIP         = "tooltip";
	public static final String ID_PROPERTY_DELIMITER       = "delimiter";
	public static final String ID_PROPERTY_POSTPROCESS     = "postprocess";
	public static final String ID_PROPERTY_COLLAPSE        = "collapse";
	public static final String ID_PROPERTY_DATASCOPE       = "datascope";
	public static final String ID_PROPERTY_LOCATION_X      = "location x";
	public static final String ID_PROPERTY_LOCATION_Y      = "location y";
	public static final String ID_PROPERTY_HEIGHT          = "height";
	public static final String ID_PROPERTY_WIDTH           = "width";
	public static final String ID_PROPERTY_FILLDIRECTION   = "filldirection";
	public static final String ID_PROPERTY_FONTFAMILY      = "fontfamily";
  public static final String ID_PROPERTY_EXECUTESCOPE    = "execute_scope";
	public static final String ID_PROPERTY_FONTWEIGHT      = "fontweight";
	public static final String ID_PROPERTY_FONTSTYLE       = "fontstyle";
	public static final String ID_PROPERTY_FONTSIZE        = "fontsize";
	public static final String ID_PROPERTY_RELATIONSET     = "relationset";
	public static final String ID_PROPERTY_ENUMVALUE       = "enum_value_";
  public static final String ID_PROPERTY_CUSTOM_ENUMVALUE= "custom enum_value_";
	public static final String ID_PROPERTY_TO_TABLE        = "to-table";
	public static final String ID_PROPERTY_FROM_TABLE      = "from-table";
	public static final String ID_PROPERTY_TABLE           = "table";
	public static final String ID_PROPERTY_BROWSERMODE_NEW = "newMode";
	public static final String ID_PROPERTY_BROWSERMODE_UPDATE= "editMode";
	public static final String ID_PROPERTY_BROWSERMODE_DEL = "deleteMode";
	public static final String ID_PROPERTY_BROWSERMODE_SEARCH = "searchMode";
	public static final String ID_PROPERTY_MULTISELECT = "multiselect";
	public static final String ID_PROPERTY_DEFAULTPACKAGE = "java_default_package";
  public static final String ID_PROPERTY_BORDER_WIDTH   = "border width";
  public static final String ID_PROPERTY_BORDER_COLOR   = "border color";
  public static final String ID_PROPERTY_BACKGROUND_COLOR  = "background color";
  public static final String ID_PROPERTY_OMIT_LOCK         = "omit lock";
  public static final String ID_PROPERTY_RECORDS_ALWAYS_DELETABLE = "records always deletable";
  public static final String ID_PROPERTY_M_TO_N_TABLE    = "MtoN table";
	
	public static final String ACTION_GENERIC                 = ClassUtil.getShortClassName(Generic.class);
	public static final String ACTION_NEWRECORD               = ClassUtil.getShortClassName(NewRecord.class);
	public static final String ACTION_UPDATERECORD            = ClassUtil.getShortClassName(UpdateRecord.class);
	public static final String ACTION_DELETERECORD            = ClassUtil.getShortClassName(DeleteRecord.class);
	public static final String ACTION_CLEARGROUP              = ClassUtil.getShortClassName(ClearGroup.class);
	public static final String ACTION_LOCALSEARCH             = "Local"+ClassUtil.getShortClassName(Search.class);
  public static final String ACTION_SEARCH                  = ClassUtil.getShortClassName(Search.class);
  public static final String ACTION_SEARCH_UPDATE           = ClassUtil.getShortClassName(SearchUpdateRecord.class);
	public static final String ACTION_SELECTED                = ClassUtil.getShortClassName(RecordSelected.class);
	public static final String ACTION_NAVIGATETOFORM          = ClassUtil.getShortClassName(NavigateToForm.class);
	
	public static final String FILLDIRECTION_BOTH             = CastorFilldirection.BOTH.toString();
	public static final String FILLDIRECTION_FORWARD          = CastorFilldirection.FORWARD.toString();
	public static final String FILLDIRECTION_BACKWARD         = CastorFilldirection.BACKWARD.toString();
	public static final String FILLDIRECTION_NONE             = CastorFilldirection.NONE.toString();

  public static final String EDITMODE_FULLEDIT              = TextInputModeType.FULLEDIT.toString();
  public static final String EDITMODE_APPEND                = TextInputModeType.APPEND.toString();
  public static final String EDITMODE_PREPEND               = TextInputModeType.PREPEND.toString();

  public final static String RELATIONSET_DEFAULT = "default";
	public final static String RELATIONSET_LOCAL   = "local";

	public static final String PROPERTYGROUP_FEATURES      ="Extended features";
	public static final String PROPERTYGROUP_DB            ="Database";
	public static final String PROPERTYGROUP_FONT          ="Font";
	public static final String PROPERTYGROUP_ENUM          ="Enum Values";
	public static final String PROPERTYGROUP_COMMON        ="Common";
	public static final String PROPERTYGROUP_STYLE         ="Style";
	public static final String PROPERTYGROUP_LOCATION      ="Location";
	public static final String PROPERTYGROUP_DIMENSION     =PROPERTYGROUP_LOCATION;//"Dimension";
	public static final String PROPERTYGROUP_ACTION        ="Action";

  public final static String SORTORDER_NONE       = "none";
  public final static String SORTORDER_ASCENDING  = CastorSortOrder.ASCENDING.toString();
  public final static String SORTORDER_DESCENDING = CastorSortOrder.DESCENDING.toString();
  
  public final static String EXECUTES_SCOPE_OUTERGROUP = UpdateRecordExecuteScopeType.OUTER_GROUP.toString();
  public final static String EXECUTES_SCOPE_TAB_PANE   = UpdateRecordExecuteScopeType.TAB_PANE.toString();

  public final static String ALIGN_LEFT   = CastorHorizontalAlignment.LEFT.toString();
  public final static String ALIGN_CENTER = CastorHorizontalAlignment.CENTER.toString();
  public final static String ALIGN_RIGHT  = CastorHorizontalAlignment.RIGHT.toString();
  
  public final static String RECONFIGUREMODE_FULL  = CastorDataSourceReconfigureType.FULL.toString();
  public final static String RECONFIGUREMODE_NONE  = CastorDataSourceReconfigureType.NONE.toString();

  public final static String DATA_SCOPE_DOMAIN = CastorDomainDataScopeType.DOMAIN.toString();
  public final static String DATA_SCOPE_FORM   = CastorDomainDataScopeType.FORM.toString();
  public final static String DATA_SCOPE_GROUP  = CastorDomainDataScopeType.GROUP.toString();

  public final static String FONTFAMILY_CURSIVE    = CastorFontFamilyType.CURSIVE.toString();
  public final static String FONTFAMILY_FANTASY    = CastorFontFamilyType.FANTASY.toString();
  public final static String FONTFAMILY_MONOSPACE  = CastorFontFamilyType.MONOSPACE.toString();
  public final static String FONTFAMILY_SANS_SERIF = CastorFontFamilyType.SANS_SERIF.toString();
  public final static String FONTFAMILY_SERIF      = CastorFontFamilyType.SERIF.toString();

  public final static String FONTSTYLE_ITALIC      = CastorFontStyleType.ITALIC.toString();
  public final static String FONTSTYLE_NORMAL      = CastorFontStyleType.NORMAL.toString();

  public final static String FONTWEIGHT_BOLD       = CastorFontWeightType.BOLD.toString();
  public final static String FONTWEIGHT_NORMAL     = CastorFontWeightType.NORMAL.toString();

  public final static String LINKTARGET_NEW_WINDOW    = CastorExternalFormTargetType.NEW_WINDOW.toString();
  public final static String LINKTARGET_CONTENT_AREA  = CastorExternalFormTargetType.CONTENT_AREA.toString();
  public final static String lINKTARGET_CURRENT_WINDOW= CastorExternalFormTargetType.CURRENT_WINDOW.toString();

  public final static String CONTENT_TYPE_PLAIN = LongTextInputContentTypeType.TEXT_PLAIN.toString();
  public final static String CONTENT_TYPE_HTML = LongTextInputContentTypeType.TEXT_HTML.toString();
  public final static String CONTENT_TYPE_WIKI   = LongTextInputContentTypeType.TEXT_WIKI.toString();
  
  public final static String RESIZE_MODE_NONE   = CastorResizeMode.NONE.toString();
  public final static String RESIZE_MODE_BOOTH   = CastorResizeMode.BOOTH.toString();
  public final static String RESIZE_MODE_WIDTH  = CastorResizeMode.WIDTH.toString();
  public final static String RESIZE_MODE_HEIGHT   = CastorResizeMode.HEIGHT.toString();

  private PropertyChangeSupport        listeners;

	public static final List<String> FILLDIRECTIONS;
  public static final List<String> EXECUTE_SCOPES;
	public static final List<String> ACTIONS;
	public static final List<String> SORTORDERS;

  public static final List<String> FONT_FAMILIES;
  public static final List<String> FONT_STYLES;
  public static final List<String> FONT_WEIGHTS;
  public static final List<String> ALIGNS;
  public static final List<String> EDIT_MODES;
  public static final List<String> RECONFIGURE_MODES;
  public static final List<String> CONTENT_TYPES;
  
  public static final List<String> LINK_TARGETS;
  public static final List<String> DATA_SCOPES;
  public static final List<String> RESIZE_MODES;

  static
	{
    List<String> list = new ArrayList<String>();
    list.add(DATA_SCOPE_DOMAIN);
    list.add(DATA_SCOPE_FORM); 
    list.add(DATA_SCOPE_GROUP); 
    DATA_SCOPES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(RESIZE_MODE_NONE);
    list.add(RESIZE_MODE_BOOTH);
    list.add(RESIZE_MODE_WIDTH);
    list.add(RESIZE_MODE_HEIGHT);
    RESIZE_MODES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(EXECUTES_SCOPE_OUTERGROUP);
    list.add(EXECUTES_SCOPE_TAB_PANE); 
    EXECUTE_SCOPES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(CONTENT_TYPE_PLAIN);
    list.add(CONTENT_TYPE_WIKI); 
    list.add(CONTENT_TYPE_HTML); 
    CONTENT_TYPES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(RECONFIGUREMODE_FULL);
    list.add(RECONFIGUREMODE_NONE); 
    RECONFIGURE_MODES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(EDITMODE_FULLEDIT);
    list.add(EDITMODE_APPEND); 
    list.add(EDITMODE_PREPEND); 
    EDIT_MODES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(ALIGN_LEFT);
    list.add(ALIGN_CENTER); 
    list.add(ALIGN_RIGHT); 
    ALIGNS = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(LINKTARGET_NEW_WINDOW);
    list.add(LINKTARGET_CONTENT_AREA); 
    list.add(lINKTARGET_CURRENT_WINDOW); 
    LINK_TARGETS = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(FONTFAMILY_CURSIVE);
    list.add(FONTFAMILY_FANTASY); 
    list.add(FONTFAMILY_MONOSPACE); 
    list.add(FONTFAMILY_SANS_SERIF); 
    list.add(FONTFAMILY_SERIF); 
    FONT_FAMILIES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(FONTSTYLE_NORMAL); 
    list.add(FONTSTYLE_ITALIC);
    FONT_STYLES = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
    list.add(FONTWEIGHT_NORMAL);
    list.add(FONTWEIGHT_BOLD); 
    FONT_WEIGHTS = ListUtils.unmodifiableList(list);

    list = new ArrayList<String>();
	  list.add(FILLDIRECTION_BOTH);
	  list.add(FILLDIRECTION_BACKWARD); 
	  list.add(FILLDIRECTION_FORWARD); 
	  list.add(FILLDIRECTION_NONE); 
	  FILLDIRECTIONS = ListUtils.unmodifiableList(list);
	  
	  list = new ArrayList<String>();
	  list.add(ACTION_GENERIC);
	  list.add(ACTION_NEWRECORD);
	  list.add(ACTION_UPDATERECORD);
	  list.add(ACTION_DELETERECORD);
	  list.add(ACTION_CLEARGROUP);
    list.add(ACTION_SEARCH);
    list.add(ACTION_LOCALSEARCH);
	  list.add(ACTION_SELECTED);
	  list.add(ACTION_NAVIGATETOFORM);
	  ACTIONS = ListUtils.unmodifiableList(list);

	  list = new ArrayList<String>();
	  list.add(SORTORDER_NONE); // first element ist the default for new created UI elements
	  list.add(SORTORDER_ASCENDING);
	  list.add(SORTORDER_DESCENDING);
	  SORTORDERS = ListUtils.unmodifiableList(list);
}

	private JacobModel jacobModel;
	
  public ObjectModel()
  {
    listeners = new PropertyChangeSupport(this);
  }

  public ObjectModel(JacobModel jacobModel)
  {
    this();
    this.jacobModel = jacobModel;
  }

  public abstract String getError();
  public abstract String getWarning();
  public abstract String getInfo();
  public abstract boolean isInUse();
  public abstract String getName();
  public abstract ObjectModel getParent();
  
  public String getLabel()
  {
    return getName();
  }
  
  public String getExtendedDescriptionLabel()
  {
    return getLabel();
  }
  
  public abstract void addReferrerObject(ReferenceSearchResult result, ObjectModel model);
  
  public void setJacobModel(JacobModel jacobModel)
  {
    this.jacobModel = jacobModel;
  }
  
  public final JacobModel getJacobModel()
  {
    return jacobModel;
  }
  
  public String getImageBaseName()
  {
    return ClassUtil.getShortClassName(this.getClass());
  }
  
  public final String getImageName()
  {
    return getImageBaseName()+".png";
  }
  
  public final Image getImage()
  {
    if(getError()!=null)
      return JacobDesigner.getImage(getImageName(), JacobDesigner.DECORATION_ERROR);
    
    if(getWarning()!=null)
      return JacobDesigner.getImage(getImageName(), JacobDesigner.DECORATION_WARNING);
    
    if(getInfo()!=null)
      return JacobDesigner.getImage(getImageName(), JacobDesigner.DECORATION_INFO);

    return JacobDesigner.getImage(getImageName(), JacobDesigner.DECORATION_NONE);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    listeners.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    listeners.removePropertyChangeListener(listener);
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
  {
    if(true)
    {
      listeners.firePropertyChange(propertyName, oldValue, newValue);
    }
    /* FOR DEBUGGIN - don't remove!!!!!!
     */
    else
    {
      StringBuffer sb = new StringBuffer(1024*10);
      PropertyChangeListener[] targets=listeners.getPropertyChangeListeners();
      System.out.println("calling "+targets.length+" listerns for event "+propertyName);
      long start = System.currentTimeMillis();
      PropertyChangeEvent evt = new PropertyChangeEvent(this,  propertyName, oldValue, newValue);
      for (int i = 0; i < targets.length; i++) 
      {
        PropertyChangeListener target = (PropertyChangeListener)targets[i];
        long s2 = System.currentTimeMillis();
        target.propertyChange(evt);
        sb.append("\n duration:");
        sb.append(Long.toString(System.currentTimeMillis()-s2));
        sb.append("\t ");
        sb.append(target.getClass().getName());
      }
      System.out.println(sb.toString());
      System.out.println("Duration:"+(System.currentTimeMillis()-start));
    }    
    // bei einem Aufruf "jacobModel.setDirty(false);" w�rd automatisch dirty wieder auf true gestzt
    // werden. Hier beist sich die Katze in den Schwanz.....
    //
    if(propertyName!=PROPERTY_JACOBMODEL_CHANGED && JacobDesigner.getPlugin().getModel()!=null)
      JacobDesigner.getPlugin().getModel().setDirty(true);
  }

  public Object getEditableValue()
  {
    return this;
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[]  descriptors = new IPropertyDescriptor[0];
    
    // Nach Plugins suchen welche das ObjectModel evnetuell um erweiterte 
    // Properties anreichern
    for (IPropertyPlugin plugin : PropertyPluginManager.createComponentPlugin(this))
    {
      descriptors = plugin.getPropertyDescriptors(this, descriptors);
    }
    return descriptors;
  }

  public Object getPropertyValue(Object propName)
  {
    for (IPropertyPlugin plugin : PropertyPluginManager.createComponentPlugin(this))
    {
      Object val =plugin.getPropertyValue(this, propName);
      if(val!=null)
        return val;
    }
    return null;
  }

  /*
   * final Object getPropertyValue(String propName) { return null; }
   */
  public boolean isPropertySet(Object propName)
  {
    return true;
  }

  /*
   * final boolean isPropertySet(String propName) { return true; }
   */
  public void resetPropertyValue(Object propName)
  {
  }

  public void setPropertyValue(Object propName, Object val)
  {
    for (IPropertyPlugin plugin : PropertyPluginManager.createComponentPlugin(this))
    {
      if(plugin.setPropertyValue(this, propName,val)==true)
        return;
    }
  }
  
  /**
   * Returns the template filename for the jACOB-Hook class or null if it not possible
   * to assign a script to this element. 
   * 
   * @return
   * @see #getTemplateClass
   */
  public String getTemplateFileName()
  {
    return null;
  }
  
  /**
   * Returns the template class for the jACOB Hook. Either the getTemplateClass returns an interface or the
   * {@link #getTemplateFileName()} returns the file name of the Hook interface. It is possible that both
   * methods returns <b>null</b>. In this case the element can't have any event handler.
   * 
   * @return the interface declaration of the event handler of the element.
   * @see #getTemplateFileName
   */
  public Class getTemplateClass()
  {
    return null;
  }
 
  
  public String getHookClassName()
  {
    return null;
  }

  public void setHookClassName(String className) throws Exception
  {
    
  }
  
	public void  resetHookClassName() throws Exception
	{
	  throw new Exception("Class ["+this.getClass().getName()+"] doesn't support java hooks at the moment.");
	}

	public void  generateHookClassName() throws Exception
	{
	  throw new Exception("Class ["+this.getClass().getName()+"] doesn't support java hooks at the moment.");
	}


	public static List getFilldirections()
  {
    return FILLDIRECTIONS;
  }

  public ObjectModel getTreeObjectModel()
  {
    return this;
  }
  
  public Version getRequiredJacobVersion()
  {
    return version;
  }  
  

  public Object getAdapter(Class aClass)
  {
      if (aClass == this.getClass())
        return this;
      
      return null;
  }
  
}