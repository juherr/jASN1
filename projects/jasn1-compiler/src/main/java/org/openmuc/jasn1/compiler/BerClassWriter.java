/*
 * Copyright 2011-15 Fraunhofer ISE
 *
 * This file is part of jASN1.
 * For more information visit http://www.openmuc.org
 *
 * jASN1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jASN1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jASN1.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jasn1.compiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.compiler.model.AsnAny;
import org.openmuc.jasn1.compiler.model.AsnAnyNoDecode;
import org.openmuc.jasn1.compiler.model.AsnBitString;
import org.openmuc.jasn1.compiler.model.AsnBoolean;
import org.openmuc.jasn1.compiler.model.AsnCharacterString;
import org.openmuc.jasn1.compiler.model.AsnChoice;
import org.openmuc.jasn1.compiler.model.AsnClassNumber;
import org.openmuc.jasn1.compiler.model.AsnComponentType;
import org.openmuc.jasn1.compiler.model.AsnConstructedType;
import org.openmuc.jasn1.compiler.model.AsnDefinedType;
import org.openmuc.jasn1.compiler.model.AsnEnum;
import org.openmuc.jasn1.compiler.model.AsnInteger;
import org.openmuc.jasn1.compiler.model.AsnModule;
import org.openmuc.jasn1.compiler.model.AsnNamedType;
import org.openmuc.jasn1.compiler.model.AsnNull;
import org.openmuc.jasn1.compiler.model.AsnObjectIdentifier;
import org.openmuc.jasn1.compiler.model.AsnOctetString;
import org.openmuc.jasn1.compiler.model.AsnReal;
import org.openmuc.jasn1.compiler.model.AsnSequenceOf;
import org.openmuc.jasn1.compiler.model.AsnSequenceSet;
import org.openmuc.jasn1.compiler.model.AsnTag;
import org.openmuc.jasn1.compiler.model.AsnType;
import org.openmuc.jasn1.compiler.model.AsnUniversalType;
import org.openmuc.jasn1.compiler.model.SymbolsFromModule;

public class BerClassWriter {

    private final String outputBaseDir;
    private final String basePackageName;
    private int indentNum = 0;
    BufferedWriter out;
    private boolean isDefaultTagExplicit = true;
    private boolean automaticTagging = false;
    private final boolean supportIndefiniteLength;
    private final boolean jaxbMode;
    private final HashMap<String, AsnModule> modulesByName;
    private AsnModule module;
    private File outputDirectory;

    BerClassWriter(HashMap<String, AsnModule> modulesByName, String outputBaseDir, String basePackageName,
            boolean jaxbMode, boolean supportIndefiniteLength) throws IOException {
        this.supportIndefiniteLength = supportIndefiniteLength;
        this.jaxbMode = jaxbMode;
        this.outputBaseDir = outputBaseDir;
        if (basePackageName.isEmpty()) {
            this.basePackageName = "";
        }
        else {
            this.basePackageName = basePackageName + ".";
        }
        this.modulesByName = modulesByName;
    }

    public void translate() throws IOException {

        for (AsnModule module : modulesByName.values()) {
            translateModule(module);
        }

    }

    public void translateModule(AsnModule module) throws IOException {

        System.out.println("Generating classes for module: " + module.moduleIdentifier.name);

        outputDirectory = new File(outputBaseDir, module.moduleIdentifier.name.replace('-', '/').toLowerCase());
        outputDirectory.mkdirs();

        this.module = module;

        if (module.tagDefault.equals("IMPLICIT")) {
            isDefaultTagExplicit = false;
        }
        else if (module.tagDefault.equals("AUTOMATIC")) {
            isDefaultTagExplicit = false;
            automaticTagging = true;
        }

        for (AsnType typeDefinition : module.typesByName.values()) {

            String typeName = cleanUpName(typeDefinition.name);

            writeClassHeader(typeName);

            if (typeDefinition instanceof AsnNamedType) {

                AsnNamedType asnTaggedType = (AsnNamedType) typeDefinition;

                String tagClass = getTagClass(asnTaggedType);
                String tagNum = getTagNum(asnTaggedType);
                String assignedTypeName = asnTaggedType.typeName;
                boolean isTagExplicit = hasExplicitTag(asnTaggedType);

                if (!assignedTypeName.isEmpty()) {
                    writeRetaggingTypeClass(tagNum, tagClass, typeName, assignedTypeName, isTagExplicit,
                            typeDefinition);
                }
                else {

                    AsnType assignedAsnType = (AsnType) asnTaggedType.typeReference;

                    if (assignedAsnType instanceof AsnConstructedType) {
                        writeConstructedTypeClass(assignedAsnType, tagNum, tagClass, typeName, false);
                    }
                    else {
                        writeRetaggingTypeClass(tagNum, tagClass, typeName, getBerType(assignedAsnType), isTagExplicit,
                                typeDefinition);
                    }
                }

            }
            else if (typeDefinition instanceof AsnDefinedType) {
                writeRetaggingTypeClass("", "", typeName, ((AsnDefinedType) typeDefinition).typeName, false,
                        typeDefinition);
            }
            else if (typeDefinition instanceof AsnConstructedType) {
                writeConstructedTypeClass(typeDefinition, "", "", typeName, false);
            }
            else {
                writeRetaggingTypeClass("", "", typeName, getBerType(typeDefinition), false, typeDefinition);
            }

            out.close();
        }

    }

    private String cleanUpName(String name) {

        int index = -1;

        while ((index = name.indexOf('-', index + 1)) != -1) {
            if ((index != (name.length() - 1)) && !Character.isUpperCase(name.charAt(index + 1))) {
                StringBuilder nameSb = new StringBuilder(name);
                nameSb.setCharAt(index + 1, Character.toUpperCase(name.charAt(index + 1)));
                name = nameSb.toString();
            }
        }

        index = -1;

        while ((index = name.indexOf('_', index + 1)) != -1) {
            if ((index != (name.length() - 1)) && !Character.isUpperCase(name.charAt(index + 1))) {
                StringBuilder nameSb = new StringBuilder(name);
                nameSb.setCharAt(index + 1, Character.toUpperCase(name.charAt(index + 1)));
                name = nameSb.toString();
            }
        }

        name = name.replace("-", "");
        name = name.replace("_", "");

        if (name.equals("null")) {
            name = name + "_";
        }
        return name;
    }

    private void writeConstructedTypeClass(AsnType asnType, String tagNum, String tagClass, String className,
            boolean asInternalClass) throws IOException {
        if (asnType instanceof AsnSequenceSet) {
            writeSequenceOrSetClass((AsnSequenceSet) asnType, tagNum, tagClass, className, asInternalClass);
        }
        else if (asnType instanceof AsnSequenceOf) {
            writeSequenceOfClass((AsnSequenceOf) asnType, tagNum, tagClass, className, asInternalClass);
        }
        else if (asnType instanceof AsnChoice) {
            writeChoiceClass((AsnChoice) asnType, tagNum, tagClass, className, asInternalClass);
        }
    }

    private void writeChoiceClass(AsnChoice asn1TypeElement, String tagNum, String tagClass, String className,
            boolean asInternalClass) throws IOException {

        if (className.isEmpty()) {
            className = cleanUpName(asn1TypeElement.name);
        }

        String isStaticStr = "";
        if (asInternalClass) {
            isStaticStr = " static";
        }

        write("public" + isStaticStr + " class " + className + " {\n");

        write("public byte[] code = null;");

        List<AsnComponentType> sequenceElements = asn1TypeElement.componentTypes;

        addAutomaticTagsIfNeeded(sequenceElements);

        for (AsnComponentType sequenceElementType : sequenceElements) {

            if (sequenceElementType.typeReference != null
                    && (sequenceElementType.typeReference instanceof AsnConstructedType)) {

                String subClassName = getClassNameOfStructureElement(sequenceElementType);
                writeConstructedTypeClass((AsnType) sequenceElementType.typeReference, "", "", subClassName, true);

            }
        }

        writePublicMembers(sequenceElements);

        writeEmptyConstructor(className, true);

        if (!jaxbMode) {
            writeEncodeConstructor(className, sequenceElements, true);
        }

        if (jaxbMode) {
            writeGetterAndSetter(sequenceElements);
        }

        writeChoiceEncodeFunction(sequenceElements);

        writeChoiceDecodeFunction(sequenceElements);

        writeEncodeAndSaveFunction();

        writeChoiceToStringFunction(sequenceElements);

        write("}\n");

    }

    private void writeChoiceToStringFunction(List<AsnComponentType> sequenceElements) throws IOException {
        write("public String toString() {");

        for (int j = 0; j < sequenceElements.size(); j++) {
            AsnComponentType sequenceElement = sequenceElements.get(j);

            write("if ( " + getSequenceElementName(sequenceElement) + "!= null) {");
            write("return \"CHOICE{" + getSequenceElementName(sequenceElement) + ": \" + "
                    + getSequenceElementName(sequenceElement) + " + \"}\";");
            write("}\n");

        }

        write("return \"unknown\";");

        write("}\n");

    }

    private void writeChoiceDecodeFunction(List<AsnComponentType> sequenceElements) throws IOException {
        write("public int decode(InputStream is, BerIdentifier berIdentifier) throws IOException {");
        write("int codeLength = 0;");
        write("BerIdentifier passedIdentifier = berIdentifier;\n");

        write("if (berIdentifier == null) {");
        write("berIdentifier = new BerIdentifier();");
        write("codeLength += berIdentifier.decode(is);");
        write("}\n");

        write("BerLength length = new BerLength();");

        String initChoiceDecodeLength = "int ";

        for (int j = 0; j < sequenceElements.size(); j++) {
            AsnComponentType sequenceElement = sequenceElements.get(j);

            String explicitEncoding = ", false";
            if (hasTag(sequenceElement)) {

                if (getUniversalType(sequenceElement) instanceof AsnChoice) {

                    write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                            + ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

                    if (hasExplicitTag(sequenceElement)) {
                        write("codeLength += length.decode(is);");
                        explicitEncoding = ", null";
                    }
                    else {
                        explicitEncoding = ", berIdentifier";
                    }
                }
                else {

                    if (hasExplicitTag(sequenceElement) || !isPrimitive(sequenceElement)) {
                        write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                + ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");
                    }
                    else {
                        write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                + ", BerIdentifier.PRIMITIVE" + ", " + getTagNum(sequenceElement) + ")) {");
                    }

                    if (hasExplicitTag(sequenceElement)) {
                        write("codeLength += length.decode(is);");
                        if (isDirectAny(sequenceElement)) {
                            explicitEncoding = ", length.val";
                        }
                        else {
                            explicitEncoding = ", true";
                        }
                    }

                }

                write(getSequenceElementName(sequenceElement) + " = new "
                        + getClassNameOfStructureElement(sequenceElement) + "();");

                write("codeLength += " + getSequenceElementName(sequenceElement) + ".decode(is" + explicitEncoding
                        + ");");

                write("return codeLength;");

                write("}\n");

            }
            else {
                if ((getUniversalType(sequenceElement) instanceof AsnAny)
                        || (getUniversalType(sequenceElement) instanceof AsnAnyNoDecode)) {
                    throw new IOException(
                            "ANY within CHOICE has no tag: " + getClassNameOfStructureElement(sequenceElement));
                }

                if (getUniversalType(sequenceElement) instanceof AsnChoice) {

                    System.out.println("CHOICE without TAG within another CHOICE: "
                            + getClassNameOfStructureElement(sequenceElement)
                            + " You could consider integrating the inner CHOICE in the parent CHOICE in order to reduce the number of Java classes/objects.");

                    explicitEncoding = ", berIdentifier";

                    write(getSequenceElementName(sequenceElement) + " = new "
                            + getClassNameOfStructureElement(sequenceElement) + "();");

                    write(initChoiceDecodeLength + "choiceDecodeLength = " + getSequenceElementName(sequenceElement)
                            + ".decode(is" + explicitEncoding + ");");
                    initChoiceDecodeLength = "";
                    write("if (choiceDecodeLength != 0) {");
                    write("codeLength += choiceDecodeLength;");
                    write("return codeLength;");
                    write("}");
                    write("else {");
                    write(getSequenceElementName(sequenceElement) + " = null;");
                    write("}\n");

                }
                else {

                    write("if (berIdentifier.equals(" + getClassNameOfStructureElement(sequenceElement)
                            + ".identifier)) {");

                    write(getSequenceElementName(sequenceElement) + " = new "
                            + getClassNameOfStructureElement(sequenceElement) + "();");

                    write("codeLength += " + getSequenceElementName(sequenceElement) + ".decode(is" + explicitEncoding
                            + ");");

                    write("return codeLength;");

                    write("}\n");
                }
            }

        }

        write("if (passedIdentifier != null) {");
        write("return 0;");
        write("}");
        write("throw new IOException(\"Error decoding BerChoice: Identifier matched to no item.\");");

        write("}\n");

    }

    private void writeChoiceEncodeFunction(List<AsnComponentType> choiceElements) throws IOException {
        write("public int encode(BerByteArrayOutputStream os, boolean explicit) throws IOException {");

        write("if (code != null) {");

        write("for (int i = code.length - 1; i >= 0; i--) {");
        write("os.write(code[i]);");
        write("}");
        write("return code.length;\n");
        write("}");

        write("int codeLength = 0;");

        for (int j = choiceElements.size() - 1; j >= 0; j--) {
            if (hasExplicitTag(choiceElements.get(j))) {
                write("int sublength;\n");
                break;
            }
        }

        for (int j = choiceElements.size() - 1; j >= 0; j--) {

            AsnComponentType choiceElement = choiceElements.get(j);

            write("if (" + getSequenceElementName(choiceElement) + " != null) {");

            String explicitEncoding = ", true";
            if (hasImplicitTag(choiceElement)) {
                explicitEncoding = ", false";
            }

            if (hasExplicitTag(choiceElement)) {
                if (isDirectAny(choiceElement)) {
                    explicitEncoding = "";
                }
                else {
                    explicitEncoding = ", true";
                }
                write("sublength = " + getSequenceElementName(choiceElement) + ".encode(os" + explicitEncoding + ");");
                write("codeLength += sublength;");
                write("codeLength += BerLength.encodeLength(os, sublength);");
            }
            else {
                write("codeLength += " + getSequenceElementName(choiceElement) + ".encode(os" + explicitEncoding
                        + ");");
            }

            if (hasTag(choiceElement)) {
                if (hasExplicitTag(choiceElement) || !isPrimitive(choiceElement)) {
                    writeEncodeIdentifier(getTagClass(choiceElement), "CONSTRUCTED", getTagNum(choiceElement));
                }
                else {
                    writeEncodeIdentifier(getTagClass(choiceElement), "PRIMITIVE", getTagNum(choiceElement));
                }
            }

            write("return codeLength;\n");

            write("}");

            write("");

        }

        write("throw new IOException(\"Error encoding BerChoice: No item in choice was selected.\");");

        write("}\n");

    }

    private void writeSequenceOfClass(AsnSequenceOf asnSequenceOf, String tagNum, String tagClass, String className,
            boolean asInternalClass) throws IOException {

        if (tagClass.isEmpty()) {
            tagClass = "UNIVERSAL_CLASS";
        }
        if (tagNum.isEmpty()) {
            if (asnSequenceOf.isSequenceOf) {
                tagNum = "16";
            }
            else {
                tagNum = "17";
            }
        }
        if (className.isEmpty()) {
            className = cleanUpName(asnSequenceOf.name);
        }

        String isStaticStr = "";
        if (asInternalClass) {
            isStaticStr = " static";
        }

        write("public" + isStaticStr + " class " + className + " {\n");

        AsnType subElementRef = asnSequenceOf.typeReference;

        String referencedTypeName = getClassNameOfSequenceOfElement(asnSequenceOf);

        if (subElementRef != null) {
            writeConstructedTypeClass(subElementRef, "", "", referencedTypeName, true);
        }

        write("public static final BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
                + ", BerIdentifier.CONSTRUCTED, " + tagNum + ");");
        write("protected BerIdentifier id;\n");

        write("public byte[] code = null;");

        if (jaxbMode) {
            write("private List<" + referencedTypeName + "> seqOf = null;\n");
        }
        else {
            write("public List<" + referencedTypeName + "> seqOf = null;\n");
        }

        write("public " + className + "() {");
        write("id = identifier;");
        write("seqOf = new ArrayList<" + referencedTypeName + ">();");
        write("}\n");

        write("public " + className + "(byte[] code) {");
        write("id = identifier;");
        write("this.code = code;");
        write("}\n");

        if (!jaxbMode) {
            write("public " + className + "(List<" + referencedTypeName + "> seqOf) {");
            write("id = identifier;");
            write("this.seqOf = seqOf;");
            write("}\n");
        }

        if (jaxbMode) {
            writeGetterForSeqOf(referencedTypeName);
        }

        writeSequenceOfEncodeFunction(asnSequenceOf);

        writeSequenceOfDecodeFunction(asnSequenceOf);

        writeEncodeAndSaveFunction();

        writeSequenceOrSetOfToStringFunction(referencedTypeName);

        write("}\n");

    }

    private void writeSequenceOrSetOfToStringFunction(String referencedTypeName) throws IOException {
        write("public String toString() {");

        write("StringBuilder sb = new StringBuilder(\"SEQUENCE OF{\");\n");

        write("if (seqOf == null) {");
        write("sb.append(\"null\");");
        write("}");
        write("else {");
        write("Iterator<" + referencedTypeName + "> it = seqOf.iterator();");
        write("if (it.hasNext()) {");
        write("sb.append(it.next());");
        write("while (it.hasNext()) {");
        write("sb.append(\", \").append(it.next());");
        write("}");
        write("}");
        write("}\n");

        write("sb.append(\"}\");\n");

        write("return sb.toString();");
        write("}\n");

    }

    private void writeSequenceOfDecodeFunction(AsnSequenceOf element) throws IOException {
        write("public int decode(InputStream is, boolean explicit) throws IOException {");
        write("int codeLength = 0;");
        write("int subCodeLength = 0;");
        write("BerIdentifier berIdentifier = new BerIdentifier();");

        write("if (explicit) {");
        write("codeLength += id.decodeAndCheck(is);");
        write("}\n");

        write("BerLength length = new BerLength();");
        write("codeLength += length.decode(is);");
        write("int totalLength = length.val;\n");

        // indefinite length
        if (supportIndefiniteLength == true) {

            write("if (length.val == -1) {");
            write("while (true) {");
            write("subCodeLength += berIdentifier.decode(is);\n");

            write("if (berIdentifier.tagNumber == 0 && berIdentifier.identifierClass == 0 && berIdentifier.primitive == 0) {");
            write("int nextByte = is.read();");
            write("if (nextByte != 0) {");
            write("if (nextByte == -1) {");
            write("throw new EOFException(\"Unexpected end of input stream.\");");
            write("}");
            write("throw new IOException(\"Decoded sequence has wrong end of contents octets\");");
            write("}");
            write("codeLength += subCodeLength + 1;");
            write("return codeLength;");
            write("}\n");

            write(getClassNameOfSequenceOfElement(element) + " element = new "
                    + getClassNameOfSequenceOfElement(element) + "();");

            if (isDirectChoice(element)) {
                write("subCodeLength += element.decode(is, berIdentifier);");
            }
            else if (isDirectAny(element)) {
                write("subCodeLength += length.decode(is);");
                write("subCodeLength += element.decode(is, length.val);");
            }
            else {
                write("subCodeLength += element.decode(is, false);");
            }
            write("seqOf.add(element);");
            write("}");

            write("}");

        }

        // definite length
        write("while (subCodeLength < totalLength) {");
        write(getClassNameOfSequenceOfElement(element) + " element = new " + getClassNameOfSequenceOfElement(element)
                + "();");

        if (element.typeReference != null && element.typeReference instanceof AsnNamedType) {

            String explicitEncoding;

            AsnNamedType sequenceElement = (AsnNamedType) element.typeReference;
            if (hasExplicitTag(sequenceElement)) {
                write("subCodeLength += berIdentifier.decode(is);");
                write("subCodeLength += length.decode(is);");
                explicitEncoding = ", true";
            }
            else {
                write("subCodeLength += berIdentifier.decode(is);");
                if (isDirectChoice(element)) {
                    explicitEncoding = ", berIdentifier";
                }
                else if (isDirectAny(element)) {
                    explicitEncoding = "";
                }
                else {
                    explicitEncoding = ", false";
                }
            }
            write("subCodeLength += element.decode(is" + explicitEncoding + ");");
        }
        else {

            if (isDirectChoice(element)) {
                write("subCodeLength += element.decode(is, null);");
            }
            else if (isDirectAny(element)) {
                write("subCodeLength += new BerIdentifier().decode(is);");
                write("subCodeLength += length.decode(is);");
                write("subCodeLength += element.decode(is, length.val);");
            }
            else {
                write("subCodeLength += element.decode(is, true);");
            }
        }
        write("seqOf.add(element);");
        write("}");
        write("if (subCodeLength != totalLength) {");
        write("throw new IOException(\"Decoded SequenceOf or SetOf has wrong length. Expected \" + totalLength + \" but has \" + subCodeLength);\n");
        write("}");
        write("codeLength += subCodeLength;\n");

        write("return codeLength;");
        write("}\n");

    }

    private void writeSequenceOfEncodeFunction(AsnSequenceOf element) throws IOException {
        write("public int encode(BerByteArrayOutputStream os, boolean explicit) throws IOException {");
        write("int codeLength;\n");

        write("if (code != null) {");
        write("codeLength = code.length;");
        write("for (int i = code.length - 1; i >= 0; i--) {");
        write("os.write(code[i]);");
        write("}");
        write("}");
        write("else {");

        write("codeLength = 0;");

        write("for (int i = (seqOf.size() - 1); i >= 0; i--) {");
        if (element.typeReference != null && element.typeReference instanceof AsnNamedType) {

            AsnNamedType sequenceElement = (AsnNamedType) element.typeReference;

            String explicitEncoding = ", true";
            if (hasImplicitTag(sequenceElement)) {
                explicitEncoding = ", false";
            }
            if (isDirectAny(sequenceElement)) {
                explicitEncoding = "";
            }

            if (hasExplicitTag(sequenceElement)) {
                write("int sublength = seqOf.get(i).encode(os" + explicitEncoding + ");");
                write("codeLength += sublength;");
                write("codeLength += BerLength.encodeLength(os, sublength);");
            }
            else {
                write("codeLength += seqOf.get(i).encode(os" + explicitEncoding + ");");
            }

            if (hasTag(sequenceElement)) {
                if (hasExplicitTag(sequenceElement) || !isPrimitive(sequenceElement)) {
                    writeEncodeIdentifier(getTagClass(sequenceElement), "CONSTRUCTED", getTagNum(sequenceElement));
                }
                else {
                    if (getUniversalType(sequenceElement) instanceof AsnAnyNoDecode) {
                        writeEncodeIdentifier("UNIVERSAL_CLASS", "PRIMITIVE", getTagNum(sequenceElement));
                    }
                    else {
                        writeEncodeIdentifier(getTagClass(sequenceElement), "PRIMITIVE", getTagNum(sequenceElement));
                    }
                }
            }
        }
        else {

            if (isDirectAny(element)) {
                write("codeLength += seqOf.get(i).encode(os);");
            }
            else {
                write("codeLength += seqOf.get(i).encode(os, true);");
            }
        }

        write("}\n");

        write("codeLength += BerLength.encodeLength(os, codeLength);\n");

        write("}\n");

        write("if (explicit) {");
        write("codeLength += id.encode(os);");
        write("}\n");

        write("return codeLength;");
        write("}\n");

    }

    private void writeSequenceOrSetClass(AsnSequenceSet asnSequenceSet, String tagNum, String tagClass,
            String className, boolean asInternalClass) throws IOException {

        if (tagClass.isEmpty()) {
            tagClass = "UNIVERSAL_CLASS";
        }
        if (tagNum.isEmpty()) {
            if (asnSequenceSet.isSequence) {
                tagNum = "16";
            }
            else {
                // is SET
                tagNum = "17";
            }
        }
        if (className.isEmpty()) {
            className = cleanUpName(asnSequenceSet.name);
        }

        String isStaticStr = "";
        if (asInternalClass) {
            isStaticStr = " static";
        }

        write("public" + isStaticStr + " class " + className + " {\n");

        List<AsnComponentType> sequenceElements = asnSequenceSet.componentTypes;

        addAutomaticTagsIfNeeded(sequenceElements);

        for (AsnComponentType sequenceElementType : sequenceElements) {

            if (sequenceElementType.typeReference != null
                    && (sequenceElementType.typeReference instanceof AsnConstructedType)) {

                String subClassName = getClassNameOfStructureElement(sequenceElementType);
                writeConstructedTypeClass((AsnType) sequenceElementType.typeReference, "", "", subClassName, true);

            }

        }

        write("public static final BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
                + ", BerIdentifier.CONSTRUCTED, " + tagNum + ");");

        write("protected BerIdentifier id;\n");

        write("public byte[] code = null;");

        writePublicMembers(sequenceElements);

        writeEmptyConstructor(className);

        if (!jaxbMode) {
            writeEncodeConstructor(className, sequenceElements, false);
        }

        if (jaxbMode) {
            writeGetterAndSetter(sequenceElements);
        }

        writeSequenceOrSetEncodeFunction(sequenceElements);

        if (asnSequenceSet.isSequence) {
            writeSequenceDecodeFunction(sequenceElements);
        }
        else {
            writeSetDecodeFunction(sequenceElements);
        }

        writeEncodeAndSaveFunction();

        writeSequenceOrSetToStringFunction(sequenceElements);

        write("}\n");

    }

    private void addAutomaticTagsIfNeeded(List<AsnComponentType> sequenceElements) {
        if (!automaticTagging) {
            return;
        }
        for (AsnComponentType element : sequenceElements) {
            if (hasTag(element)) {
                return;
            }
        }
        int i = 0;
        for (AsnComponentType element : sequenceElements) {
            element.tag = new AsnTag();
            element.tag.classNumber = new AsnClassNumber();
            element.tag.classNumber.num = i;
            i++;
        }

    }

    private void writeEncodeAndSaveFunction() throws IOException {
        write("public void encodeAndSave(int encodingSizeGuess) throws IOException {");
        write("BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);");
        write("encode(os, false);");
        write("code = os.getArray();");
        write("}\n");
    }

    private void writeSetDecodeFunction(List<AsnComponentType> sequenceElements) throws IOException {
        write("public int decode(InputStream is, boolean explicit) throws IOException {");
        write("int codeLength = 0;");
        write("int subCodeLength = 0;");
        write("BerIdentifier berIdentifier = new BerIdentifier();\n");

        write("if (explicit) {");
        write("codeLength += id.decodeAndCheck(is);");
        write("}\n");
        write("BerLength length = new BerLength();");
        write("codeLength += length.decode(is);\n");
        write("int totalLength = length.val;");

        write("while (subCodeLength < totalLength) {");
        write("subCodeLength += berIdentifier.decode(is);");

        for (int j = 0; j < sequenceElements.size(); j++) {
            AsnComponentType sequenceElement = sequenceElements.get(j);

            String explicitEncoding = "false";

            String elseString = "";

            if (j != 0) {
                elseString = "else ";
            }

            if (getUniversalType(sequenceElement) instanceof AsnChoice) {

                if (!hasExplicitTag(sequenceElement)) {
                    throw new IOException("choice within set has no explict tag.");
                }
                write(elseString + "if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                        + ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

                write("subCodeLength += new BerLength().decode(is);");
                explicitEncoding = "null";

                write(getSequenceElementName(sequenceElement) + " = new "
                        + getClassNameOfStructureElement(sequenceElement) + "();");

                write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, " + explicitEncoding
                        + ");");

                write("}");

            }
            else {

                if (hasTag(sequenceElement)) {
                    if (hasExplicitTag(sequenceElement) || !isPrimitive(sequenceElement)) {
                        write(elseString + "if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                + ", BerIdentifier.CONSTRUCTED, " + getTagNum(sequenceElement) + ")) {");
                    }
                    else {
                        write(elseString + "if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                + ", BerIdentifier.PRIMITIVE, " + getTagNum(sequenceElement) + ")) {");
                    }
                    if (hasExplicitTag(sequenceElement)) {
                        write("subCodeLength += new BerLength().decode(is);");
                        if (getUniversalType(sequenceElement) instanceof AsnChoice) {
                            explicitEncoding = "null";
                        }
                        else {
                            explicitEncoding = "true";
                        }
                    }
                    else {
                        if (getUniversalType(sequenceElement) instanceof AsnChoice) {
                            explicitEncoding = "null";
                        }
                    }
                }
                else {
                    write(elseString + "if (berIdentifier.equals(" + getClassNameOfStructureElement(sequenceElement)
                            + ".identifier)) {");
                }

                write(getSequenceElementName(sequenceElement) + " = new "
                        + getClassNameOfStructureElement(sequenceElement) + "();");

                if ("null".equals(explicitEncoding)) {
                    write("BerLength length2 = new BerLength();");
                    write("subCodeLength += length2.decode(is);");
                }

                write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, " + explicitEncoding
                        + ");");

                write("}");

            }

        }

        write("}");

        write("if (subCodeLength != totalLength) {");
        write("throw new IOException(\"Length of set does not match length tag, length tag: \" + totalLength + \", actual set length: \" + subCodeLength);\n");
        write("}");
        write("codeLength += subCodeLength;\n");

        write("return codeLength;");
        write("}\n");
    }

    private void writeSequenceDecodeFunction(List<AsnComponentType> sequenceElements) throws IOException {
        write("public int decode(InputStream is, boolean explicit) throws IOException {");
        write("int codeLength = 0;");
        write("int subCodeLength = 0;");
        write("BerIdentifier berIdentifier = new BerIdentifier();\n");

        write("if (explicit) {");
        write("codeLength += id.decodeAndCheck(is);");
        write("}\n");
        write("BerLength length = new BerLength();");
        write("codeLength += length.decode(is);\n");
        write("int totalLength = length.val;");
        write("codeLength += totalLength;\n");

        // indefinite length

        if (supportIndefiniteLength == true) {

            write("if (totalLength == -1) {");
            write("subCodeLength += berIdentifier.decode(is);\n");

            String initChoiceDecodeLength = "int ";

            for (AsnComponentType sequenceElement : sequenceElements) {

                write("if (berIdentifier.tagNumber == 0 && berIdentifier.identifierClass == 0 && berIdentifier.primitive == 0) {");
                write("int nextByte = is.read();");
                write("if (nextByte != 0) {");
                write("if (nextByte == -1) {");
                write("throw new EOFException(\"Unexpected end of input stream.\");");
                write("}");
                write("throw new IOException(\"Decoded sequence has wrong end of contents octets\");");
                write("}");
                write("codeLength += subCodeLength + 1;");
                write("return codeLength;");
                write("}");

                String explicitEncoding;

                if (getUniversalType(sequenceElement) instanceof AsnChoice) {
                    if (hasExplicitTag(sequenceElement)) {
                        write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                + ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

                        write("subCodeLength += length.decode(is);");
                        explicitEncoding = "null";
                    }
                    else {
                        explicitEncoding = "berIdentifier";
                    }

                    write(getSequenceElementName(sequenceElement) + " = new "
                            + getClassNameOfStructureElement(sequenceElement) + "();");

                    write(initChoiceDecodeLength + "choiceDecodeLength = " + getSequenceElementName(sequenceElement)
                            + ".decode(is, " + explicitEncoding + ");");
                    if (!hasExplicitTag(sequenceElement)) {
                        initChoiceDecodeLength = "";
                    }
                    write("if (choiceDecodeLength != 0) {");
                    write("subCodeLength += choiceDecodeLength;");

                    write("subCodeLength += berIdentifier.decode(is);");
                    write("}");
                    write("else {");
                    write(getSequenceElementName(sequenceElement) + " = null;");
                    write("}\n");

                    if (hasExplicitTag(sequenceElement)) {
                        write("}");
                    }

                }
                else {

                    explicitEncoding = ", false";

                    if (hasTag(sequenceElement)) {
                        if (hasExplicitTag(sequenceElement) || !isPrimitive(sequenceElement)) {
                            if (getUniversalType(sequenceElement) instanceof AsnAnyNoDecode) {
                                write("if (berIdentifier.tagNumber == " + getTagNum(sequenceElement) + ") {");
                            }
                            else {
                                write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                        + ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");
                            }
                        }
                        else {
                            write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                    + ", BerIdentifier.PRIMITIVE" + ", " + getTagNum(sequenceElement) + ")) {");
                        }

                        if (hasExplicitTag(sequenceElement)) {
                            write("codeLength += length.decode(is);");
                            if (isDirectAny(sequenceElement)) {
                                explicitEncoding = ", length.val";
                            }
                            else {
                                explicitEncoding = ", true";
                            }
                        }
                    }
                    else {
                        if (!isDirectAny(sequenceElement)) {
                            write("if (berIdentifier.equals(" + getClassNameOfStructureElement(sequenceElement)
                                    + ".identifier)) {");
                        }
                    }

                    write(getSequenceElementName(sequenceElement) + " = new "
                            + getClassNameOfStructureElement(sequenceElement) + "();");

                    if (isDirectAny(sequenceElement)) {
                        write("subCodeLength += " + getSequenceElementName(sequenceElement)
                                + ".decode(is, length.val);");

                    }
                    else {
                        write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is"
                                + explicitEncoding + ");");
                    }

                    write("subCodeLength += berIdentifier.decode(is);");

                    if (hasTag(sequenceElement) || !isDirectAny(sequenceElement)) {
                        write("}");
                    }

                }

                // TODO has be reinserted:
                // if (!isOptional(sequenceElement) && ((!(getUniversalType(sequenceElement) instanceof AsnChoice))
                // || hasExplicitTag(sequenceElement))) {
                // write("else {");
                // write("throw new IOException(\"Identifier does not match required sequence element identifer.\");");
                // write("}");
                // }

            }

            write("int nextByte = is.read();");
            write("if (berIdentifier.tagNumber != 0 || berIdentifier.identifierClass != 0 || berIdentifier.primitive != 0");
            write("|| nextByte != 0) {");
            write("if (nextByte == -1) {");
            write("throw new EOFException(\"Unexpected end of input stream.\");");
            write("}");
            write("throw new IOException(\"Decoded sequence has wrong end of contents octets\");");
            write("}");
            write("codeLength += subCodeLength + 1;");

            write("return codeLength;");
            write("}\n");

        }

        // definite length

        int lastNoneOptionalFieldIndex = -1;
        for (int j = 0; j < sequenceElements.size(); j++) {
            AsnComponentType sequenceElement = sequenceElements.get(sequenceElements.size() - 1 - j);
            if (!isOptional(sequenceElement)) {
                lastNoneOptionalFieldIndex = sequenceElements.size() - 1 - j;
                break;
            }
        }

        if (lastNoneOptionalFieldIndex == -1) {
            write("if (totalLength == 0) {");
            write("return codeLength;");
            write("}");
        }

        write("subCodeLength += berIdentifier.decode(is);");

        String initChoiceDecodeLength = "int ";

        for (int j = 0; j < sequenceElements.size(); j++) {
            AsnComponentType sequenceElement = sequenceElements.get(j);

            String explicitEncoding;

            if (getUniversalType(sequenceElement) instanceof AsnChoice) {
                if (hasExplicitTag(sequenceElement)) {
                    write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                            + ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

                    write("subCodeLength += length.decode(is);");
                    explicitEncoding = "null";
                }
                else {
                    explicitEncoding = "berIdentifier";
                }

                write(getSequenceElementName(sequenceElement) + " = new "
                        + getClassNameOfStructureElement(sequenceElement) + "();");

                if (isOptional(sequenceElement) && !hasExplicitTag(sequenceElement)) {

                    write(initChoiceDecodeLength + "choiceDecodeLength = " + getSequenceElementName(sequenceElement)
                            + ".decode(is, " + explicitEncoding + ");");

                    initChoiceDecodeLength = "";

                    if (j != (sequenceElements.size() - 1)) {

                        write("if (choiceDecodeLength != 0) {");
                        write("subCodeLength += choiceDecodeLength;");

                        if (lastNoneOptionalFieldIndex <= j) {
                            write("if (subCodeLength == totalLength) {");
                            write("return codeLength;");
                            write("}");
                        }
                        write("subCodeLength += berIdentifier.decode(is);");
                        write("}");
                        write("else {");
                        write(getSequenceElementName(sequenceElement) + " = null;");
                        write("}");

                    }
                    else {
                        // if last sequence element
                        write("subCodeLength += choiceDecodeLength;");
                        write("if (subCodeLength == totalLength) {");
                        write("return codeLength;");
                        write("}");
                    }
                }
                else {
                    write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, "
                            + explicitEncoding + ");");
                    if (j != (sequenceElements.size() - 1)) {
                        if (lastNoneOptionalFieldIndex <= j) {
                            write("if (subCodeLength == totalLength) {");
                            write("return codeLength;");
                            write("}");
                        }
                        write("subCodeLength += berIdentifier.decode(is);");
                    }
                    else {
                        write("if (subCodeLength == totalLength) {");
                        write("return codeLength;");
                        write("}");
                    }
                }

                if (hasExplicitTag(sequenceElement)) {
                    write("}");
                }

                if (j == (sequenceElements.size() - 1)) {
                    write("throw new IOException(\"Unexpected end of sequence, length tag: \" + totalLength + \", actual sequence length: \" + subCodeLength);\n");
                }
                else if (hasExplicitTag(sequenceElement) && !isOptional(sequenceElement)) {
                    write("else {");
                    write("throw new IOException(\"Identifier does not match required sequence element identifer.\");");
                    write("}");
                }

            }
            else if (isTaglessAny(sequenceElement)) {
                write(getSequenceElementName(sequenceElement) + " = new "
                        + getClassNameOfStructureElement(sequenceElement) + "();");
                write("subCodeLength += " + getSequenceElementName(sequenceElement)
                        + ".decode(is, totalLength - subCodeLength);");
                write("return codeLength;");
            }
            else {

                explicitEncoding = ", false";

                if (hasTag(sequenceElement)) {
                    if (hasExplicitTag(sequenceElement) || !isPrimitive(sequenceElement)) {
                        write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                + ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");
                    }
                    else {
                        if (getUniversalType(sequenceElement) instanceof AsnAnyNoDecode) {
                            write("if (berIdentifier.equals(BerIdentifier.UNIVERSAL_CLASS, BerIdentifier.PRIMITIVE"
                                    + ", " + getTagNum(sequenceElement) + ")) {");
                        }
                        else {
                            write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
                                    + ", BerIdentifier.PRIMITIVE" + ", " + getTagNum(sequenceElement) + ")) {");
                        }
                    }

                    if (hasExplicitTag(sequenceElement)) {
                        write("subCodeLength += length.decode(is);");
                        if (isDirectAny(sequenceElement)) {
                            explicitEncoding = ", length.val";
                        }
                        else {
                            explicitEncoding = ", true";
                        }
                    }
                }
                else {
                    write("if (berIdentifier.equals(" + getClassNameOfStructureElement(sequenceElement)
                            + ".identifier)) {");
                }

                write(getSequenceElementName(sequenceElement) + " = new "
                        + getClassNameOfStructureElement(sequenceElement) + "();");

                write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is" + explicitEncoding
                        + ");");

                if (lastNoneOptionalFieldIndex <= j) {
                    write("if (subCodeLength == totalLength) {");
                    write("return codeLength;");
                    write("}");
                }
                if (j != (sequenceElements.size() - 1)) {
                    if (!isTaglessAny(sequenceElements.get(j + 1))) {
                        write("subCodeLength += berIdentifier.decode(is);");
                    }
                }

                write("}");

                if (j == (sequenceElements.size() - 1)) {
                    write("throw new IOException(\"Unexpected end of sequence, length tag: \" + totalLength + \", actual sequence length: \" + subCodeLength);\n");
                }
                else if (!isOptional(sequenceElement)) {
                    write("else {");
                    write("throw new IOException(\"Identifier does not match the mandatory sequence element identifer.\");");
                    write("}");
                }

            }

            write("");

        }

        if (sequenceElements.isEmpty()) {
            write("return subCodeLength;");
        }
        write("}\n");

    }

    private boolean isTaglessAny(AsnComponentType sequenceElement) {
        return (!hasTag(sequenceElement) && isDirectAny(sequenceElement));
    }

    private void writeSequenceOrSetToStringFunction(List<AsnComponentType> sequenceElements) throws IOException {
        write("public String toString() {");

        write("StringBuilder sb = new StringBuilder(\"SEQUENCE{\");");

        boolean checkIfFirstSelectedElement = true;

        int j = 0;

        for (AsnComponentType sequenceElement : sequenceElements) {

            if (isOptional(sequenceElement)) {
                if (j == 0) {
                    write("boolean firstSelectedElement = true;");
                }
                write("if (" + getSequenceElementName(sequenceElement) + " != null) {");
            }

            if (j != 0) {
                if (checkIfFirstSelectedElement) {

                    write("if (!firstSelectedElement) {");

                }
                write("sb.append(\", \");");
                if (checkIfFirstSelectedElement) {
                    write("}");
                }
            }

            write("sb.append(\"" + getSequenceElementName(sequenceElement) + ": \").append("
                    + getSequenceElementName(sequenceElement) + ");");

            if (isOptional(sequenceElement)) {
                if (checkIfFirstSelectedElement) {
                    write("firstSelectedElement = false;");
                }
                write("}");
            }
            else {
                checkIfFirstSelectedElement = false;
            }

            write("");

            j++;

        }

        write("sb.append(\"}\");");

        write("return sb.toString();");

        write("}\n");

    }

    private void writeSequenceOrSetEncodeFunction(List<AsnComponentType> sequenceElements) throws IOException {
        write("public int encode(BerByteArrayOutputStream os, boolean explicit) throws IOException {\n");

        write("int codeLength;\n");

        write("if (code != null) {");
        write("codeLength = code.length;");
        write("for (int i = code.length - 1; i >= 0; i--) {");
        write("os.write(code[i]);");
        write("}");
        write("}");
        write("else {");

        write("codeLength = 0;");

        for (int j = sequenceElements.size() - 1; j >= 0; j--) {
            if (hasExplicitTag(sequenceElements.get(j))) {
                write("int sublength;\n");
                break;
            }
        }

        for (int j = sequenceElements.size() - 1; j >= 0; j--) {

            AsnComponentType sequenceElement = sequenceElements.get(j);

            if (isOptional(sequenceElement)) {
                write("if (" + getSequenceElementName(sequenceElement) + " != null) {");
            }

            String explicitEncoding = ", true";
            if (hasImplicitTag(sequenceElement)) {
                explicitEncoding = ", false";
            }
            if (isDirectAny(sequenceElement)) {
                explicitEncoding = "";
            }

            if (hasExplicitTag(sequenceElement)) {
                write("sublength = " + getSequenceElementName(sequenceElement) + ".encode(os" + explicitEncoding
                        + ");");
                write("codeLength += sublength;");
                write("codeLength += BerLength.encodeLength(os, sublength);");
            }
            else {
                write("codeLength += " + getSequenceElementName(sequenceElement) + ".encode(os" + explicitEncoding
                        + ");");
            }

            if (hasTag(sequenceElement)) {
                if (hasExplicitTag(sequenceElement) || !isPrimitive(sequenceElement)) {
                    writeEncodeIdentifier(getTagClass(sequenceElement), "CONSTRUCTED", getTagNum(sequenceElement));
                }
                else {
                    if (getUniversalType(sequenceElement) instanceof AsnAnyNoDecode) {
                        writeEncodeIdentifier("UNIVERSAL_CLASS", "PRIMITIVE", getTagNum(sequenceElement));
                    }
                    else {
                        writeEncodeIdentifier(getTagClass(sequenceElement), "PRIMITIVE", getTagNum(sequenceElement));
                    }
                }
            }
            if (isOptional(sequenceElement)) {
                write("}");
            }

            write("");

        }

        write("codeLength += BerLength.encodeLength(os, codeLength);");
        write("}\n");
        write("if (explicit) {");
        write("codeLength += id.encode(os);");
        write("}\n");

        write("return codeLength;\n");

        write("}\n");
    }

    private void writeEncodeIdentifier(String tagClass, String cpString, String tagNum)
            throws IOException, NumberFormatException {
        int cp;
        if (cpString.equals("CONSTRUCTED")) {
            cp = BerIdentifier.CONSTRUCTED;
        }
        else {
            cp = BerIdentifier.PRIMITIVE;
        }

        BerIdentifier berIdentifier = new BerIdentifier(getTagClassId(tagClass), cp, Integer.parseInt(tagNum));

        write("// write tag: " + tagClass + ", " + cpString + ", " + tagNum);
        for (int i = (berIdentifier.identifier.length - 1); i >= 0; i--) {
            write("os.write(" + HexConverter.toHexString(berIdentifier.identifier[i]) + ");");
        }

        write("codeLength += " + berIdentifier.identifier.length + ";");

    }

    private int getTagClassId(String tagClass) {

        if (tagClass.equals("UNIVERSAL_CLASS")) {
            return BerIdentifier.UNIVERSAL_CLASS;
        }
        else if (tagClass.equals("APPLICATION_CLASS")) {
            return BerIdentifier.APPLICATION_CLASS;
        }
        else if (tagClass.equals("CONTEXT_CLASS")) {
            return BerIdentifier.CONTEXT_CLASS;
        }
        else if (tagClass.equals("PRIVATE_CLASS")) {
            return BerIdentifier.PRIVATE_CLASS;
        }
        else {
            throw new IllegalStateException("unknown tag class");
        }

    }

    private boolean isPrimitiveOrRetaggedPrimitive(AsnType asnType) throws IOException {
        return isPrimitiveOrRetaggedPrimitive(asnType, module);
    }

    private boolean isPrimitiveOrRetaggedPrimitive(AsnType asnType, AsnModule module) throws IOException {

        if ((asnType instanceof AsnSequenceSet) || (asnType instanceof AsnSequenceOf)) {
            return false;
        }
        else if (asnType instanceof AsnNamedType) {
            AsnNamedType asnTaggedType = (AsnNamedType) asnType;

            if (asnTaggedType.typeReference != null) {
                return isPrimitiveOrRetaggedPrimitive((AsnType) (asnTaggedType.typeReference));
            }
            else {
                return isPrimitiveOrRetaggedPrimitive(asnTaggedType.typeName, module);
            }
        }
        else if (asnType instanceof AsnDefinedType) {
            return isPrimitiveOrRetaggedPrimitive(((AsnDefinedType) asnType).typeName, module);
        }
        else if (asnType instanceof AsnChoice) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isPrimitiveOrRetaggedPrimitive(String typeName, AsnModule module) throws IOException {

        AsnType asnType = module.typesByName.get(typeName);
        if (asnType != null) {
            return isPrimitive(asnType, module);
        }
        for (SymbolsFromModule symbolsFromModule : module.importSymbolFromModuleList) {
            for (String importedTypeName : symbolsFromModule.symbolList) {
                if (typeName.equals(importedTypeName)) {
                    return isPrimitiveOrRetaggedPrimitive(typeName, modulesByName.get(symbolsFromModule.modref));
                }
            }
        }
        throw new IllegalStateException("Type definition \"" + typeName + "\" was not found in module \""
                + module.moduleIdentifier.name + "\"");
    }

    private boolean isPrimitive(AsnType asnType) throws IOException {
        return isPrimitive(asnType, module);
    }

    private boolean isPrimitive(AsnType asnType, AsnModule module) throws IOException {

        if ((asnType instanceof AsnSequenceSet) || (asnType instanceof AsnSequenceOf)) {
            return false;
        }
        else if (asnType instanceof AsnNamedType) {
            AsnNamedType asnTaggedType = (AsnNamedType) asnType;

            if (hasExplicitTag(asnTaggedType)) {
                return false;
            }

            if (asnTaggedType.typeReference != null) {
                return isPrimitive((AsnType) (asnTaggedType.typeReference));
            }
            else {
                return isPrimitive(asnTaggedType.typeName, module);
            }
        }
        else if (asnType instanceof AsnDefinedType) {
            return isPrimitive(((AsnDefinedType) asnType).typeName, module);
        }
        else if (asnType instanceof AsnChoice) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isPrimitive(String typeName, AsnModule module) throws IOException {

        AsnType asnType = module.typesByName.get(typeName);
        if (asnType != null) {
            return isPrimitive(asnType, module);
        }
        for (SymbolsFromModule symbolsFromModule : module.importSymbolFromModuleList) {
            for (String importedTypeName : symbolsFromModule.symbolList) {
                if (typeName.equals(importedTypeName)) {
                    return isPrimitive(typeName, modulesByName.get(symbolsFromModule.modref));
                }
            }
        }
        throw new IllegalStateException("Type definition \"" + typeName + "\" was not found in module \""
                + module.moduleIdentifier.name + "\"");
    }

    private AsnUniversalType getUniversalType(AsnType asnType) throws IOException {
        return getUniversalType(asnType, module);
    }

    private AsnUniversalType getUniversalType(AsnType asnType, AsnModule module) throws IOException {

        if (asnType instanceof AsnNamedType) {
            AsnNamedType asnTaggedType = (AsnNamedType) asnType;
            if (asnTaggedType.typeReference != null) {
                return getUniversalType((AsnType) (asnTaggedType.typeReference));
            }
            else {
                return getUniversalType(asnTaggedType.typeName, module);
            }
        }
        else if (asnType instanceof AsnDefinedType) {
            return getUniversalType(((AsnDefinedType) asnType).typeName, module);
        }
        else {
            return (AsnUniversalType) asnType;
        }
    }

    private AsnUniversalType getUniversalType(String typeName) throws IOException {
        return getUniversalType(typeName, module);

    }

    private AsnUniversalType getUniversalType(String typeName, AsnModule module) throws IOException {

        AsnType asnType = module.typesByName.get(typeName);
        if (asnType != null) {
            return getUniversalType(asnType, module);
        }
        for (SymbolsFromModule symbolsFromModule : module.importSymbolFromModuleList) {
            for (String importedTypeName : symbolsFromModule.symbolList) {
                if (typeName.equals(importedTypeName)) {
                    return getUniversalType(typeName, modulesByName.get(symbolsFromModule.modref));
                }
            }
        }
        throw new IllegalStateException("Type definition \"" + typeName + "\" was not found in module \""
                + module.moduleIdentifier.name + "\"");
    }

    private AsnType getTypeDefinition(String typeName, AsnModule module) throws IOException {

        AsnType asnType = module.typesByName.get(typeName);

        if (asnType != null) {
            return asnType;
        }

        for (SymbolsFromModule symbolsFromModule : module.importSymbolFromModuleList) {
            for (String importedTypeName : symbolsFromModule.symbolList) {
                if (typeName.equals(importedTypeName)) {
                    return getTypeDefinition(typeName, modulesByName.get(symbolsFromModule.modref));
                }
            }
        }
        throw new IllegalStateException("Type definition \"" + typeName + "\" was not found in module \""
                + module.moduleIdentifier.name + "\"");

    }

    private AsnType getTypeDefinition(String typeName) throws IOException {
        return getTypeDefinition(typeName, module);
    }

    private String getSequenceElementName(AsnComponentType sequenceElement) {
        return cleanUpName(sequenceElement.name);
    }

    private boolean isOptional(AsnComponentType sequenceElement) {
        return (sequenceElement.isOptional || sequenceElement.isDefault);
    }

    private boolean hasExplicitTag(AsnNamedType taggedType) throws IOException {
        if (!hasTag(taggedType)) {
            return false;
        }
        if (isDirectChoice(taggedType) || isDirectAny(taggedType)) {
            return true;
        }

        return isExplicit(taggedType.tagType);

    }

    private boolean hasImplicitTag(AsnNamedType taggedType) throws IOException {
        if (!hasTag(taggedType)) {
            return false;
        }

        return !hasExplicitTag(taggedType);

    }

    private boolean isExplicit(String tagType) {
        if (tagType.isEmpty()) {
            return isDefaultTagExplicit;
        }
        else if (tagType.equals("IMPLICIT")) {
            return false;
        }
        else if (tagType.equals("EXPLICIT")) {
            return true;
        }
        else {
            throw new IllegalStateException("unexpected tag type: " + tagType);
        }

    }

    private boolean hasTag(AsnNamedType taggedType) {
        return taggedType.tag != null;
    }

    private void writeEncodeConstructor(String className, List<AsnComponentType> sequenceElements, boolean isChoice)
            throws IOException {

        if (sequenceElements.isEmpty()) {
            return;
        }

        String line = "public " + className + "(";

        int j = 0;

        for (AsnComponentType sequenceElement : sequenceElements) {

            if (j != 0) {
                line += ", ";
            }
            j++;
            line += (getClassNameOfStructureElement(sequenceElement) + " " + cleanUpName(sequenceElement.name));

        }

        write(line + ") {");
        if (!isChoice) {
            write("id = identifier;");
        }

        for (AsnComponentType sequenceElement : sequenceElements) {

            String elementName = cleanUpName(sequenceElement.name);

            write("this." + elementName + " = " + elementName + ";");

        }

        write("}\n");
    }

    private void writeEmptyConstructor(String className) throws IOException {
        writeEmptyConstructor(className, false);
    }

    private void writeEmptyConstructor(String className, boolean isChoice) throws IOException {
        write("public " + className + "() {");
        if (!isChoice) {
            write("id = identifier;");
        }
        write("}\n");

        write("public " + className + "(byte[] code) {");
        if (!isChoice) {
            write("id = identifier;");
        }
        write("this.code = code;");
        write("}\n");
    }

    private void writePublicMembers(List<AsnComponentType> sequenceElements) throws IOException {
        for (AsnComponentType element : sequenceElements) {
            if (jaxbMode) {
                write("private " + getClassNameOfStructureElement(element) + " " + cleanUpName(element.name)
                        + " = null;\n");
            }
            else {
                write("public " + getClassNameOfStructureElement(element) + " " + cleanUpName(element.name)
                        + " = null;\n");
            }
        }
    }

    private void writeGetterAndSetter(List<AsnComponentType> sequenceElements) throws IOException {
        for (AsnComponentType element : sequenceElements) {
            String typeName = getClassNameOfStructureElement(element);
            String instanceName = cleanUpName(element.name);
            write("public void set" + capitalizeFirstCharacter(instanceName) + "(" + typeName + " " + instanceName
                    + ") {");
            write("this." + instanceName + " = " + instanceName + ";");
            write("}\n");
            write("public " + typeName + " get" + capitalizeFirstCharacter(instanceName) + "() {");
            write("return " + instanceName + ";");
            write("}\n");
        }
    }

    private void writeGetterForSeqOf(String referencedTypeName) throws IOException {
        write("public List<" + referencedTypeName + "> get" + referencedTypeName + "() {");
        write("if (seqOf == null) {");
        write("seqOf = new ArrayList<" + referencedTypeName + ">();");
        write("}");
        write("return seqOf;");
        write("}\n");
    }

    private String getClassNameOfSequenceOfElement(AsnSequenceOf asnElementType) throws IOException {
        if (asnElementType.typeReference == null) {
            return cleanUpName(asnElementType.typeName);
        }
        else {

            AsnType typeDefinition = asnElementType.typeReference;
            if (typeDefinition instanceof AsnNamedType) {
                return getClassNameOfSequenceOfTypeReference((AsnType) ((AsnNamedType) typeDefinition).typeReference);
            }
            return getClassNameOfSequenceOfTypeReference(typeDefinition);
        }
    }

    private String getClassNameOfSequenceOfTypeReference(AsnType typeDefinition) {
        if (typeDefinition instanceof AsnConstructedType) {

            String subClassName;

            if (typeDefinition instanceof AsnSequenceSet) {

                if (((AsnSequenceSet) typeDefinition).isSequence) {
                    subClassName = "SEQUENCE";
                }
                else {
                    subClassName = "SET";
                }

            }
            else if (typeDefinition instanceof AsnSequenceOf) {

                // TODO does a sub seq of exist at all?

                if (((AsnSequenceOf) typeDefinition).isSequenceOf) {
                    subClassName = "SubSeqOf";
                }
                else {
                    subClassName = "SubSetOf";
                }

            }
            else {
                subClassName = "CHOICE";
            }

            return subClassName;

        }
        return getBerType(typeDefinition);
    }

    private String getClassNameOfStructureElement(AsnComponentType asnElementType) throws IOException {

        if (asnElementType.typeReference == null) {
            return cleanUpName(asnElementType.typeName);
        }
        else {

            AsnType typeDefinition = (AsnType) asnElementType.typeReference;

            if (typeDefinition instanceof AsnConstructedType) {

                return cleanUpName(capitalizeFirstCharacter(asnElementType.name));

            }

            return getBerType(typeDefinition);

        }
    }

    private String capitalizeFirstCharacter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private String getBerType(AsnType asnType) {

        String fullClassName = asnType.getClass().getName();

        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

        if (className.equals("AsnCharacterString")) {
            AsnCharacterString asnCharacterString = (AsnCharacterString) asnType;
            if (asnCharacterString.stringtype.equals("ISO646String")) {
                return "BerVisibleString";
            }
            else if (asnCharacterString.stringtype.equals("T61String")) {
                return "BerTeletexString";
            }
            return "Ber" + ((AsnCharacterString) asnType).stringtype;
        }
        return "Ber" + className.substring(3);
    }

    private void writeRetaggingTypeClass(String tagNum, String tagClass, String typeName, String assignedTypeName,
            boolean explicit, AsnType typeDefinition) throws IOException {

        boolean assignedTypeIsDirectChoice = isDirectChoice(assignedTypeName);

        // if the assigned type is a direct choice (not a tagged choice) then the tag is always explicit.
        if (assignedTypeIsDirectChoice && !tagNum.isEmpty()) {
            explicit = true;
        }

        write("public class " + typeName + " extends " + cleanUpName(assignedTypeName) + " {\n");

        if (!tagNum.isEmpty()) {

            String primitiveOrConstructedString = "PRIMITIVE";

            if (explicit || !isPrimitive(typeDefinition)) {
                primitiveOrConstructedString = "CONSTRUCTED";
            }

            write("public static final BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
                    + ", BerIdentifier." + primitiveOrConstructedString + ", " + tagNum + ");\n");

            if (explicit) {
                write("protected BerIdentifier id;\n");

                write("public byte[] code = null;\n");
            }
        }

        write("public " + typeName + "() {");
        if (!tagNum.isEmpty()) {
            write("id = identifier;");
        }
        write("}\n");

        String[] constructorParameters = getConstructorParameters(getUniversalType(typeDefinition));

        if (constructorParameters.length != 2 || constructorParameters[0] != "byte[]") {
            write("public " + typeName + "(byte[] code) {");
            write("super(code);");
            if (!tagNum.isEmpty()) {
                write("id = identifier;");
            }
            write("}\n");
        }

        if ((!jaxbMode || isPrimitiveOrRetaggedPrimitive(typeDefinition)) && (constructorParameters.length != 0)) {
            String constructorParameterString = "";
            String superCallParameterString = "";
            for (int i = 0; i < constructorParameters.length; i += 2) {
                if (i > 0) {
                    constructorParameterString += ", ";
                    superCallParameterString += ", ";
                }
                constructorParameterString += constructorParameters[i] + " " + constructorParameters[i + 1];
                superCallParameterString += constructorParameters[i + 1];
            }

            write("public " + typeName + "(" + constructorParameterString + ") {");
            write("super(" + superCallParameterString + ");");
            if (!tagNum.isEmpty()) {
                write("id = identifier;");
            }
            write("}\n");
        }

        if (explicit) {

            write("public int encode(BerByteArrayOutputStream os, boolean explicit) throws IOException {\n");

            write("int codeLength;\n");

            write("if (code != null) {");
            write("codeLength = code.length;");
            write("for (int i = code.length - 1; i >= 0; i--) {");
            write("os.write(code[i]);");
            write("}");
            write("}");
            write("else {");
            write("codeLength = super.encode(os, true);");
            write("codeLength += BerLength.encodeLength(os, codeLength);");
            write("}\n");

            write("if (explicit) {");
            write("codeLength += id.encode(os);");
            write("}\n");

            write("return codeLength;");
            write("}\n");

            write("public int decode(InputStream is, boolean explicit) throws IOException {\n");

            write("int codeLength = 0;\n");

            write("if (explicit) {");
            write("codeLength += id.decodeAndCheck(is);");
            write("}\n");

            write("BerLength length = new BerLength();");
            write("codeLength += length.decode(is);\n");

            if (getUniversalType(typeDefinition) instanceof AsnChoice) {
                write("codeLength += super.decode(is, null);\n");
            }
            else {
                write("codeLength += super.decode(is, true);\n");
            }

            write("return codeLength;");
            write("}\n");
        }

        write("}");

    }

    private String[] getConstructorParameters(AsnUniversalType typeDefinition) throws IOException {

        if (typeDefinition instanceof AsnInteger || typeDefinition instanceof AsnEnum) {
            return new String[] { "long", "value" };
        }
        else if (typeDefinition instanceof AsnReal) {
            return new String[] { "double", "value" };
        }
        else if (typeDefinition instanceof AsnBoolean) {
            return new String[] { "boolean", "value" };
        }
        else if (typeDefinition instanceof AsnObjectIdentifier) {
            return new String[] { "int[]", "value" };
        }
        else if (typeDefinition instanceof AsnBitString) {
            return new String[] { "byte[]", "value", "int", "numBits" };
        }
        else if (typeDefinition instanceof AsnOctetString || typeDefinition instanceof AsnCharacterString) {
            /*
             * case "BerOctetString": case "BerGeneralizedTime": case "BerBMPString": case "BerGeneralString": case
             * "BerGraphicString": case "BerIA5String": case "BerNumericString": case "BerPrintableString": case
             * "BerTeletexString": case "BerUniversalString": case "BerUTF8String": case "BerVideotexString": case
             * "BerVisibleString":
             */
            return new String[] { "byte[]", "value" };
        }
        else if (typeDefinition instanceof AsnNull) {
            return new String[0];
        }
        else if ((typeDefinition instanceof AsnSequenceSet) || (typeDefinition instanceof AsnChoice)) {
            return getConstructorParametersFromConstructedElement((AsnConstructedType) typeDefinition);
        }
        else if (typeDefinition instanceof AsnSequenceOf) {
            return new String[] { "List<" + getClassNameOfSequenceOfElement((AsnSequenceOf) typeDefinition) + ">",
                    "seqOf" };
        }
        else if (typeDefinition instanceof AsnAny) {
            return new String[] { "byte[]", "value" };
        }
        else {
            throw new IllegalStateException("type of unknown class: " + typeDefinition.name);
        }

    }

    private String[] getConstructorParametersFromConstructedElement(AsnConstructedType assignedTypeDefinition)
            throws IOException {

        List<AsnComponentType> sequenceElements;

        if (assignedTypeDefinition instanceof AsnSequenceSet) {

            sequenceElements = ((AsnSequenceSet) assignedTypeDefinition).componentTypes;
        }
        else {
            sequenceElements = ((AsnChoice) assignedTypeDefinition).componentTypes;
        }

        String[] constructorParameters = new String[sequenceElements.size() * 2];

        for (int j = 0; j < sequenceElements.size(); j++) {
            AsnComponentType sequenceElement = sequenceElements.get(j);

            constructorParameters[j * 2] = getClassNameOfStructureElement(sequenceElement);
            constructorParameters[j * 2 + 1] = cleanUpName(sequenceElement.name);

        }
        return constructorParameters;
    }

    private boolean isDirectChoice(String typeName) {
        return isDirectChoice(typeName, module);
    }

    private boolean isDirectChoice(AsnNamedType taggedType) {
        if ((!taggedType.typeName.isEmpty() && isDirectChoice(taggedType.typeName))
                || ((taggedType.typeReference != null) && (taggedType.typeReference instanceof AsnChoice))) {
            return true;
        }
        return false;
    }

    private boolean isDirectChoice(AsnSequenceOf taggedType) {
        if ((!taggedType.typeName.isEmpty() && isDirectChoice(taggedType.typeName))
                || ((taggedType.typeReference != null) && (taggedType.typeReference instanceof AsnChoice))) {
            return true;
        }
        return false;
    }

    private boolean isDirectChoice(String typeName, AsnModule module) {
        if (typeName.startsWith("Ber")) {
            return false;
        }
        else {

            AsnType asnType = module.typesByName.get(typeName);

            if (asnType != null) {
                if (asnType instanceof AsnDefinedType) {
                    return isDirectChoice(((AsnDefinedType) asnType).typeName, module);
                }
                else if (asnType instanceof AsnChoice) {
                    return true;
                }
                else {
                    return false;
                }
            }

            for (SymbolsFromModule symbolsFromModule : module.importSymbolFromModuleList) {
                for (String importedTypeName : symbolsFromModule.symbolList) {
                    if (typeName.equals(importedTypeName)) {
                        return isDirectChoice(typeName, modulesByName.get(symbolsFromModule.modref));
                    }
                }
            }
            throw new IllegalStateException("Type definition \"" + typeName + "\" was not found in module \""
                    + module.moduleIdentifier.name + "\"");

        }
    }

    private boolean isDirectAny(String typeName) {
        return isDirectAny(typeName, module);
    }

    private boolean isDirectAny(AsnNamedType taggedType) {
        if ((!taggedType.typeName.isEmpty() && isDirectAny(taggedType.typeName))
                || ((taggedType.typeReference != null) && ((taggedType.typeReference instanceof AsnAny)
                        || (taggedType.typeReference instanceof AsnAnyNoDecode)))) {
            return true;
        }
        return false;
    }

    private boolean isDirectAny(AsnSequenceOf taggedType) {
        if ((!taggedType.typeName.isEmpty() && isDirectAny(taggedType.typeName))
                || ((taggedType.typeReference != null) && ((taggedType.typeReference instanceof AsnAny)
                        || (taggedType.typeReference instanceof AsnAnyNoDecode)))) {
            return true;
        }
        return false;
    }

    private boolean isDirectAny(String typeName, AsnModule module) {
        if (typeName.startsWith("Ber")) {
            return false;
        }
        else {

            AsnType asnType = module.typesByName.get(typeName);

            if (asnType != null) {
                if (asnType instanceof AsnDefinedType) {
                    return isDirectAny(((AsnDefinedType) asnType).typeName, module);
                }
                else if ((asnType instanceof AsnAny) || (asnType instanceof AsnAnyNoDecode)) {
                    return true;
                }
                else {
                    return false;
                }
            }

            for (SymbolsFromModule symbolsFromModule : module.importSymbolFromModuleList) {
                for (String importedTypeName : symbolsFromModule.symbolList) {
                    if (typeName.equals(importedTypeName)) {
                        return isDirectAny(typeName, modulesByName.get(symbolsFromModule.modref));
                    }
                }
            }
            throw new IllegalStateException("Type definition \"" + typeName + "\" was not found in module \""
                    + module.moduleIdentifier.name + "\"");

        }
    }

    private String getTagNum(AsnNamedType asnTaggedType) {

        if (asnTaggedType.tag == null) {
            throw new IllegalStateException("tag is null");
        }

        Integer tagNum = asnTaggedType.tag.classNumber.num;

        if (tagNum == null) {
            throw new IllegalStateException("tag number is null");
        }

        return tagNum.toString();
    }

    private String getTagClass(AsnNamedType asnTaggedType) {
        if (asnTaggedType.tag == null) {
            throw new IllegalStateException("tag is null");
        }

        String tagClass = asnTaggedType.tag.clazz;
        if (tagClass.isEmpty()) {
            return "CONTEXT_CLASS";
        }
        else {
            return tagClass + "_CLASS";
        }
    }

    private void writeClassHeader(String typeName) throws IOException {
        FileWriter fstream = new FileWriter(new File(outputDirectory, typeName + ".java"));
        out = new BufferedWriter(fstream);
        writeHeader(new String[] { "import java.util.List;", "import java.util.ArrayList;",
                "import java.util.Iterator;", "import java.io.UnsupportedEncodingException;" });
    }

    private void writeHeader(String[] additionalImports) throws IOException {

        write("/**");
        write(" * This class file was automatically generated by jASN1 v" + Compiler.VERSION
                + " (http://www.openmuc.org)\n */\n");
        write("package " + basePackageName + module.moduleIdentifier.name.replace('-', '.').toLowerCase() + ";\n");

        write("import java.io.IOException;");
        write("import java.io.EOFException;");
        write("import java.io.InputStream;");
        if (additionalImports != null) {
            for (String importStatement : additionalImports) {
                write(importStatement);
            }
        }
        write("import org.openmuc.jasn1.ber.*;");
        write("import org.openmuc.jasn1.ber.types.*;");
        write("import org.openmuc.jasn1.ber.types.string.*;\n");

        for (AsnModule module : modulesByName.values()) {
            if (module != this.module) {
                write("import " + basePackageName + module.moduleIdentifier.name.replace('-', '.').toLowerCase()
                        + ".*;");
            }
        }
        write("");

    }

    private void write(String line) throws IOException {
        if (line.startsWith("}")) {
            indentNum--;
        }
        for (int i = 0; i < indentNum; i++) {
            out.write("\t");
        }
        out.write(line + "\n");

        if (line.endsWith(" {") || line.endsWith(" {\n") || line.endsWith(" {\n\n")) {
            indentNum++;
        }
    }

}
