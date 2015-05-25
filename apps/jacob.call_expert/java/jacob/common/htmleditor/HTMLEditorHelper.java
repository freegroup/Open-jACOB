package jacob.common.htmleditor;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.util.StringUtil;

public class HTMLEditorHelper
{
    public static void insertTag(IClientContext context, String beginTag, String endTag)
    {
        IInFormLongText textarea = (IInFormLongText) context.getGroup().findByName("storage_email_outboundText_body");
        String value = textarea.getValue();
        // NOTE: A linebreak is only ONE caret position. You must handle the \r\n
        // issue by your self.
        //
        value = StringUtil.replace(value, "\r\n", "\n");
        int pos = textarea.getCaretPosition()+beginTag.length();
        if (textarea.getSelectionStart() != textarea.getSelectionEnd())
        {
            String start = value.substring(0, textarea.getSelectionStart());
            String center = value.substring(textarea.getSelectionStart(), textarea.getSelectionEnd());
            String end = value.substring(textarea.getSelectionEnd(), value.length());
            textarea.setValue(start + beginTag + center + endTag + end);
        }
        else
        {
            String start = value.substring(0, textarea.getCaretPosition());
            String end = value.substring(textarea.getCaretPosition(), value.length());
            textarea.setValue(start + beginTag + endTag + end);
        }
        textarea.setCaretPosition(pos);
    }

    public static void insertTextBlock(IClientContext context, int caretPosition, int selectionStart, int selectionEnd, String textBlock)
    {
        IInFormLongText textarea = (IInFormLongText) context.getGroup().findByName("storage_email_outboundText_body");
        String value = textarea.getValue();
        // NOTE: A linebreak is only ONE caret position. You must handle the \r\n
        // issue by your self.
        //
        value = StringUtil.replace(value, "\r\n", "\n");
        if (selectionStart != selectionEnd)
        {
            String start = value.substring(0, selectionStart);
            String center = value.substring(selectionStart, selectionEnd);
            String end = value.substring(selectionEnd, value.length());
            //If something is selected we will replace it with the textblock
            center = textBlock;
            textarea.setValue(start + center + end);
        }
        else
        {

            String start = value.substring(0, caretPosition);
            String end = value.substring(caretPosition, value.length());
            textarea.setValue(start + textBlock + end);
        }
        textarea.setCaretPosition(caretPosition);
    }

    public static void insertListTag(IClientContext context, boolean orderedList) throws Exception
    {
        IInFormLongText textarea = (IInFormLongText) context.getGroup().findByName("storage_email_outboundText_body");
        String value = textarea.getValue();
        // NOTE: A linebreak is only ONE caret position. You must handle the \r\n
        // issue by your self.
        //
        value = StringUtil.replace(value, "\r\n", "\n");

        if (textarea.getSelectionStart() != textarea.getSelectionEnd())
        {
            String start = value.substring(0, textarea.getSelectionStart());
            String center = value.substring(textarea.getSelectionStart(), textarea.getSelectionEnd());
            String end = value.substring(textarea.getSelectionEnd(), value.length());
            if (center.contains("\n"))
            {
                center = StringUtil.replace(center,"\n", "</li>\n<li>");

                if (orderedList)
                {
                    center = "<ol><li>" + center +"</li></ol>";
                }
                else
                {
                    center = "<ul><li>" + center +"</li></ul>";
                }
                textarea.setValue(start + center + end);
            }

            else
            {
                start = value.substring(0, textarea.getCaretPosition());
                end = value.substring(textarea.getCaretPosition(), value.length());
                textarea.setValue(start + "<li></li>" + end);
            }
        }
        else
        {
            String start = value.substring(0, textarea.getCaretPosition());
            String end = value.substring(textarea.getCaretPosition(), value.length());
            textarea.setValue(start + "<li></li>" + end);
        }
        previewBody(context);
    }

    public static void previewBody(IClientContext context) throws Exception
    {
        String begin = "<div style=\"width:100%; height:100%; padding:2px; border: thin solid black;  overflow: auto;\">";
        String end = "</div>";
        String center = context.getGroup().getInputFieldValue("storage_email_outboundText_body");
        center = StringUtil.replace(center, "\n", "<br>\n");
        String body =begin+center+end;
        context.getGroup().findByName("storage_email_outboundStyledText_Preview").setLabel(body);
    }

    public static String setScrollBar(String sHtml)
    {
        String begin = "<div style=\"width:100%; height:100%; padding:2px; border: thin solid black;  overflow: auto;\">";
        String end = "</div>";
        String center = sHtml;
        String retVAl =begin+center+end;
        return retVAl;
    }
}
