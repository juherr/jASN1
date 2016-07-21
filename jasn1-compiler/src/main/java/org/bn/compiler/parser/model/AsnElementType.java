package org.bn.compiler.parser.model;

public class AsnElementType {
    public boolean  isComponentsOf;
    public boolean  isDefault;
    public boolean  isDefinedType;    // Element type       is defined Type
    public boolean  isOptional;
    public boolean  isTag;
    public boolean  isTagDefault;
    public String   name;             // type
    public AsnTag   tag;
    public String   typeName;         // If defined type then     typeName
    public Object   typeReference;    // type        Reference
    public String   typeTagDefault;
    public AsnValue value;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnElementType() {
        isOptional = false;
    }

    //~--- methods ------------------------------------------------------------

    // toString() Method Definition
    public String toString() {
        String ts = "";

        if (isComponentsOf) {
            ts += ("\tCOMPONENTS        OF\t");
        } else {
            ts += (name);

            if (isTag) {
                ts += ("\t");
                ts += (tag);
            }

            if (isTagDefault) {
                ts += ("\t");
                ts += (typeTagDefault);
            }
        }

        if (isDefinedType) {
            ts += ("\t");
            ts += (typeName);
        } else {
            ts += (typeReference.getClass().getName());
        }

        if (isOptional) {
            ts += ("\tOPTIONAL");
        }

        if (isDefault) {
            ts += ("\tDEFAULT\t");
            ts += (value);
        }

        return ts;
    }
}
