package org.bn.compiler.parser.model;

//
//DefinitionofAnyType
//
public class AsnAny {
    final String   BUILTINTYPE = "ANY";
    public String  definedByType;
    public boolean isDefinedBy;
    public String  name;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnAny() {
        name          = "";
        isDefinedBy   = false;
        definedByType = "";
    }

    //~--- methods ------------------------------------------------------------

    public String toString() {
        return name;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
