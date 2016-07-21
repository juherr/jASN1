package org.bn.compiler.parser.model;

public class AsnDefinedType {
    public AsnConstraint constraint;
    public boolean       isModuleReference;
    public String        moduleReference;
    public String        name;
    public String        typeName;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnDefinedType() {
        name              = "";
        moduleReference   = "";
        typeName     = "";
        isModuleReference = false;
    }

    // toString() Definition

    //~--- methods ------------------------------------------------------------

    public String toString() {
        String ts = "";

        if (isModuleReference) {
            ts += (moduleReference + "." + typeName);
        } else {
            ts += (typeName);
        }

        if (constraint != null) {
            ts += constraint;
        }

        return ts;
    }
}
