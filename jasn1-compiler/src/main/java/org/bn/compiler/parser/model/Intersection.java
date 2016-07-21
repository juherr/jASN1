package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class Intersection {
    public ArrayList cnsElemList;
    public ArrayList exceptCnsElem;
    public boolean   isExcept;
    public boolean   isInterSection;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public Intersection() {
        cnsElemList   = new ArrayList();
        exceptCnsElem = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    // toString Method
    public String toString() {
        String   ts = "";
        Iterator e  = cnsElemList.iterator();
        Iterator i  = exceptCnsElem.iterator();

        while (e.hasNext()) {
            ts += "\t" + e.next();
        }

        if (isExcept) {
            ts += "EXCEPT";

            while (i.hasNext()) {
                ts += "\t" + i.next();
            }
        }

        return ts;
    }
}
