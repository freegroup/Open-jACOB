package de.shorti.dragdrop;


import java.util.Map;
import java.util.Vector;

public interface FREECopyEnvironment
    extends Map
{

    public abstract void clearDelayeds();

    public abstract boolean isEmptyDelayeds();

    public abstract int sizeDelayeds();

    public abstract boolean isDelayed(Object obj);

    public abstract void delay(Object obj);

    public abstract void removeDelayed(Object obj);

    public abstract Vector getDelayeds();
}
