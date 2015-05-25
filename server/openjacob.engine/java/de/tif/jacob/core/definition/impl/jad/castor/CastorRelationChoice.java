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
 * Class CastorRelationChoice.
 * 
 * @version $Revision$ $Date$
 */
public class CastorRelationChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _oneToMany
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany _oneToMany;

    /**
     * Field _manyToMany
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany _manyToMany;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorRelationChoice() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorRelationChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'manyToMany'.
     * 
     * @return the value of field 'manyToMany'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany getManyToMany()
    {
        return this._manyToMany;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany getManyToMany() 

    /**
     * Returns the value of field 'oneToMany'.
     * 
     * @return the value of field 'oneToMany'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany getOneToMany()
    {
        return this._oneToMany;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany getOneToMany() 

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
     * Sets the value of field 'manyToMany'.
     * 
     * @param manyToMany the value of field 'manyToMany'.
     */
    public void setManyToMany(de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany manyToMany)
    {
        this._manyToMany = manyToMany;
    } //-- void setManyToMany(de.tif.jacob.core.definition.impl.jad.castor.CastorManyToMany) 

    /**
     * Sets the value of field 'oneToMany'.
     * 
     * @param oneToMany the value of field 'oneToMany'.
     */
    public void setOneToMany(de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany oneToMany)
    {
        this._oneToMany = oneToMany;
    } //-- void setOneToMany(de.tif.jacob.core.definition.impl.jad.castor.CastorOneToMany) 

    /**
     * Method unmarshalCastorRelationChoice
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorRelationChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorRelationChoice) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorRelationChoice.class, reader);
    } //-- java.lang.Object unmarshalCastorRelationChoice(java.io.Reader) 

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
