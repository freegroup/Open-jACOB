package de.shorti.dragdrop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;

public class FREEGlobal
{

    private FREEGlobal()
    {
    }

    public static Color getPrimarySelectionColor()
    {
        return FA;
    }

    public static void setPrimarySelectionColor(Color color)
    {
        FA = color;
    }

    public static Color getSecondarySelectionColor()
    {
        return FB;
    }

    public static void setSecondarySelectionColor(Color color)
    {
        FB = color;
    }

    public static int getPortGravity()
    {
        return _fld0142;
    }

    public static void setPortGravity(int i)
    {
        _fld0142 = i;
    }

    public static void TRACE(String s)
    {
        System.err.println(s);
    }

    public static double getFREEVersion()
    {
        return 3.02D;
    }

    public static boolean isFREEVersion(double d)
    {
        return getFREEVersion() == d;
    }

    public static boolean isAtLeastFREEVersion(double d)
    {
        return getFREEVersion() >= d;
    }

    public static double getJavaVersion()
    {
        if(_fld0141 == -1D)
            try
            {
                _fld0141 = _mth0141(System.getProperty("java.version", "1.0"));
            }
            catch(SecurityException securityexception)
            {
                _fld0141 = 1.0D;
            }
        return _fld0141;
    }

    public static boolean isJavaVersion(double d)
    {
        return getJavaVersion() == d;
    }

    public static boolean isAtLeastJavaVersion(double d)
    {
        return getJavaVersion() >= d;
    }

    private static double _mth0141(String s)
    {
        int i = s.length();
        String s1 = "";
        boolean flag = false;
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            if(Character.isDigit(c) || c == '.' && !flag)
                s1 = s1 + c;
            if(c == '.')
                flag = true;
        }

        Double double1;
        try
        {
            double1 = new Double(s1);
        }
        catch(NumberFormatException numberformatexception)
        {
            double1 = new Double(1.0D);
        }
        return double1.doubleValue();
    }

    public static Component getComponent()
    {
        return component;
    }

    public static void setComponent(Component c)
    {
        component = c;
    }

    public static Toolkit getToolkit()
    {
        if(getComponent() != null)
            return getComponent().getToolkit();
        else
            return Toolkit.getDefaultToolkit();
    }

    private static double _fld0141 = -1D;
    private static Color FA;
    private static Color FB;
    private static int _fld0142 = 100;
    private static Component component = null;

    static
    {
        FA = Color.green;
        FB = Color.cyan;
    }
}
