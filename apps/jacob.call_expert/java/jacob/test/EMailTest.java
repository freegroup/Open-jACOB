package jacob.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.io.EOLConvertingInputStream;

import sun.misc.BASE64Decoder;

import com.sun.mail.util.BASE64DecoderStream;

import de.tif.jacob.util.Base64;
import de.tif.jacob.util.StringUtil;

public class EMailTest 
{
	public static void main(String args[]) throws Exception 
	{
		System.out.println("Start");
	    // Get a Properties object
	    Properties props = System.getProperties();
		Session session = Session.getInstance(props, null);

		File messageFile = new File("./messages/148923.msg");
String m = IOUtils.toString(new FileInputStream(messageFile));
m = StringUtil.replace(m, IOUtils.LINE_SEPARATOR_WINDOWS,IOUtils.LINE_SEPARATOR);
		MimeMessage msg = new MimeMessage(session,new StringBufferInputStream(m) );
System.out.println("..done!");
		Object content = msg.getContent();
		if (content instanceof Multipart) 
			handleMultipart((Multipart) content);
		else 
			handlePart(msg);
	}

	public static void handleMultipart(Multipart multipart)	throws MessagingException, IOException 
	{
		for (int i = 0, n = multipart.getCount(); i < n; i++) 
		{
			handlePart(multipart.getBodyPart(i));
		}
	}

	public static void handlePart(Part part) throws MessagingException,	IOException 
	{
		System.out.println("===========================================================================");
		String disposition = part.getDisposition();
		String contentType = part.getContentType();
		if (disposition == null) 
		{ 
			System.out.println("Null: " + contentType);
			System.out.println("_______________________________________________________________________");
			// Check if plain
			if(contentType.toLowerCase().startsWith("text/plain"))
			{
				String body = (String)part.getContent();
				System.out.println(body.substring(0,100));
			} 
			else 
			{ 
				System.out.println("Other body: " + contentType);
				saveFile("content.html", part.getInputStream());
			}
		} 
		else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) 
		{
			System.out.println("Attachment: " + part.getFileName() + " : "+ contentType);
			saveFile(part.getFileName(), part.getInputStream());
		} 
		else if (disposition.equalsIgnoreCase(Part.INLINE)) 
		{
			System.out.println("Inline: " + part.getFileName() + " : "+ contentType);
			saveFile(part.getFileName(), part.getInputStream());
		} 
		else 
		{ 
			System.out.println("Other: " + disposition);
		}
	}

	public static void saveFile(String filename, InputStream input)	throws IOException 
	{
		filename = MimeUtility.decodeText(filename);
		System.out.println(filename);
		if (filename == null) 
		{
			filename = "juhu.tiff";
		}
		// Do no overwrite existing file
		File file = new File(filename);
		for (int i = 0; file.exists(); i++) {
			file = new File(filename + i);
		}
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		BufferedInputStream bis = new BufferedInputStream(input);
		int aByte;
		while ((aByte = bis.read()) != -1) {
			bos.write(aByte);
		}
		bos.flush();
		bos.close();
		bis.close();
	}

}
