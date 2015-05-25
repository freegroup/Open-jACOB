/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.core.definition.impl.jad.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Jacob.
 * 
 * @version $Revision$ $Date$
 */
public class Jacob implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _version
     */
    private java.lang.String _version;

    /**
     * Field _engineVersion
     */
    private java.lang.String _engineVersion;

    /**
     * Field _defaultApplication
     */
    private java.lang.String _defaultApplication;

    /**
     * Field _erroneous
     */
    private boolean _erroneous = false;

    /**
     * keeps track of state for field: _erroneous
     */
    private boolean _has_erroneous;

    /**
     * Field _dataSources
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DataSources _dataSources;

    /**
     * Sollte beim nächsten Majorrelease zu Pflicht gemacht werden!
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Roles _roles;

    /**
     * Field _tables
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Tables _tables;

    /**
     * Field _tableAliases
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TableAliases _tableAliases;

    /**
     * Field _browsers
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Browsers _browsers;

    /**
     * Field _relations
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Relations _relations;

    /**
     * Field _relationsets
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Relationsets _relationsets;

    /**
     * Field _forms
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Forms _forms;

    /**
     * Field _domains
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Domains _domains;

    /**
     * Field _applications
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Applications _applications;

    /**
     * Kann beim nächsten Majorrelease gelöscht werden. Wird nicht
     * mehr benötigt!
     */
    private de.tif.jacob.core.definition.impl.jad.castor.ExternalModules _externalModules;

    /**
     * Field _diagrams
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Diagrams _diagrams;


      //----------------/
     //- Constructors -/
    //----------------/

    public Jacob() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Jacob()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteErroneous
     */
    public void deleteErroneous()
    {
        this._has_erroneous= false;
    } //-- void deleteErroneous() 

    /**
     * Returns the value of field 'applications'.
     * 
     * @return the value of field 'applications'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Applications getApplications()
    {
        return this._applications;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Applications getApplications() 

    /**
     * Returns the value of field 'browsers'.
     * 
     * @return the value of field 'browsers'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Browsers getBrowsers()
    {
        return this._browsers;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Browsers getBrowsers() 

    /**
     * Returns the value of field 'dataSources'.
     * 
     * @return the value of field 'dataSources'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DataSources getDataSources()
    {
        return this._dataSources;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DataSources getDataSources() 

    /**
     * Returns the value of field 'defaultApplication'.
     * 
     * @return the value of field 'defaultApplication'.
     */
    public java.lang.String getDefaultApplication()
    {
        return this._defaultApplication;
    } //-- java.lang.String getDefaultApplication() 

    /**
     * Returns the value of field 'diagrams'.
     * 
     * @return the value of field 'diagrams'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Diagrams getDiagrams()
    {
        return this._diagrams;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Diagrams getDiagrams() 

    /**
     * Returns the value of field 'domains'.
     * 
     * @return the value of field 'domains'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Domains getDomains()
    {
        return this._domains;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Domains getDomains() 

    /**
     * Returns the value of field 'engineVersion'.
     * 
     * @return the value of field 'engineVersion'.
     */
    public java.lang.String getEngineVersion()
    {
        return this._engineVersion;
    } //-- java.lang.String getEngineVersion() 

    /**
     * Returns the value of field 'erroneous'.
     * 
     * @return the value of field 'erroneous'.
     */
    public boolean getErroneous()
    {
        return this._erroneous;
    } //-- boolean getErroneous() 

    /**
     * Returns the value of field 'externalModules'. The field
     * 'externalModules' has the following description: Kann beim
     * nächsten Majorrelease gelöscht werden. Wird nicht mehr
     * benötigt!
     * 
     * @return the value of field 'externalModules'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ExternalModules getExternalModules()
    {
        return this._externalModules;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ExternalModules getExternalModules() 

    /**
     * Returns the value of field 'forms'.
     * 
     * @return the value of field 'forms'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Forms getForms()
    {
        return this._forms;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Forms getForms() 

    /**
     * Returns the value of field 'relations'.
     * 
     * @return the value of field 'relations'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Relations getRelations()
    {
        return this._relations;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Relations getRelations() 

    /**
     * Returns the value of field 'relationsets'.
     * 
     * @return the value of field 'relationsets'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Relationsets getRelationsets()
    {
        return this._relationsets;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Relationsets getRelationsets() 

    /**
     * Returns the value of field 'roles'. The field 'roles' has
     * the following description: Sollte beim nächsten Majorrelease
     * zu Pflicht gemacht werden!
     * 
     * @return the value of field 'roles'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Roles getRoles()
    {
        return this._roles;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Roles getRoles() 

    /**
     * Returns the value of field 'tableAliases'.
     * 
     * @return the value of field 'tableAliases'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TableAliases getTableAliases()
    {
        return this._tableAliases;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TableAliases getTableAliases() 

    /**
     * Returns the value of field 'tables'.
     * 
     * @return the value of field 'tables'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Tables getTables()
    {
        return this._tables;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Tables getTables() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return the value of field 'version'.
     */
    public java.lang.String getVersion()
    {
        return this._version;
    } //-- java.lang.String getVersion() 

    /**
     * Method hasErroneous
     */
    public boolean hasErroneous()
    {
        return this._has_erroneous;
    } //-- boolean hasErroneous() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'applications'.
     * 
     * @param applications the value of field 'applications'.
     */
    public void setApplications(de.tif.jacob.core.definition.impl.jad.castor.Applications applications)
    {
        this._applications = applications;
    } //-- void setApplications(de.tif.jacob.core.definition.impl.jad.castor.Applications) 

    /**
     * Sets the value of field 'browsers'.
     * 
     * @param browsers the value of field 'browsers'.
     */
    public void setBrowsers(de.tif.jacob.core.definition.impl.jad.castor.Browsers browsers)
    {
        this._browsers = browsers;
    } //-- void setBrowsers(de.tif.jacob.core.definition.impl.jad.castor.Browsers) 

    /**
     * Sets the value of field 'dataSources'.
     * 
     * @param dataSources the value of field 'dataSources'.
     */
    public void setDataSources(de.tif.jacob.core.definition.impl.jad.castor.DataSources dataSources)
    {
        this._dataSources = dataSources;
    } //-- void setDataSources(de.tif.jacob.core.definition.impl.jad.castor.DataSources) 

    /**
     * Sets the value of field 'defaultApplication'.
     * 
     * @param defaultApplication the value of field
     * 'defaultApplication'.
     */
    public void setDefaultApplication(java.lang.String defaultApplication)
    {
        this._defaultApplication = defaultApplication;
    } //-- void setDefaultApplication(java.lang.String) 

    /**
     * Sets the value of field 'diagrams'.
     * 
     * @param diagrams the value of field 'diagrams'.
     */
    public void setDiagrams(de.tif.jacob.core.definition.impl.jad.castor.Diagrams diagrams)
    {
        this._diagrams = diagrams;
    } //-- void setDiagrams(de.tif.jacob.core.definition.impl.jad.castor.Diagrams) 

    /**
     * Sets the value of field 'domains'.
     * 
     * @param domains the value of field 'domains'.
     */
    public void setDomains(de.tif.jacob.core.definition.impl.jad.castor.Domains domains)
    {
        this._domains = domains;
    } //-- void setDomains(de.tif.jacob.core.definition.impl.jad.castor.Domains) 

    /**
     * Sets the value of field 'engineVersion'.
     * 
     * @param engineVersion the value of field 'engineVersion'.
     */
    public void setEngineVersion(java.lang.String engineVersion)
    {
        this._engineVersion = engineVersion;
    } //-- void setEngineVersion(java.lang.String) 

    /**
     * Sets the value of field 'erroneous'.
     * 
     * @param erroneous the value of field 'erroneous'.
     */
    public void setErroneous(boolean erroneous)
    {
        this._erroneous = erroneous;
        this._has_erroneous = true;
    } //-- void setErroneous(boolean) 

    /**
     * Sets the value of field 'externalModules'. The field
     * 'externalModules' has the following description: Kann beim
     * nächsten Majorrelease gelöscht werden. Wird nicht mehr
     * benötigt!
     * 
     * @param externalModules the value of field 'externalModules'.
     */
    public void setExternalModules(de.tif.jacob.core.definition.impl.jad.castor.ExternalModules externalModules)
    {
        this._externalModules = externalModules;
    } //-- void setExternalModules(de.tif.jacob.core.definition.impl.jad.castor.ExternalModules) 

    /**
     * Sets the value of field 'forms'.
     * 
     * @param forms the value of field 'forms'.
     */
    public void setForms(de.tif.jacob.core.definition.impl.jad.castor.Forms forms)
    {
        this._forms = forms;
    } //-- void setForms(de.tif.jacob.core.definition.impl.jad.castor.Forms) 

    /**
     * Sets the value of field 'relations'.
     * 
     * @param relations the value of field 'relations'.
     */
    public void setRelations(de.tif.jacob.core.definition.impl.jad.castor.Relations relations)
    {
        this._relations = relations;
    } //-- void setRelations(de.tif.jacob.core.definition.impl.jad.castor.Relations) 

    /**
     * Sets the value of field 'relationsets'.
     * 
     * @param relationsets the value of field 'relationsets'.
     */
    public void setRelationsets(de.tif.jacob.core.definition.impl.jad.castor.Relationsets relationsets)
    {
        this._relationsets = relationsets;
    } //-- void setRelationsets(de.tif.jacob.core.definition.impl.jad.castor.Relationsets) 

    /**
     * Sets the value of field 'roles'. The field 'roles' has the
     * following description: Sollte beim nächsten Majorrelease zu
     * Pflicht gemacht werden!
     * 
     * @param roles the value of field 'roles'.
     */
    public void setRoles(de.tif.jacob.core.definition.impl.jad.castor.Roles roles)
    {
        this._roles = roles;
    } //-- void setRoles(de.tif.jacob.core.definition.impl.jad.castor.Roles) 

    /**
     * Sets the value of field 'tableAliases'.
     * 
     * @param tableAliases the value of field 'tableAliases'.
     */
    public void setTableAliases(de.tif.jacob.core.definition.impl.jad.castor.TableAliases tableAliases)
    {
        this._tableAliases = tableAliases;
    } //-- void setTableAliases(de.tif.jacob.core.definition.impl.jad.castor.TableAliases) 

    /**
     * Sets the value of field 'tables'.
     * 
     * @param tables the value of field 'tables'.
     */
    public void setTables(de.tif.jacob.core.definition.impl.jad.castor.Tables tables)
    {
        this._tables = tables;
    } //-- void setTables(de.tif.jacob.core.definition.impl.jad.castor.Tables) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version)
    {
        this._version = version;
    } //-- void setVersion(java.lang.String) 

    /**
     * Method unmarshalJacob
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalJacob(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Jacob) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Jacob.class, reader);
    } //-- java.lang.Object unmarshalJacob(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
