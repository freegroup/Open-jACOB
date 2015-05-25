/*
 * Created on 28.07.2011
 *
 */
package de.tif.jacob.components.leaflet;
import java.util.ArrayList;
import java.util.List;

public class LeafletPolyline
{
  int weight = 2; // => line width in pixels
  boolean fill = false; // => true to fill the area from the line to the x axis,false for (transparent) no fill
  boolean stroke = true; // => hide / show the outer stroke of the polygon
  String fillColor = LeafletColors.ORANGE; // => fill color
  String lineColor = LeafletColors.LIGHT_BLUE;
  float fillOpacity = 0.5f; // fill opacity [0..1]
  float opacity = 0.8f; // stroke opacity [0..1]
  private List<LeafletLatLng> points = new ArrayList<LeafletLatLng>();

  public int getWeight()
  {
    return weight;
  }

  public void setWeight(int weight)
  {
    this.weight = weight;
  }

  public boolean isFill()
  {
    return fill;
  }

  public void setFill(boolean fill)
  {
    this.fill = fill;
  }

  public boolean isStroke()
  {
    return stroke;
  }

  public void setStroke(boolean stroke)
  {
    this.stroke = stroke;
  }

  public String getFillColor()
  {
    return fillColor;
  }

  public void setFillColor(String fillColor)
  {
    this.fillColor = fillColor;
  }

  public String getLineColor()
  {
    return lineColor;
  }

  public void setLineColor(String lineColor)
  {
    this.lineColor = lineColor;
  }

  public float getFillOpacity()
  {
    return fillOpacity;
  }

  public void setFillOpacity(float fillOpacity)
  {
    this.fillOpacity = fillOpacity;
  }

  public float getOpacity()
  {
    return opacity;
  }

  public void setOpacity(float opacity)
  {
    this.opacity = opacity;
  }

  public List<LeafletLatLng> getPoints()
  {
    return points;
  }

  public void setPoints(List<LeafletLatLng> points)
  {
    this.points = points;
  }
  
  public void addLocation(LeafletLatLng loc)
  {
    this.points.add(loc);
  }
}
