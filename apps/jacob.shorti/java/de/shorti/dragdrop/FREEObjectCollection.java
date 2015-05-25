//
//
//
// Source File Name:   com/nwoods//FREEObjectCollection

package de.shorti.dragdrop;


import java.awt.Point;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObjectSimpleCollection, FREEObject, FREEListPosition

public interface FREEObjectCollection
    extends FREEObjectSimpleCollection
{

    public abstract FREEListPosition addObjectAtHead(FREEObject object);

    public abstract FREEListPosition addObjectAtTail(FREEObject object);

    public abstract FREEListPosition insertObjectBefore(FREEListPosition listposition, FREEObject object);

    public abstract FREEListPosition insertObjectAfter(FREEListPosition listposition, FREEObject object);

    public abstract void bringObjectToFront(FREEObject object);

    public abstract void sendObjectToBack(FREEObject object);

    public abstract void removeObject(FREEObject object);

    public abstract FREEObject removeObjectAtPos(FREEListPosition listposition);

    public abstract FREEObject pickObject(Point point, boolean flag);

    public abstract FREEListPosition getLastObjectPos();

    public abstract FREEListPosition getPrevObjectPos(FREEListPosition listposition);

    public abstract FREEListPosition findObject(FREEObject object);
}
