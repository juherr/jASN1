package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class AsnCharacterStringValue {
    String           cStr;
    public ArrayList charDefsList;
    public boolean   isCharDefList;
    boolean          isQuadruple;
    public boolean   isTuple;
    public ArrayList tupleQuad;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnCharacterStringValue() {
        charDefsList = new ArrayList();
        tupleQuad    = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    public String toString() {
        String ts = "";

        if (isTuple || isQuadruple) {
            Iterator i = tupleQuad.iterator();

            while (i.hasNext()) {
                ts += i.next() + "\n";
            }
        } else if (isCharDefList) {
            Iterator i = charDefsList.iterator();

            while (i.hasNext()) {
                ts += i.next();
            }
        }

        return ts;
    }
}
