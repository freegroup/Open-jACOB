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

import de.tif.jacob.ruleengine.castor.types.DecisionType;
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
 * Class Decision.
 * 
 * @version $Revision$ $Date$
 */
public class Decision implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _decisionClass
     */
    private java.lang.String _decisionClass;

    /**
     * Field _methodName
     */
    private java.lang.String _methodName;

    /**
     * Field _signature
     */
    private java.lang.String _signature;

    /**
     * Field _type
     */
    private de.tif.jacob.ruleengine.castor.types.DecisionType _type;

    /**
     * Field _conditionalExitList
     */
    private java.util.Vector _conditionalExitList;

    /**
     * Field _parameterList
     */
    private java.util.Vector _parameterList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Decision() {
        super();
        _conditionalExitList = new Vector();
        _parameterList = new Vector();
    } //-- de.tif.jacob.ruleengine.castor.Decision()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addConditionalExit
     * 
     * 
     * 
     * @param vConditionalExit
     */
    public void addConditionalExit(de.tif.jacob.ruleengine.castor.ConditionalExit vConditionalExit)
        throws java.lang.IndexOutOfBoundsException
    {
        _conditionalExitList.addElement(vConditionalExit);
    } //-- void addConditionalExit(de.tif.jacob.ruleengine.castor.ConditionalExit) 

    /**
     * Method addConditionalExit
     * 
     * 
     * 
     * @param index
     * @param vConditionalExit
     */
    public void addConditionalExit(int index, de.tif.jacob.ruleengine.castor.ConditionalExit vConditionalExit)
        throws java.lang.IndexOutOfBoundsException
    {
        _conditionalExitList.insertElementAt(vConditionalExit, index);
    } //-- void addConditionalExit(int, de.tif.jacob.ruleengine.castor.ConditionalExit) 

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
     * Method enumerateConditionalExit
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateConditionalExit()
    {
        return _conditionalExitList.elements();
    } //-- java.util.Enumeration enumerateConditionalExit() 

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
     * Method getConditionalExit
     * 
     * 
     * 
     * @param index
     * @return ConditionalExit
     */
    public de.tif.jacob.ruleengine.castor.ConditionalExit getConditionalExit(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _conditionalExitList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.ruleengine.castor.ConditionalExit) _conditionalExitList.elementAt(index);
    } //-- de.tif.jacob.ruleengine.castor.ConditionalExit getConditionalExit(int) 

    /**
     * Method getConditionalExit
     * 
     * 
     * 
     * @return ConditionalExit
     */
    public de.tif.jacob.ruleengine.castor.ConditionalExit[] getConditionalExit()
    {
        int size = _conditionalExitList.size();
        de.tif.jacob.ruleengine.castor.ConditionalExit[] mArray = new de.tif.jacob.ruleengine.castor.ConditionalExit[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.ruleengine.castor.ConditionalExit) _conditionalExitList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.ruleengine.castor.ConditionalExit[] getConditionalExit() 

    /**
     * Method getConditionalExitCount
     * 
     * 
     * 
     * @return int
     */
    public int getConditionalExitCount()
    {
        return _conditionalExitList.size();
    } //-- int getConditionalExitCount() 

    /**
     * Returns the value of field 'decisionClass'.
     * 
     * @return String
     * @return the value of field 'decisionClass'.
     */
    public java.lang.String getDecisionClass()
    {
        return this._decisionClass;
    } //-- java.lang.String getDecisionClass() 

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
     * Returns the value of field 'type'.
     * 
     * @return DecisionType
     * @return the value of field 'type'.
     */
    public de.tif.jacob.ruleengine.castor.types.DecisionType getType()
    {
        return this._type;
    } //-- de.tif.jacob.ruleengine.castor.types.DecisionType getType() 

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
     * Method removeAllConditionalExit
     * 
     */
    public void removeAllConditionalExit()
    {
        _conditionalExitList.removeAllElements();
    } //-- void removeAllConditionalExit() 

    /**
     * Method removeAllParameter
     * 
     */
    public void removeAllParameter()
    {
        _parameterList.removeAllElements();
    } //-- void removeAllParameter() 

    /**
     * Method removeConditionalExit
     * 
     * 
     * 
     * @param index
     * @return ConditionalExit
     */
    public de.tif.jacob.ruleengine.castor.ConditionalExit removeConditionalExit(int index)
    {
        java.lang.Object obj = _conditionalExitList.elementAt(index);
        _conditionalExitList.removeElementAt(index);
        return (de.tif.jacob.ruleengine.castor.ConditionalExit) obj;
    } //-- de.tif.jacob.ruleengine.castor.ConditionalExit removeConditionalExit(int) 

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
     * Method setConditionalExit
     * 
     * 
     * 
     * @param index
     * @param vConditionalExit
     */
    public void setConditionalExit(int index, de.tif.jacob.ruleengine.castor.ConditionalExit vConditionalExit)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _conditionalExitList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _conditionalExitList.setElementAt(vConditionalExit, index);
    } //-- void setConditionalExit(int, de.tif.jacob.ruleengine.castor.ConditionalExit) 

    /**
     * Method setConditionalExit
     * 
     * 
     * 
     * @param conditionalExitArray
     */
    public void setConditionalExit(de.tif.jacob.ruleengine.castor.ConditionalExit[] conditionalExitArray)
    {
        //-- copy array
        _conditionalExitList.removeAllElements();
        for (int i = 0; i < conditionalExitArray.length; i++) {
            _conditionalExitList.addElement(conditionalExitArray[i]);
        }
    } //-- void setConditionalExit(de.tif.jacob.ruleengine.castor.ConditionalExit) 

    /**
     * Sets the value of field 'decisionClass'.
     * 
     * @param decisionClass the value of field 'decisionClass'.
     */
    public void setDecisionClass(java.lang.String decisionClass)
    {
        this._decisionClass = decisionClass;
    } //-- void setDecisionClass(java.lang.String) 

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
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(de.tif.jacob.ruleengine.castor.types.DecisionType type)
    {
        this._type = type;
    } //-- void setType(de.tif.jacob.ruleengine.castor.types.DecisionType) 

    /**
     * Method unmarshalDecision
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalDecision(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.ruleengine.castor.Decision) Unmarshaller.unmarshal(de.tif.jacob.ruleengine.castor.Decision.class, reader);
    } //-- java.lang.Object unmarshalDecision(java.io.Reader) 

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
