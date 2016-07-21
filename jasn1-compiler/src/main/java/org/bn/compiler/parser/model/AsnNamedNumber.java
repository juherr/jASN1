package org.bn.compiler.parser.model;

public class AsnNamedNumber {
    public AsnDefinedValue definedValue;
    public boolean         isSignedNumber;
    public String          name;
    public AsnSignedNumber signedNumber;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnNamedNumber() {
        name = "";
    }

    //~--- methods ------------------------------------------------------------

    // toString() Method Definition
    public String toString() {
        String ts = "";

        ts += (name + "\t(");

        if (isSignedNumber) {
            ts += (signedNumber);
        } else {
            ts += (definedValue);
        }

        ts += (")");

        return ts;
    }
}
