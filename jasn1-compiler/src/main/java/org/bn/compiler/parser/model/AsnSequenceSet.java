package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class AsnSequenceSet {
    final String              BUILTINTYPE  = "SEQUENCE";
    final String              BUILTINTYPE1 = "SET";
    public AsnElementTypeList elementTypeList;
    public boolean            isSequence;
    public String             name;    // Name of Sequence

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnSequenceSet() {
        name       = "";
        isSequence = false;
    }

    //~--- methods ------------------------------------------------------------

    // toString definition
    public String toString() {
        String ts = "";

        ts += (name);

        if (isSequence) {
            ts += "\t::=" + BUILTINTYPE + "\t";
        } else {
            ts += "\t::=" + BUILTINTYPE1 + "\t";
        }

        ts += "{";

        if (elementTypeList != null) {
            Iterator e = elementTypeList.elements.iterator();

            while (e.hasNext()) {
                ts += e.next();
            }
        }

        ts += "}";

        return ts;
    }
}
