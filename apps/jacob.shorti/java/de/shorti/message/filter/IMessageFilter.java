package de.shorti.message.filter;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */
import de.shorti.message.*;

public interface IMessageFilter
{
    public boolean onBeginProcess(Message m);
    public boolean onEndProcess(Message m);
}