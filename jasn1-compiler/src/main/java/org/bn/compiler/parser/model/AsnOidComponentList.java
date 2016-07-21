package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

//
//DefinitionofOID_Component_LIST
//
public class AsnOidComponentList {
    public ArrayList       components;
    public AsnDefinedValue defval;
    public boolean         isDefinitive;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnOidComponentList() {
        components = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    // toString Method
    public String toString() {
        String ts = "";

        if (isDefinitive) {
            ts += defval;
        }

        if (components != null) {
            Iterator i = components.iterator();

            while (i.hasNext()) {
                ts += (i.next());
            }
        }

        return ts;
    }
}
