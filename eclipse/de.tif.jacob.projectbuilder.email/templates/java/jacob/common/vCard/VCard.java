package jacob.common.vCard;

import jacob.model.Contact;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.util.Base64;

public class VCard
{
   private static class Contactinfo
  {
    String firstname;
    String lastname;
    DataDocumentValue image;
    String work_email;
    String work_phone;
    String work_fax;
    String private_email;
    String private_phone;
    String private_fax;
    
    
  }
  private static String getImageType(String name)
  {
    String[] dummy = name.split("\\.");
    String type = "unknown";
    if (dummy.length > 1)
      type = dummy[dummy.length - 1].toUpperCase();

    if (type.equals("JPG"))
      type = "JPEG";
    if (type.equals("TIF"))
      type = "TIFF";
    return type;
  }

  private static StringBuffer getPicture(IDataTableRecord rec) throws Exception
  {
    StringBuffer value = new StringBuffer();

    DataDocumentValue image = rec.getDocumentValue(Contact.image);
    if (image == null)
      return value;

    String name = image.getName();

    value.append("PHOTO;ENCODING=BASE64;TYPE=");
    value.append(getImageType(name)).append(":");
    value.append(Base64.encode(image.getContent())).append("\n");

    return value;
  }

  public static void exportVCard(IClientContext context) throws Exception
  {
    IDataTableRecord rec = context.getSelectedRecord();

    StringBuffer vCard = new StringBuffer();
    vCard.append("BEGIN:VCARD\n");
    vCard.append("VERSION:2.1\n");

    String value = null;
    value = rec.getStringValue(Contact.lastname);
    if (value == null)
      value = "-";
    // Contact Lastname
    vCard.append("N:").append(value).append(";");
    // Contact Firstname
    value = rec.getStringValue(Contact.firstname);
    if (value == null)
      value = "-";
    vCard.append(value).append("\n");

    // Contact Function
    vCard.append("FN:").append(rec.getSaveStringValue(Contact.fullname)).append("\n");

    // Contact Tel Work
    vCard.append("TEL;WORK;VOICE:");
    vCard.append(rec.getSaveStringValue(Contact.work_phone));
    vCard.append("\n");
    // Contact FAX Work
    vCard.append("TEL;WORK;FAX:");
    vCard.append(rec.getSaveStringValue(Contact.work_fax));
    vCard.append("\n");
    // Contact Tel home
    vCard.append("TEL;HOME;VOICE:");
    vCard.append(rec.getSaveStringValue(Contact.private_phone));
    vCard.append("\n");
    // Contact FAX Home
    vCard.append("TEL;HOME;FAX:");
    vCard.append(rec.getSaveStringValue(Contact.private_fax));
    vCard.append("\n");

    // Contact E-Mail
    vCard.append("EMAIL;PREF;INTERNET:");
    vCard.append(rec.getSaveStringValue(Contact.privat_email));
    vCard.append("\n");

    // Contact work E-Mail
    vCard.append("EMAIL;INTERNET:");
    vCard.append(rec.getSaveStringValue(Contact.work_email));
    vCard.append("\n");

    // picture muss noch getestet werden
   // vCard.append(getPicture(rec));

    vCard.append("END:VCARD\n");

    context.createDocumentDialog("text/x-vCard", "contact.vcf", vCard.toString().getBytes("ISO-8859-1")).show();
    // oder besser US-ASCII ??
  }

  /*
  private static void parseVcard(net.sf.vcard4j.java.VCard vcard, Contactinfo data ) throws Exception
  {
    if (vcard.getTypes("N").hasNext())
    {
      Object obj= vcard.getTypes("N").next();
      System.out.println("obj="+obj.getClass());
      N n = (N) obj;
      data.firstname = n.getGiven();
      data.lastname = n.getFamily();
    }
    for (Iterator tels = vcard.getTypes("TEL"); tels.hasNext();)
    {
      TEL tel = (TEL) tels.next();
      TEL.Parameters tel_param =(TEL.Parameters)tel.getParameters();
      if (tel_param.containsTYPE(TEL.Parameters.TYPE_WORK))
      {
        tel_param.removeTYPE(TEL.Parameters.TYPE_WORK);
        if (tel_param.containsTYPE(TEL.Parameters.TYPE_FAX))
        {
          data.work_fax=tel.get();
        }
        else if(tel_param.containsTYPE(TEL.Parameters.TYPE_VOICE)||(!tel_param.iteratorTYPE().hasNext()))
        {
          // if type voice or no more type information use phone
          data.work_phone=tel.get();
        }
      }
      else 
      {  
        if(tel_param.containsTYPE(TEL.Parameters.TYPE_HOME))
        {
          tel_param.removeTYPE(TEL.Parameters.TYPE_HOME);
          if (tel_param.containsTYPE(TEL.Parameters.TYPE_FAX))
          {
            data.private_fax=tel.get();
          }
          else if(tel_param.containsTYPE(TEL.Parameters.TYPE_VOICE)||(!tel_param.iteratorTYPE().hasNext()))
          {
            // if type voice or no more type information use phone
            data.private_phone=tel.get();
          }
        }
        else 
        {
          if(tel_param.containsTYPE(TEL.Parameters.TYPE_FAX))
        
          {
            data.private_fax=tel.get();
          }
          else if(tel_param.containsTYPE(TEL.Parameters.TYPE_VOICE))
            {
              data.private_phone=tel.get();
            }
         }
      }
    }
    for (Iterator emails = vcard.getTypes("EMAIL"); emails.hasNext();)
    {
      EMAIL email = (EMAIL) emails.next();
      EMAIL.Parameters email_param =(EMAIL.Parameters) email.getParameters();
      if(email_param.containsTYPE(EMAIL.Parameters.TYPE_INTERNET))
      {
        if(email_param.containsTYPE(EMAIL.Parameters.TYPE_PREF))
        {
          if(data.work_email==null && data.private_email !=null)
          {
            data.work_email=data.private_email;
          }
          data.private_email=email.get();
        }
        else
        {   
          if(data.private_email==null)
          {
            data.private_email=email.get();
          }
          else
          {
            data.work_email=email.get();
          }
        }
      }
      
    }
    // PHOTO Object ist in der API nicht sauber implementiert
//    if (vcard.getTypes("PHOTO").hasNext())
//    {
//      PHOTO photo = (PHOTO) vcard.getTypes("PHOTO").next();  
//      
//    }
  }
  private static void createContact(IClientContext context,Contactinfo vcarddata) throws Exception
  {
    IDataTable table = context.getDataTable();
    IDataTransaction trans = table.startNewTransaction();
    try
    {
      IDataTableRecord rec = table.newRecord(trans);
      rec.setValue(trans,Contact.mandator_id,context.getUser().getMandatorId());
      rec.setValue(trans,Contact.person_key,context.getUser().getKey());
      if(CommonContact.NAME.equals(table.getName()))
      {
        rec.setValue(trans,Contact.type,Contact.type_ENUM._common);
      }
      else
      {
        rec.setValue(trans,Contact.type,Contact.type_ENUM._privat);
      }
      rec.setValue(trans,Contact.firstname,vcarddata.firstname);
      rec.setValue(trans,Contact.lastname,vcarddata.lastname);
      rec.setValue(trans,Contact.image,vcarddata.image);
      rec.setValue(trans,Contact.privat_email,vcarddata.private_email);
      rec.setValue(trans,Contact.private_fax,vcarddata.private_fax);
      rec.setValue(trans,Contact.private_phone,vcarddata.private_phone);
      rec.setValue(trans,Contact.work_email,vcarddata.work_email);
      rec.setValue(trans,Contact.work_fax,vcarddata.work_fax);
      rec.setValue(trans,Contact.work_phone,vcarddata.work_phone);

      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }
  public static void importVCard(IClientContext context, byte[] fileData) throws Exception
  {

    DomParser parser = new DomParser();
    Document document = new DocumentImpl();
    parser.parse(new ByteArrayInputStream(fileData), document);
    AddressBook addressBook = new AddressBook(document);
    for (Iterator vcards = addressBook.getVCards(); vcards.hasNext();)
    {
      net.sf.vcard4j.java.VCard vcard = (net.sf.vcard4j.java.VCard) vcards.next();
      Contactinfo contact = new Contactinfo();
      parseVcard(vcard,contact);
      createContact(context,contact);
    }
  }

*/
  /**
   * @param args
   */
  
  public static void main(String args[]) {
    try {

  
      
    } catch (Exception e) {
      e.printStackTrace();
    
    }
}

}
