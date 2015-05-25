/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.ruleengine.castor;

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
 * Class Rule.
 * 
 * @version $Revision$ $Date$
 */
public class Rule implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ruleId
     */
    private java.lang.String _ruleId;

    /**
     * Field _posX
     */
    private int _posX;

    /**
     * keeps track of state for field: _posX
     */
    private boolean _has_posX;

    /**
     * Field _posY
     */
    private int _posY;

    /**
     * keeps track of state for field: _posY
     */
    private boolean _has_posY;

    /**
     * Field _businessObjectMethod
     */
    private de.tif.jacob.ruleengine.castor.BusinessObjectMethod _businessObjectMethod;

    /**
     * Field _decision
     */
    private de.tif.jacob.ruleengine.castor.Decision _decision;


      //----------------/
     //- Constructors -/
    //----------------/

    public Rule() {
        super();
    } //-- de.tif.jacob.ruleengine.castor.Rule()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deletePosX
     * 
     */
    public void deletePosX()
    {
        this._has_posX= false;
    } //-- void deletePosX() 

    /**
     * Method deletePosY
     * 
     */
    public void deletePosY()
    {
        this._has_posY= false;
    } //-- void deletePosY() 

    /**
     * Returns the value of field 'businessObjectMethod'.
     * 
     * @return BusinessObjectMethod
     * @return the value of field 'businessObjectMethod'.
     */
    public de.tif.jacob.ruleengine.castor.BusinessObjectMethod getBusinessObjectMethod()
    {
        return this._businessObjectMethod;
    } //-- de.tif.jacob.ruleengine.castor.BusinessObjectMethod getBusinessObjectMethod() 

    /**
     * Returns the value of field 'decision'.
     * 
     * @return Decision
     * @return the value of field 'decision'.
     */
    public de.tif.jacob.ruleengine.castor.Decision getDecision()
    {
        return this._decision;
    } //-- de.tif.jacob.ruleengine.castor.Decision getDecision() 

    /**
     * Returns the value of field 'posX'.
     * 
     * @return int
     * @return the value of field 'posX'.
     */
    public int getPosX()
    {
        return this._posX;
    } //-- int getPosX() 

    /**
     * Returns the value of field 'posY'.
     * 
     * @return int
     * @return the value of field 'posY'.
     */
    public int getPosY()
    {
        return this._posY;
    } //-- int getPosY() 

    /**
     * Returns the value of field 'ruleId'.
     * 
     * @return String
     * @return the value of field 'ruleId'.
     */
    public java.lang.String getRuleId()
    {
        return this._ruleId;
    } //-- java.lang.String getRuleId() 

    /**
     * Method hasPosX
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasPosX()
    {
        return this._has_posX;
    } //-- boolean hasPosX() 

    /**
     * Method hasPosY
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasPosY()
    {
        return this._has_posY;
    } //-- boolean hasPosY() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
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
     * 
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
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'businessObjectMethod'.
     * 
     * @param businessObjectMethod the value of field
     * 'businessObjectMethod'.
     */
    public void setBusinessObjectMethod(de.tif.jacob.ruleengine.castor.BusinessObjectMethod businessObjectMethod)
    {
        this._businessObjectMethod = businessObjectMethod;
    } //-- void setBusinessObjectMethod(de.tif.jacob.ruleengine.castor.BusinessObjectMethod) 

    /**
     * Sets the value of field 'decision'.
     * 
     * @param decision the value of field 'decision'.
     */
    public void setDecision(de.tif.jacob.ruleengine.castor.Decision decision)
    {
        this._decision = decision;
    } //-- void setDecision(de.tif.jacob.ruleengine.castor.Decision) 

    /**
     * Sets the value of field 'posX'.
     * 
     * @param posX the value of field 'posX'.
     */
    public void setPosX(int posX)
    {
        this._posX = posX;
        this._has_posX = true;
    } //-- void setPosX(int) 

    /**
     * Sets the value of field 'posY'.
     * 
     * @param posY the value of field 'posY'.
     */
    public void setPosY(int posY)
    {
        this._posY = posY;
        this._has_posY = true;
    } //-- void setPosY(int) 

    /**
     * Sets the value of field 'ruleId'.
     * 
     * @param ruleId the value of field 'ruleId'.
     */
    public void setRuleId(java.lang.String ruleId)
    {
        this._ruleId = ruleId;
    } //-- void setRuleId(java.lang.String) 

    /**
     * Method unmarshalRule
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalRule(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.ruleengine.castor.Rule) Unmarshaller.unmarshal(de.tif.jacob.ruleengine.castor.Rule.class, reader);
    } //-- java.lang.Object unmarshalRule(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
