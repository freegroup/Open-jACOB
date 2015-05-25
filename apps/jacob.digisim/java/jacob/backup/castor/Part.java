/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML
 * Schema.
 * $Id: Part.java,v 1.1 2007/02/02 22:26:48 freegroup Exp $
 */

package jacob.backup.castor;

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
 * Class Part.
 * 
 * @version $Revision: 1.1 $ $Date: 2007/02/02 22:26:48 $
 */
public class Part implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pkey
     */
    private java.lang.String _pkey;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _state
     */
    private java.lang.String _state;

    /**
     * Field _code
     */
    private java.lang.String _code;

    /**
     * Field _comment
     */
    private java.lang.String _comment;

    /**
     * Field _tool_image
     */
    private byte[] _tool_image;

    /**
     * Field _resource_image
     */
    private byte[] _resource_image;

    /**
     * Field _ownerKey
     */
    private java.lang.String _ownerKey;

    /**
     * Field _partKey
     */
    private java.lang.String _partKey;

    /**
     * Field _history
     */
    private java.lang.String _history;

    /**
     * Field _lastRelease
     */
    private java.lang.String _lastRelease;

    /**
     * Field _assigneeKey
     */
    private java.lang.String _assigneeKey;

    /**
     * Field _mandatorId
     */
    private java.lang.String _mandatorId;

    /**
     * Field _partKey2
     */
    private java.lang.String _partKey2;


      //----------------/
     //- Constructors -/
    //----------------/

    public Part() {
        super();
    } //-- jacob.backup.castor.Part()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'assigneeKey'.
     * 
     * @return String
     * @return the value of field 'assigneeKey'.
     */
    public java.lang.String getAssigneeKey()
    {
        return this._assigneeKey;
    } //-- java.lang.String getAssigneeKey() 

    /**
     * Returns the value of field 'code'.
     * 
     * @return String
     * @return the value of field 'code'.
     */
    public java.lang.String getCode()
    {
        return this._code;
    } //-- java.lang.String getCode() 

    /**
     * Returns the value of field 'comment'.
     * 
     * @return String
     * @return the value of field 'comment'.
     */
    public java.lang.String getComment()
    {
        return this._comment;
    } //-- java.lang.String getComment() 

    /**
     * Returns the value of field 'history'.
     * 
     * @return String
     * @return the value of field 'history'.
     */
    public java.lang.String getHistory()
    {
        return this._history;
    } //-- java.lang.String getHistory() 

    /**
     * Returns the value of field 'lastRelease'.
     * 
     * @return String
     * @return the value of field 'lastRelease'.
     */
    public java.lang.String getLastRelease()
    {
        return this._lastRelease;
    } //-- java.lang.String getLastRelease() 

    /**
     * Returns the value of field 'mandatorId'.
     * 
     * @return String
     * @return the value of field 'mandatorId'.
     */
    public java.lang.String getMandatorId()
    {
        return this._mandatorId;
    } //-- java.lang.String getMandatorId() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return String
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'ownerKey'.
     * 
     * @return String
     * @return the value of field 'ownerKey'.
     */
    public java.lang.String getOwnerKey()
    {
        return this._ownerKey;
    } //-- java.lang.String getOwnerKey() 

    /**
     * Returns the value of field 'partKey'.
     * 
     * @return String
     * @return the value of field 'partKey'.
     */
    public java.lang.String getPartKey()
    {
        return this._partKey;
    } //-- java.lang.String getPartKey() 

    /**
     * Returns the value of field 'partKey2'.
     * 
     * @return String
     * @return the value of field 'partKey2'.
     */
    public java.lang.String getPartKey2()
    {
        return this._partKey2;
    } //-- java.lang.String getPartKey2() 

    /**
     * Returns the value of field 'pkey'.
     * 
     * @return String
     * @return the value of field 'pkey'.
     */
    public java.lang.String getPkey()
    {
        return this._pkey;
    } //-- java.lang.String getPkey() 

    /**
     * Returns the value of field 'resource_image'.
     * 
     * @return byte
     * @return the value of field 'resource_image'.
     */
    public byte[] getResource_image()
    {
        return this._resource_image;
    } //-- byte[] getResource_image() 

    /**
     * Returns the value of field 'state'.
     * 
     * @return String
     * @return the value of field 'state'.
     */
    public java.lang.String getState()
    {
        return this._state;
    } //-- java.lang.String getState() 

    /**
     * Returns the value of field 'tool_image'.
     * 
     * @return byte
     * @return the value of field 'tool_image'.
     */
    public byte[] getTool_image()
    {
        return this._tool_image;
    } //-- byte[] getTool_image() 

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
     * Sets the value of field 'assigneeKey'.
     * 
     * @param assigneeKey the value of field 'assigneeKey'.
     */
    public void setAssigneeKey(java.lang.String assigneeKey)
    {
        this._assigneeKey = assigneeKey;
    } //-- void setAssigneeKey(java.lang.String) 

    /**
     * Sets the value of field 'code'.
     * 
     * @param code the value of field 'code'.
     */
    public void setCode(java.lang.String code)
    {
        this._code = code;
    } //-- void setCode(java.lang.String) 

    /**
     * Sets the value of field 'comment'.
     * 
     * @param comment the value of field 'comment'.
     */
    public void setComment(java.lang.String comment)
    {
        this._comment = comment;
    } //-- void setComment(java.lang.String) 

    /**
     * Sets the value of field 'history'.
     * 
     * @param history the value of field 'history'.
     */
    public void setHistory(java.lang.String history)
    {
        this._history = history;
    } //-- void setHistory(java.lang.String) 

    /**
     * Sets the value of field 'lastRelease'.
     * 
     * @param lastRelease the value of field 'lastRelease'.
     */
    public void setLastRelease(java.lang.String lastRelease)
    {
        this._lastRelease = lastRelease;
    } //-- void setLastRelease(java.lang.String) 

    /**
     * Sets the value of field 'mandatorId'.
     * 
     * @param mandatorId the value of field 'mandatorId'.
     */
    public void setMandatorId(java.lang.String mandatorId)
    {
        this._mandatorId = mandatorId;
    } //-- void setMandatorId(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'ownerKey'.
     * 
     * @param ownerKey the value of field 'ownerKey'.
     */
    public void setOwnerKey(java.lang.String ownerKey)
    {
        this._ownerKey = ownerKey;
    } //-- void setOwnerKey(java.lang.String) 

    /**
     * Sets the value of field 'partKey'.
     * 
     * @param partKey the value of field 'partKey'.
     */
    public void setPartKey(java.lang.String partKey)
    {
        this._partKey = partKey;
    } //-- void setPartKey(java.lang.String) 

    /**
     * Sets the value of field 'partKey2'.
     * 
     * @param partKey2 the value of field 'partKey2'.
     */
    public void setPartKey2(java.lang.String partKey2)
    {
        this._partKey2 = partKey2;
    } //-- void setPartKey2(java.lang.String) 

    /**
     * Sets the value of field 'pkey'.
     * 
     * @param pkey the value of field 'pkey'.
     */
    public void setPkey(java.lang.String pkey)
    {
        this._pkey = pkey;
    } //-- void setPkey(java.lang.String) 

    /**
     * Sets the value of field 'resource_image'.
     * 
     * @param resource_image the value of field 'resource_image'.
     */
    public void setResource_image(byte[] resource_image)
    {
        this._resource_image = resource_image;
    } //-- void setResource_image(byte) 

    /**
     * Sets the value of field 'state'.
     * 
     * @param state the value of field 'state'.
     */
    public void setState(java.lang.String state)
    {
        this._state = state;
    } //-- void setState(java.lang.String) 

    /**
     * Sets the value of field 'tool_image'.
     * 
     * @param tool_image the value of field 'tool_image'.
     */
    public void setTool_image(byte[] tool_image)
    {
        this._tool_image = tool_image;
    } //-- void setTool_image(byte) 

    /**
     * Method unmarshalPart
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshalPart(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.backup.castor.Part) Unmarshaller.unmarshal(jacob.backup.castor.Part.class, reader);
    } //-- java.lang.Object unmarshalPart(java.io.Reader) 

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
