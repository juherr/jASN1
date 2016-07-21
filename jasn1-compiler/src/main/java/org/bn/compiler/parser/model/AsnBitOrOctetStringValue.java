package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class AsnBitOrOctetStringValue {
    public String    bhStr;
    public ArrayList idlist;
    public boolean   isBString;
    public boolean   isHString;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnBitOrOctetStringValue() {
        idlist = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    public String toString() {
        String ts = "";

        if (isHString || isBString) {
            ts += bhStr;
        } else {
            if (idlist != null) {
                Iterator e = idlist.iterator();

                while (e.hasNext()) {
                    ts += e.next();
                }
            }
        }

        return ts;
    }
}

