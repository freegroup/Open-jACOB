//
//
//
// Source File Name:   com/nwoods//FREEObjectSimpleCollection

package de.shorti.dragdrop;


// Referenced classes of package de.shorti.dragdrop:
//            FREEListPosition, FREEObject

public interface FREEObjectSimpleCollection
{

    public abstract int getNumObjects();

    public abstract boolean isEmpty();

    public abstract FREEListPosition getFirstObjectPos();

    public abstract FREEListPosition getNextObjectPos(FREEListPosition listposition);

    public abstract FREEListPosition getNextObjectPosAtTop(FREEListPosition listposition);

    public abstract FREEObject getObjectAtPos(FREEListPosition listposition);
}
