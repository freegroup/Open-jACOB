package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.db.*;

public class SystemStatistik extends _dbSystemStatistik
{
    public void incValue()
    {
        setValue(value+1,true);
    }
}