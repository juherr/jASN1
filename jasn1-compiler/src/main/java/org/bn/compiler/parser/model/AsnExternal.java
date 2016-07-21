package org.bn.compiler.parser.model;

//
//DefinitionofEXTERNAL
// 
public class AsnExternal {
    final String  BUILTINTYPE = "EXTERNAL";

    // Default Constructor

    public String name;

    //~--- constructors -------------------------------------------------------

    public AsnExternal() {}

    //~--- methods ------------------------------------------------------------

    public String toString() {
        String ts = "";

        ts += BUILTINTYPE;

        return ts;
    }
}
