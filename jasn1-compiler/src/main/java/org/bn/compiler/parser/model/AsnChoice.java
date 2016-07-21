package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

//
//DefinitionofChoice
// 
public class AsnChoice {
    final String              BUILTINTYPE = "CHOICE";
    public AsnElementTypeList elementTypeList;
    public String             name;
    public final boolean            isChoice = true;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnChoice() {
        name = "";
    }

    //~--- methods ------------------------------------------------------------

    public String toString() {
        String ts = "";

        ts += name + "\t::=\t" + BUILTINTYPE + "\t {";

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
