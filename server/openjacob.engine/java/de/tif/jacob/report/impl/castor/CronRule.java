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
 * Class CronRule.
 * 
 * @version $Revision$ $Date$
 */
public class CronRule implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _rule
     */
    private java.lang.String _rule;

    /**
     * old cron rule
     */
    private int _minute;

    /**
     * keeps track of state for field: _minute
     */
    private boolean _has_minute;

    /**
     * old cron rule
     */
    private int _hour;

    /**
     * keeps track of state for field: _hour
     */
    private boolean _has_hour;

    /**
     * old cron rule
     */
    private java.util.Vector _daysList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CronRule() {
        super();
        _daysList = new Vector();
    } //-- de.tif.jacob.report.impl.castor.CronRule()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDays
     * 
     * @param vDays
     */
    public void addDays(int vDays)
        throws java.lang.IndexOutOfBoundsException
    {
        _daysList.addElement(new java.lang.Integer(vDays));
    } //-- void addDays(int) 

    /**
     * Method addDays
     * 
     * @param index
     * @param vDays
     */
    public void addDays(int index, int vDays)
        throws java.lang.IndexOutOfBoundsException
    {
        _daysList.insertElementAt(new java.lang.Integer(vDays), index);
    } //-- void addDays(int, int) 

    /**
     * Method deleteHour
     */
    public void deleteHour()
    {
        this._has_hour= false;
    } //-- void deleteHour() 

    /**
     * Method deleteMinute
     */
    public void deleteMinute()
    {
        this._has_minute= false;
    } //-- void deleteMinute() 

    /**
     * Method enumerateDays
     */
    public java.util.Enumeration enumerateDays()
    {
        return _daysList.elements();
    } //-- java.util.Enumeration enumerateDays() 

    /**
     * Method getDays
     * 
     * @param index
     */
    public int getDays(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _daysList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return ((java.lang.Integer)_daysList.elementAt(index)).intValue();
    } //-- int getDays(int) 

    /**
     * Method getDays
     */
    public int[] getDays()
    {
        int size = _daysList.size();
        int[] mArray = new int[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = ((java.lang.Integer)_daysList.elementAt(index)).intValue();
        }
        return mArray;
    } //-- int[] getDays() 

    /**
     * Method getDaysCount
     */
    public int getDaysCount()
    {
        return _daysList.size();
    } //-- int getDaysCount() 

    /**
     * Returns the value of field 'hour'. The field 'hour' has the
     * following description: old cron rule
     * 
     * @return the value of field 'hour'.
     */
    public int getHour()
    {
        return this._hour;
    } //-- int getHour() 

    /**
     * Returns the value of field 'minute'. The field 'minute' has
     * the following description: old cron rule
     * 
     * @return the value of field 'minute'.
     */
    public int getMinute()
    {
        return this._minute;
    } //-- int getMinute() 

    /**
     * Returns the value of field 'rule'.
     * 
     * @return the value of field 'rule'.
     */
    public java.lang.String getRule()
    {
        return this._rule;
    } //-- java.lang.String getRule() 

    /**
     * Method hasHour
     */
    public boolean hasHour()
    {
        return this._has_hour;
    } //-- boolean hasHour() 

    /**
     * Method hasMinute
     */
    public boolean hasMinute()
    {
        return this._has_minute;
    } //-- boolean hasMinute() 

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
     * Method removeAllDays
     */
    public void removeAllDays()
    {
        _daysList.removeAllElements();
    } //-- void removeAllDays() 

    /**
     * Method removeDays
     * 
     * @param index
     */
    public int removeDays(int index)
    {
        java.lang.Object obj = _daysList.elementAt(index);
        _daysList.removeElementAt(index);
        return ((java.lang.Integer)obj).intValue();
    } //-- int removeDays(int) 

    /**
     * Method setDays
     * 
     * @param index
     * @param vDays
     */
    public void setDays(int index, int vDays)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _daysList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _daysList.setElementAt(new java.lang.Integer(vDays), index);
    } //-- void setDays(int, int) 

    /**
     * Method setDays
     * 
     * @param daysArray
     */
    public void setDays(int[] daysArray)
    {
        //-- copy array
        _daysList.removeAllElements();
        for (int i = 0; i < daysArray.length; i++) {
            _daysList.addElement(new java.lang.Integer(daysArray[i]));
        }
    } //-- void setDays(int) 

    /**
     * Sets the value of field 'hour'. The field 'hour' has the
     * following description: old cron rule
     * 
     * @param hour the value of field 'hour'.
     */
    public void setHour(int hour)
    {
        this._hour = hour;
        this._has_hour = true;
    } //-- void setHour(int) 

    /**
     * Sets the value of field 'minute'. The field 'minute' has the
     * following description: old cron rule
     * 
     * @param minute the value of field 'minute'.
     */
    public void setMinute(int minute)
    {
        this._minute = minute;
        this._has_minute = true;
    } //-- void setMinute(int) 

    /**
     * Sets the value of field 'rule'.
     * 
     * @param rule the value of field 'rule'.
     */
    public void setRule(java.lang.String rule)
    {
        this._rule = rule;
    } //-- void setRule(java.lang.String) 

    /**
     * Method unmarshalCronRule
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCronRule(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.report.impl.castor.CronRule) Unmarshaller.unmarshal(de.tif.jacob.report.impl.castor.CronRule.class, reader);
    } //-- java.lang.Object unmarshalCronRule(java.io.Reader) 

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
