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
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CastorActivityDiagram.
 * 
 * @version $Revision$ $Date$
 */
public class CastorActivityDiagram implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _createDate
     */
    private java.util.Date _createDate;

    /**
     * Field _diagramNodeList
     */
    private java.util.Vector _diagramNodeList;

    /**
     * Field _transitionList
     */
    private java.util.Vector _transitionList;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorActivityDiagram() {
        super();
        _diagramNodeList = new Vector();
        _transitionList = new Vector();
        _propertyList = new Vector();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDiagramNode
     * 
     * @param vDiagramNode
     */
    public void addDiagramNode(de.tif.jacob.core.definition.impl.jad.castor.DiagramNode vDiagramNode)
        throws java.lang.IndexOutOfBoundsException
    {
        _diagramNodeList.addElement(vDiagramNode);
    } //-- void addDiagramNode(de.tif.jacob.core.definition.impl.jad.castor.DiagramNode) 

    /**
     * Method addDiagramNode
     * 
     * @param index
     * @param vDiagramNode
     */
    public void addDiagramNode(int index, de.tif.jacob.core.definition.impl.jad.castor.DiagramNode vDiagramNode)
        throws java.lang.IndexOutOfBoundsException
    {
        _diagramNodeList.insertElementAt(vDiagramNode, index);
    } //-- void addDiagramNode(int, de.tif.jacob.core.definition.impl.jad.castor.DiagramNode) 

    /**
     * Method addProperty
     * 
     * @param vProperty
     */
    public void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.addElement(vProperty);
    } //-- void addProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method addProperty
     * 
     * @param index
     * @param vProperty
     */
    public void addProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.insertElementAt(vProperty, index);
    } //-- void addProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method addTransition
     * 
     * @param vTransition
     */
    public void addTransition(de.tif.jacob.core.definition.impl.jad.castor.Transition vTransition)
        throws java.lang.IndexOutOfBoundsException
    {
        _transitionList.addElement(vTransition);
    } //-- void addTransition(de.tif.jacob.core.definition.impl.jad.castor.Transition) 

    /**
     * Method addTransition
     * 
     * @param index
     * @param vTransition
     */
    public void addTransition(int index, de.tif.jacob.core.definition.impl.jad.castor.Transition vTransition)
        throws java.lang.IndexOutOfBoundsException
    {
        _transitionList.insertElementAt(vTransition, index);
    } //-- void addTransition(int, de.tif.jacob.core.definition.impl.jad.castor.Transition) 

    /**
     * Method enumerateDiagramNode
     */
    public java.util.Enumeration enumerateDiagramNode()
    {
        return _diagramNodeList.elements();
    } //-- java.util.Enumeration enumerateDiagramNode() 

    /**
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Method enumerateTransition
     */
    public java.util.Enumeration enumerateTransition()
    {
        return _transitionList.elements();
    } //-- java.util.Enumeration enumerateTransition() 

    /**
     * Returns the value of field 'createDate'.
     * 
     * @return the value of field 'createDate'.
     */
    public java.util.Date getCreateDate()
    {
        return this._createDate;
    } //-- java.util.Date getCreateDate() 

    /**
     * Method getDiagramNode
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DiagramNode getDiagramNode(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _diagramNodeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.DiagramNode) _diagramNodeList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DiagramNode getDiagramNode(int) 

    /**
     * Method getDiagramNode
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DiagramNode[] getDiagramNode()
    {
        int size = _diagramNodeList.size();
        de.tif.jacob.core.definition.impl.jad.castor.DiagramNode[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.DiagramNode[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.DiagramNode) _diagramNodeList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DiagramNode[] getDiagramNode() 

    /**
     * Method getDiagramNodeCount
     */
    public int getDiagramNodeCount()
    {
        return _diagramNodeList.size();
    } //-- int getDiagramNodeCount() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty getProperty(int) 

    /**
     * Method getProperty
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty()
    {
        int size = _propertyList.size();
        de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) _propertyList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] getProperty() 

    /**
     * Method getPropertyCount
     */
    public int getPropertyCount()
    {
        return _propertyList.size();
    } //-- int getPropertyCount() 

    /**
     * Method getTransition
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Transition getTransition(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _transitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.tif.jacob.core.definition.impl.jad.castor.Transition) _transitionList.elementAt(index);
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Transition getTransition(int) 

    /**
     * Method getTransition
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Transition[] getTransition()
    {
        int size = _transitionList.size();
        de.tif.jacob.core.definition.impl.jad.castor.Transition[] mArray = new de.tif.jacob.core.definition.impl.jad.castor.Transition[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.tif.jacob.core.definition.impl.jad.castor.Transition) _transitionList.elementAt(index);
        }
        return mArray;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Transition[] getTransition() 

    /**
     * Method getTransitionCount
     */
    public int getTransitionCount()
    {
        return _transitionList.size();
    } //-- int getTransitionCount() 

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
     * Method removeAllDiagramNode
     */
    public void removeAllDiagramNode()
    {
        _diagramNodeList.removeAllElements();
    } //-- void removeAllDiagramNode() 

    /**
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeAllTransition
     */
    public void removeAllTransition()
    {
        _transitionList.removeAllElements();
    } //-- void removeAllTransition() 

    /**
     * Method removeDiagramNode
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DiagramNode removeDiagramNode(int index)
    {
        java.lang.Object obj = _diagramNodeList.elementAt(index);
        _diagramNodeList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.DiagramNode) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DiagramNode removeDiagramNode(int) 

    /**
     * Method removeProperty
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int index)
    {
        java.lang.Object obj = _propertyList.elementAt(index);
        _propertyList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorProperty removeProperty(int) 

    /**
     * Method removeTransition
     * 
     * @param index
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Transition removeTransition(int index)
    {
        java.lang.Object obj = _transitionList.elementAt(index);
        _transitionList.removeElementAt(index);
        return (de.tif.jacob.core.definition.impl.jad.castor.Transition) obj;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Transition removeTransition(int) 

    /**
     * Sets the value of field 'createDate'.
     * 
     * @param createDate the value of field 'createDate'.
     */
    public void setCreateDate(java.util.Date createDate)
    {
        this._createDate = createDate;
    } //-- void setCreateDate(java.util.Date) 

    /**
     * Method setDiagramNode
     * 
     * @param index
     * @param vDiagramNode
     */
    public void setDiagramNode(int index, de.tif.jacob.core.definition.impl.jad.castor.DiagramNode vDiagramNode)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _diagramNodeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _diagramNodeList.setElementAt(vDiagramNode, index);
    } //-- void setDiagramNode(int, de.tif.jacob.core.definition.impl.jad.castor.DiagramNode) 

    /**
     * Method setDiagramNode
     * 
     * @param diagramNodeArray
     */
    public void setDiagramNode(de.tif.jacob.core.definition.impl.jad.castor.DiagramNode[] diagramNodeArray)
    {
        //-- copy array
        _diagramNodeList.removeAllElements();
        for (int i = 0; i < diagramNodeArray.length; i++) {
            _diagramNodeList.addElement(diagramNodeArray[i]);
        }
    } //-- void setDiagramNode(de.tif.jacob.core.definition.impl.jad.castor.DiagramNode) 

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
     * Method setProperty
     * 
     * @param index
     * @param vProperty
     */
    public void setProperty(int index, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propertyList.setElementAt(vProperty, index);
    } //-- void setProperty(int, de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method setProperty
     * 
     * @param propertyArray
     */
    public void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty[] propertyArray)
    {
        //-- copy array
        _propertyList.removeAllElements();
        for (int i = 0; i < propertyArray.length; i++) {
            _propertyList.addElement(propertyArray[i]);
        }
    } //-- void setProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty) 

    /**
     * Method setTransition
     * 
     * @param index
     * @param vTransition
     */
    public void setTransition(int index, de.tif.jacob.core.definition.impl.jad.castor.Transition vTransition)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _transitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _transitionList.setElementAt(vTransition, index);
    } //-- void setTransition(int, de.tif.jacob.core.definition.impl.jad.castor.Transition) 

    /**
     * Method setTransition
     * 
     * @param transitionArray
     */
    public void setTransition(de.tif.jacob.core.definition.impl.jad.castor.Transition[] transitionArray)
    {
        //-- copy array
        _transitionList.removeAllElements();
        for (int i = 0; i < transitionArray.length; i++) {
            _transitionList.addElement(transitionArray[i]);
        }
    } //-- void setTransition(de.tif.jacob.core.definition.impl.jad.castor.Transition) 

    /**
     * Method unmarshalCastorActivityDiagram
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorActivityDiagram(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram.class, reader);
    } //-- java.lang.Object unmarshalCastorActivityDiagram(java.io.Reader) 

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
