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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Diagrams.
 * 
 * @version $Revision$ $Date$
 */
public class Diagrams implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _activityDiagramList
     */
    private java.util.Vector _activityDiagramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Diagrams() {
        super();
        _activityDiagramList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Diagrams()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addActivityDiagram
     * 
     * @param vActivityDiagram
     */
    public void addActivityDiagram(de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram vActivityDiagram)
        throws java.lang.IndexOutOfBoundsException
    {
        _activityDiagramList.addElement(vActivityDiagram);
    } //-- void addActivityDiagram(de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) 

    /**
     * Method addActivityDiagram
     * 
     * @param index
     * @param vActivityDiagram
     */
    public void addActivityDiagram(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram vActivityDiagram)
        throws java.lang.IndexOutOfBoundsException
    {
        _activityDiagramList.insertElementAt(vActivityDiagram, index);
    } //-- void addActivityDiagram(int, de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) 

    /**
     * Method enumerateActivityDiagram
     */
    public java.util.Enumeration enumerateActivityDiagram()
    {
        return _activityDiagramList.elements();
    } //-- java.util.Enumeration enumerateActivityDiagram() 

    /**
     * Method getActivityDiagram
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram getActivityDiagram(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _activityDiagramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) _activityDiagramList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram getActivityDiagram(int) 

    /**
     * Method getActivityDiagram
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram[] getActivityDiagram()
    {
        int size = _activityDiagramList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) _activityDiagramList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram[] getActivityDiagram() 

    /**
     * Method getActivityDiagramCount
     */
    public int getActivityDiagramCount()
    {
        return _activityDiagramList.size();
    } //-- int getActivityDiagramCount() 

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
     * Method removeActivityDiagram
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram removeActivityDiagram(int index)
    {
        java.lang.Object obj = _activityDiagramList.elementAt(index);
        _activityDiagramList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram removeActivityDiagram(int) 

    /**
     * Method removeAllActivityDiagram
     */
    public void removeAllActivityDiagram()
    {
        _activityDiagramList.removeAllElements();
    } //-- void removeAllActivityDiagram() 

    /**
     * Method setActivityDiagram
     * 
     * @param index
     * @param vActivityDiagram
     */
    public void setActivityDiagram(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram vActivityDiagram)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _activityDiagramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _activityDiagramList.setElementAt(vActivityDiagram, index);
    } //-- void setActivityDiagram(int, de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) 

    /**
     * Method setActivityDiagram
     * 
     * @param activityDiagramArray
     */
    public void setActivityDiagram(de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram[] activityDiagramArray)
    {
        //-- copy array
        _activityDiagramList.removeAllElements();
        for (int i = 0; i < activityDiagramArray.length; i++) {
            _activityDiagramList.addElement(activityDiagramArray[i]);
        }
    } //-- void setActivityDiagram(de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) 

    /**
     * Method unmarshalDiagrams
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDiagrams(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.Diagrams) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.Diagrams.class, reader);
    } //-- java.lang.Object unmarshalDiagrams(java.io.Reader) 

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
