/*
 * Created on 02.09.2005 by mike
 * 
 *
 */
package jacob.common;

import java.util.HashSet;
import java.util.Set;


public class ENUM
{
    
    // die unterschiedlichen Meldungsstatus
    public static final String CALLSTATUS_OPEN = "Offen";

    public static final String CALLSTATUS_ASSIGNED = "Zugewiesen";

    public static final String CALLSTATUS_OWNED = "Angenommen";

    public static final String CALLSTATUS_SCHEDULED = "Terminiert";

    public static final String CALLSTATUS_DONE = "Fertiggemeldet";

    public static final String CALLSTATUS_CLOSED = "Abgeschlossen";
    
    // die unterschiedlichen Meldungsprioritäten
    public static final String CALLPRIORITY_P1="P1";
    
    public static final String CALLPRIORITY_P2="P2";
    
    public static final String CALLPRIORITY_P3="P3";
    
    public static final String CALLPRIORITY_P4="P4";
    
    public static final String CALLPRIORITY_P5="P5";
    
    // die unterschiedlichen Auftraggsstatus
    public static final String TASKSTATUS_OPEN = "Offen";

    public static final String TASKSTATUS_ASSIGNED = "Zugewiesen";

    public static final String TASKSTATUS_OWNED = "Angenommen";

    public static final String TASKSTATUS_SCHEDULED = "Terminiert";

    public static final String TASKSTATUS_DONE = "Fertiggemeldet";

    public static final String TASKSTATUS_CLOSED = "Abgeschlossen";
    
    public static final Set allCallStatusBeforeDone= new HashSet();
    public static final Set allCallStatusAllowTask= new HashSet();
    public static final Set allTaskStatusBeforeDone= new HashSet();
    static
    {
        allCallStatusAllowTask.add(CALLSTATUS_DONE);
        allCallStatusAllowTask.add(CALLSTATUS_OWNED);
        allCallStatusAllowTask.add(CALLSTATUS_SCHEDULED);

        allCallStatusBeforeDone.add(CALLSTATUS_OPEN);
        allCallStatusBeforeDone.add(CALLSTATUS_ASSIGNED);
        allCallStatusBeforeDone.add(CALLSTATUS_OWNED);
        allCallStatusBeforeDone.add(CALLSTATUS_SCHEDULED);
        
        allTaskStatusBeforeDone.add(TASKSTATUS_OPEN);
        allTaskStatusBeforeDone.add(TASKSTATUS_ASSIGNED);
        allTaskStatusBeforeDone.add(TASKSTATUS_OWNED);
        allTaskStatusBeforeDone.add(TASKSTATUS_SCHEDULED);
        
    }

}
