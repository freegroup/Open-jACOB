package de.shorti.util.file;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author
 * @version 1.0
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams
{
    public static void copyStream(InputStream in, OutputStream out) throws IOException
    {
        synchronized(in)
        {
            synchronized(out)
            {
                byte[] buffer = new byte[256];
                while(true)
                {
                    int bytesRead = in.read(buffer);
                    if(bytesRead == -1)
                        break;
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}