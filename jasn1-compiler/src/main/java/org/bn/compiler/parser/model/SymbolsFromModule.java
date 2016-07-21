package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

//
//DefinitionofSymbolsFromModuleList
//
public class SymbolsFromModule {
    public AsnOidComponentList cmplist;
    public AsnDefinedValue     defval;
    public boolean             isDefinedValue;
    public boolean             isOidValue;
    public String              modref;
    public ArrayList           symbolList;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public SymbolsFromModule() {
        symbolList = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    // toString Method
    public String toString() {
        String   ts = "Following SYMBOLS ::\n";
        Iterator s  = symbolList.iterator();

        if (s != null) {
            while (s.hasNext()) {
                ts += s.next() + "\n";
            }
        }

        ts += "ARE IMPORTED FROM \n";
        ts += modref;

        if (isOidValue) {
            ts += cmplist;
        }

        if (isDefinedValue) {
            ts += defval;
        }

        return ts;
    }
}
