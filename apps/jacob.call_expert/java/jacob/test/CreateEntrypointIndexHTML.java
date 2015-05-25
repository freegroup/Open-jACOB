package jacob.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringBufferInputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;

import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.file.Directory;

public class CreateEntrypointIndexHTML 
{
	public static void main(String args[]) throws Exception 
	{
		List<File> files = Directory.getAll(new File("./messages"), false);
		StringBuffer html = new StringBuffer();
		html.append("<html><body>");
		for (File file : files) 
		{
			if(!file.getName().endsWith(".msg"))
				continue;
			String baseName = FilenameUtils.getBaseName(file.getName());
			html.append("<a href=\"");
			html.append("http://localhost:8080/jacob/enter?entry=CTI_email&app=CallExpert&user=admin&pwd=&messageid="+baseName+"&type=email&eduid=schnuller");
			html.append("\" target=\"_blank\">"+baseName+"</a><br>");
		}
		html.append("</body></html>");
		FileUtils.writeStringToFile(new File("./messages/index.html"),html.toString());
	}
}
