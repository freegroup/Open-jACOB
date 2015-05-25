//
//
//
// Source File Name:   com/nwoods//FREEListPosition

package de.shorti.dragdrop;


import java.io.Serializable;

// Referenced classes of package de.shorti.dragdrop:
//            FREEObject

public class FREEListPosition
    implements Serializable
{

    FREEListPosition(FREEObject object, FREEListPosition listposition, FREEListPosition listposition1)
    {
        obj = object;
        next = listposition1;
        prev = listposition;
    }

    FREEObject obj;
    FREEListPosition next;
    FREEListPosition prev;
}
