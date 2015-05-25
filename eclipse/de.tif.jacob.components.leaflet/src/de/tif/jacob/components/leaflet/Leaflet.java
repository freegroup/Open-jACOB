package de.tif.jacob.components.leaflet;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.PluginComponentImpl;
public class Leaflet extends PluginComponentImpl implements ILeaflet
{
  public final static String   PROPERTY_MAPSERVER     = "Map Server URL";

  List<LeafletLatLng> marker;
  List<LeafletLatLng> draggableMarker;
  List<LeafletPolyline> polygons;
  LeafletLatLng center = null;
  boolean initViewport = true;
  int zoom = 14;
  public final static String DEFAULT_URL ="http://service{s}.droyd.de/tiles/14/{z}/{x}/{y}";

  public Leaflet()
  {
    marker = new ArrayList<LeafletLatLng>();
    polygons = new ArrayList<LeafletPolyline>();
  }

  public void calculateHTML(ClientContext context, Writer w) throws Exception
    {
        int width = wrapper.getBoundingRect().width;
        int height = wrapper.getBoundingRect().height;
        String id = Integer.toString(getId());
        w.write((new StringBuilder()).append("<div id='container").append(id).append("' style=\"position:absolute; width: ").append(width).append("px; height: ").append(height).append("px;\"></div>").toString());
        w.write("<script type=\"text/javascript\">\n");

        w.write("var cloudmadeUrl = '"+getMapServerUrl()+"',\n");
        w.write("cloudmadeAttribution = '&copy; 2011 CloudMade',\n");
        w.write("cloudmade = new L.TileLayer(cloudmadeUrl, {zoom:"+this.zoom+", maxZoom: 15, attribution: cloudmadeAttribution, subdomains : '0123456789'});\n");
        w.write("var map = new L.Map('container"+id+"', {zoom:"+this.zoom+",  fadeAnimation:false, zoomAnimation:false});\n");
        w.write("var boundings = [];\n");
    
        if(center!=null)
        {
          w.write("map.setView(new L.LatLng("+center.getLat()+", "+center.getLon()+"), "+zoom+").addLayer(cloudmade);\n");
        }
        else
        {
          w.write("map.setView(new L.LatLng(48.77608, 9.16403), "+zoom+").addLayer(cloudmade);\n");
        }
        
        // Draw the single Locations on the map
        //
        for (LeafletLatLng pos : marker) {
          w.write("boundings.push(new L.LatLng("+pos.getLat()+", "+pos.getLon()+"));\n");
          w.write("var props = [];");
          w.write("props.draggable="+pos.isDraggable()+";");
          w.write("var marker = new L.Marker(new L.LatLng("+pos.getLat()+", "+pos.getLon()+"),props);\n");
          if(pos.getLabelHTML()!=null)
            w.write("marker.bindPopup(\""+pos.getLabelHTML()+"\");\n");
          w.write("marker.jacob_guid='"+pos.guid+"'\n");
          w.write("marker.on('dragend',function(event){\n");
          w.write("   var map = event.target._map;\n");
          w.write("   var latlng = event.target.getLatLng();\n");
          w.write("   var value = [];\n");
          w.write("   value.push(event.target.jacob_guid);\n");
          w.write("   value.push(latlng.lat);\n"); // new coordinate of the marker
          w.write("   value.push(latlng.lng);\n");
          w.write("   var c = map.getCenter();\n");
          w.write("   value.push(c.lat);\n");
          w.write("   value.push(c.lng);\n");
          w.write("   value.push(map._zoom);\n");
          w.write("   FireEventData('" + Integer.toString(getId()) + "', 'click',value.join(';'));\n");
          w.write("});\n");
          w.write("map.addLayer(marker);\n");
        }

        // Draw the polygons on the map
        //
        for (LeafletPolyline polygon : polygons) {
          w.write("{\n");
          w.write("var latlngs = [];\n");
          for (LeafletLatLng pos : polygon.getPoints()) {
            w.write("latlngs.push(new L.LatLng("+pos.getLat()+", "+pos.getLon()+"));\n");
            w.write("boundings.push(new L.LatLng("+pos.getLat()+", "+pos.getLon()+"));\n");
          }
          w.write("var polyline = new L.Polyline(latlngs, {");
          if (polygon.fill)
          {
            w.write("fill: " + polygon.fill + ", ");
            w.write("fillColor: '" + polygon.fillColor + "', ");
            w.write("fillOpacity: " + polygon.fillOpacity + ", ");
          }
          w.write("color: '"+polygon.lineColor+"', ");
          w.write("stroke: "+polygon.stroke+", ");
          w.write("weight: "+polygon.weight+", ");
          w.write("opacity: "+polygon.opacity);  
          w.write("});\n");
          w.write("map.addLayer(polyline);\n");
          w.write("}\n");
        }
        if(initViewport)
          w.write("map.fitBounds(new L.LatLngBounds(boundings));\n");
        w.write("</script>\n");
        super.calculateHTML(context, w);
    }

  
  public String getMapServerUrl() throws Exception
  {
      String url = this.getProperty(PROPERTY_MAPSERVER);
      if(url==null  || url.length()==0)
        return DEFAULT_URL;
      
      return url;
  }

  public void addMarker(IClientContext context, LeafletLatLng latLon) throws Exception
  {
    this.addMarker(context, latLon, false);
  }


  public void addMarker(IClientContext context, LeafletLatLng latLon, boolean draggable) throws Exception
  {
    latLon.setDraggable(draggable);
    marker.add(latLon);
  }
  
  public void addPolygon(IClientContext context, LeafletPolyline polygon) throws Exception
  {
    polygons.add(polygon);
  }

  public void resetPositions(IClientContext context) throws Exception
  {
    this.clear(context);
    this.resetCache();
    initViewport = true;
    center = null;
    marker = new ArrayList<LeafletLatLng>();
    polygons = new ArrayList<LeafletPolyline>();
  }

  public void clear(IClientContext context) throws Exception
  {
    super.clear(context);
  }

  public void onGroupDataStatusChanged(IClientContext context, de.tif.jacob.screen.IGuiElement.GroupState newGroupDataStatus) throws Exception
  {
    LeafletEventHandler handler = (LeafletEventHandler) wrapper.getEventHandler(context);
    if (handler != null)
      handler.onGroupStatusChanged(context, newGroupDataStatus, (ILeaflet) this);
  }

  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if(guid==this.getId()){
      String[] values = value.split("[;]");
      if(values.length==6){
        this.resetCache();
        this.center = new LeafletLatLng(Double.parseDouble(values[3]),Double.parseDouble(values[4]));
        this.zoom = Integer.parseInt(values[5]);
        this.initViewport = false;
 
        String markerGuid = values[0];
        for (LeafletLatLng poi : marker){
          if(poi.guid.equals(markerGuid)){
            poi.setLatLng(Double.parseDouble(values[1]),Double.parseDouble(values[2]));
            LeafletEventHandler handler = (LeafletEventHandler) wrapper.getEventHandler(context);
            if (handler != null)
              handler.onMarkerDragged(context, (ILeaflet) this, poi);
            break;
          }
        }
      }
      return true;
    }
    return super.processEvent(context, guid, event, value);
  }

  public boolean processParameter(int guid, String value)
  {
    return super.processParameter(guid, value);
  }

  public Properties getProperties()
  {
    Properties props = new Properties();
    // props.put("zoomEnabled", new Boolean(zoomEnabled));
    return props;
  }

  public void setProperties(Properties properties)
  {
    // zoomEnabled = ((Boolean)properties.get("zoomEnabled")).booleanValue();
  }

  public String[] getRequiredIncludeFiles()
  {
    return (new String[] { "leaflet.js", "leaflet.css" });
  }
}
