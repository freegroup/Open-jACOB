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
 * Class Scripts.
 * 
 * @version $Revision$ $Date$
 */
public class Scripts implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _scriptList
     */
    private java.util.Vector _scriptList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Scripts() {
        super();
        _scriptList = new Vector();
    } //-- de.tif.qes.adf.castor.Scripts()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addScript
     * 
     * @param vScript
     */
    public void addScript(de.tif.qes.adf.castor.Script vScript)
        throws java.lang.IndexOutOfBoundsException
    {
        _scriptList.addElement(vScript);
    } //-- void addScript(de.tif.qes.adf.castor.Script) 

    /**
     * Method addScript
     * 
     * @param index
     * @param vScript
     */
    public void addScript(int index, de.tif.qes.adf.castor.Script vScript)
        throws java.lang.IndexOutOfBoundsException
    {
        _scriptList.insertElementAt(vScript, index);
    } //-- void addScript(int, de.tif.qes.adf.castor.Script) 

    /**
     * Method enumerateScript
     */
    public java.util.Enumeration enumerateScript()
    {
        return _scriptList.elements();
    } //-- java.util.Enumeration enumerateScript() 

    /**
     * Method getScript
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Script getScript(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _scriptList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.qes.adf.castor.Script) _scriptList.elementAt(index);
    } //-- de.tif.qes.adf.castor.Script getScript(int) 

    /**
     * Method getScript
     */
    public de.tif.qes.adf.castor.Script[] getScript()
    {
        int size = _scriptList.size();
        de.tif.qes.adf.castor.Script[] mArray = new de.tif.qes.adf.castor.Script[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.qes.adf.castor.Script) _scriptList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.qes.adf.castor.Script[] getScript() 

    /**
     * Method getScriptCount
     */
    public int getScriptCount()
    {
        return _scriptList.size();
    } //-- int getScriptCount() 

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
     * Method removeAllScript
     */
    public void removeAllScript()
    {
        _scriptList.removeAllElements();
    } //-- void removeAllScript() 

    /**
     * Method removeScript
     * 
     * @param index
     */
    public de.tif.qes.adf.castor.Script removeScript(int index)
    {
        java.lang.Object obj = _scriptList.elementAt(index);
        _scriptList.removeElementAt(index);
        return (de.tif.qes.adf.castor.Script) obj;
    } //-- de.tif.qes.adf.castor.Script removeScript(int) 

    /**
     * Method setScript
     * 
     * @param index
     * @param vScript
     */
    public void setScript(int index, de.tif.qes.adf.castor.Script vScript)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _scriptList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _scriptList.setElementAt(vScript, index);
    } //-- void setScript(int, de.tif.qes.adf.castor.Script) 

    /**
     * Method setScript
     * 
     * @param scriptArray
     */
    public void setScript(de.tif.qes.adf.castor.Script[] scriptArray)
    {
        //-- copy array
        _scriptList.removeAllElements();
        for (int i = 0; i < scriptArray.length; i++) {
            _scriptList.addElement(scriptArray[i]);
        }
    } //-- void setScript(de.tif.qes.adf.castor.Script) 

    /**
     * Method unmarshalScripts
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalScripts(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.qes.adf.castor.Scripts) Unmarshaller.unmarshal(de.tif.qes.adf.castor.Scripts.class, reader);
    } //-- java.lang.Object unmarshalScripts(java.io.Reader) 

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
