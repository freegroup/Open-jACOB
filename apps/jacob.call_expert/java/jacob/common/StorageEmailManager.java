package jacob.common;

import jacob.model.Qem_message;
import jacob.model.Storage_email;
import jacob.model.Storage_email_attachment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.util.List;
import javax.mail.internet.MimeUtility;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.field.address.AddressList;
import org.apache.james.mime4j.field.address.MailboxList;
import org.apache.james.mime4j.message.BinaryBody;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;
import com.hecc.jacob.properties.Property;
import com.hecc.jacob.properties.PropertyAccessor;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.util.StringUtil;

public class StorageEmailManager
{
    private static String PROPERTY_EMAIL_PATH = "email.store.dir";

    public static IDataTableRecord findByExternalId(IClientContext context, String id)	throws Exception
    {
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataTable typeTable = acc.getTable(Storage_email.NAME);
        typeTable.qbeSetKeyValue(Storage_email.external_id, id);
        typeTable.search(IRelationSet.LOCAL_NAME);
        switch (typeTable.recordCount())
        {
            case 0:
                // try to retrieve the message from the avaya table and copy them into the internal
                // storage
                return copyFromQemToCallexpert(context, id);
            case 1:
                return typeTable.getSelectedRecord();
            default:
                throw new UserException("Unable to find unique Record for media type with  external_id=" + id);
        }
    }

    private static IDataTableRecord copyFromQemToCallexpert(IClientContext context, String pkey) throws Exception
    {
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataTable qemMessageTable = acc.getTable(Qem_message.NAME);
        qemMessageTable.qbeSetKeyValue(Qem_message.pkey, pkey);
        qemMessageTable.search(IRelationSet.LOCAL_NAME);
        switch (qemMessageTable.recordCount())
        {
            case 0:
                throw new UserException("Message with the pkey="+pkey+" not found in the qem_message table...call Rob :-)");
            case 1:
                IDataTransaction trans = context.getDataAccessor().newTransaction();
                try
                {
                    IDataTableRecord qemMessage = qemMessageTable.getSelectedRecord();
                    IDataTableRecord internalMessage = Storage_email.newRecord(acc,trans);

                    internalMessage.setValue(trans, Storage_email.external_id, pkey);
                    internalMessage.setValue(trans, Storage_email.direction, Storage_email.direction_ENUM._in);
                    internalMessage.setValue(trans, Storage_email.original_mime_message, qemMessage.getValue(Qem_message.messagebody));

                    // Get a Properties object
                    String messagebody = qemMessage.getStringValue(Qem_message.messagebody);
                    messagebody = StringUtil.replace(messagebody, new String(new byte[]{13}),"");
                    messagebody = StringUtil.replace(messagebody, new String(new byte[]{10}), new String(new byte[]{13,10}));

                    Message msg = null;

                    if("<FILE>".equals(messagebody))
                    {
                        IDataAccessor accessor = context.getDataAccessor().newAccessor();
                        Property property = PropertyAccessor.getProperty(context, PROPERTY_EMAIL_PATH, accessor);
                        if (property != null && property.isSet())
                        {
                            String path = property.asString();
                            if (!path.endsWith(File.separator))
                            {
                                path = path+File.separator;
                            }
                            File file = new File(path+pkey+".msg");
                            FileInputStream in = new FileInputStream(file);
                            try
                            {
                                msg =new Message(in);
                            }
                            finally
                            {
                                in.close();
                            }
                        }
                        else
                        {
                            // entire property does not exist, or does not have a value
                            throw new UserException("");
                        }
                    }
                    else
                    {
                        msg =new Message(new StringBufferInputStream(messagebody));
                    }

                    internalMessage.setValue(trans, Storage_email.subject, MimeUtility.decodeText(msg.getSubject()));
                    internalMessage.setValue(trans, Storage_email.date, msg.getDate());

                    MailboxList froms = msg.getFrom();
                    if (froms==null||froms.size()==0)
                    {
                        internalMessage.setValue(trans, Storage_email.from, "--unknown--");
                        internalMessage.setValue(trans, Storage_email.reply_to, "--unknown--");
                    }
                    else
                    {
                        internalMessage.setValue(trans, Storage_email.from, froms.get(0).getAddress());
                        internalMessage.setValue(trans, Storage_email.reply_to, froms.get(0).getAddress());
                    }

                    AddressList recipients = msg.getTo();
                    if(recipients == null || recipients.size() == 0)
                    {
                        internalMessage.setValue(trans, Storage_email.to, "--unknown--");
                    }
                    else
                    {
                        internalMessage.setValue(trans, Storage_email.to, recipients.get(0).getDisplayString());
                    }

                    AddressList replyTo = msg.getReplyTo();
                    if (replyTo != null && replyTo.size() > 0)
                    {
                        internalMessage.setValue(trans, Storage_email.reply_to, replyTo.get(0).getDisplayString());
                    }

                    if (msg.isMultipart())
                    {
                        Multipart part = (Multipart)msg.getBody();
                        List<BodyPart> parts = part.getBodyParts();
                        for (BodyPart bodyPart: parts)
                        {
                            handleMultipart(context, internalMessage, trans, bodyPart);
                        }
                    }
                    else
                    {
                        handleBody(context, internalMessage, trans, msg);
                    }
                    trans.commit();
                    return internalMessage;
                }
                finally
                {
                    trans.close();
                }
            default:
                throw new UserException("Unable to find unique record  in qem_message table with pkey=" + pkey);
        }
    }

    private static void handleMultipart(Context context, IDataTableRecord messageRecord,IDataTransaction trans, BodyPart part) throws Exception
    {
        if (part.isMultipart())
        {
            Multipart innerPart = (Multipart)part.getBody();

            List<BodyPart> parts = innerPart.getBodyParts();
            for (BodyPart bodyPart: parts)
            {
                handleMultipart(context, messageRecord, trans, bodyPart);
            }
        }
        else
        {
            handleBody(context, messageRecord, trans, part);
        }
    }

    private static void handleBody(Context context, IDataTableRecord messageRecord,IDataTransaction trans, Entity entity) throws Exception
    {
        Body body = entity.getBody();
        if(body instanceof BinaryBody)
        {
            BinaryBody binBody = (BinaryBody)body;
            String name = decode(entity.getFilename());
            if (name == null)
            {
                String tmp = entity.getHeader().getField("Content-Type").getBody();
                int index1=tmp.indexOf(" name=\"");
                int index2=tmp.indexOf(" name=");
                if (index1 != -1)
                {
                    name=decode(tmp.substring(index1+7, tmp.length() - 1));
                }
                else if (index2 != -1)
                {
                    name=decode(tmp.substring(index2 + 6));
                }
            }
            createAttachment(context,messageRecord,trans,name,entity.getMimeType(), binBody.getInputStream());
        }
        else if (body instanceof TextBody)
        {
            TextBody textBody = (TextBody)body;
            Reader reader = textBody.getReader();
            String text = decode(new String(IOUtils.toByteArray(reader)));
            if(messageRecord.hasNullValue(Storage_email.text_body))
            {
                messageRecord.setValue(trans, Storage_email.text_body, text);
                messageRecord.setValue(trans, Storage_email.emailmimetype, entity.getMimeType());
            }
            else
            {
                createAttachment(context,messageRecord,trans,decode( entity.getFilename()),entity.getMimeType(), new StringBufferInputStream(text));
            }
        }
        else
        {
            // unknow body type
            String text = body.toString();
            createAttachment(context,messageRecord,trans,decode( entity.getFilename()),entity.getMimeType(), new StringBufferInputStream(text));
        }
    }

    private static void createAttachment(Context context, IDataTableRecord messageRecord,IDataTransaction trans, String filename,String mimeType, InputStream stream) throws Exception
    {
        filename = decode(filename);
        if (filename == null)
        {
            filename="unknown.txt";
        }
        filename = StringUtil.replace(filename, " ", "_");
        filename = StringUtil.replace(filename, "\\", "_");
        filename = StringUtil.replace(filename, "/", "_");
        filename = StringUtil.replace(filename, "*", "_");

        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataTableRecord attachmentRecord = Storage_email_attachment.newRecord(acc, trans);
        DataDocumentValue doc = DataDocumentValue.create(filename, IOUtils.toByteArray(stream));

        attachmentRecord.setValue(trans, Storage_email_attachment.document, doc);
        attachmentRecord.setValue(trans, Storage_email_attachment.mime_type, mimeType);
        attachmentRecord.setValue(trans, Storage_email_attachment.storage_email_key, messageRecord.getValue(Storage_email.pkey));
    }

    private static String decode(String text) throws Exception
    {
        if (text == null)
        {
            return null;
        }
        return MimeUtility.decodeText(text);
    }
}
