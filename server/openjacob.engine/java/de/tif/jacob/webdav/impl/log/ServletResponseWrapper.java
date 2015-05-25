/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
package de.tif.jacob.webdav.impl.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class ServletResponseWrapper implements ServletResponse
{
  static public final transient String RCS_ID = "$Id: ServletResponseWrapper.java,v 1.2 2010/07/07 15:33:34 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private class MyPrintWriter extends PrintWriter
  {
    private final PrintWriter embedded;
    private final PrintWriter pw;
    private final ByteArrayOutputStream os;

    private MyPrintWriter(PrintWriter embedded)
    {
      super(embedded);
      this.embedded = embedded;
      this.os = new ByteArrayOutputStream(1024);
      this.pw = new PrintWriter(os);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#checkError()
     */
    public boolean checkError()
    {
      return this.embedded.checkError();
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#close()
     */
    public void close()
    {
      this.embedded.close();
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#flush()
     */
    public void flush()
    {
      this.embedded.flush();
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(boolean)
     */
    public void print(boolean b)
    {
      this.pw.print(b);
      this.embedded.print(b);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(char)
     */
    public void print(char c)
    {
      this.pw.print(c);
      this.embedded.print(c);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(char[])
     */
    public void print(char[] s)
    {
      this.pw.print(s);
      this.embedded.print(s);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(double)
     */
    public void print(double d)
    {
      this.pw.print(d);
      this.embedded.print(d);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(float)
     */
    public void print(float f)
    {
      this.pw.print(f);
      this.embedded.print(f);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(int)
     */
    public void print(int i)
    {
      this.pw.print(i);
      this.embedded.print(i);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(long)
     */
    public void print(long l)
    {
      this.pw.print(l);
      this.embedded.print(l);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(java.lang.Object)
     */
    public void print(Object obj)
    {
      this.pw.print(obj);
      this.embedded.print(obj);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(java.lang.String)
     */
    public void print(String s)
    {
      this.pw.print(s);
      this.embedded.print(s);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println()
     */
    public void println()
    {
      this.pw.println();
      this.embedded.println();
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(boolean)
     */
    public void println(boolean x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(char)
     */
    public void println(char x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(char[])
     */
    public void println(char[] x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(double)
     */
    public void println(double x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(float)
     */
    public void println(float x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(int)
     */
    public void println(int x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(long)
     */
    public void println(long x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(java.lang.Object)
     */
    public void println(Object x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(java.lang.String)
     */
    public void println(String x)
    {
      this.pw.println(x);
      this.embedded.println(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#setError()
     */
    protected void setError()
    {
      throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#write(char[], int, int)
     */
    public void write(char[] buf, int off, int len)
    {
      this.pw.write(buf, off, len);
      this.embedded.write(buf, off, len);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#write(char[])
     */
    public void write(char[] buf)
    {
      this.pw.write(buf);
      this.embedded.write(buf);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#write(int)
     */
    public void write(int c)
    {
      this.pw.write(c);
      this.embedded.write(c);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#write(java.lang.String, int, int)
     */
    public void write(String s, int off, int len)
    {
      this.pw.write(s, off, len);
      this.embedded.write(s, off, len);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#write(java.lang.String)
     */
    public void write(String s)
    {
      this.pw.write(s);
      this.embedded.write(s);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
      return this.embedded.equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
      return this.embedded.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.embedded.toString();
    }

  }

  private class MyStream extends ServletOutputStream
  {
    private final ServletOutputStream embedded;
    private final ByteArrayOutputStream os;

    private MyStream(ServletOutputStream embedded)
    {
      this.embedded = embedded;
      this.os = new ByteArrayOutputStream(1024);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    public void flush() throws IOException
    {
      this.embedded.flush();
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len) throws IOException
    {
      this.embedded.write(b, off, len);
      this.os.write(b, off, len);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] b) throws IOException
    {
      this.embedded.write(b);
      this.os.write(b);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException
    {
      this.embedded.write(b);
      this.os.write(b);
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    public void close() throws IOException
    {
      this.embedded.close();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    protected Object clone() throws CloneNotSupportedException
    {
      throw new CloneNotSupportedException();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
      return this.embedded.equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
      return this.embedded.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.embedded.toString();
    }
  }

  private final ServletResponse embedded;
  private MyStream stream;
  private MyPrintWriter writer;
  private String contentType;
  private int contentLength;

  public ServletResponseWrapper(ServletResponse embedded) throws IOException
  {
    this.embedded = embedded;
  }

  public byte[] getBytesRead()
  {
    if (this.stream != null)
      return this.stream.os.toByteArray();

    if (this.writer != null)
    {
      this.writer.pw.flush();
      return this.writer.os.toByteArray();
    }

    return new byte[0];
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#flushBuffer()
   */
  public void flushBuffer() throws IOException
  {
    this.embedded.flushBuffer();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#getBufferSize()
   */
  public int getBufferSize()
  {
    return this.embedded.getBufferSize();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#getCharacterEncoding()
   */
  public String getCharacterEncoding()
  {
    return this.embedded.getCharacterEncoding();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#getLocale()
   */
  public Locale getLocale()
  {
    return this.embedded.getLocale();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#getOutputStream()
   */
  public ServletOutputStream getOutputStream() throws IOException
  {
    if (this.writer != null)
      throw new RuntimeException("A writer already exists");

    if (this.stream == null)
      this.stream = new MyStream(embedded.getOutputStream());

    return this.stream;
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#getWriter()
   */
  public PrintWriter getWriter() throws IOException
  {
    if (this.stream != null)
      throw new RuntimeException("A stream already exists");
    if (this.writer == null)
      this.writer = new MyPrintWriter(embedded.getWriter());

    return this.writer;
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#isCommitted()
   */
  public boolean isCommitted()
  {
    return this.embedded.isCommitted();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#reset()
   */
  public void reset()
  {
    this.embedded.reset();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#resetBuffer()
   */
  public void resetBuffer()
  {
    this.embedded.resetBuffer();
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#setBufferSize(int)
   */
  public void setBufferSize(int arg0)
  {
    this.embedded.setBufferSize(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#setContentLength(int)
   */
  public void setContentLength(int arg0)
  {
    this.contentLength = arg0;
    this.embedded.setContentLength(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
   */
  public void setContentType(String arg0)
  {
    this.contentType = arg0;
    this.embedded.setContentType(arg0);
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
   */
  public void setLocale(Locale arg0)
  {
    this.embedded.setLocale(arg0);
  }

  public void setCharacterEncoding(String arg0)
  {
    this.embedded.setCharacterEncoding(arg0);
  }

  /**
   * @return Returns the contentLength.
   */
  public final int getContentLength()
  {
    return contentLength;
  }

  /**
   * @return Returns the contentType.
   */
  public final String getContentType()
  {
    return contentType;
  }

}
