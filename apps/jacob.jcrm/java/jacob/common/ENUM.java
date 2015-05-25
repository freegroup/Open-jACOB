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
    public static final String CALLSTATUS_NEW = "New";

    public static final String CALLSTATUS_ASSIGNED = "Assigned";

    public static final String CALLSTATUS_OWNED = "Owned";

    public static final String CALLSTATUS_QA = "QA";


    public static final String CALLSTATUS_CLOSED = "Closed";
    
 
    
    // die unterschiedlichen Auftraggsstatus
    public static final String TASKSTATUS_NEW = "New";

    public static final String TASKSTATUS_ASSIGNED = "Assigned";

    public static final String TASKSTATUS_OWNED = "Owned";

    public static final String TASKSTATUS_QA = "QA";


    public static final String TASKSTATUS_CLOSED = "Closed";
    
    public static final Set allCallStatusBeforeQA= new HashSet();
    public static final Set allCallStatusAllowTask= new HashSet();
    public static final Set allTaskStatusBeforeQA= new HashSet();
    static
    {

        allCallStatusBeforeQA.add(CALLSTATUS_NEW);
        allCallStatusBeforeQA.add(CALLSTATUS_ASSIGNED);
        allCallStatusBeforeQA.add(CALLSTATUS_OWNED);

        
        allTaskStatusBeforeQA.add(TASKSTATUS_NEW);
        allTaskStatusBeforeQA.add(TASKSTATUS_ASSIGNED);
        allTaskStatusBeforeQA.add(TASKSTATUS_OWNED);
        allTaskStatusBeforeQA.add(TASKSTATUS_QA);        
    }

}
