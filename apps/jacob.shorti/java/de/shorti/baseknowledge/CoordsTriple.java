package de.shorti.baseknowledge;

/**
 * Title:        short-i
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer
 * @version 1.0
 */

public class CoordsTriple
{
    private int degrees;
    private int minutes;
    private int seconds;

    public CoordsTriple()
    {
        degrees = 0;
        minutes = 0;
        seconds = 0;
    }

    public CoordsTriple(int pDegrees, int pMinutes, int pSeconds)
    {
        setGradMinSec(pDegrees, pMinutes, pSeconds);
    }

    public void setGradMinSec(int pDegrees, int pMinutes, int pSeconds)
    {
        degrees = pDegrees;
        minutes = pMinutes;
        seconds = pSeconds;
    }

    public void setDegrees(int pDegrees)
    {
        degrees = pDegrees;
    }
    public void setMinutes(int pMinutes)
    {
        minutes = pMinutes;
    }
    public void setSeconds(int pSeconds)
    {
        seconds = pSeconds;
    }

    public int getDegrees()
    {
        return degrees;
    }
    public int getMinutes()
    {
        return minutes;
    }
    public int getSeconds()
    {
        return seconds;
    }
}