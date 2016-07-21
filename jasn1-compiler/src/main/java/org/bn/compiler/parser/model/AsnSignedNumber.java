package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.math.BigInteger;

//~--- classes ----------------------------------------------------------------

public class AsnSignedNumber {
    public BigInteger num;
    public boolean    positive;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnSignedNumber() {
        positive = true;
    }

    //~--- methods ------------------------------------------------------------

    // toString() Method Definition
    public String toString() {
        String ts = "";

        if (num != null) {
            ts += (num);
        } else {
            ts += ("Signed Number is Null");
        }

        return ts;
    }
}
