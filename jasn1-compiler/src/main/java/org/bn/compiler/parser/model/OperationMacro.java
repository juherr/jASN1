package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class OperationMacro {
    public String    argumentName;            // Argument     Type Name
    public Object    argumentType;            // Holds the argument Type
    public String    argumentTypeIdentifier;    // Argument NamedType identifier
    public ArrayList errorList;
    public boolean   isArgumentName;
    public boolean   isErrors;
    public boolean   isLinkedOperation;
    public boolean   isResult;
    public boolean   isResultName;
    public ArrayList linkedOpList;
    public String    name;
    public String    resultName;              // Result Type Name
    public Object    resultType;              // Holds the resultType
    public String    resultTypeIdentifier;    // Result NamedType identifier

    //~--- constructors -------------------------------------------------------

    // Default Constructors
    public OperationMacro() {
        errorList    = new ArrayList();
        linkedOpList = new ArrayList();
    }

    //~--- methods ------------------------------------------------------------

    // Get the first linked operationName
    public String get_firstLinkedOpName() {
        Object obj = linkedOpList.get(0);

        if ((AsnValue.class).isInstance(obj)) {
            return "isValue";
        } else if ((AsnDefinedType.class).isInstance(obj)) {
            return ((AsnDefinedType) obj).typeName;
        } else {
            String nameoftype = null;

            try {
                Field nameField;
                Class c = obj.getClass();

                nameField  = c.getField("name");
                nameoftype = (String) nameField.get(obj);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }

            return nameoftype;
        }
    }

    // toString() definition
    public String toString() {
        String ts = "";

        ts += (name + "\t::=\t OPERATION");

        if (isArgumentName) {
            ts += ("ARGUMENT\t\t" + argumentName);
        }

        if (isResult) {
            ts += ("RESULT\t\t" + resultName);
        }

        if (isLinkedOperation) {
            ts += ("LINKED OPERATION\t");

            Iterator e = linkedOpList.iterator();

            if (e.hasNext()) {
                while (e.hasNext()) {
                    ts += (e.next());
                }
            }

            ts += ("}");
        }

        if (isErrors) {
            ts += ("ERRORS\t{");

            Iterator e = errorList.iterator();

            if (e.hasNext()) {
                while (e.hasNext()) {
                    ts += e.next();
                }
            }

            ts += ("}");
        }

        return ts;
    }
}
