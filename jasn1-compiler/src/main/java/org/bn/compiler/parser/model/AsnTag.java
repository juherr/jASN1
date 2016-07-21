package org.bn.compiler.parser.model;

public class AsnTag {
    public AsnClassNumber classNumber;
    public String         clazz;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnTag() {
        clazz = "";
    }

    //~--- methods ------------------------------------------------------------

    // toString() definition
    public String toString() {
        String ts = "";

        ts += ("[");

        if (clazz != "") {
            ts += (clazz);
        }

        if (classNumber != null) {
            ts += (classNumber);
        }

        ts += ("]");

        return ts;
    }
}
