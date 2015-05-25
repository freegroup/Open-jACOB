package jacob.common.nntp;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Adds extra dot if dot occurs in message body at beginning of line (according to RFC1939)
 * Compare also org.apache.james.smtpserver.SMTPInputStream
 */
public class ExtraDotOutputStream extends FilterOutputStream 
{
  /**
   * Counter for number of last (0A or 0D).
   */
  protected int countLast0A0D;

    /*
    static public void main(String[] args) throws IOException
    {
        String data = ".This is a test\r\nof the thing.\r\nWe should not have much trouble.\r\n.doubled?\r\nor not?\n.doubled\nor not?\r\n\r\n\n\n\r\r\r\n";

        OutputStream os = new ExtraDotOutputStream(System.out);
        os.write(data.getBytes());
    }
    */


    /**
     * Constructor that wraps an OutputStream.
     *
     * @param out the OutputStream to be wrapped
     */
    public ExtraDotOutputStream(OutputStream out) 
    {
        super(out);
        countLast0A0D = 2; // we already assume a CRLF at beginning (otherwise TOP would not work correctly !)
    }

    /**
     * Writes a byte to the stream, adding dots where appropriate.
     * Also fixes any naked CR or LF to the RFC 2821 mandated CFLF
     * pairing.
     *
     * @param b the byte to write
     *
     * @throws IOException if an error occurs writing the byte
     */
    public void write(int b) throws IOException 
    {
        switch (b) {
            case '.':
                if (countLast0A0D == 2) {
                    // add extra dot (the first of the pair)
                    out.write('.');
                }
                countLast0A0D = 0;
                break;
            case '\r':
                if (countLast0A0D == 1) out.write('\n'); // two CR in a row, so insert an LF first
                countLast0A0D = 1;
                break;
            case '\n':
                /* RFC 2821 #2.3.7 mandates that line termination is
                 * CRLF, and that CR and LF must not be transmitted
                 * except in that pairing.  If we get a naked LF,
                 * convert to CRLF.
                 */
                if (countLast0A0D != 1) out.write('\r');
                countLast0A0D = 2;
                break;
            default:
                // we're  no longer at the start of a line
                countLast0A0D = 0;
                break;
        }
        out.write(b);
    }
}
