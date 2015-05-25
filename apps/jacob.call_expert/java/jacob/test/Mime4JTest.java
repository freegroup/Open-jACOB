package jacob.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringBufferInputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;

import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.file.Directory;

public class Mime4JTest 
{
	public static void main(String args[]) throws Exception 
	{
	    Properties props = System.getProperties();
		Session session = Session.getInstance(props, null);
		List<File> files = Directory.getAll(new File("./messages"), false);
		int error =0;
		int all =0;
		for (File file : files) 
		{
			if(!file.getName().endsWith(".msg"))
				continue;
			all++;
			try
			{
				System.out.println(file.getName());
				long start = System.currentTimeMillis();
				String fileContent = IOUtils.toString(new FileInputStream(file));
				fileContent = StringUtil.replace(fileContent, IOUtils.LINE_SEPARATOR_WINDOWS,IOUtils.LINE_SEPARATOR);
				System.out.println("duration:"+(System.currentTimeMillis()-start));
				
				Message m = new Message(new StringBufferInputStream(fileContent));
				
				if(m.isMultipart())
				{
					
                   System.out.println(decode(m.getSubject()));
				   Multipart part = (Multipart)m.getBody();
				   List<BodyPart> parts = part.getBodyParts();
				   for (BodyPart bodyPart : parts) 
				   {
					   System.out.println("\t"+ decode(bodyPart.getFilename())+" ("+bodyPart.getClass().getName()+")" );
				   }
				   
				}
				else
				{
					System.out.println(decode(m.getSubject()));
					System.out.println(m.getClass().getName());
				}
				System.out.println("-------------------------------------------------------------");
			}
			catch(Exception exc)
			{
				error++;
				System.out.println(file.getName()+":"+ exc.getMessage());
			}
			
		}
		System.out.println("Message count: "+all);
		System.out.println("Error count: "+error);
	}


	private static String decode(String text) throws Exception
	{
		if(text==null)
			return null;
		return MimeUtility.decodeText(text);
	}
}
