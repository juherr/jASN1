package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class CharDef {
    public String          cStr;
    public AsnDefinedValue defval;
    public boolean         isCString;
    boolean                isDefinedValue;
    public boolean         isQuadruple;
    public boolean         isTuple;
    public ArrayList       tupleQuad;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public CharDef() {
        tupleQuad = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    public String toString() {
        String ts = "";

        if (isCString) {
            ts += ("\t" + cStr);
        } else if (isTuple || isQuadruple) {
            Iterator i = tupleQuad.iterator();

            while (i.hasNext()) {
                ts += i.next() + "\n";
            }
        } else if (isDefinedValue) {
            ts += ("\t" + defval);
        }

        return ts;
    }
}
