package jacob.common.task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;


public class SendAttachment implements DataSource
{
  private final String mimeType;
  private final String filename;
  private final byte[] content;

  protected SendAttachment(String mimeType, String filename, byte[] content)
  {
    this.mimeType = mimeType;
    this.filename = filename;
    this.content = content;
  }

  /**
   * @return Returns the content.
   */
  public byte[] getContent()
  {
    return content;
  }

  /**
   * @return Returns the filename.
   */
  public String getFilename()
  {
    return filename;
  }

  /**
   * @return Returns the mimeType.
   */
  public String getMimeType()
  {
    return mimeType;
  }

  /* (non-Javadoc)
   * @see javax.activation.DataSource#getContentType()
   */
  public String getContentType()
  {
    return getMimeType();
  }

  /* (non-Javadoc)
   * @see javax.activation.DataSource#getInputStream()
   */
  public InputStream getInputStream() throws IOException
  {
    return new ByteArrayInputStream(this.content);
  }

  /* (non-Javadoc)
   * @see javax.activation.DataSource#getName()
   */
  public String getName()
  {
    return getFilename();
  }

  /* (non-Javadoc)
   * @see javax.activation.DataSource#getOutputStream()
   */
  public OutputStream getOutputStream() throws IOException
  {
    throw new UnsupportedOperationException();
  }

}
