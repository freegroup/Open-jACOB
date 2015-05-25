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
 * Class Ruleset.
 * 
 * @version $Revision$ $Date$
 */
public class Ruleset implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ruleList
     */
    private java.util.Vector _ruleList;

    /**
     * Field _annotationList
     */
    private java.util.Vector _annotationList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Ruleset() {
        super();
        _ruleList = new Vector();
        _annotationList = new Vector();
    } //-- de.tif.jacob.ruleengine.castor.Ruleset()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAnnotation
     * 
     * 
     * 
     * @param vAnnotation
     */
    public void addAnnotation(de.tif.jacob.ruleengine.castor.Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        _annotationList.addElement(vAnnotation);
    } //-- void addAnnotation(de.tif.jacob.ruleengine.castor.Annotation) 

    /**
     * Method addAnnotation
     * 
     * 
     * 
     * @param index
     * @param vAnnotation
     */
    public void addAnnotation(int index, de.tif.jacob.ruleengine.castor.Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        _annotationList.insertElementAt(vAnnotation, index);
    } //-- void addAnnotation(int, de.tif.jacob.ruleengine.castor.Annotation) 

    /**
     * Method addRule
     * 
     * 
     * 
     * @param vRule
     */
    public void addRule(de.tif.jacob.ruleengine.castor.Rule vRule)
        throws java.lang.IndexOutOfBoundsException
    {
        _ruleList.addElement(vRule);
    } //-- void addRule(de.tif.jacob.ruleengine.castor.Rule) 

    /**
     * Method addRule
     * 
     * 
     * 
     * @param index
     * @param vRule
     */
    public void addRule(int index, de.tif.jacob.ruleengine.castor.Rule vRule)
        throws java.lang.IndexOutOfBoundsException
    {
        _ruleList.insertElementAt(vRule, index);
    } //-- void addRule(int, de.tif.jacob.ruleengine.castor.Rule) 

    /**
     * Method enumerateAnnotation
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateAnnotation()
    {
        return _annotationList.elements();
    } //-- java.util.Enumeration enumerateAnnotation() 

    /**
     * Method enumerateRule
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateRule()
    {
        return _ruleList.elements();
    } //-- java.util.Enumeration enumerateRule() 

    /**
     * Method getAnnotation
     * 
     * 
     * 
     * @param index
     * @return Annotation
     */
    public de.tif.jacob.ruleengine.castor.Annotation getAnnotation(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _annotationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.ruleengine.castor.Annotation) _annotationList.elementAt(index);
    } //-- de.tif.jacob.ruleengine.castor.Annotation getAnnotation(int) 

    /**
     * Method getAnnotation
     * 
     * 
     * 
     * @return Annotation
     */
    public de.tif.jacob.ruleengine.castor.Annotation[] getAnnotation()
    {
        int size = _annotationList.size();
        de.tif.jacob.ruleengine.castor.Annotation[] mArray = new de.tif.jacob.ruleengine.castor.Annotation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.ruleengine.castor.Annotation) _annotationList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.ruleengine.castor.Annotation[] getAnnotation() 

    /**
     * Method getAnnotationCount
     * 
     * 
     * 
     * @return int
     */
    public int getAnnotationCount()
    {
        return _annotationList.size();
    } //-- int getAnnotationCount() 

    /**
     * Method getRule
     * 
     * 
     * 
     * @param index
     * @return Rule
     */
    public de.tif.jacob.ruleengine.castor.Rule getRule(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ruleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.ruleengine.castor.Rule) _ruleList.elementAt(index);
    } //-- de.tif.jacob.ruleengine.castor.Rule getRule(int) 

    /**
     * Method getRule
     * 
     * 
     * 
     * @return Rule
     */
    public de.tif.jacob.ruleengine.castor.Rule[] getRule()
    {
        int size = _ruleList.size();
        de.tif.jacob.ruleengine.castor.Rule[] mArray = new de.tif.jacob.ruleengine.castor.Rule[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.ruleengine.castor.Rule) _ruleList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.ruleengine.castor.Rule[] getRule() 

    /**
     * Method getRuleCount
     * 
     * 
     * 
     * @return int
     */
    public int getRuleCount()
    {
        return _ruleList.size();
    } //-- int getRuleCount() 

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
     * Method removeAllAnnotation
     * 
     */
    public void removeAllAnnotation()
    {
        _annotationList.removeAllElements();
    } //-- void removeAllAnnotation() 

    /**
     * Method removeAllRule
     * 
     */
    public void removeAllRule()
    {
        _ruleList.removeAllElements();
    } //-- void removeAllRule() 

    /**
     * Method removeAnnotation
     * 
     * 
     * 
     * @param index
     * @return Annotation
     */
    public de.tif.jacob.ruleengine.castor.Annotation removeAnnotation(int index)
    {
        java.lang.Object obj = _annotationList.elementAt(index);
        _annotationList.removeElementAt(index);
        return (de.tif.jacob.ruleengine.castor.Annotation) obj;
    } //-- de.tif.jacob.ruleengine.castor.Annotation removeAnnotation(int) 

    /**
     * Method removeRule
     * 
     * 
     * 
     * @param index
     * @return Rule
     */
    public de.tif.jacob.ruleengine.castor.Rule removeRule(int index)
    {
        java.lang.Object obj = _ruleList.elementAt(index);
        _ruleList.removeElementAt(index);
        return (de.tif.jacob.ruleengine.castor.Rule) obj;
    } //-- de.tif.jacob.ruleengine.castor.Rule removeRule(int) 

    /**
     * Method setAnnotation
     * 
     * 
     * 
     * @param index
     * @param vAnnotation
     */
    public void setAnnotation(int index, de.tif.jacob.ruleengine.castor.Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _annotationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _annotationList.setElementAt(vAnnotation, index);
    } //-- void setAnnotation(int, de.tif.jacob.ruleengine.castor.Annotation) 

    /**
     * Method setAnnotation
     * 
     * 
     * 
     * @param annotationArray
     */
    public void setAnnotation(de.tif.jacob.ruleengine.castor.Annotation[] annotationArray)
    {
        //-- copy array
        _annotationList.removeAllElements();
        for (int i = 0; i < annotationArray.length; i++) {
            _annotationList.addElement(annotationArray[i]);
        }
    } //-- void setAnnotation(de.tif.jacob.ruleengine.castor.Annotation) 

    /**
     * Method setRule
     * 
     * 
     * 
     * @param index
     * @param vRule
     */
    public void setRule(int index, de.tif.jacob.ruleengine.castor.Rule vRule)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ruleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _ruleList.setElementAt(vRule, index);
    } //-- void setRule(int, de.tif.jacob.ruleengine.castor.Rule) 

    /**
     * Method setRule
     * 
     * 
     * 
     * @param ruleArray
     */
    public void setRule(de.tif.jacob.ruleengine.castor.Rule[] ruleArray)
    {
        //-- copy array
        _ruleList.removeAllElements();
        for (int i = 0; i < ruleArray.length; i++) {
            _ruleList.addElement(ruleArray[i]);
        }
    } //-- void setRule(de.tif.jacob.ruleengine.castor.Rule) 

    /**
     * Method unmarshalRuleset
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalRuleset(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.ruleengine.castor.Ruleset) Unmarshaller.unmarshal(de.tif.jacob.ruleengine.castor.Ruleset.class, reader);
    } //-- java.lang.Object unmarshalRuleset(java.io.Reader) 

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
