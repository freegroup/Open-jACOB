/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.qes.adf.castor;

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
 * Class ActiveXEvents.
 * 
 * @version $Revision$ $Date$
 */
public class ActiveXEvents implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _eventList
     */
    private java.util.Vector _eventList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ActiveXEvents() {
        super();
        _eventList = new Vector();
    } //-- de.tif.qes.adf.castor.ActiveXEvents()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addEvent
     * 
     * @param vEvent
     */
    public void addEvent(de.tif.qes.adf.castor.Event vEvent)
        throws java.lang.IndexOutOfBoundsException
    {
        _eventList.addElement(vEvent);
    } //-- void addEvent(de.tif.qes.adf.castor.Event) 

    /**
     * Method addEvent
     * 
     * @param index
     * @param vEvent
     */
    public void addEvent(int index, de.tif.qes.adf.castor.Event vEvent)
        throws java.lang.IndexOutOfBoundsException
    {
        _eventList.insertElementAt(vEvent, index);
    } //-- void addEvent(int, de.tif.qes.adf.castor.Event) 

    /**
     * Method enumerateEvent
     */
    public java.util.Enumeration enumerateEvent()
    {
        return _eventList.elements();
    } //-- java.util.Enumeration enumerateEvent() 

    /**
     * Method getEvent
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Event getEvent(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _eventList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.qes.adf.castor.Event) _eventList.elementAt(index);
    } //-- de.tif.qes.adf.castor.Event getEvent(int) 

    /**
     * Method getEvent
     */
    public de.tif.qes.adf.castor.Event[] getEvent()
    {
        int size = _eventList.size();
        de.tif.qes.adf.castor.Event[] mArray = new de.tif.qes.adf.castor.Event[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.qes.adf.castor.Event) _eventList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.qes.adf.castor.Event[] getEvent() 

    /**
     * Method getEventCount
     */
    public int getEventCount()
    {
        return _eventList.size();
    } //-- int getEventCount() 

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
     * Method removeAllEvent
     */
    public void removeAllEvent()
    {
        _eventList.removeAllElements();
    } //-- void removeAllEvent() 

    /**
     * Method removeEvent
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Event removeEvent(int index)
    {
        java.lang.Object obj = _eventList.elementAt(index);
        _eventList.removeElementAt(index);
        return (de.tif.qes.adf.castor.Event) obj;
    } //-- de.tif.qes.adf.castor.Event removeEvent(int) 

    /**
     * Method setEvent
     * 
     * @param index
     * @param vEvent
     */
    public void setEvent(int index, de.tif.qes.adf.castor.Event vEvent)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _eventList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _eventList.setElementAt(vEvent, index);
    } //-- void setEvent(int, de.tif.qes.adf.castor.Event) 

    /**
     * Method setEvent
     * 
     * @param eventArray
     */
    public void setEvent(de.tif.qes.adf.castor.Event[] eventArray)
    {
        //-- copy array
        _eventList.removeAllElements();
        for (int i = 0; i < eventArray.length; i++) {
            _eventList.addElement(eventArray[i]);
        }
    } //-- void setEvent(de.tif.qes.adf.castor.Event) 

    /**
     * Method unmarshalActiveXEvents
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalActiveXEvents(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.ActiveXEvents) Unmarshaller.unmarshal(de.tif.qes.adf.castor.ActiveXEvents.class, reader);
    } //-- java.lang.Object unmarshalActiveXEvents(java.io.Reader) 

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
