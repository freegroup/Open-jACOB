package de.tif.jacob.components.leaflet;

import de.tif.jacob.screen.IClientContext;


public interface ILeaflet
{
    public abstract void addMarker(IClientContext context, LeafletLatLng latLon) throws Exception;
    
    public abstract void addMarker(IClientContext context, LeafletLatLng latLon, boolean draggable) throws Exception;
    
    public abstract void addPolygon(IClientContext context,LeafletPolyline polygon) throws Exception;
    
    public abstract void resetPositions(IClientContext context) throws Exception;

}
