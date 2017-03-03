/*
 * Copyright 2011-17 Fraunhofer ISE
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.openmuc.jasn1.ber.BerTag;
import org.openmuc.jasn1.compiler.model.AsnAny;
import org.openmuc.jasn1.compiler.model.AsnAnyNoDecode;
import org.openmuc.jasn1.compiler.model.AsnBitString;
import org.openmuc.jasn1.compiler.model.AsnBoolean;
import org.openmuc.jasn1.compiler.model.AsnCharacterString;
import org.openmuc.jasn1.compiler.model.AsnChoice;
import org.openmuc.jasn1.compiler.model.AsnClassNumber;
import org.openmuc.jasn1.compiler.model.AsnConstructedType;
import org.openmuc.jasn1.compiler.model.AsnDefinedType;
import org.openmuc.jasn1.compiler.model.AsnElementType;
import org.openmuc.jasn1.compiler.model.AsnEnum;
import org.openmuc.jasn1.compiler.model.AsnInteger;
import org.openmuc.jasn1.compiler.model.AsnModule;
import org.openmuc.jasn1.compiler.model.AsnModule.TagDefault;
import org.openmuc.jasn1.compiler.model.AsnNull;
import org.openmuc.jasn1.compiler.model.AsnObjectIdentifier;
import org.openmuc.jasn1.compiler.model.AsnOctetString;
import org.openmuc.jasn1.compiler.model.AsnReal;
import org.openmuc.jasn1.compiler.model.AsnSequenceOf;
import org.openmuc.jasn1.compiler.model.AsnSequenceSet;
import org.openmuc.jasn1.compiler.model.AsnTag;
import org.openmuc.jasn1.compiler.model.AsnTaggedType;
import org.openmuc.jasn1.compiler.model.AsnType;
import org.openmuc.jasn1.compiler.model.AsnUniversalType;
import org.openmuc.jasn1.compiler.model.SymbolsFromModule;

public class BerClassWriter {

    private static Tag stdSeqTag = new Tag();
    private static Tag stdSetTag = new Tag();

    static {
        stdSeqTag.tagClass = TagClass.UNIVERSAL;
        stdSeqTag.value = 16;
        stdSeqTag.typeStructure = TypeStructure.CONSTRUCTED;

        stdSetTag.tagClass = TagClass.UNIVERSAL;
        stdSetTag.value = 17;
        stdSetTag.typeStructure = TypeStructure.CONSTRUCTED;
    }

    public enum TagClass {
        UNIVERSAL,
        APPLICATION,
        CONTEXT,
        PRIVATE
    };

    public enum TagType {
        EXPLICIT,
        IMPLICIT
    };

    public enum TypeStructure {
        PRIMITIVE,
        CONSTRUCTED
    }

    public static class Tag {
        public int value;
        public TagClass tagClass;
        public TagType type;
        public TypeStructure typeStructure;
    }

    private TagDefault tagDefault;
    private final String outputBaseDir;
    private final String basePackageName;
    private int indentNum = 0;
    BufferedWriter out;

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
        tagDefault = module.tagDefault;

        for (AsnType typeDefinition : module.typesByName.values()) {

            String typeName = cleanUpName(typeDefinition.name);

            writeClassHeader(typeName);

            if (typeDefinition instanceof AsnTaggedType) {

                AsnTaggedType asnTaggedType = (AsnTaggedType) typeDefinition;

                Tag tag = getTag(asnTaggedType);

                String assignedTypeName = asnTaggedType.typeName;

                if (!assignedTypeName.isEmpty()) {
                    writeRetaggingTypeClass(typeName, assignedTypeName, typeDefinition, tag);
                }
                else {

                    AsnType assignedAsnType = (AsnType) asnTaggedType.typeReference;

                    if (assignedAsnType instanceof AsnConstructedType) {
                        writeConstructedTypeClass(typeName, assignedAsnType, tag, false);
                    }
                    else {
                        writeRetaggingTypeClass(typeName, getBerType(assignedAsnType), typeDefinition, tag);
                    }
                }

            }
            else if (typeDefinition instanceof AsnDefinedType) {
                writeRetaggingTypeClass(typeName, ((AsnDefinedType) typeDefinition).typeName, typeDefinition, null);
            }
            else if (typeDefinition instanceof AsnConstructedType) {
                writeConstructedTypeClass(typeName, typeDefinition, null, false);
            }
            else {
                writeRetaggingTypeClass(typeName, getBerType(typeDefinition), typeDefinition, null);
            }

            out.close();
        }

    }

    /**
     * Gets the tag from the AsnTaggedType structure. The returned tag will contain the correct class and type (explicit
     * or implicit). Return null if the passed tagged type does not have a tag.
     * 
     * @param asnTaggedType
     * @return
     */
    private Tag getTag(AsnTaggedType asnTaggedType) {

        AsnTag asnTag = asnTaggedType.tag;

        if (asnTag == null) {
            return null;
        }

        Tag tag = new Tag();

        String tagClassString = asnTag.clazz;
        if (tagClassString.isEmpty() || "CONTEXT".equals(tagClassString)) {
            tag.tagClass = TagClass.CONTEXT;
        }
        else if ("APPLICATION".equals(tagClassString)) {
            tag.tagClass = TagClass.APPLICATION;
        }
        else if ("PRIVATE".equals(tagClassString)) {
            tag.tagClass = TagClass.PRIVATE;
        }
        else if ("UNIVERSAL".equals(tagClassString)) {
            tag.tagClass = TagClass.UNIVERSAL;
        }
        else {
            throw new IllegalStateException("unknown tag class: " + tagClassString);
        }

        String tagTypeString = asnTaggedType.tagType;

        if (tagTypeString.isEmpty()) {
            if (tagDefault == TagDefault.EXPLICIT) {
                tag.type = TagType.EXPLICIT;
            }
            else {
                tag.type = TagType.IMPLICIT;
            }
        }
        else if (tagTypeString.equals("IMPLICIT")) {
            tag.type = TagType.IMPLICIT;
        }
        else if (tagTypeString.equals("EXPLICIT")) {
            tag.type = TagType.EXPLICIT;
        }
        else {
            throw new IllegalStateException("unexpected tag type: " + tagTypeString);
        }

        if (tag.type == TagType.IMPLICIT) {
            if (isDirectChoice(asnTaggedType) || isDirectAny(asnTaggedType)) {
                tag.type = TagType.EXPLICIT;
            }
        }

        if ((tag.type == TagType.IMPLICIT) && isPrimitive(asnTaggedType)) {
            tag.typeStructure = TypeStructure.PRIMITIVE;
        }
        else {
            tag.typeStructure = TypeStructure.CONSTRUCTED;
        }

        tag.value = asnTaggedType.tag.classNumber.num;

        return tag;

    }

    private String cleanUpName(String name) {

        name = replaceCharByCamelCase(name, '-');
        name = replaceCharByCamelCase(name, '_');

        if (name.equals("null")) {
            name = name + "_";
        }
        return name;
    }

    private String replaceCharByCamelCase(String name, char charToBeReplaced) {
        StringBuilder nameSb = new StringBuilder(name);

        int index = name.indexOf(charToBeReplaced);
        while (index != -1 && index != (name.length() - 1)) {
            if (!Character.isUpperCase(name.charAt(index + 1))) {
                nameSb.setCharAt(index + 1, Character.toUpperCase(name.charAt(index + 1)));
            }
            index = name.indexOf(charToBeReplaced, index + 1);
        }

        name = nameSb.toString();
        name = name.replace("" + charToBeReplaced, "");

        return name;
    }

    private void writeConstructedTypeClass(String className, AsnType asnType, Tag tag, boolean asInternalClass)
            throws IOException {

        String isStaticStr = "";
        if (asInternalClass) {
            isStaticStr = " static";
        }

        if (asnType instanceof AsnSequenceSet) {
            writeSequenceOrSetClass(className, (AsnSequenceSet) asnType, tag, isStaticStr);
        }
        else if (asnType instanceof AsnSequenceOf) {
            writeSequenceOfClass(className, (AsnSequenceOf) asnType, tag, isStaticStr);
        }
        else if (asnType instanceof AsnChoice) {
            writeChoiceClass(className, (AsnChoice) asnType, tag, isStaticStr);
        }
    }

    private void writeChoiceClass(String className, AsnChoice asn1TypeElement, Tag tag, String isStaticStr)
            throws IOException {

        write("public" + isStaticStr + " class " + className + " {\n");

        write("public byte[] code = null;");

        if (tag != null) {
            write("public static final BerTag tag = new BerTag(" + getBerTagParametersString(tag) + ");\n");

        }

        List<AsnElementType> componentTypes = asn1TypeElement.componentTypes;

        addAutomaticTagsIfNeeded(componentTypes);

        for (AsnElementType componentType : componentTypes) {

            if (componentType.typeReference != null && (componentType.typeReference instanceof AsnConstructedType)) {

                String subClassName = getClassNameOfComponent(componentType);
                writeConstructedTypeClass(subClassName, (AsnType) componentType.typeReference, null, true);

            }
        }

        writePublicMembers(componentTypes);

        writeEmptyConstructor(className);

        if (!jaxbMode) {
            writeEncodeConstructor(className, componentTypes);
        }

        if (jaxbMode) {
            writeGetterAndSetter(componentTypes);
        }

        writeChoiceEncodeFunction(componentTypes, tag != null);

        writeChoiceDecodeFunction(componentTypes, tag != null);

        writeEncodeAndSaveFunction(tag == null);

        writeChoiceToStringFunction(componentTypes);

        write("}\n");

    }

    private void writeSequenceOrSetClass(String className, AsnSequenceSet asnSequenceSet, Tag tag, String isStaticStr)
            throws IOException {

        write("public" + isStaticStr + " class " + className + " {\n");

        List<AsnElementType> componentTypes = asnSequenceSet.componentTypes;

        addAutomaticTagsIfNeeded(componentTypes);

        for (AsnElementType componentType : componentTypes) {

            if (componentType.typeReference != null && (componentType.typeReference instanceof AsnConstructedType)) {

                String subClassName = getClassNameOfComponent(componentType);

                writeConstructedTypeClass(subClassName, (AsnType) componentType.typeReference, null, true);

            }

        }

        Tag mainTag;
        if (tag == null) {
            if (asnSequenceSet.isSequence) {
                mainTag = stdSeqTag;
            }
            else {
                mainTag = stdSetTag;
            }
        }
        else {
            mainTag = tag;
        }

        write("public static final BerTag tag = new BerTag(" + getBerTagParametersString(mainTag) + ");\n");

        write("public byte[] code = null;");

        writePublicMembers(componentTypes);

        writeEmptyConstructor(className);

        if (!jaxbMode) {
            writeEncodeConstructor(className, componentTypes);
        }

        if (jaxbMode) {
            writeGetterAndSetter(componentTypes);
        }

        boolean hasExplicitTag = (tag != null) && (tag.type == TagType.EXPLICIT);

        writeSimpleEncodeFunction();

        writeSequenceOrSetEncodeFunction(componentTypes, hasExplicitTag, asnSequenceSet.isSequence);

        writeSimpleDecodeFunction("true");

        if (asnSequenceSet.isSequence) {
            writeSequenceDecodeFunction(componentTypes, hasExplicitTag);
        }
        else {
            writeSetDecodeFunction(componentTypes);
        }

        writeEncodeAndSaveFunction();

        writeSequenceOrSetToStringFunction(componentTypes);

        write("}\n");

    }

    private void writeSimpleDecodeFunction(String param) throws IOException {
        write("public int decode(InputStream is) throws IOException {");
        write("return decode(is, " + param + ");");
        write("}\n");
    }

    private void writeSimpleEncodeFunction() throws IOException {
        write("public int encode(BerByteArrayOutputStream os) throws IOException {");
        write("return encode(os, true);");
        write("}\n");
    }

    private void writeSequenceOfClass(String className, AsnSequenceOf asnSequenceOf, Tag tag, String isStaticStr)
            throws IOException {

        write("public" + isStaticStr + " class " + className + " {\n");

        AsnElementType componentType = asnSequenceOf.componentType;

        String referencedTypeName = getClassNameOfSequenceOfElement(className, componentType);

        if (componentType.typeReference != null && (componentType.typeReference instanceof AsnConstructedType)) {
            writeConstructedTypeClass(referencedTypeName, (AsnType) componentType.typeReference, null, true);
        }

        Tag mainTag;
        if (tag == null) {
            if (asnSequenceOf.isSequenceOf) {
                mainTag = stdSeqTag;
            }
            else {
                mainTag = stdSetTag;
            }
        }
        else {
            mainTag = tag;
        }

        write("public static final BerTag tag = new BerTag(" + getBerTagParametersString(mainTag) + ");");

        write("public byte[] code = null;");

        if (jaxbMode) {
            write("private List<" + referencedTypeName + "> seqOf = null;\n");
        }
        else {
            write("public List<" + referencedTypeName + "> seqOf = null;\n");
        }

        write("public " + className + "() {");
        write("seqOf = new ArrayList<" + referencedTypeName + ">();");
        write("}\n");

        write("public " + className + "(byte[] code) {");
        write("this.code = code;");
        write("}\n");

        if (!jaxbMode) {
            write("public " + className + "(List<" + referencedTypeName + "> seqOf) {");
            write("this.seqOf = seqOf;");
            write("}\n");
        }

        if (jaxbMode) {
            writeGetterForSeqOf(referencedTypeName);
        }

        boolean hasExplicitTag = (tag != null) && (tag.type == TagType.EXPLICIT);

        writeSimpleEncodeFunction();

        writeSequenceOfEncodeFunction(componentType, hasExplicitTag, asnSequenceOf.isSequenceOf);

        writeSimpleDecodeFunction("true");

        writeSequenceOrSetOfDecodeFunction(componentType, referencedTypeName, hasExplicitTag,
                asnSequenceOf.isSequenceOf);

        writeEncodeAndSaveFunction();

        writeSequenceOrSetOfToStringFunction(referencedTypeName);

        write("}\n");

    }

    private void writeRetaggingTypeClass(String typeName, String assignedTypeName, AsnType typeDefinition, Tag tag)
            throws IOException {

        write("public class " + typeName + " extends " + cleanUpName(assignedTypeName) + " {\n");

        if (tag != null) {

            write("public static final BerTag tag = new BerTag(" + getBerTagParametersString(tag) + ");\n");

            if (tag.type == TagType.EXPLICIT) {
                write("public byte[] code = null;\n");
            }
        }

        write("public " + typeName + "() {");
        write("}\n");

        String[] constructorParameters = getConstructorParameters(getUniversalType(typeDefinition));

        if (constructorParameters.length != 2 || constructorParameters[0] != "byte[]") {
            write("public " + typeName + "(byte[] code) {");
            write("super(code);");
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
            write("}\n");

            if (constructorParameters[0].equals("BigInteger")) {
                write("public " + typeName + "(long value) {");
                write("super(value);");
                write("}\n");
            }

        }

        if (tag != null) {

            if (isDirectAny((AsnTaggedType) typeDefinition) || isDirectChoice((AsnTaggedType) typeDefinition)) {
                writeSimpleEncodeFunction();
            }

            write("public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {\n");

            if (constructorParameters.length != 2 || constructorParameters[0] != "byte[]") {
                write("if (code != null) {");
                write("for (int i = code.length - 1; i >= 0; i--) {");
                write("os.write(code[i]);");
                write("}");
                write("if (withTag) {");
                write("return tag.encode(os) + code.length;");
                write("}");
                write("return code.length;");
                write("}\n");
            }

            write("int codeLength;\n");

            if (tag.type == TagType.EXPLICIT) {
                if (isDirectAny((AsnTaggedType) typeDefinition) || isDirectChoice((AsnTaggedType) typeDefinition)) {
                    write("codeLength = super.encode(os);");
                }
                else {
                    write("codeLength = super.encode(os, true);");
                }
                write("codeLength += BerLength.encodeLength(os, codeLength);");
            }
            else {
                write("codeLength = super.encode(os, false);");
            }

            write("if (withTag) {");
            write("codeLength += tag.encode(os);");
            write("}\n");

            write("return codeLength;");
            write("}\n");

            if (isDirectAny((AsnTaggedType) typeDefinition) || isDirectChoice((AsnTaggedType) typeDefinition)) {
                writeSimpleDecodeFunction("true");
            }

            write("public int decode(InputStream is, boolean withTag) throws IOException {\n");

            write("int codeLength = 0;\n");

            write("if (withTag) {");
            write("codeLength += tag.decodeAndCheck(is);");
            write("}\n");

            if (tag.type == TagType.EXPLICIT) {

                write("BerLength length = new BerLength();");
                write("codeLength += length.decode(is);\n");

                if (isDirectChoice((AsnTaggedType) typeDefinition)) {
                    write("codeLength += super.decode(is, null);\n");
                }
                else if (getUniversalType(typeDefinition) instanceof AsnAny
                        || getUniversalType(typeDefinition) instanceof AsnAnyNoDecode) {
                    write("codeLength += super.decode(is, length.val);\n");
                }
                else {
                    write("codeLength += super.decode(is, true);\n");
                }
            }
            else {
                write("codeLength += super.decode(is, false);\n");
            }

            write("return codeLength;");
            write("}\n");
        }

        write("}");

    }

    private void writeChoiceEncodeFunction(List<AsnElementType> componentTypes, boolean hasExplicitTag)
            throws IOException {
        if (hasExplicitTag) {
            writeSimpleEncodeFunction();
            write("public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {\n");
        }
        else {
            write("public int encode(BerByteArrayOutputStream os) throws IOException {\n");
        }

        write("if (code != null) {");
        write("for (int i = code.length - 1; i >= 0; i--) {");
        write("os.write(code[i]);");
        write("}");
        if (hasExplicitTag) {
            write("if (withTag) {");
            write("return tag.encode(os) + code.length;");
            write("}");
        }
        write("return code.length;");
        write("}\n");

        write("int codeLength = 0;");

        for (int j = componentTypes.size() - 1; j >= 0; j--) {
            if (isExplicit(getTag(componentTypes.get(j)))) {
                write("int sublength;\n");
                break;
            }
        }

        for (int j = componentTypes.size() - 1; j >= 0; j--) {

            AsnElementType componentType = componentTypes.get(j);

            Tag componentTag = getTag(componentType);

            write("if (" + getName(componentType) + " != null) {");

            String explicitEncoding = getExplicitEncodingParameter(componentType);

            if (isExplicit(componentTag)) {
                write("sublength = " + getName(componentType) + ".encode(os" + explicitEncoding + ");");
                write("codeLength += sublength;");
                write("codeLength += BerLength.encodeLength(os, sublength);");
            }
            else {
                write("codeLength += " + getName(componentType) + ".encode(os" + explicitEncoding + ");");
            }

            if (componentTag != null) {
                writeEncodeTag(componentTag);
            }

            if (hasExplicitTag) {
                write("codeLength += BerLength.encodeLength(os, codeLength);");
                write("if (withTag) {");
                write("codeLength += tag.encode(os);");
                write("}");
            }

            write("return codeLength;");
            write("}");

            write("");

        }

        write("throw new IOException(\"Error encoding BerChoice: No item in choice was selected.\");");

        write("}\n");

    }

    private void writeSequenceOrSetEncodeFunction(List<AsnElementType> componentTypes, boolean hasExplicitTag,
            boolean isSequence) throws IOException {
        write("public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {\n");

        write("if (code != null) {");
        write("for (int i = code.length - 1; i >= 0; i--) {");
        write("os.write(code[i]);");
        write("}");
        write("if (withTag) {");
        write("return tag.encode(os) + code.length;");
        write("}");
        write("return code.length;");
        write("}\n");

        write("int codeLength = 0;");

        for (int j = componentTypes.size() - 1; j >= 0; j--) {
            if (isExplicit(getTag(componentTypes.get(j)))) {
                write("int sublength;\n");
                break;
            }
        }

        for (int j = componentTypes.size() - 1; j >= 0; j--) {

            AsnElementType componentType = componentTypes.get(j);

            Tag componentTag = getTag(componentType);

            if (isOptional(componentType)) {
                write("if (" + getName(componentType) + " != null) {");
            }

            String explicitEncoding = getExplicitEncodingParameter(componentType);

            if (isExplicit(componentTag)) {
                write("sublength = " + getName(componentType) + ".encode(os" + explicitEncoding + ");");
                write("codeLength += sublength;");
                write("codeLength += BerLength.encodeLength(os, sublength);");
            }
            else {
                write("codeLength += " + getName(componentType) + ".encode(os" + explicitEncoding + ");");
            }

            if (componentTag != null) {
                writeEncodeTag(componentTag);
            }
            if (isOptional(componentType)) {
                write("}");
            }

            write("");

        }

        if (hasExplicitTag) {
            write("codeLength += BerLength.encodeLength(os, codeLength);");
            if (isSequence) {
                write("os.write(0x30);");
            }
            else {
                write("os.write(0x31);");
            }
            write("codeLength++;\n");
        }

        write("codeLength += BerLength.encodeLength(os, codeLength);\n");

        write("if (withTag) {");
        write("codeLength += tag.encode(os);");
        write("}\n");

        write("return codeLength;\n");

        write("}\n");
    }

    private void writeSequenceOfEncodeFunction(AsnElementType componentType, boolean hasExplicitTag, boolean isSequence)
            throws IOException {
        write("public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {\n");

        write("if (code != null) {");
        write("for (int i = code.length - 1; i >= 0; i--) {");
        write("os.write(code[i]);");
        write("}");
        write("if (withTag) {");
        write("return tag.encode(os) + code.length;");
        write("}");
        write("return code.length;");
        write("}\n");

        write("int codeLength = 0;");

        write("for (int i = (seqOf.size() - 1); i >= 0; i--) {");

        Tag componentTag = getTag(componentType);
        String explicitEncoding = getExplicitEncodingParameter(componentType);

        if (componentTag != null) {

            if (componentTag.type == TagType.EXPLICIT) {
                write("int sublength = seqOf.get(i).encode(os" + explicitEncoding + ");");
                write("codeLength += sublength;");
                write("codeLength += BerLength.encodeLength(os, sublength);");
            }
            else {
                write("codeLength += seqOf.get(i).encode(os" + explicitEncoding + ");");
            }

            if (componentTag != null) {
                writeEncodeTag(componentTag);
            }
        }
        else {

            if (isDirectAny(componentType) || isDirectChoice(componentType)) {
                write("codeLength += seqOf.get(i).encode(os);");
            }
            else {
                write("codeLength += seqOf.get(i).encode(os, true);");
            }
        }

        write("}\n");

        if (hasExplicitTag) {
            write("codeLength += BerLength.encodeLength(os, codeLength);");
            if (isSequence) {
                write("os.write(0x30);");
            }
            else {
                write("os.write(0x31);");
            }
            write("codeLength++;\n");
        }

        write("codeLength += BerLength.encodeLength(os, codeLength);\n");

        write("if (withTag) {");
        write("codeLength += tag.encode(os);");
        write("}\n");

        write("return codeLength;");
        write("}\n");

    }

    private String getExplicitEncodingParameter(AsnTaggedType componentType) throws IOException {
        Tag tag = getTag(componentType);

        if (tag != null && tag.type == TagType.IMPLICIT) {
            return ", false";
        }
        else {
            if (isDirectAny(componentType) || isDirectChoice(componentType)) {
                return "";
            }
            else {
                return ", true";
            }
        }

    }

    private void writeChoiceDecodeFunction(List<AsnElementType> componentTypes, boolean hasExplicitTag)
            throws IOException {

        if (hasExplicitTag) {
            writeSimpleDecodeFunction("true");

            write("public int decode(InputStream is, boolean withTag) throws IOException {");
            write("int codeLength = 0;");
            write("BerLength length = new BerLength();");
            write("BerTag berTag = new BerTag();\n");

            write("if (withTag) {");
            write("codeLength += tag.decodeAndCheck(is);");
            write("}\n");

            write("codeLength += length.decode(is);");
            write("codeLength += berTag.decode(is);\n");
        }
        else {

            writeSimpleDecodeFunction("null");

            write("public int decode(InputStream is, BerTag berTag) throws IOException {\n");

            write("int codeLength = 0;");
            write("BerTag passedTag = berTag;\n");

            write("if (berTag == null) {");
            write("berTag = new BerTag();");
            write("codeLength += berTag.decode(is);");
            write("}\n");
        }

        String initChoiceDecodeLength = "int ";

        for (int j = 0; j < componentTypes.size(); j++) {
            AsnElementType componentType = componentTypes.get(j);

            String explicitEncoding = getExplicitDecodingParameter(componentType);

            Tag componentTag = getTag(componentType);

            if (componentTag != null) {

                write("if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");

                if (componentTag.type == TagType.EXPLICIT) {
                    if (isDirectAny(componentType)) {
                        write("BerLength length = new BerLength();");
                        write("codeLength += length.decode(is);");
                    }
                    else {
                        write("codeLength += BerLength.skip(is);");
                    }
                }

                write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                write("codeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");

                write("return codeLength;");

                write("}\n");

            }
            else {
                if (isDirectChoice(componentType)) {

                    System.out.println("CHOICE without TAG within another CHOICE: "
                            + getClassNameOfComponent(componentType)
                            + " You could consider integrating the inner CHOICE in the parent CHOICE in order to reduce the number of Java classes/objects.");

                    write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                    write(initChoiceDecodeLength + "choiceDecodeLength = " + getName(componentType) + ".decode(is"
                            + explicitEncoding + ");");
                    initChoiceDecodeLength = "";
                    write("if (choiceDecodeLength != 0) {");
                    write("return codeLength + choiceDecodeLength;");
                    write("}");
                    write("else {");
                    write(getName(componentType) + " = null;");
                    write("}\n");

                }
                else {

                    write("if (berTag.equals(" + getClassNameOfComponent(componentType) + ".tag)) {");

                    write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                    write("codeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");

                    write("return codeLength;");

                    write("}\n");
                }
            }

        }

        if (!hasExplicitTag)

        {
            write("if (passedTag != null) {");
            write("return 0;");
            write("}\n");
        }

        write("throw new IOException(\"Error decoding BerChoice: Tag matched to no item.\");");

        write("}\n");

    }

    private void writeSequenceDecodeFunction(List<AsnElementType> componentTypes, boolean hasExplicitTag)
            throws IOException {
        write("public int decode(InputStream is, boolean withTag) throws IOException {");
        write("int codeLength = 0;");
        write("int subCodeLength = 0;");
        write("BerTag berTag = new BerTag();\n");

        write("if (withTag) {");
        write("codeLength += tag.decodeAndCheck(is);");
        write("}\n");
        write("BerLength length = new BerLength();");
        write("codeLength += length.decode(is);\n");
        write("int totalLength = length.val;");

        if (supportIndefiniteLength == true) {
            writeSequenceDecodeIndefiniteLenghtPart(componentTypes);
        }

        write("codeLength += totalLength;\n");

        if (hasExplicitTag) {
            write("int nextByte = is.read();");
            write("if (nextByte == -1) {");
            write("throw new EOFException(\"Unexpected end of input stream.\");");
            write("}");
            write("if (nextByte != (0x30)) {");
            write("throw new IOException(\"Tag does not match!\");");
            write("}");
            write("length.decode(is);");
            write("totalLength = length.val;\n");
        }

        int lastNoneOptionalFieldIndex = -1;
        for (int j = 0; j < componentTypes.size(); j++) {
            AsnElementType componentType = componentTypes.get(componentTypes.size() - 1 - j);
            if (!isOptional(componentType)) {
                lastNoneOptionalFieldIndex = componentTypes.size() - 1 - j;
                break;
            }
        }

        if (lastNoneOptionalFieldIndex == -1) {
            write("if (totalLength == 0) {");
            write("return codeLength;");
            write("}");
        }

        write("subCodeLength += berTag.decode(is);");

        String initChoiceDecodeLength = "int ";

        for (int j = 0; j < componentTypes.size(); j++) {

            AsnElementType componentType = componentTypes.get(j);
            String explicitEncoding = getExplicitDecodingParameter(componentType);
            Tag componentTag = getTag(componentType);

            if (componentTag != null) {
                write("if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");
                if (isExplicit(componentTag)) {
                    write("subCodeLength += length.decode(is);");
                }

                write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");
                write("subCodeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");

                if (lastNoneOptionalFieldIndex <= j) {
                    write("if (subCodeLength == totalLength) {");
                    write("return codeLength;");
                    write("}");
                }
                if (j != (componentTypes.size() - 1)) {
                    if (!isTaglessAny(componentTypes.get(j + 1))) {
                        write("subCodeLength += berTag.decode(is);");
                    }
                }

                write("}");

                if (j == (componentTypes.size() - 1)) {
                    write("throw new IOException(\"Unexpected end of sequence, length tag: \" + totalLength + \", actual sequence length: \" + subCodeLength);\n");
                }
                else if (!isOptional(componentType)) {
                    write("else {");
                    write("throw new IOException(\"Tag does not match the mandatory sequence element tag.\");");
                    write("}");
                }

            }
            else {

                if (isDirectChoice(componentType)) {

                    write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                    if (isOptional(componentType)) {
                        write(initChoiceDecodeLength + "choiceDecodeLength = " + getName(componentType) + ".decode(is"
                                + explicitEncoding + ");");

                        initChoiceDecodeLength = "";

                        if (j != (componentTypes.size() - 1)) {

                            write("if (choiceDecodeLength != 0) {");
                            write("subCodeLength += choiceDecodeLength;");

                            if (lastNoneOptionalFieldIndex <= j) {
                                write("if (subCodeLength == totalLength) {");
                                write("return codeLength;");
                                write("}");
                            }
                            write("subCodeLength += berTag.decode(is);");
                            write("}");
                            write("else {");
                            write(getName(componentType) + " = null;");
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
                        write("subCodeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");
                        if (j != (componentTypes.size() - 1)) {
                            if (lastNoneOptionalFieldIndex <= j) {
                                write("if (subCodeLength == totalLength) {");
                                write("return codeLength;");
                                write("}");
                            }
                            write("subCodeLength += berTag.decode(is);");
                        }
                        else {
                            write("if (subCodeLength == totalLength) {");
                            write("return codeLength;");
                            write("}");
                        }
                    }

                    if (j == (componentTypes.size() - 1)) {
                        write("throw new IOException(\"Unexpected end of sequence, length tag: \" + totalLength + \", actual sequence length: \" + subCodeLength);\n");
                    }

                }
                else if (isTaglessAny(componentType)) {
                    write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");
                    write("subCodeLength += " + getName(componentType) + ".decode(is, totalLength - subCodeLength);");
                    write("return codeLength;");
                }
                else {

                    write("if (berTag.equals(" + getClassNameOfComponent(componentType) + ".tag)) {");

                    write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                    write("subCodeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");

                    if (lastNoneOptionalFieldIndex <= j) {
                        write("if (subCodeLength == totalLength) {");
                        write("return codeLength;");
                        write("}");
                    }
                    if (j != (componentTypes.size() - 1)) {
                        if (!isTaglessAny(componentTypes.get(j + 1))) {
                            write("subCodeLength += berTag.decode(is);");
                        }
                    }

                    write("}");

                    if (j == (componentTypes.size() - 1)) {
                        write("throw new IOException(\"Unexpected end of sequence, length tag: \" + totalLength + \", actual sequence length: \" + subCodeLength);\n");
                    }
                    else if (!isOptional(componentType)) {
                        write("else {");
                        write("throw new IOException(\"Tag does not match the mandatory sequence element tag.\");");
                        write("}");
                    }

                }
            }

            write("");

        }

        if (componentTypes.isEmpty()) {
            write("return subCodeLength;");
        }
        write("}\n");

    }

    private boolean isTaglessAny(AsnElementType componentType) {
        return (!hasTag(componentType) && isDirectAny(componentType));
    }

    private void writeSequenceOrSetOfDecodeFunction(AsnElementType componentType, String classNameOfSequenceOfElement,
            boolean hasExplicitTag, boolean isSequence) throws IOException {
        write("public int decode(InputStream is, boolean withTag) throws IOException {");
        write("int codeLength = 0;");
        write("int subCodeLength = 0;");
        write("BerTag berTag = new BerTag();");

        write("if (withTag) {");
        write("codeLength += tag.decodeAndCheck(is);");
        write("}\n");

        write("BerLength length = new BerLength();");
        write("codeLength += length.decode(is);");
        write("int totalLength = length.val;\n");

        if (supportIndefiniteLength == true) {
            writeSequenceOfDecodeIndefiniteLenghtPart(componentType, classNameOfSequenceOfElement, hasExplicitTag);
        }

        if (hasExplicitTag) {
            write("int nextByte = is.read();");
            write("if (nextByte == -1) {");
            write("throw new EOFException(\"Unexpected end of input stream.\");");
            write("}");
            if (isSequence) {
                write("if (nextByte != (0x30)) {");
            }
            else {
                write("if (nextByte != (0x31)) {");
            }
            write("throw new IOException(\"Tag does not match!\");");
            write("}");
            write("length.decode(is);");
            write("totalLength = length.val;\n");
        }

        write("while (subCodeLength < totalLength) {");
        write(classNameOfSequenceOfElement + " element = new " + classNameOfSequenceOfElement + "();");

        Tag componentTag = getTag(componentType);

        String explicitEncoding = getExplicitDecodingParameter(componentType);

        if (componentTag != null) {

            if (componentTag.type == TagType.EXPLICIT) {
                write("subCodeLength += berTag.decode(is);");
                write("subCodeLength += length.decode(is);");
                explicitEncoding = ", true";
            }
            else {
                write("subCodeLength += berTag.decode(is);");
                if (isDirectChoice(componentType)) {
                    explicitEncoding = ", berTag";
                }
                else if (isDirectAny(componentType)) {
                    explicitEncoding = "";
                }
                else {
                    explicitEncoding = ", false";
                }
            }
            write("subCodeLength += element.decode(is" + explicitEncoding + ");");
        }
        else {

            if (isDirectChoice(componentType)) {
                write("subCodeLength += element.decode(is, null);");
            }
            else if (isDirectAny(componentType)) {
                write("subCodeLength += new BerTag().decode(is);");
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

    private static String getBerTagParametersString(Tag tag) {
        return "BerTag." + tag.tagClass + "_CLASS, BerTag." + tag.typeStructure.toString() + ", " + tag.value;
    }

    private void writeChoiceToStringFunction(List<AsnElementType> componentTypes) throws IOException {
        write("public String toString() {");

        for (int j = 0; j < componentTypes.size(); j++) {
            AsnElementType componentType = componentTypes.get(j);

            write("if (" + getName(componentType) + " != null) {");
            write("return \"CHOICE{" + getName(componentType) + ": \" + " + getName(componentType) + " + \"}\";");
            write("}\n");

        }

        write("return \"unknown\";");

        write("}\n");

    }

    private String getExplicitDecodingParameter(AsnElementType componentType) throws IOException {

        Tag tag = getTag(componentType);

        String explicitEncoding;

        if (tag != null && tag.type == TagType.EXPLICIT) {
            if (isDirectChoice(componentType)) {
                explicitEncoding = ", null";
            }
            else if (isDirectAny(componentType)) {
                explicitEncoding = ", length.val";
            }
            else {
                explicitEncoding = ", true";
            }
        }
        else {
            if (isDirectChoice(componentType)) {
                explicitEncoding = ", berTag";
            }
            else if (isDirectAny(componentType)) {
                explicitEncoding = ", length.val";
                // throw new IOException("ANY within CHOICE has no tag: " + getClassNameOfComponent(componentType));
            }
            else {
                explicitEncoding = ", false";
            }
        }
        return explicitEncoding;
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

    private void writeSequenceOfDecodeIndefiniteLenghtPart(AsnElementType componentType,
            String classNameOfSequenceOfElement, boolean isTagExplicit) throws IOException {
        write("if (length.val == -1) {");
        write("while (true) {");
        write("subCodeLength += berTag.decode(is);\n");

        write("if (berTag.tagNumber == 0 && berTag.tagClass == 0 && berTag.primitive == 0) {");
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

        write(classNameOfSequenceOfElement + " element = new " + classNameOfSequenceOfElement + "();");

        if (isDirectChoice(componentType)) {
            write("subCodeLength += element.decode(is, berTag);");
        }
        else if (isDirectAny(componentType)) {
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

    private void addAutomaticTagsIfNeeded(List<AsnElementType> componentTypes) {
        if (tagDefault != TagDefault.AUTOMATIC) {
            return;
        }
        for (AsnElementType element : componentTypes) {
            if (getTag(element) != null) {
                return;
            }
        }
        int i = 0;
        for (AsnElementType element : componentTypes) {
            element.tag = new AsnTag();
            element.tag.classNumber = new AsnClassNumber();
            element.tag.classNumber.num = i;
            i++;
        }

    }

    private void writeEncodeAndSaveFunction() throws IOException {
        writeEncodeAndSaveFunction(false);
    }

    private void writeEncodeAndSaveFunction(boolean isTagless) throws IOException {
        write("public void encodeAndSave(int encodingSizeGuess) throws IOException {");
        write("BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);");
        if (isTagless) {
            write("encode(os);");
        }
        else {
            write("encode(os, false);");
        }
        write("code = os.getArray();");
        write("}\n");
    }

    private void writeSetDecodeFunction(List<AsnElementType> componentTypes) throws IOException {
        write("public int decode(InputStream is, boolean withTag) throws IOException {");
        write("int codeLength = 0;");
        write("int subCodeLength = 0;");
        write("BerTag berTag = new BerTag();\n");

        write("if (withTag) {");
        write("codeLength += tag.decodeAndCheck(is);");
        write("}\n");
        write("BerLength length = new BerLength();");
        write("codeLength += length.decode(is);\n");
        write("int totalLength = length.val;");

        write("while (subCodeLength < totalLength) {");
        write("subCodeLength += berTag.decode(is);");

        for (int j = 0; j < componentTypes.size(); j++) {
            AsnElementType componentType = componentTypes.get(j);

            Tag componentTag = getTag(componentType);

            String explicitEncoding = ", false";

            String elseString = "";

            if (j != 0) {
                elseString = "else ";
            }

            if (isDirectChoice(componentType)) {

                if (!isExplicit(componentTag)) {
                    throw new IOException("choice within set has no explict tag.");
                }
                write(elseString + "if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");

                write("subCodeLength += new BerLength().decode(is);");
                explicitEncoding = ", null";

                write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                write("subCodeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");

                write("}");

            }
            else {

                if (componentTag != null) {
                    if (isExplicit(componentTag) || !isPrimitive(componentType)) {
                        write(elseString + "if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");
                    }
                    else {
                        write(elseString + "if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");
                    }
                    if (isExplicit(componentTag)) {
                        write("subCodeLength += new BerLength().decode(is);");
                        if (isDirectAny(componentType)) {
                            explicitEncoding = ", length.val";
                        }
                        else {
                            explicitEncoding = ", true";
                        }
                    }
                }
                else {
                    write(elseString + "if (berTag.equals(" + getClassNameOfComponent(componentType) + ".tag)) {");
                }

                write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                if (", null".equals(explicitEncoding)) {
                    write("BerLength length2 = new BerLength();");
                    write("subCodeLength += length2.decode(is);");
                }

                write("subCodeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");

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

    private void writeSequenceDecodeIndefiniteLenghtPart(List<AsnElementType> componentTypes) throws IOException {
        write("if (totalLength == -1) {");
        write("subCodeLength += berTag.decode(is);\n");

        String initChoiceDecodeLength = "int ";

        for (AsnElementType componentType : componentTypes) {

            Tag componentTag = getTag(componentType);

            write("if (berTag.tagNumber == 0 && berTag.tagClass == 0 && berTag.primitive == 0) {");
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

            if (isDirectChoice(componentType)) {
                if (isExplicit(componentTag)) {
                    write("if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");

                    write("subCodeLength += length.decode(is);");
                    explicitEncoding = "null";
                }
                else {
                    explicitEncoding = "berTag";
                }

                write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                write(initChoiceDecodeLength + "choiceDecodeLength = " + getName(componentType) + ".decode(is, "
                        + explicitEncoding + ");");
                if (!isExplicit(componentTag)) {
                    initChoiceDecodeLength = "";
                }
                write("if (choiceDecodeLength != 0) {");
                write("subCodeLength += choiceDecodeLength;");

                write("subCodeLength += berTag.decode(is);");
                write("}");
                write("else {");
                write(getName(componentType) + " = null;");
                write("}\n");

                if (isExplicit(componentTag)) {
                    write("}");
                }

            }
            else {

                explicitEncoding = ", false";

                if (componentTag != null) {
                    if (isExplicit(componentTag) || !isPrimitive(componentType)) {
                        if (getUniversalType(componentType) instanceof AsnAnyNoDecode) {
                            write("if (berTag.tagNumber == " + componentTag.value + ") {");
                        }
                        else {
                            write("if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");
                        }
                    }
                    else {
                        write("if (berTag.equals(" + getBerTagParametersString(componentTag) + ")) {");
                    }

                    if (isExplicit(componentTag)) {
                        write("codeLength += length.decode(is);");
                        if (isDirectAny(componentType)) {
                            explicitEncoding = ", length.val";
                        }
                        else {
                            explicitEncoding = ", true";
                        }
                    }
                }
                else {
                    if (!isDirectAny(componentType)) {
                        write("if (berTag.equals(" + getClassNameOfComponent(componentType) + ".tag)) {");
                    }
                }

                write(getName(componentType) + " = new " + getClassNameOfComponent(componentType) + "();");

                if (isDirectAny(componentType)) {
                    write("subCodeLength += " + getName(componentType) + ".decode(is, length.val);");

                }
                else {
                    write("subCodeLength += " + getName(componentType) + ".decode(is" + explicitEncoding + ");");
                }

                write("subCodeLength += berTag.decode(is);");

                if (componentTag != null || !isDirectAny(componentType)) {
                    write("}");
                }

            }

            // TODO has be reinserted:
            // if (!isOptional(componentType) && ((!(getUniversalType(componentType) instanceof AsnChoice))
            // || hasExplicitTag(componentType))) {
            // write("else {");
            // write("throw new IOException(\"Tag does not match required sequence element identifer.\");");
            // write("}");
            // }

        }

        write("int nextByte = is.read();");
        write("if (berTag.tagNumber != 0 || berTag.tagClass != 0 || berTag.primitive != 0");
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

    private void writeSequenceOrSetToStringFunction(List<AsnElementType> componentTypes) throws IOException {
        write("public String toString() {");

        write("StringBuilder sb = new StringBuilder(\"SEQUENCE{\");");

        boolean checkIfFirstSelectedElement = true;

        int j = 0;

        for (AsnElementType componentType : componentTypes) {

            if (isOptional(componentType)) {
                if (j == 0) {
                    write("boolean firstSelectedElement = true;");
                }
                write("if (" + getName(componentType) + " != null) {");
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

            write("sb.append(\"" + getName(componentType) + ": \").append(" + getName(componentType) + ");");

            if (isOptional(componentType)) {
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

    private void writeEncodeTag(Tag tag) throws IOException {
        int typeStructure;

        if (tag.typeStructure == TypeStructure.CONSTRUCTED) {
            typeStructure = BerTag.CONSTRUCTED;
        }
        else {
            typeStructure = BerTag.PRIMITIVE;
        }

        BerTag berTag = new BerTag(getTagClassId(tag.tagClass.toString()), typeStructure, tag.value);

        write("// write tag: " + tag.tagClass + "_CLASS, " + tag.typeStructure + ", " + tag.value);
        for (int i = (berTag.tagBytes.length - 1); i >= 0; i--) {
            write("os.write(" + HexConverter.toHexString(berTag.tagBytes[i]) + ");");
        }

        write("codeLength += " + berTag.tagBytes.length + ";");

    }

    private int getTagClassId(String tagClass) {

        if (tagClass.equals("UNIVERSAL")) {
            return BerTag.UNIVERSAL_CLASS;
        }
        else if (tagClass.equals("APPLICATION")) {
            return BerTag.APPLICATION_CLASS;
        }
        else if (tagClass.equals("CONTEXT")) {
            return BerTag.CONTEXT_CLASS;
        }
        else if (tagClass.equals("PRIVATE")) {
            return BerTag.PRIVATE_CLASS;
        }
        else {
            throw new IllegalStateException("unknown tag class: " + tagClass);
        }

    }

    private boolean isPrimitiveOrRetaggedPrimitive(AsnType asnType) throws IOException {
        return isPrimitiveOrRetaggedPrimitive(asnType, module);
    }

    private boolean isPrimitiveOrRetaggedPrimitive(AsnType asnType, AsnModule module) throws IOException {

        if ((asnType instanceof AsnSequenceSet) || (asnType instanceof AsnSequenceOf)) {
            return false;
        }
        else if (asnType instanceof AsnTaggedType) {
            AsnTaggedType asnTaggedType = (AsnTaggedType) asnType;

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

    private boolean isPrimitive(AsnType asnType) {
        return isPrimitive(asnType, module);
    }

    private boolean isPrimitive(AsnType asnType, AsnModule module) {

        if ((asnType instanceof AsnSequenceSet) || (asnType instanceof AsnSequenceOf)) {
            return false;
        }
        else if (asnType instanceof AsnTaggedType) {
            AsnTaggedType asnTaggedType = (AsnTaggedType) asnType;

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

    private boolean isPrimitive(String typeName, AsnModule module) {

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

        if (asnType instanceof AsnTaggedType) {
            AsnTaggedType asnTaggedType = (AsnTaggedType) asnType;
            if (asnTaggedType.typeReference != null) {
                return (AsnUniversalType) asnTaggedType.typeReference;
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

    private String getName(AsnElementType componentType) {
        return cleanUpName(componentType.name);
    }

    private boolean isOptional(AsnElementType componentType) {
        return (componentType.isOptional || componentType.isDefault);
    }

    private boolean isExplicit(Tag tag) {
        return (tag != null) && (tag.type == TagType.EXPLICIT);
    }

    // TODO remove:
    private boolean hasExplicitTag(AsnTaggedType taggedType) {
        if (!hasTag(taggedType)) {
            return false;
        }
        if (isDirectChoice(taggedType) || isDirectAny(taggedType)) {
            return true;
        }

        return isExplicit(taggedType.tagType);

    }

    private boolean isExplicit(String tagType) {
        if (tagType.isEmpty()) {
            return (tagDefault == TagDefault.EXPLICIT);
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

    private boolean hasTag(AsnTaggedType taggedType) {
        return taggedType.tag != null;
    }

    private void writeEncodeConstructor(String className, List<AsnElementType> componentTypes) throws IOException {

        if (componentTypes.isEmpty()) {
            return;
        }

        String line = "public " + className + "(";

        int j = 0;

        for (AsnElementType componentType : componentTypes) {

            if (j != 0) {
                line += ", ";
            }
            j++;
            line += (getClassNameOfComponent(componentType) + " " + cleanUpName(componentType.name));

        }

        write(line + ") {");

        for (AsnElementType componentType : componentTypes) {

            String elementName = cleanUpName(componentType.name);

            write("this." + elementName + " = " + elementName + ";");

        }

        write("}\n");
    }

    private void writeEmptyConstructor(String className) throws IOException {
        write("public " + className + "() {");
        write("}\n");

        write("public " + className + "(byte[] code) {");
        write("this.code = code;");
        write("}\n");
    }

    private void writePublicMembers(List<AsnElementType> componentTypes) throws IOException {
        for (AsnElementType element : componentTypes) {
            if (jaxbMode) {
                write("private " + getClassNameOfComponent(element) + " " + cleanUpName(element.name) + " = null;");
            }
            else {
                write("public " + getClassNameOfComponent(element) + " " + cleanUpName(element.name) + " = null;");
            }
        }
        write("");
    }

    private void writeGetterAndSetter(List<AsnElementType> componentTypes) throws IOException {
        for (AsnElementType element : componentTypes) {
            String typeName = getClassNameOfComponent(element);
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

    private String getClassNameOfSequenceOfElement(String className, AsnElementType componentType) throws IOException {
        String classNameOfSequenceElement = getClassNameOfSequenceOfElement(componentType);
        if (classNameOfSequenceElement.equals(className)) {
            return basePackageName + module.moduleIdentifier.name.replace('-', '.').toLowerCase() + "."
                    + classNameOfSequenceElement;
        }
        return classNameOfSequenceElement;

    }

    private String getClassNameOfSequenceOfElement(AsnElementType componentType) throws IOException {
        if (componentType.typeReference == null) {
            return cleanUpName(componentType.typeName);
        }
        else {
            AsnType typeDefinition = (AsnType) componentType.typeReference;
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
                if (((AsnSequenceOf) typeDefinition).isSequenceOf) {
                    subClassName = "SEQUENCEOF";
                }
                else {
                    subClassName = "SETOF";
                }

            }
            else {
                subClassName = "CHOICE";
            }

            return subClassName;

        }
        return getBerType(typeDefinition);
    }

    private String getClassNameOfComponent(AsnElementType asnElementType) throws IOException {

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

    private String[] getConstructorParameters(AsnUniversalType typeDefinition) throws IOException {

        if (typeDefinition instanceof AsnInteger || typeDefinition instanceof AsnEnum) {
            return new String[] { "BigInteger", "value" };
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
            return new String[] { "byte[]", "value" };
        }
        else if (typeDefinition instanceof AsnNull) {
            return new String[0];
        }
        else if ((typeDefinition instanceof AsnSequenceSet) || (typeDefinition instanceof AsnChoice)) {
            return getConstructorParametersFromConstructedElement((AsnConstructedType) typeDefinition);
        }
        else if (typeDefinition instanceof AsnSequenceOf) {
            return new String[] {
                    "List<" + getClassNameOfSequenceOfElement(((AsnSequenceOf) typeDefinition).componentType) + ">",
                    "seqOf" };
        }
        else if (typeDefinition instanceof AsnAny || typeDefinition instanceof AsnAnyNoDecode) {
            return new String[] { "byte[]", "value" };
        }
        else {
            throw new IllegalStateException("type of unknown class: " + typeDefinition.name);
        }

    }

    private String[] getConstructorParametersFromConstructedElement(AsnConstructedType assignedTypeDefinition)
            throws IOException {

        List<AsnElementType> componentTypes;

        if (assignedTypeDefinition instanceof AsnSequenceSet) {

            componentTypes = ((AsnSequenceSet) assignedTypeDefinition).componentTypes;
        }
        else {
            componentTypes = ((AsnChoice) assignedTypeDefinition).componentTypes;
        }

        String[] constructorParameters = new String[componentTypes.size() * 2];

        for (int j = 0; j < componentTypes.size(); j++) {
            AsnElementType componentType = componentTypes.get(j);

            constructorParameters[j * 2] = getClassNameOfComponent(componentType);
            constructorParameters[j * 2 + 1] = cleanUpName(componentType.name);

        }
        return constructorParameters;
    }

    private boolean isDirectChoice(AsnTaggedType taggedType) {

        if ((!taggedType.typeName.isEmpty() && isDirectChoice(taggedType.typeName, module))
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

    private boolean isDirectAny(AsnTaggedType taggedType) {
        if ((!taggedType.typeName.isEmpty() && isDirectAny(taggedType.typeName, module))
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

    private void writeClassHeader(String typeName) throws IOException {
        FileWriter fstream = new FileWriter(new File(outputDirectory, typeName + ".java"));
        out = new BufferedWriter(fstream);

        write("/**");
        write(" * This class file was automatically generated by jASN1 v" + Compiler.VERSION
                + " (http://www.openmuc.org)\n */\n");
        write("package " + basePackageName + module.moduleIdentifier.name.replace('-', '.').toLowerCase() + ";\n");

        write("import java.io.IOException;");
        write("import java.io.EOFException;");
        write("import java.io.InputStream;");
        write("import java.util.List;");
        write("import java.util.ArrayList;");
        write("import java.util.Iterator;");
        write("import java.io.UnsupportedEncodingException;");
        write("import java.math.BigInteger;");

        write("import org.openmuc.jasn1.ber.*;");
        write("import org.openmuc.jasn1.ber.types.*;");
        write("import org.openmuc.jasn1.ber.types.string.*;\n");

        List<String> modulePackages = new ArrayList<>();
        for (AsnModule module : modulesByName.values()) {
            if (module != this.module) {
                modulePackages.add(module.moduleIdentifier.name.replace('-', '.').toLowerCase());
            }
        }
        Collections.sort(modulePackages);
        for (String modulePackage : modulePackages) {
            write("import " + basePackageName + modulePackage + ".*;");
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
