package de.shorti.dragdrop;



import java.util.EventListener;

// Referenced classes of package de.shorti.dragdrop:
//            FREEDocumentEvent

public interface FREEDocumentListener
    extends EventListener
{

    public abstract void documentChanged(FREEDocumentEvent documentevent);
}
