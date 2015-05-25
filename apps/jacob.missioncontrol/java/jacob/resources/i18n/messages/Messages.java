/*
 * Created on 21.02.2005 by mike
 * 
 *
 */
package jacob.resources.i18n.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.tif.jacob.core.Context;

/**
 * @author mike
 *
 */
public class Messages
{
    private static final String BUNDLE_NAME = "jacob.resources.i18n.messages";//$NON-NLS-1$
    private static final String LABEL_NAME = "jacob.resources.i18n.applicationLabel";//$NON-NLS-1$



    private Messages()
    {
    }

    public static String getString(String key, Context context)
    {
        try
        {
            ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,context.getLocale());
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
    public static String getLabel(String key, Context context)
    {
        try
        {
            ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(LABEL_NAME,context.getLocale());
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
    public static String getString(String key, Context context,String[] arguments)
    {
        if (arguments == null)
        {
            return getString(key,context);
        }
        return MessageFormat.format(getString(key,context),arguments);
        
    }
}
