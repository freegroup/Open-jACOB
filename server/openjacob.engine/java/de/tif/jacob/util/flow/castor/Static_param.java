/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: Static_param.java,v 1.3 2011/07/01 21:23:55 freegroup Exp $
 */

package de.tif.jacob.util.flow.castor;

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
 * Class Static_param.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/07/01 21:23:55 $
 */
public class Static_param implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _blockList
     */
    private java.util.Vector _blockList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Static_param() {
        super();
        _blockList = new Vector();
    } //-- de.tif.jacob.util.flow.castor.Static_param()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addBlock
     * 
     * @param vBlock
     */
    public void addBlock(de.tif.jacob.util.flow.castor.Block vBlock)
        throws java.lang.IndexOutOfBoundsException
    {
        _blockList.addElement(vBlock);
    } //-- void addBlock(de.tif.jacob.util.flow.castor.Block) 

    /**
     * Method addBlock
     * 
     * @param index
     * @param vBlock
     */
    public void addBlock(int index, de.tif.jacob.util.flow.castor.Block vBlock)
        throws java.lang.IndexOutOfBoundsException
    {
        _blockList.insertElementAt(vBlock, index);
    } //-- void addBlock(int, de.tif.jacob.util.flow.castor.Block) 

    /**
     * Method enumerateBlock
     */
    public java.util.Enumeration enumerateBlock()
    {
        return _blockList.elements();
    } //-- java.util.Enumeration enumerateBlock() 

    /**
     * Method getBlock
     * 
     * @param index
     */
    public de.tif.jacob.util.flow.castor.Block getBlock(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _blockList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.util.flow.castor.Block) _blockList.elementAt(index);
    } //-- de.tif.jacob.util.flow.castor.Block getBlock(int) 

    /**
     * Method getBlock
     */
    public de.tif.jacob.util.flow.castor.Block[] getBlock()
    {
        int size = _blockList.size();
        de.tif.jacob.util.flow.castor.Block[] mArray = new de.tif.jacob.util.flow.castor.Block[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.util.flow.castor.Block) _blockList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.util.flow.castor.Block[] getBlock() 

    /**
     * Method getBlockCount
     */
    public int getBlockCount()
    {
        return _blockList.size();
    } //-- int getBlockCount() 

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
     * Method removeAllBlock
     */
    public void removeAllBlock()
    {
        _blockList.removeAllElements();
    } //-- void removeAllBlock() 

    /**
     * Method removeBlock
     * 
     * @param index
     */
    public de.tif.jacob.util.flow.castor.Block removeBlock(int index)
    {
        java.lang.Object obj = _blockList.elementAt(index);
        _blockList.removeElementAt(index);
        return (de.tif.jacob.util.flow.castor.Block) obj;
    } //-- de.tif.jacob.util.flow.castor.Block removeBlock(int) 

    /**
     * Method setBlock
     * 
     * @param index
     * @param vBlock
     */
    public void setBlock(int index, de.tif.jacob.util.flow.castor.Block vBlock)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _blockList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _blockList.setElementAt(vBlock, index);
    } //-- void setBlock(int, de.tif.jacob.util.flow.castor.Block) 

    /**
     * Method setBlock
     * 
     * @param blockArray
     */
    public void setBlock(de.tif.jacob.util.flow.castor.Block[] blockArray)
    {
        //-- copy array
        _blockList.removeAllElements();
        for (int i = 0; i < blockArray.length; i++) {
            _blockList.addElement(blockArray[i]);
        }
    } //-- void setBlock(de.tif.jacob.util.flow.castor.Block) 

    /**
     * Method unmarshalStatic_param
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalStatic_param(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.util.flow.castor.Static_param) Unmarshaller.unmarshal(de.tif.jacob.util.flow.castor.Static_param.class, reader);
    } //-- java.lang.Object unmarshalStatic_param(java.io.Reader) 

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
