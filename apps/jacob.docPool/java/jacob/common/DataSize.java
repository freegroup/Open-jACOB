package jacob.common;

import java.text.DecimalFormat;

/**
 * The class <code>DataSize</code>is a utility class that provides methods to
 * parse and autmatically convert or normalize date or file sizes. This class
 * can be used to parse or specify the size of a data chunk, data file, hard
 * disk space, network bandwidth, etc. The data size is defined by an integer
 * value that usually has an additional unit specifier. Depending on the context
 * the measurement unit <code>1024</code> or <code>1000</code> for a kilo is
 * used. This situation can cause confusion, especially in respect to hard disk
 * size measurements, where both measurements are often used. By default this
 * class uses the "technical" factor <code>1024</code> for a kilo, but may be
 * set manually to <code>1000</code>.
 *
 * <br>
 * Definition: <br>
 * <li>A kilobit (kb) equals 1024 (or 1000) bits
 * <li>A kilobyte (K,KB) equals 1024 (or 1000)bytes
 * <li>A megabyte (M,MB,MEG) equals 1024 (or 1000) KB
 * <li>A gigabyte (G,GB,GIG) equals 1024 (or 1000) MB
 * <li>A terabyte (TB) equals 1024 (or 1000) GB
 */
public class DataSize
{
  /**
   * Traditionally, a kilo byte means 2^10 = 1024 bytes.
   */
  public static final int TECHNICAL = 0;

  /**
   * Informally, a kilo byte means 1000 bytes.
   */
  public static final int INFORMAL = 1;

  /**
   *
   */
  protected static final long[] FACTOR = new long[] { 1024, 1000};

  protected static final int DIM = 5;

  /**
   *
   */
  protected static final String DECIMAL_PATTERN = "0.#";

  protected static final DecimalFormat DECIMAL = new DecimalFormat(DECIMAL_PATTERN);

  /**
   * This array stores the scale factors from 1 up to 1 tera.
   */
  private static final long[][] SCALE = new long[][]
  {
      { 1L,            // 2^0
        1024L,         // 2^10
        1048576L,      // 2^20
        1073741824L,   // 2^30
        1099511627776L // 2^40
      },
      { 1L,            // 10^0
        1000L,         // 10^3
        1000000L,      // 10^6
        1000000000L,   // 10^9
        1000000000000L // 10^12
      }
  };

  /**
   *
   */
  protected static final String[][] UNIT_NAMES = new String[][]
  {
      { "Bytes", "Byte", "B"},
      { "kB", "KILOBYTES", "KILOBYTE", "KILO", "K"},
      { "MB", "MEGABYTES", "MEGABYTE", "MEGA", "MEG", "M"},
      { "GB", "GIGABYTES", "GIGABYTE", "GIGA", "GIG", "G"},
      { "TB", "TERRABYTES", "TERRABYTE", "TERABYTES", "TERRABYTE", "TERRA", "TERA", "T"}
  };

  /**
   * The member <code>size</code> stores the actual file/bandwidth size in
   * bytes/bits.
   */
  private long size = 0;

  /**
   * The member <code>mode</code> defines the kilo factor to be applied,
   * either <code>TECHNICAL</code> (1024) or <code>INFORMAL</code> (1000).
   */
  private int mode = TECHNICAL;

  /**
   *
   * The Constructor <code>DataSize</code> parses the given string for a valid
   * decimal number and optional unit.
   *
   * @param number
   */
  public DataSize(String number)
  {
    setSize(number);
  }

  /**
   *
   * The Constructor <code>DataSize</code> sets the size of the data to the
   * given long value
   *
   * @param size
   */
  public DataSize(long size)
  {
    setSize(size);
  }

  /**
   * The method <code>setSize</code> sets the actual size values for this
   * object.
   *
   * @param size
   *        the size to be set. Negative values are permitted.
   */
  public void setSize(long size)
  {
    this.size = size;
  }

  /**
   * The method <code>setSize</code> parses the passed number string for a
   * valid decimal number and optional unit. The scale strings defined in the
   * member <code>UNIT_NAMES</code> are allowed. Negative values are permitted
   * as well. <br>
   * Examples (mode=TECHNICAL): <br>
   * <li>"1024" is converted to the long integer 1024
   * <li>"10MB" is converted to the long integer 10485760
   * <li>"6.5k" is converted to the long integer 6656
   * <li>"4 Giga" is converted to the long integer 4294967296
   *
   * @param number
   *        the number string to be parsed and coverted to a size value
   */
  public void setSize(String number)
  {
    number = number.trim().toUpperCase();
    for (int d = 0; d < DIM; d++)
    {
      for (int i = 0; i < UNIT_NAMES[d].length; i++)
      {
        if (number.endsWith(UNIT_NAMES[d][i].toUpperCase()))
        {
          final String t = number.substring(0, number.lastIndexOf(UNIT_NAMES[d][i].toUpperCase())).trim();
          try
          {
            double s = 0.0;
            if (t.length() == 0)
              s = 1.0;
            else
              s = Double.valueOf(t).doubleValue();
            size = (long)(s * SCALE[mode][d]);
            return;
          }
          catch (NumberFormatException e)
          {
          }
        }
      }
    }
    size = 0;
  }

  /**
   *
   */
  public void setMode(int mode)
  {
    if (mode == TECHNICAL || mode == INFORMAL)
      this.mode = mode;
  }

  /**
   * The method <code>getSize</code> returns the actual data size in bytes (or
   * bits depending on the context).
   *
   * @return long returns the straight data size (omitting units/factor)
   */
  public long getSize()
  {
    return size;
  }

  /**
   *
   */
  public String toString()
  {
    final double s = Math.abs(size / (double)FACTOR[mode]);
    int d = 0;
    while (d < DIM - 1 && SCALE[mode][d] < s)
      d++;

    return DECIMAL.format(size / (double)SCALE[mode][d]) + " " + UNIT_NAMES[d][0];
  }

  /**
   * Implemented for test purposes only.
   */
  public static void main(String args[])
  {
    long size = Long.valueOf(args[0]).longValue();
    DataSize dataSize = new DataSize(size);
    //    dataSize.setMode(INFORMAL);
//    dataSize.setSize(size);
    System.err.println(dataSize);

    dataSize.setSize(args[1]);
    System.err.println(dataSize.getSize());
  }
}