/*
 * Created on 28.07.2011
 *
 */
package de.tif.jacob.components.leaflet;

import java.util.UUID;

import de.tif.jacob.util.StringUtil;

/**
 * Wrapper for Latitute / Longitude 
 */
public class LeafletLatLng
{
  public final String guid;
  private double lat;
  private double lon;
  private String labelHTML;
  private boolean draggable;
  private Object userData;
  
  public LeafletLatLng(String htmlLabel, double lat, double lon)
  {
    this.guid = UUID.randomUUID().toString();
    this.labelHTML=StringUtil.replace(htmlLabel,"\"","'");
    this.lat = lat;
    this.lon = lon;
  }  
  public LeafletLatLng(double lat, double lon)
  {
    this.guid = UUID.randomUUID().toString();
    this.labelHTML=null;
    this.lat = lat;
    this.lon = lon;
  }
  
  boolean isDraggable()
  {
    return draggable;
  }
  
  void setDraggable(boolean draggable)
  {
    this.draggable = draggable;
  }
  
  public double getLat()
  {
    return lat;
  }
  
  public double getLon()
  {
    return lon;
  }
  public void setLatLng(double lat, double lon)
  {
    this.lat = lat;
    this.lon = lon;
  }
  
  public String getLabelHTML()
  {
    return labelHTML;
  }
  public void setLabelHTML(String labelHTML)
  {
    this.labelHTML = labelHTML;
  }
  
  public Object getUserData()
  {
    return userData;
  }
  
  public void setUserData(Object userData)
  {
    this.userData = userData;
  }
}
