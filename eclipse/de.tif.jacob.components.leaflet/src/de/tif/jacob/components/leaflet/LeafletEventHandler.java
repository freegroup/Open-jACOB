package de.tif.jacob.components.leaflet;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;


public abstract class LeafletEventHandler extends IGroupMemberEventHandler
{
    public LeafletEventHandler()
    {
    }

    public final void onGroupStatusChanged(IClientContext context, de.tif.jacob.screen.IGuiElement.GroupState state, IGuiElement element) throws Exception
    {
        onGroupStatusChanged(context, state, (ILeaflet)element);
    }

    public void onMarkerDragged(IClientContext context,  ILeaflet leaflet, LeafletLatLng latlng){
      
    }
    
    public abstract void onGroupStatusChanged(IClientContext iclientcontext, de.tif.jacob.screen.IGuiElement.GroupState groupstate, ILeaflet leaflet) throws Exception;
}
