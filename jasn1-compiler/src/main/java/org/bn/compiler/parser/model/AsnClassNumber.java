package org.bn.compiler.parser.model;

public class AsnClassNumber {
    public String  name;
    public Integer num;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnClassNumber() {}

    //~--- methods ------------------------------------------------------------

    // toString definition
    public String toString() {
        String ts = "";

        if (num != null) {
            ts += (num);
        } else {
            ts += (name);
        }

        return ts;
    }
}
