package de.shorti.baseknowledge;
/**
 * Title:        short-i
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer
 * @version 1.0
 */

public class GISCoordinate
{
    private boolean flagNorth;
    private boolean flagEast;

    private double widthDegrees;
    private double lengthDegrees;

    private double widthRadians;
    private double lengthRadians;

    public GISCoordinate()
    {
        flagNorth = true;
        flagEast = true;

        widthDegrees = 0.0;
        lengthDegrees = 0.0;

        widthRadians = 0.0;
        lengthRadians = 0.0;
    }

    public GISCoordinate(CoordsTriple pWidth, CoordsTriple pLength, boolean pNorth, boolean pEast)
    {
        setWidthTriple(pWidth);
        setLengthTriple(pLength);
        setFlagNorth(pNorth);
        setFlagEast(pEast);
    }

    public GISCoordinate(double pWidth, double pLength, boolean pDegrees, boolean pNorth, boolean pEast)
    {
        if(pDegrees)
        {
            setWidthDegrees(pWidth);
            setLengthDegrees(pLength);
        }
        else
        {
            setWidthRadians(pWidth);
            setLengthRadians(pLength);
        }
        setFlagNorth(pNorth);
        setFlagEast(pEast);
    }

    public GISCoordinate(double pWidth, double pLength)
    {
        setWidthDegrees(pWidth);
        setLengthDegrees(pLength);
        setFlagNorth(true);
        setFlagEast(true);
    }

    public void setWidthTriple(CoordsTriple pWidth)
    {
        widthDegrees = coordsTripleToDegrees(pWidth);
        widthRadians = Math.toRadians(widthDegrees);
    }
    public void setLengthTriple(CoordsTriple pLength)
    {
        lengthDegrees = coordsTripleToDegrees(pLength);
        lengthRadians = Math.toRadians(lengthDegrees);
    }
    public void setWidthDegrees(double pWidthDegrees)
    {
        widthDegrees = pWidthDegrees;
        widthRadians = Math.toRadians(widthDegrees);
    }
    public void setLengthDegrees(double pLengthDegrees)
    {
        lengthDegrees = pLengthDegrees;
        lengthRadians = Math.toRadians(lengthDegrees);
    }

    public void setWidthRadians(double pWidthRadians)
    {
        widthRadians = pWidthRadians;
        widthDegrees = Math.toDegrees(widthRadians);
    }
    public void setLengthRadians(double pLengthRadians)
    {
        lengthRadians = pLengthRadians;
        lengthDegrees = Math.toDegrees(lengthRadians);
    }
    public void setFlagNorth(boolean pNorth)
    {
        flagNorth = pNorth;
    }
    public void setFlagEast(boolean pEast)
    {
        flagEast = pEast;
    }

    public boolean isNorthernHemisphere()
    {
        return flagNorth;
    }
    public boolean isEastOfGreenwich()
    {
        return flagEast;
    }

    private double coordsTripleToDegrees(CoordsTriple triple)
    {
        return (triple.getDegrees()
              +(triple.getMinutes() / 60.0)
              +(triple.getSeconds() / 3600.0));
    }

    public static double getDistance(GISCoordinate coordinate1, GISCoordinate coordinate2)
    {
        int c1NorthSign = coordinate1.isNorthernHemisphere()?1:-1;
        int c1EastSign  = coordinate1.isEastOfGreenwich()?1:-1;
        int c2NorthSign = coordinate2.isNorthernHemisphere()?1:-1;
        int c2EastSign  = coordinate2.isEastOfGreenwich()?1:-1;

        return (Math.acos( Math.sin(c1NorthSign * coordinate1.widthRadians) *
                Math.sin(c2NorthSign * coordinate2.widthRadians) +
                Math.cos(c1NorthSign * coordinate1.widthRadians) *
                Math.cos(c2NorthSign * coordinate2.widthRadians) *
                Math.cos((c2EastSign * coordinate2.lengthRadians) -
                (c1EastSign * coordinate1.lengthRadians))))* 6378388.0; // Earth radius = 6378388 m
    }

    public double getDistance(GISCoordinate coordinate2)
    {
        return getDistance(this, coordinate2);
    }

    public static void main(String[] args)
    {
        GISCoordinate GISFromDegFrankfurt = new GISCoordinate(50.11222, 8.68194, true, true, true);
        GISCoordinate GISFromRadFrankfurt = new GISCoordinate(0.87462, 0.15153, false, true, true);

        GISCoordinate GISFromTripleFrankfurt = new GISCoordinate(new CoordsTriple(50, 6, 44), new CoordsTriple(8, 40, 55), true, true);
        GISCoordinate GISFromTripleBerlin = new GISCoordinate(new CoordsTriple(52, 31, 20), new CoordsTriple(13, 17, 51), true, true);

        double distance = GISCoordinate.getDistance(GISFromTripleFrankfurt, GISFromTripleBerlin);
        System.out.println(distance);

        distance = GISFromTripleFrankfurt.getDistance(GISFromTripleBerlin);
        System.out.println(distance);
    }
}
