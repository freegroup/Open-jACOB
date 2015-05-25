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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class BusinessObjectMethod.
 * 
 * @version $Revision$ $Date$
 */
public class BusinessObjectMethod implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _businessClass
     */
    private java.lang.String _businessClass;

    /**
     * Field _methodName
     */
    private java.lang.String _methodName;

    /**
     * Field _signature
     */
    private java.lang.String _signature;

    /**
     * Field _nextRuleId
     */
    private java.lang.String _nextRuleId;

    /**
     * Field _parameterList
     */
    private java.util.Vector _parameterList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BusinessObjectMethod() {
        super();
        _parameterList = new Vector();
    } //-- de.tif.jacob.ruleengine.castor.BusinessObjectMethod()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParameter
     * 
     * 
     * 
     * @param vParameter
     */
    public void addParameter(java.lang.String vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterList.addElement(vParameter);
    } //-- void addParameter(java.lang.String) 

    /**
     * Method addParameter
     * 
     * 
     * 
     * @param index
     * @param vParameter
     */
    public void addParameter(int index, java.lang.String vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterList.insertElementAt(vParameter, index);
    } //-- void addParameter(int, java.lang.String) 

    /**
     * Method enumerateParameter
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateParameter()
    {
        return _parameterList.elements();
    } //-- java.util.Enumeration enumerateParameter() 

    /**
     * Returns the value of field 'businessClass'.
     * 
     * @return String
     * @return the value of field 'businessClass'.
     */
    public java.lang.String getBusinessClass()
    {
        return this._businessClass;
    } //-- java.lang.String getBusinessClass() 

    /**
     * Returns the value of field 'methodName'.
     * 
     * @return String
     * @return the value of field 'methodName'.
     */
    public java.lang.String getMethodName()
    {
        return this._methodName;
    } //-- java.lang.String getMethodName() 

    /**
     * Returns the value of field 'nextRuleId'.
     * 
     * @return String
     * @return the value of field 'nextRuleId'.
     */
    public java.lang.String getNextRuleId()
    {
        return this._nextRuleId;
    } //-- java.lang.String getNextRuleId() 

    /**
     * Method getParameter
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getParameter(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_parameterList.elementAt(index);
    } //-- java.lang.String getParameter(int) 

    /**
     * Method getParameter
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getParameter()
    {
        int size = _parameterList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_parameterList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getParameter() 

    /**
     * Method getParameterCount
     * 
     * 
     * 
     * @return int
     */
    public int getParameterCount()
    {
        return _parameterList.size();
    } //-- int getParameterCount() 

    /**
     * Returns the value of field 'signature'.
     * 
     * @return String
     * @return the value of field 'signature'.
     */
    public java.lang.String getSignature()
    {
        return this._signature;
    } //-- java.lang.String getSignature() 

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
     * Method removeAllParameter
     * 
     */
    public void removeAllParameter()
    {
        _parameterList.removeAllElements();
    } //-- void removeAllParameter() 

    /**
     * Method removeParameter
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeParameter(int index)
    {
        java.lang.Object obj = _parameterList.elementAt(index);
        _parameterList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeParameter(int) 

    /**
     * Sets the value of field 'businessClass'.
     * 
     * @param businessClass the value of field 'businessClass'.
     */
    public void setBusinessClass(java.lang.String businessClass)
    {
        this._businessClass = businessClass;
    } //-- void setBusinessClass(java.lang.String) 

    /**
     * Sets the value of field 'methodName'.
     * 
     * @param methodName the value of field 'methodName'.
     */
    public void setMethodName(java.lang.String methodName)
    {
        this._methodName = methodName;
    } //-- void setMethodName(java.lang.String) 

    /**
     * Sets the value of field 'nextRuleId'.
     * 
     * @param nextRuleId the value of field 'nextRuleId'.
     */
    public void setNextRuleId(java.lang.String nextRuleId)
    {
        this._nextRuleId = nextRuleId;
    } //-- void setNextRuleId(java.lang.String) 

    /**
     * Method setParameter
     * 
     * 
     * 
     * @param index
     * @param vParameter
     */
    public void setParameter(int index, java.lang.String vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterList.setElementAt(vParameter, index);
    } //-- void setParameter(int, java.lang.String) 

    /**
     * Method setParameter
     * 
     * 
     * 
     * @param parameterArray
     */
    public void setParameter(java.lang.String[] parameterArray)
    {
        //-- copy array
        _parameterList.removeAllElements();
        for (int i = 0; i < parameterArray.length; i++) {
            _parameterList.addElement(parameterArray[i]);
        }
    } //-- void setParameter(java.lang.String) 

    /**
     * Sets the value of field 'signature'.
     * 
     * @param signature the value of field 'signature'.
     */
    public void setSignature(java.lang.String signature)
    {
        this._signature = signature;
    } //-- void setSignature(java.lang.String) 

    /**
     * Method unmarshalBusinessObjectMethod
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalBusinessObjectMethod(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.ruleengine.castor.BusinessObjectMethod) Unmarshaller.unmarshal(de.tif.jacob.ruleengine.castor.BusinessObjectMethod.class, reader);
    } //-- java.lang.Object unmarshalBusinessObjectMethod(java.io.Reader) 

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
