
/**     This class defines the class holding for ASN.1 modules and basic Types          */
package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.util.*;

import javax.xml.bind.*;

//~--- classes ----------------------------------------------------------------

/**     This class defines the class holding for ASN.1 modules and basic Types          */
public class ASNModule {
    public AsnTypes            asnTypes;
    public ArrayList           asnValues;
    public ArrayList           exportSymbolList;
    public boolean             exported;
    public boolean             extensible;
    public ArrayList           importSymbolFromModuleList;
    ArrayList                  importSymbolList;
    public boolean             imported;
    public AsnModuleIdentifier moduleIdentifier;    // Name of Module
    public boolean             tag;
    public String              tagDefault;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public ASNModule() {
        exportSymbolList           = new ArrayList();
        importSymbolList           = new ArrayList();
        importSymbolFromModuleList = new ArrayList();
        asnTypes                   = new AsnTypes();
        asnValues                  = new ArrayList();
        tagDefault                 = "";
    }

    //~--- methods ------------------------------------------------------------

    // To String Method
    public String toString() {
        String   ts = "";
        Iterator ii;

        ts += "MODULE NAME ::= \n";
        ts += moduleIdentifier + "\n";
        ts += "IMPORT SYMBOL LIST" + "\n";
        ii = importSymbolList.iterator();

        while (ii.hasNext()) {
            ts += ii.next() + "\n";
        }

        ts += "IMPORT SYMBOLS FROM MODULE \n";
        ii = importSymbolFromModuleList.iterator();

        while (ii.hasNext()) {
            ts += ii.next() + "\n";
        }

        ts += "EXPORT SYMBOL LIST \n";
        ii = exportSymbolList.iterator();

        while (ii.hasNext()) {
            ts += ii.next() + "\n";
        }

        ts += "ASN TYPES LIST \n";
        ts += asnTypes + "\n";
        ts += "ASN VALUES LIST \n";
        ii = asnValues.iterator();

        while (ii.hasNext()) {
            ts += ii.next() + "\n";
        }

        return ts;
    }
}



/* Causing infinite recursion
//*********************************************
// Definition of TypeAndConstraint
//*********************************************
class AsnTypeAndConstraint {
        Object type;
        AsnConstraint constraint;

        AsnTypeAndConstraint(){
        }
        //Define To String Method
}
*/
