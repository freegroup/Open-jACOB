package jacob.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.util.List;

import javax.mail.internet.MimeUtility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.message.BinaryBody;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;

import de.tif.jacob.util.StringUtil;

public class ConvertMessage 
{
//	static String number ="112";
	static String number ="307503";
	public static void main(String args[]) throws Exception 
	{
	    // Get a Properties object

		File messageFile = new File("./messages/"+number+".msg");
		String m = IOUtils.toString(new FileInputStream(messageFile));
		m = StringUtil.replace(m, new String(new byte[]{13}),"");
		m = StringUtil.replace(m, new String(new byte[]{10}), new String(new byte[]{13,10}));
		Message msg = new Message(new StringBufferInputStream(m));
		if(msg.isMultipart())
		{
		   Multipart part = (Multipart)msg.getBody();
		   List<BodyPart> parts = part.getBodyParts();
		   for (BodyPart bodyPart : parts) 
		   {
			   handleMultipart( bodyPart);
		   }
		}
		else
		{
			System.out.println( msg.getBody().toString());
		}
		FileUtils.writeStringToFile(new File("./messages/"+number+".msg_correct"), m);
		
	}

	private static void handleMultipart( BodyPart part) throws Exception
	{
		if (part.isMultipart()) 
		{
		   org.apache.james.mime4j.message.Multipart innerPart = (org.apache.james.mime4j.message.Multipart)part.getBody();

		   List<BodyPart> parts = innerPart.getBodyParts();
		   for (BodyPart bodyPart : parts) 
		   {
			   handleMultipart( bodyPart);
		   }
		}
		else
		{
			Body body =part.getBody();
			if(body instanceof BinaryBody)
			{
				BinaryBody binBody = (BinaryBody)body;
				String name = decode(part.getFilename());
				if(name==null)
				{
					String tmp =part.getHeader().getField("Content-Type").getBody();
					int index=tmp.indexOf(" name=");
					if(index!=-1)
					{
						name=decode(tmp.substring(index+7, tmp.length()-1));
					}
				}
				dumpAttachment(name,part.getMimeType(), binBody.getInputStream());
			}
			else if (body instanceof TextBody)
			{
				TextBody textBody = (TextBody)body;
				Reader reader = textBody.getReader();
				String text = decode(new String(IOUtils.toByteArray(reader)));
				dumpAttachment(decode( part.getFilename()),part.getMimeType(), new StringBufferInputStream(text));
			}
			else
			{
				// unknow body type
				String text = body.toString();
				dumpAttachment(decode( part.getFilename()),part.getMimeType(), new StringBufferInputStream(text));
			}
		}
	}
	
	private static void dumpAttachment(String filename,String mimeType, InputStream stream) throws Exception
	{
		if(filename==null)
			filename="unknown.txt";
		System.out.println("\t"+filename+" ("+mimeType+")");
	}
	
	private static String decode(String text) throws Exception
	{
		if(text==null)
			return null;
		return MimeUtility.decodeText(text);
	}	
	
}
