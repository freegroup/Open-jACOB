/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package jacob.scheduler.system.callTaskSynchronizer.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType;
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
 * Ort nach Faplisnotation
 * 
 * @version $Revision$ $Date$
 */
public class Location implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _baxis_key
     */
    private java.lang.String _baxis_key;

    /**
     * Field _building_key
     */
    private java.lang.String _building_key;

    /**
     * Field _buildingpart_key
     */
    private java.lang.String _buildingpart_key;

    /**
     * Field _buildpartobj_key
     */
    private java.lang.String _buildpartobj_key;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _floor_key
     */
    private java.lang.String _floor_key;

    /**
     * Field _gdsbaxis_key
     */
    private java.lang.String _gdsbaxis_key;

    /**
     * Field _gdszaxis_key
     */
    private java.lang.String _gdszaxis_key;

    /**
     * Field _note
     */
    private java.lang.String _note;

    /**
     * Field _orientation
     */
    private jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType _orientation;

    /**
     * Field _plane_key
     */
    private java.lang.String _plane_key;

    /**
     * Field _room_key
     */
    private java.lang.String _room_key;

    /**
     * Field _site_key
     */
    private java.lang.String _site_key;

    /**
     * Field _sitepart_key
     */
    private java.lang.String _sitepart_key;

    /**
     * Field _zaxis_key
     */
    private java.lang.String _zaxis_key;


      //----------------/
     //- Constructors -/
    //----------------/

    public Location() {
        super();
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.Location()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'baxis_key'.
     * 
     * @return the value of field 'baxis_key'.
     */
    public java.lang.String getBaxis_key()
    {
        return this._baxis_key;
    } //-- java.lang.String getBaxis_key() 

    /**
     * Returns the value of field 'building_key'.
     * 
     * @return the value of field 'building_key'.
     */
    public java.lang.String getBuilding_key()
    {
        return this._building_key;
    } //-- java.lang.String getBuilding_key() 

    /**
     * Returns the value of field 'buildingpart_key'.
     * 
     * @return the value of field 'buildingpart_key'.
     */
    public java.lang.String getBuildingpart_key()
    {
        return this._buildingpart_key;
    } //-- java.lang.String getBuildingpart_key() 

    /**
     * Returns the value of field 'buildpartobj_key'.
     * 
     * @return the value of field 'buildpartobj_key'.
     */
    public java.lang.String getBuildpartobj_key()
    {
        return this._buildpartobj_key;
    } //-- java.lang.String getBuildpartobj_key() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'floor_key'.
     * 
     * @return the value of field 'floor_key'.
     */
    public java.lang.String getFloor_key()
    {
        return this._floor_key;
    } //-- java.lang.String getFloor_key() 

    /**
     * Returns the value of field 'gdsbaxis_key'.
     * 
     * @return the value of field 'gdsbaxis_key'.
     */
    public java.lang.String getGdsbaxis_key()
    {
        return this._gdsbaxis_key;
    } //-- java.lang.String getGdsbaxis_key() 

    /**
     * Returns the value of field 'gdszaxis_key'.
     * 
     * @return the value of field 'gdszaxis_key'.
     */
    public java.lang.String getGdszaxis_key()
    {
        return this._gdszaxis_key;
    } //-- java.lang.String getGdszaxis_key() 

    /**
     * Returns the value of field 'note'.
     * 
     * @return the value of field 'note'.
     */
    public java.lang.String getNote()
    {
        return this._note;
    } //-- java.lang.String getNote() 

    /**
     * Returns the value of field 'orientation'.
     * 
     * @return the value of field 'orientation'.
     */
    public jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType getOrientation()
    {
        return this._orientation;
    } //-- jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType getOrientation() 

    /**
     * Returns the value of field 'plane_key'.
     * 
     * @return the value of field 'plane_key'.
     */
    public java.lang.String getPlane_key()
    {
        return this._plane_key;
    } //-- java.lang.String getPlane_key() 

    /**
     * Returns the value of field 'room_key'.
     * 
     * @return the value of field 'room_key'.
     */
    public java.lang.String getRoom_key()
    {
        return this._room_key;
    } //-- java.lang.String getRoom_key() 

    /**
     * Returns the value of field 'site_key'.
     * 
     * @return the value of field 'site_key'.
     */
    public java.lang.String getSite_key()
    {
        return this._site_key;
    } //-- java.lang.String getSite_key() 

    /**
     * Returns the value of field 'sitepart_key'.
     * 
     * @return the value of field 'sitepart_key'.
     */
    public java.lang.String getSitepart_key()
    {
        return this._sitepart_key;
    } //-- java.lang.String getSitepart_key() 

    /**
     * Returns the value of field 'zaxis_key'.
     * 
     * @return the value of field 'zaxis_key'.
     */
    public java.lang.String getZaxis_key()
    {
        return this._zaxis_key;
    } //-- java.lang.String getZaxis_key() 

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
     * Sets the value of field 'baxis_key'.
     * 
     * @param baxis_key the value of field 'baxis_key'.
     */
    public void setBaxis_key(java.lang.String baxis_key)
    {
        this._baxis_key = baxis_key;
    } //-- void setBaxis_key(java.lang.String) 

    /**
     * Sets the value of field 'building_key'.
     * 
     * @param building_key the value of field 'building_key'.
     */
    public void setBuilding_key(java.lang.String building_key)
    {
        this._building_key = building_key;
    } //-- void setBuilding_key(java.lang.String) 

    /**
     * Sets the value of field 'buildingpart_key'.
     * 
     * @param buildingpart_key the value of field 'buildingpart_key'
     */
    public void setBuildingpart_key(java.lang.String buildingpart_key)
    {
        this._buildingpart_key = buildingpart_key;
    } //-- void setBuildingpart_key(java.lang.String) 

    /**
     * Sets the value of field 'buildpartobj_key'.
     * 
     * @param buildpartobj_key the value of field 'buildpartobj_key'
     */
    public void setBuildpartobj_key(java.lang.String buildpartobj_key)
    {
        this._buildpartobj_key = buildpartobj_key;
    } //-- void setBuildpartobj_key(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'floor_key'.
     * 
     * @param floor_key the value of field 'floor_key'.
     */
    public void setFloor_key(java.lang.String floor_key)
    {
        this._floor_key = floor_key;
    } //-- void setFloor_key(java.lang.String) 

    /**
     * Sets the value of field 'gdsbaxis_key'.
     * 
     * @param gdsbaxis_key the value of field 'gdsbaxis_key'.
     */
    public void setGdsbaxis_key(java.lang.String gdsbaxis_key)
    {
        this._gdsbaxis_key = gdsbaxis_key;
    } //-- void setGdsbaxis_key(java.lang.String) 

    /**
     * Sets the value of field 'gdszaxis_key'.
     * 
     * @param gdszaxis_key the value of field 'gdszaxis_key'.
     */
    public void setGdszaxis_key(java.lang.String gdszaxis_key)
    {
        this._gdszaxis_key = gdszaxis_key;
    } //-- void setGdszaxis_key(java.lang.String) 

    /**
     * Sets the value of field 'note'.
     * 
     * @param note the value of field 'note'.
     */
    public void setNote(java.lang.String note)
    {
        this._note = note;
    } //-- void setNote(java.lang.String) 

    /**
     * Sets the value of field 'orientation'.
     * 
     * @param orientation the value of field 'orientation'.
     */
    public void setOrientation(jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType orientation)
    {
        this._orientation = orientation;
    } //-- void setOrientation(jacob.scheduler.system.callTaskSynchronizer.castor.types.OrientationType) 

    /**
     * Sets the value of field 'plane_key'.
     * 
     * @param plane_key the value of field 'plane_key'.
     */
    public void setPlane_key(java.lang.String plane_key)
    {
        this._plane_key = plane_key;
    } //-- void setPlane_key(java.lang.String) 

    /**
     * Sets the value of field 'room_key'.
     * 
     * @param room_key the value of field 'room_key'.
     */
    public void setRoom_key(java.lang.String room_key)
    {
        this._room_key = room_key;
    } //-- void setRoom_key(java.lang.String) 

    /**
     * Sets the value of field 'site_key'.
     * 
     * @param site_key the value of field 'site_key'.
     */
    public void setSite_key(java.lang.String site_key)
    {
        this._site_key = site_key;
    } //-- void setSite_key(java.lang.String) 

    /**
     * Sets the value of field 'sitepart_key'.
     * 
     * @param sitepart_key the value of field 'sitepart_key'.
     */
    public void setSitepart_key(java.lang.String sitepart_key)
    {
        this._sitepart_key = sitepart_key;
    } //-- void setSitepart_key(java.lang.String) 

    /**
     * Sets the value of field 'zaxis_key'.
     * 
     * @param zaxis_key the value of field 'zaxis_key'.
     */
    public void setZaxis_key(java.lang.String zaxis_key)
    {
        this._zaxis_key = zaxis_key;
    } //-- void setZaxis_key(java.lang.String) 

    /**
     * Method unmarshalLocation
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalLocation(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (jacob.scheduler.system.callTaskSynchronizer.castor.Location) Unmarshaller.unmarshal(jacob.scheduler.system.callTaskSynchronizer.castor.Location.class, reader);
    } //-- java.lang.Object unmarshalLocation(java.io.Reader) 

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
