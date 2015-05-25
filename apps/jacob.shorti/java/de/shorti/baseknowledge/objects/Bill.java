package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */

public class Bill extends _dbBill
{
    public final static int STATUS_CREATED    = 1;
    public final static int STATUS_OPEN       = 2;
    public final static int STATUS_REMINDED   = 3;
    public final static int STATUS_PAYED      = 4;
    public final static int STATUS_REMOVEABLE = 5;
}