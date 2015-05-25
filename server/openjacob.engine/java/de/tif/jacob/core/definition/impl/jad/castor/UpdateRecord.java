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

import de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType;
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
 * Class UpdateRecord.
 * 
 * @version $Revision$ $Date$
 */
public class UpdateRecord implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _changeUpdate
     */
    private boolean _changeUpdate;

    /**
     * keeps track of state for field: _changeUpdate
     */
    private boolean _has_changeUpdate;

    /**
     * Field _executeScope
     */
    private de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType _executeScope = de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType.valueOf("outer group");


      //----------------/
     //- Constructors -/
    //----------------/

    public UpdateRecord() {
        super();
        setExecuteScope(de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType.valueOf("outer group"));
    } //-- de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteChangeUpdate
     */
    public void deleteChangeUpdate()
    {
        this._has_changeUpdate= false;
    } //-- void deleteChangeUpdate() 

    /**
     * Returns the value of field 'changeUpdate'.
     * 
     * @return the value of field 'changeUpdate'.
     */
    public boolean getChangeUpdate()
    {
        return this._changeUpdate;
    } //-- boolean getChangeUpdate() 

    /**
     * Returns the value of field 'executeScope'.
     * 
     * @return the value of field 'executeScope'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType getExecuteScope()
    {
        return this._executeScope;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType getExecuteScope() 

    /**
     * Method hasChangeUpdate
     */
    public boolean hasChangeUpdate()
    {
        return this._has_changeUpdate;
    } //-- boolean hasChangeUpdate() 

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
     * Sets the value of field 'changeUpdate'.
     * 
     * @param changeUpdate the value of field 'changeUpdate'.
     */
    public void setChangeUpdate(boolean changeUpdate)
    {
        this._changeUpdate = changeUpdate;
        this._has_changeUpdate = true;
    } //-- void setChangeUpdate(boolean) 

    /**
     * Sets the value of field 'executeScope'.
     * 
     * @param executeScope the value of field 'executeScope'.
     */
    public void setExecuteScope(de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType executeScope)
    {
        this._executeScope = executeScope;
    } //-- void setExecuteScope(de.tif.jacob.core.definition.impl.jad.castor.types.UpdateRecordExecuteScopeType) 

    /**
     * Method unmarshalUpdateRecord
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalUpdateRecord(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord.class, reader);
    } //-- java.lang.Object unmarshalUpdateRecord(java.io.Reader) 

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
