package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class ElementSetSpec {
    public ConstraintElements allExceptCnselem;
    public ArrayList          intersectionList;
    public boolean            isAllExcept;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public ElementSetSpec() {
        intersectionList = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    // toString Method
    public String toString() {
        String   ts = "";
        Iterator e  = intersectionList.iterator();

        if (e != null) {
            while (e.hasNext()) {
                ts += e.next();
                ts += "|";
            }
        }

        if (isAllExcept) {
            ts += "ALL EXCEPT  " + allExceptCnselem;
        }

        return ts;
    }
}
