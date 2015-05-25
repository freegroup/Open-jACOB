package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.context.basis.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.*;
import de.shorti.util.basic.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import de.shorti.message.Message;

public class Calendar extends GenericContext
{
    public Calendar()
    {
        super("./context/Calendar.xml");
    }
}