package org.bn.compiler.parser.model;

public class AsnValue {
    String                          bStr;
    public AsnBitOrOctetStringValue bStrValue;
    public String                   cStr;
    public AsnCharacterStringValue  cStrValue;
    public AsnChoiceValue           chval;
    public AsnDefinedValue          definedValue;
    String                          hStr, enumIntVal;
    public boolean                  isAsnOIDValue;
    boolean                         isBStrOrOstrValue;
    public boolean                  isCStrValue;
    public boolean                  isCString;
    public boolean                  isChoiceValue;
    public boolean                  isDefinedValue;
    boolean                         isEnumIntValue;
    public boolean                  isFalseKW;
    public boolean                  isMinusInfinity;
    public boolean                  isNullKW;
    public boolean                  isPlusInfinity;
    public boolean                  isSequenceOfValue;
    public boolean                  isSequenceValue;
    public boolean                  isSignedNumber;
    public boolean                  isTrueKW;
    public String                   name;
    public AsnOidComponentList      oidval;
    public AsnSequenceOfValue       seqOfVal;
    public AsnSequenceValue         seqval;
    public AsnSignedNumber          signedNumber;
    public String                   typeName;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnValue() {}

    //~--- methods ------------------------------------------------------------

    // toString() Method Definition
    public String toString() {
        String ts = "";

        if ((name != null) && (typeName != null)) {
            ts += (name + "\t" + typeName);
        }

        if (isTrueKW) {
            ts += ("\tTRUE");
        } else if (isFalseKW) {
            ts += ("\tFALSE");
        } else if (isNullKW) {
            ts += ("\tNULL");
        } else if (isPlusInfinity) {
            ts += ("\tplusInfinity");
        } else if (isMinusInfinity) {
            ts += ("\tminusInfinity");
        } else if (isCString) {
            ts += ("\t" + cStr);
        } else if (isBStrOrOstrValue) {
            ts += ("\t" + bStrValue);
        } else if (isCStrValue) {
            ts += ("\t" + cStrValue);
        } else if (isSequenceValue) {
            ts += ("\t" + seqval);
        } else if (isChoiceValue) {
            ts += ("\t" + chval);
        } else if (isEnumIntValue) {
            ts += ("\t" + enumIntVal);
        } else if (isSignedNumber) {
            ts += signedNumber;
        } else if (isAsnOIDValue) {
            ts += oidval;
        } else if (isSequenceOfValue) {
            ts += seqOfVal;
        } else if (isDefinedValue) {
            ts += (definedValue);
        } else {
            ts += ("Unknown     Value");
        }

        return ts;
    }
}
