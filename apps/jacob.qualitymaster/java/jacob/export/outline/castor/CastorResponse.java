/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: CastorResponse.java,v 1.2 2009/01/26 15:37:22 herz Exp $
 */

package jacob.export.outline.castor;

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
 * Class CastorResponse.
 * 
 * @version $Revision: 1.2 $ $Date: 2009/01/26 15:37:22 $
 */
public class CastorResponse implements java.io.Serializable {

  

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _castorDefectList
     */
    private java.util.Vector _castorDefectList;

    /**
     * Field _errorMessage
     */
    private java.lang.String _errorMessage;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorResponse() {
        super();
        _castorDefectList = new Vector();
    } //-- jacob.export.outline.castor.CastorResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addCastorDefect
     * 
     * @param vCastorDefect
     */
    public void addCastorDefect(CastorDefect vCastorDefect)
        throws java.lang.IndexOutOfBoundsException
    {
        _castorDefectList.addElement(vCastorDefect);
    } //-- void addCastorDefect(CastorDefect) 

    /**
     * Method addCastorDefect
     * 
     * @param index
     * @param vCastorDefect
     */
    public void addCastorDefect(int index, CastorDefect vCastorDefect)
        throws java.lang.IndexOutOfBoundsException
    {
        _castorDefectList.insertElementAt(vCastorDefect, index);
    } //-- void addCastorDefect(int, CastorDefect) 

    /**
     * Method enumerateCastorDefect
     */
    public java.util.Enumeration enumerateCastorDefect()
    {
        return _castorDefectList.elements();
    } //-- java.util.Enumeration enumerateCastorDefect() 

    /**
     * Method getCastorDefect
     * 
     * @param index
     */
    public CastorDefect getCastorDefect(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _castorDefectList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (CastorDefect) _castorDefectList.elementAt(index);
    } //-- CastorDefect getCastorDefect(int) 

    /**
     * Method getCastorDefect
     */
    public CastorDefect[] getCastorDefect()
    {
        int size = _castorDefectList.size();
        CastorDefect[] mArray = new CastorDefect[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (CastorDefect) _castorDefectList.elementAt(index);
        }
        return mArray;
    } //-- CastorDefect[] getCastorDefect() 

    /**
     * Method getCastorDefectCount
     */
    public int getCastorDefectCount()
    {
        return _castorDefectList.size();
    } //-- int getCastorDefectCount() 

    /**
     * Returns the value of field 'errorMessage'.
     * 
     * @return the value of field 'errorMessage'.
     */
    public java.lang.String getErrorMessage()
    {
        return this._errorMessage;
    } //-- java.lang.String getErrorMessage() 

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
     * Method removeAllCastorDefect
     */
    public void removeAllCastorDefect()
    {
        _castorDefectList.removeAllElements();
    } //-- void removeAllCastorDefect() 

    /**
     * Method removeCastorDefect
     * 
     * @param index
     */
    public CastorDefect removeCastorDefect(int index)
    {
        java.lang.Object obj = _castorDefectList.elementAt(index);
        _castorDefectList.removeElementAt(index);
        return (CastorDefect) obj;
    } //-- CastorDefect removeCastorDefect(int) 

    /**
     * Method setCastorDefect
     * 
     * @param index
     * @param vCastorDefect
     */
    public void setCastorDefect(int index, CastorDefect vCastorDefect)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _castorDefectList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _castorDefectList.setElementAt(vCastorDefect, index);
    } //-- void setCastorDefect(int, CastorDefect) 

    /**
     * Method setCastorDefect
     * 
     * @param castorDefectArray
     */
    public void setCastorDefect(CastorDefect[] castorDefectArray)
    {
        //-- copy array
        _castorDefectList.removeAllElements();
        for (int i = 0; i < castorDefectArray.length; i++) {
            _castorDefectList.addElement(castorDefectArray[i]);
        }
    } //-- void setCastorDefect(CastorDefect) 

    /**
     * Sets the value of field 'errorMessage'.
     * 
     * @param errorMessage the value of field 'errorMessage'.
     */
    public void setErrorMessage(java.lang.String errorMessage)
    {
        this._errorMessage = errorMessage;
    } //-- void setErrorMessage(java.lang.String) 

    /**
     * Method unmarshalCastorResponse
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorResponse(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.export.outline.castor.CastorResponse) Unmarshaller.unmarshal(jacob.export.outline.castor.CastorResponse.class, reader);
    } //-- java.lang.Object unmarshalCastorResponse(java.io.Reader) 

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
