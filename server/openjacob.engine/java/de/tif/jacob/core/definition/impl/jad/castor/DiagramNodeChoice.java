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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class DiagramNodeChoice.
 * 
 * @version $Revision$ $Date$
 */
public class DiagramNodeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _activity
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Activity _activity;

    /**
     * Field _forkJoin
     */
    private de.tif.jacob.core.definition.impl.jad.castor.ForkJoin _forkJoin;

    /**
     * Field _branchMerge
     */
    private de.tif.jacob.core.definition.impl.jad.castor.BranchMerge _branchMerge;

    /**
     * Field _start
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Start _start;

    /**
     * Field _end
     */
    private de.tif.jacob.core.definition.impl.jad.castor.End _end;


      //----------------/
     //- Constructors -/
    //----------------/

    public DiagramNodeChoice() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DiagramNodeChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'activity'.
     * 
     * @return the value of field 'activity'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Activity getActivity()
    {
        return this._activity;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Activity getActivity() 

    /**
     * Returns the value of field 'branchMerge'.
     * 
     * @return the value of field 'branchMerge'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.BranchMerge getBranchMerge()
    {
        return this._branchMerge;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.BranchMerge getBranchMerge() 

    /**
     * Returns the value of field 'end'.
     * 
     * @return the value of field 'end'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.End getEnd()
    {
        return this._end;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.End getEnd() 

    /**
     * Returns the value of field 'forkJoin'.
     * 
     * @return the value of field 'forkJoin'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ForkJoin getForkJoin()
    {
        return this._forkJoin;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ForkJoin getForkJoin() 

    /**
     * Returns the value of field 'start'.
     * 
     * @return the value of field 'start'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Start getStart()
    {
        return this._start;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Start getStart() 

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
     * Sets the value of field 'activity'.
     * 
     * @param activity the value of field 'activity'.
     */
    public void setActivity(de.tif.jacob.core.definition.impl.jad.castor.Activity activity)
    {
        this._activity = activity;
    } //-- void setActivity(de.tif.jacob.core.definition.impl.jad.castor.Activity) 

    /**
     * Sets the value of field 'branchMerge'.
     * 
     * @param branchMerge the value of field 'branchMerge'.
     */
    public void setBranchMerge(de.tif.jacob.core.definition.impl.jad.castor.BranchMerge branchMerge)
    {
        this._branchMerge = branchMerge;
    } //-- void setBranchMerge(de.tif.jacob.core.definition.impl.jad.castor.BranchMerge) 

    /**
     * Sets the value of field 'end'.
     * 
     * @param end the value of field 'end'.
     */
    public void setEnd(de.tif.jacob.core.definition.impl.jad.castor.End end)
    {
        this._end = end;
    } //-- void setEnd(de.tif.jacob.core.definition.impl.jad.castor.End) 

    /**
     * Sets the value of field 'forkJoin'.
     * 
     * @param forkJoin the value of field 'forkJoin'.
     */
    public void setForkJoin(de.tif.jacob.core.definition.impl.jad.castor.ForkJoin forkJoin)
    {
        this._forkJoin = forkJoin;
    } //-- void setForkJoin(de.tif.jacob.core.definition.impl.jad.castor.ForkJoin) 

    /**
     * Sets the value of field 'start'.
     * 
     * @param start the value of field 'start'.
     */
    public void setStart(de.tif.jacob.core.definition.impl.jad.castor.Start start)
    {
        this._start = start;
    } //-- void setStart(de.tif.jacob.core.definition.impl.jad.castor.Start) 

    /**
     * Method unmarshalDiagramNodeChoice
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalDiagramNodeChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.DiagramNodeChoice) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.DiagramNodeChoice.class, reader);
    } //-- java.lang.Object unmarshalDiagramNodeChoice(java.io.Reader) 

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
