package org.bn.compiler.parser.model;

public class AsnChoiceValue {
    public String   name;
    public AsnValue value;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnChoiceValue() {}

    //~--- methods ------------------------------------------------------------

    // toString Method
    public String toString() {
        String ts = "";

        ts += name;
        ts += "\t" + ":" + value;

        return ts;
    }
}

