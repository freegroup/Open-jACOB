/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.report.impl.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Comment describing your root element
 * 
 * @version $Revision$ $Date$
 */
public class ReportDefinition implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _guid
     */
    private java.lang.String _guid;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _desciption
     */
    private java.lang.String _desciption;

    /**
     * The anchor domain for this report. This value must be set if
     * you want backfill the report into the GUI. 
     */
    private java.lang.String _anchorDomain;

    /**
     * The anchor form for this report. This value must be set if
     * you want backfill the report into the GUI. 
     */
    private java.lang.String _anchorForm;

    /**
     * Field _application
     */
    private de.tif.jacob.report.impl.castor.Application _application;

    /**
     * Field _input
     */
    private de.tif.jacob.report.impl.castor.Input _input;

    /**
     * Field _output
     */
    private de.tif.jacob.report.impl.castor.Output _output;

    /**
     * Field _layouts
     */
    private de.tif.jacob.report.impl.castor.Layouts _layouts;

    /**
     * the user and the cron rule for an report 'news letter'. The
     * report will be send to the url (via YAN) with the cronRule
     * time intervall
     */
    private java.util.Vector _notifyeeList;

    /**
     * default mimeType for the report is application/excel
     */
    private java.lang.String _defaultMimeType = "application/excel";

    /**
     * The loginId of the user which owns this report. The report
     * is not visible to other users, if the 'private' attribute is
     * set.
     */
    private java.lang.String _owner;

    /**
     * if the flag is set, the report is only visible for the owner
     */
    private boolean _private;

    /**
     * keeps track of state for field: _private
     */
    private boolean _has_private;


      //----------------/
     //- Constructors -/
    //----------------/

    public ReportDefinition() {
        super();
        _notifyeeList = new Vector();
        setDefaultMimeType("application/excel");
    } //-- de.tif.jacob.report.impl.castor.ReportDefinition()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addNotifyee
     * 
     * @param vNotifyee
     */
    public void addNotifyee(de.tif.jacob.report.impl.castor.Notifyee vNotifyee)
        throws java.lang.IndexOutOfBoundsException
    {
        _notifyeeList.addElement(vNotifyee);
    } //-- void addNotifyee(de.tif.jacob.report.impl.castor.Notifyee) 

    /**
     * Method addNotifyee
     * 
     * @param index
     * @param vNotifyee
     */
    public void addNotifyee(int index, de.tif.jacob.report.impl.castor.Notifyee vNotifyee)
        throws java.lang.IndexOutOfBoundsException
    {
        _notifyeeList.insertElementAt(vNotifyee, index);
    } //-- void addNotifyee(int, de.tif.jacob.report.impl.castor.Notifyee) 

    /**
     * Method deletePrivate
     */
    public void deletePrivate()
    {
        this._has_private= false;
    } //-- void deletePrivate() 

    /**
     * Method enumerateNotifyee
     */
    public java.util.Enumeration enumerateNotifyee()
    {
        return _notifyeeList.elements();
    } //-- java.util.Enumeration enumerateNotifyee() 

    /**
     * Returns the value of field 'anchorDomain'. The field
     * 'anchorDomain' has the following description: The anchor
     * domain for this report. This value must be set if you want
     * backfill the report into the GUI. 
     * 
     * @return the value of field 'anchorDomain'.
     */
    public java.lang.String getAnchorDomain()
    {
        return this._anchorDomain;
    } //-- java.lang.String getAnchorDomain() 

    /**
     * Returns the value of field 'anchorForm'. The field
     * 'anchorForm' has the following description: The anchor form
     * for this report. This value must be set if you want backfill
     * the report into the GUI. 
     * 
     * @return the value of field 'anchorForm'.
     */
    public java.lang.String getAnchorForm()
    {
        return this._anchorForm;
    } //-- java.lang.String getAnchorForm() 

    /**
     * Returns the value of field 'application'.
     * 
     * @return the value of field 'application'.
     */
    public de.tif.jacob.report.impl.castor.Application getApplication()
    {
        return this._application;
    } //-- de.tif.jacob.report.impl.castor.Application getApplication() 

    /**
     * Returns the value of field 'defaultMimeType'. The field
     * 'defaultMimeType' has the following description: default
     * mimeType for the report is application/excel
     * 
     * @return the value of field 'defaultMimeType'.
     */
    public java.lang.String getDefaultMimeType()
    {
        return this._defaultMimeType;
    } //-- java.lang.String getDefaultMimeType() 

    /**
     * Returns the value of field 'desciption'.
     * 
     * @return the value of field 'desciption'.
     */
    public java.lang.String getDesciption()
    {
        return this._desciption;
    } //-- java.lang.String getDesciption() 

    /**
     * Returns the value of field 'guid'.
     * 
     * @return the value of field 'guid'.
     */
    public java.lang.String getGuid()
    {
        return this._guid;
    } //-- java.lang.String getGuid() 

    /**
     * Returns the value of field 'input'.
     * 
     * @return the value of field 'input'.
     */
    public de.tif.jacob.report.impl.castor.Input getInput()
    {
        return this._input;
    } //-- de.tif.jacob.report.impl.castor.Input getInput() 

    /**
     * Returns the value of field 'layouts'.
     * 
     * @return the value of field 'layouts'.
     */
    public de.tif.jacob.report.impl.castor.Layouts getLayouts()
    {
        return this._layouts;
    } //-- de.tif.jacob.report.impl.castor.Layouts getLayouts() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getNotifyee
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.Notifyee getNotifyee(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _notifyeeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.report.impl.castor.Notifyee) _notifyeeList.elementAt(index);
    } //-- de.tif.jacob.report.impl.castor.Notifyee getNotifyee(int) 

    /**
     * Method getNotifyee
     */
    public de.tif.jacob.report.impl.castor.Notifyee[] getNotifyee()
    {
        int size = _notifyeeList.size();
        de.tif.jacob.report.impl.castor.Notifyee[] mArray = new de.tif.jacob.report.impl.castor.Notifyee[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.report.impl.castor.Notifyee) _notifyeeList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.report.impl.castor.Notifyee[] getNotifyee() 

    /**
     * Method getNotifyeeCount
     */
    public int getNotifyeeCount()
    {
        return _notifyeeList.size();
    } //-- int getNotifyeeCount() 

    /**
     * Returns the value of field 'output'.
     * 
     * @return the value of field 'output'.
     */
    public de.tif.jacob.report.impl.castor.Output getOutput()
    {
        return this._output;
    } //-- de.tif.jacob.report.impl.castor.Output getOutput() 

    /**
     * Returns the value of field 'owner'. The field 'owner' has
     * the following description: The loginId of the user which
     * owns this report. The report is not visible to other users,
     * if the 'private' attribute is set.
     * 
     * @return the value of field 'owner'.
     */
    public java.lang.String getOwner()
    {
        return this._owner;
    } //-- java.lang.String getOwner() 

    /**
     * Returns the value of field 'private'. The field 'private'
     * has the following description: if the flag is set, the
     * report is only visible for the owner
     * 
     * @return the value of field 'private'.
     */
    public boolean getPrivate()
    {
        return this._private;
    } //-- boolean getPrivate() 

    /**
     * Method hasPrivate
     */
    public boolean hasPrivate()
    {
        return this._has_private;
    } //-- boolean hasPrivate() 

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
     * Method removeAllNotifyee
     */
    public void removeAllNotifyee()
    {
        _notifyeeList.removeAllElements();
    } //-- void removeAllNotifyee() 

    /**
     * Method removeNotifyee
     * 
     * @param index
     */
    public de.tif.jacob.report.impl.castor.Notifyee removeNotifyee(int index)
    {
        java.lang.Object obj = _notifyeeList.elementAt(index);
        _notifyeeList.removeElementAt(index);
        return (de.tif.jacob.report.impl.castor.Notifyee) obj;
    } //-- de.tif.jacob.report.impl.castor.Notifyee removeNotifyee(int) 

    /**
     * Sets the value of field 'anchorDomain'. The field
     * 'anchorDomain' has the following description: The anchor
     * domain for this report. This value must be set if you want
     * backfill the report into the GUI. 
     * 
     * @param anchorDomain the value of field 'anchorDomain'.
     */
    public void setAnchorDomain(java.lang.String anchorDomain)
    {
        this._anchorDomain = anchorDomain;
    } //-- void setAnchorDomain(java.lang.String) 

    /**
     * Sets the value of field 'anchorForm'. The field 'anchorForm'
     * has the following description: The anchor form for this
     * report. This value must be set if you want backfill the
     * report into the GUI. 
     * 
     * @param anchorForm the value of field 'anchorForm'.
     */
    public void setAnchorForm(java.lang.String anchorForm)
    {
        this._anchorForm = anchorForm;
    } //-- void setAnchorForm(java.lang.String) 

    /**
     * Sets the value of field 'application'.
     * 
     * @param application the value of field 'application'.
     */
    public void setApplication(de.tif.jacob.report.impl.castor.Application application)
    {
        this._application = application;
    } //-- void setApplication(de.tif.jacob.report.impl.castor.Application) 

    /**
     * Sets the value of field 'defaultMimeType'. The field
     * 'defaultMimeType' has the following description: default
     * mimeType for the report is application/excel
     * 
     * @param defaultMimeType the value of field 'defaultMimeType'.
     */
    public void setDefaultMimeType(java.lang.String defaultMimeType)
    {
        this._defaultMimeType = defaultMimeType;
    } //-- void setDefaultMimeType(java.lang.String) 

    /**
     * Sets the value of field 'desciption'.
     * 
     * @param desciption the value of field 'desciption'.
     */
    public void setDesciption(java.lang.String desciption)
    {
        this._desciption = desciption;
    } //-- void setDesciption(java.lang.String) 

    /**
     * Sets the value of field 'guid'.
     * 
     * @param guid the value of field 'guid'.
     */
    public void setGuid(java.lang.String guid)
    {
        this._guid = guid;
    } //-- void setGuid(java.lang.String) 

    /**
     * Sets the value of field 'input'.
     * 
     * @param input the value of field 'input'.
     */
    public void setInput(de.tif.jacob.report.impl.castor.Input input)
    {
        this._input = input;
    } //-- void setInput(de.tif.jacob.report.impl.castor.Input) 

    /**
     * Sets the value of field 'layouts'.
     * 
     * @param layouts the value of field 'layouts'.
     */
    public void setLayouts(de.tif.jacob.report.impl.castor.Layouts layouts)
    {
        this._layouts = layouts;
    } //-- void setLayouts(de.tif.jacob.report.impl.castor.Layouts) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setNotifyee
     * 
     * @param index
     * @param vNotifyee
     */
    public void setNotifyee(int index, de.tif.jacob.report.impl.castor.Notifyee vNotifyee)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _notifyeeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _notifyeeList.setElementAt(vNotifyee, index);
    } //-- void setNotifyee(int, de.tif.jacob.report.impl.castor.Notifyee) 

    /**
     * Method setNotifyee
     * 
     * @param notifyeeArray
     */
    public void setNotifyee(de.tif.jacob.report.impl.castor.Notifyee[] notifyeeArray)
    {
        //-- copy array
        _notifyeeList.removeAllElements();
        for (int i = 0; i < notifyeeArray.length; i++) {
            _notifyeeList.addElement(notifyeeArray[i]);
        }
    } //-- void setNotifyee(de.tif.jacob.report.impl.castor.Notifyee) 

    /**
     * Sets the value of field 'output'.
     * 
     * @param output the value of field 'output'.
     */
    public void setOutput(de.tif.jacob.report.impl.castor.Output output)
    {
        this._output = output;
    } //-- void setOutput(de.tif.jacob.report.impl.castor.Output) 

    /**
     * Sets the value of field 'owner'. The field 'owner' has the
     * following description: The loginId of the user which owns
     * this report. The report is not visible to other users, if
     * the 'private' attribute is set.
     * 
     * @param owner the value of field 'owner'.
     */
    public void setOwner(java.lang.String owner)
    {
        this._owner = owner;
    } //-- void setOwner(java.lang.String) 

    /**
     * Sets the value of field 'private'. The field 'private' has
     * the following description: if the flag is set, the report is
     * only visible for the owner
     * 
     * @param _private
     * @param private the value of field 'private'.
     */
    public void setPrivate(boolean _private)
    {
        this._private = _private;
        this._has_private = true;
    } //-- void setPrivate(boolean) 

    /**
     * Method unmarshalReportDefinition
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalReportDefinition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.ReportDefinition) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.ReportDefinition.class, reader);
    } //-- java.lang.Object unmarshalReportDefinition(java.io.Reader) 

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
