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
import org.openmuc.jasn1.compiler.model.AsnConstructedType;
import org.openmuc.jasn1.compiler.model.AsnDefinedType;
import org.openmuc.jasn1.compiler.model.AsnElementType;
import org.openmuc.jasn1.compiler.model.AsnEnum;
import org.openmuc.jasn1.compiler.model.AsnInteger;
import org.openmuc.jasn1.compiler.model.AsnModule;
import org.openmuc.jasn1.compiler.model.AsnNull;
import org.openmuc.jasn1.compiler.model.AsnObjectIdentifier;
import org.openmuc.jasn1.compiler.model.AsnOctetString;
import org.openmuc.jasn1.compiler.model.AsnReal;
import org.openmuc.jasn1.compiler.model.AsnSequenceOf;
import org.openmuc.jasn1.compiler.model.AsnSequenceSet;
import org.openmuc.jasn1.compiler.model.AsnTaggedType;
import org.openmuc.jasn1.compiler.model.AsnType;
import org.openmuc.jasn1.compiler.model.AsnTypes;
import org.openmuc.jasn1.compiler.model.AsnUniversalType;
import org.openmuc.jasn1.compiler.model.SymbolsFromModule;

public class BerClassWriter {

	private final String outputBaseDir;
	private final String basePackageName;
	private int indentNum = 0;
	BufferedWriter out;
	private boolean isDefaultTagExplicit = true;
	private boolean supportIndefiniteLength = false;
	private final HashMap<String, AsnModule> modulesByName;
	private AsnModule module;
	private File outputDirectory;

	BerClassWriter(HashMap<String, AsnModule> modulesByName, String outputBaseDir, String basePackageName,
			boolean supportIndefiniteLength) throws IOException {
		this.supportIndefiniteLength = supportIndefiniteLength;
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

		AsnTypes asnTypes = module.asnTypes;

		if (module.tagDefault.equals("IMPLICIT")) {
			isDefaultTagExplicit = false;
		}

		for (AsnType typeDefinition : asnTypes.typesByName.values()) {

			String typeName = cleanUpName(typeDefinition.name);

			writeClassHeader(typeName);

			if (typeDefinition instanceof AsnTaggedType) {

				AsnTaggedType asnTaggedType = (AsnTaggedType) typeDefinition;

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
		name = name.replace('-', '_');
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

		List<AsnElementType> sequenceElements = asn1TypeElement.elementTypeList.elements;

		for (AsnElementType sequenceElementType : sequenceElements) {

			if (sequenceElementType.typeReference != null
					&& (sequenceElementType.typeReference instanceof AsnConstructedType)) {

				String subClassName = getClassNameOfStructureElement(sequenceElementType);
				writeConstructedTypeClass((AsnType) sequenceElementType.typeReference, "", "", subClassName, true);

			}
		}

		writePublicMembers(out, sequenceElements);

		writeEmptyConstructor(className, true);

		writeEncodeConstructor(className, sequenceElements, true);

		writeChoiceEncodeFunction(sequenceElements);

		writeChoiceDecodeFunction(sequenceElements);

		writeEncodeAndSaveFunction();

		writeChoiceToStringFunction(sequenceElements);

		write("}\n");

	}

	private void writeChoiceToStringFunction(List<AsnElementType> sequenceElements) throws IOException {
		write("public String toString() {");

		for (int j = 0; j < sequenceElements.size(); j++) {
			AsnElementType sequenceElement = sequenceElements.get(j);

			write("if ( " + getSequenceElementName(sequenceElement) + "!= null) {");
			write("return \"CHOICE{" + getSequenceElementName(sequenceElement) + ": \" + "
					+ getSequenceElementName(sequenceElement) + " + \"}\";");
			write("}\n");

		}

		write("return \"unknown\";");

		write("}\n");

	}

	private void writeChoiceDecodeFunction(List<AsnElementType> sequenceElements) throws IOException {
		write("public int decode(InputStream is, BerIdentifier berIdentifier) throws IOException {");
		write("int codeLength = 0;");
		write("BerIdentifier passedIdentifier = berIdentifier;\n");

		write("if (berIdentifier == null) {");
		write("berIdentifier = new BerIdentifier();");
		write("codeLength += berIdentifier.decode(is);");
		write("}");

		String initChoiceDecodeLength = "int ";

		for (int j = 0; j < sequenceElements.size(); j++) {
			AsnElementType sequenceElement = sequenceElements.get(j);

			String explicitEncoding = "false";
			if (hasTag(sequenceElement)) {

				if (getUniversalType(sequenceElement) instanceof AsnChoice) {

					if (hasExplicitTag(sequenceElement)) {
						write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
								+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

						write("codeLength += new BerLength().decode(is);");
						explicitEncoding = "null";
					}
					else {
						throw new IOException("Element \"" + getSequenceElementName(sequenceElement)
								+ " is a CHOICE and has an implicit tag\"");
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

					if ((getUniversalType(sequenceElement) instanceof AsnAny)
							|| (getUniversalType(sequenceElement) instanceof AsnAnyNoDecode)) {
						if (!hasExplicitTag(sequenceElement)) {
							throw new IOException(
									"ANY within CHOICE has no tag: " + getClassNameOfStructureElement(sequenceElement));
						}
					}
					else if (hasExplicitTag(sequenceElement)) {

						write("codeLength += new BerLength().decode(is);");
						explicitEncoding = "true";
					}

				}

				write(getSequenceElementName(sequenceElement) + " = new "
						+ getClassNameOfStructureElement(sequenceElement) + "();");

				write("codeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, " + explicitEncoding
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

					explicitEncoding = "berIdentifier";

					write(getSequenceElementName(sequenceElement) + " = new "
							+ getClassNameOfStructureElement(sequenceElement) + "();");

					write(initChoiceDecodeLength + "choiceDecodeLength = " + getSequenceElementName(sequenceElement)
							+ ".decode(is, " + explicitEncoding + ");");
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

					write("codeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, " + explicitEncoding
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

	private void writeChoiceEncodeFunction(List<AsnElementType> choiceElements) throws IOException {
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

			AsnElementType choiceElement = choiceElements.get(j);

			write("if (" + getSequenceElementName(choiceElement) + " != null) {");

			String explicitEncoding = "true";
			if (hasImplicitTag(choiceElement)) {
				explicitEncoding = "false";
			}

			if (hasExplicitTag(choiceElement)) {
				write("sublength = " + getSequenceElementName(choiceElement) + ".encode(os, " + "true" + ");");
				write("codeLength += sublength;");
				write("codeLength += BerLength.encodeLength(os, sublength);");
			}
			else {
				write("codeLength += " + getSequenceElementName(choiceElement) + ".encode(os, " + explicitEncoding
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

		AsnType subElementRef = (AsnType) asnSequenceOf.typeReference;

		// Element subElementRef = (Element) xPath.evaluate("typeReference", asnSequenceOf, XPathConstants.NODE);

		String referencedTypeName = getClassNameOfSequenceOfElement(asnSequenceOf);

		if (subElementRef != null) {
			writeConstructedTypeClass(subElementRef, "", "", referencedTypeName, true);
		}

		write("public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
				+ ", BerIdentifier.CONSTRUCTED, " + tagNum + ");");
		write("protected BerIdentifier id;\n");

		write("public byte[] code = null;");

		write("public List<" + referencedTypeName + "> seqOf = null;\n");

		write("public " + className + "() {");
		write("id = identifier;");
		write("seqOf = new ArrayList<" + referencedTypeName + ">();");
		write("}\n");

		write("public " + className + "(byte[] code) {");
		write("id = identifier;");
		write("this.code = code;");
		write("}\n");

		write("public " + className + "(List<" + referencedTypeName + "> seqOf) {");
		write("id = identifier;");
		write("this.seqOf = seqOf;");
		write("}\n");

		writeSequenceOfEncodeFunction();

		writeSequenceOfDecodeFunction(asnSequenceOf, referencedTypeName);

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

	private void writeSequenceOfDecodeFunction(AsnSequenceOf element, String referencedTypeName) throws IOException {
		write("public int decode(InputStream is, boolean explicit) throws IOException {");
		write("int codeLength = 0;");
		write("int subCodeLength = 0;");

		write("if (explicit) {");
		write("codeLength += id.decodeAndCheck(is);");
		write("}\n");

		write("BerLength length = new BerLength();");
		write("codeLength += length.decode(is);\n");

		// indefinite length
		if (supportIndefiniteLength == true) {

			write("if (length.val == -1) {");
			write("BerIdentifier berIdentifier = new BerIdentifier();");
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

			write(referencedTypeName + " element = new " + referencedTypeName + "();");

			AsnType asnType;

			if (element.typeReference != null) {
				asnType = getUniversalType((AsnType) (element.typeReference));
			}
			else {
				asnType = getUniversalType(element.typeName);
			}

			if (asnType instanceof AsnChoice) {
				write("subCodeLength += element.decode(is, berIdentifier);");
			}
			else {
				write("subCodeLength += element.decode(is, false);");
			}
			write("seqOf.add(element);");
			write("}");

			write("}");

		}

		// definite length
		write("while (subCodeLength < length.val) {");
		write(getClassNameOfSequenceOfElement(element) + " element = new " + getClassNameOfSequenceOfElement(element)
				+ "();");

		AsnType asnType;

		if (element.typeReference != null) {
			asnType = getUniversalType((AsnType) (element.typeReference));
		}
		else {
			asnType = getUniversalType(getTypeDefinition(element.typeName));
		}

		if (asnType instanceof AsnChoice) {
			write("subCodeLength += element.decode(is, null);");
		}
		else {
			write("subCodeLength += element.decode(is, true);");
		}
		write("seqOf.add(element);");
		write("}");
		write("if (subCodeLength != length.val) {");
		write("throw new IOException(\"Decoded SequenceOf or SetOf has wrong length tag\");\n");
		write("}");
		write("codeLength += subCodeLength;\n");

		write("return codeLength;");
		write("}\n");

	}

	private void writeSequenceOfEncodeFunction() throws IOException {
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
		write("codeLength += seqOf.get(i).encode(os, true);");
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

		List<AsnElementType> sequenceElements = asnSequenceSet.elementTypeList.elements;

		for (AsnElementType sequenceElementType : sequenceElements) {

			if (sequenceElementType.typeReference != null
					&& (sequenceElementType.typeReference instanceof AsnConstructedType)) {

				String subClassName = getClassNameOfStructureElement(sequenceElementType);
				writeConstructedTypeClass((AsnType) sequenceElementType.typeReference, "", "", subClassName, true);

			}

		}

		write("public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
				+ ", BerIdentifier.CONSTRUCTED, " + tagNum + ");");

		write("protected BerIdentifier id;\n");

		write("public byte[] code = null;");

		writePublicMembers(out, sequenceElements);

		writeEmptyConstructor(className);

		writeEncodeConstructor(className, sequenceElements);

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

	private void writeEncodeAndSaveFunction() throws IOException {
		write("public void encodeAndSave(int encodingSizeGuess) throws IOException {");
		write("BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);");
		write("encode(os, false);");
		write("code = os.getArray();");
		write("}\n");
	}

	private void writeSetDecodeFunction(List<AsnElementType> sequenceElements) throws IOException {
		write("public int decode(InputStream is, boolean explicit) throws IOException {");
		write("int codeLength = 0;");
		write("int subCodeLength = 0;");
		write("BerIdentifier berIdentifier = new BerIdentifier();\n");

		write("if (explicit) {");
		write("codeLength += id.decodeAndCheck(is);");
		write("}\n");
		write("BerLength length = new BerLength();");
		write("codeLength += length.decode(is);\n");

		write("while (subCodeLength < length.val) {");
		write("subCodeLength += berIdentifier.decode(is);");

		for (int j = 0; j < sequenceElements.size(); j++) {
			AsnElementType sequenceElement = sequenceElements.get(j);

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

		write("if (subCodeLength != length.val) {");
		write("throw new IOException(\"Length of set does not match length tag, length tag: \" + length.val + \", actual set length: \" + subCodeLength);\n");
		write("}");
		write("codeLength += subCodeLength;\n");

		write("return codeLength;");
		write("}\n");
	}

	private void writeSequenceDecodeFunction(List<AsnElementType> sequenceElements) throws IOException {
		write("public int decode(InputStream is, boolean explicit) throws IOException {");
		write("int codeLength = 0;");
		write("int subCodeLength = 0;");
		write("BerIdentifier berIdentifier = new BerIdentifier();\n");

		write("if (explicit) {");
		write("codeLength += id.decodeAndCheck(is);");
		write("}\n");
		write("BerLength length = new BerLength();");
		write("codeLength += length.decode(is);\n");
		write("codeLength += length.val;\n");

		// indefinite length

		if (supportIndefiniteLength == true) {

			write("if (length.val == -1) {");
			write("subCodeLength += berIdentifier.decode(is);\n");

			String initChoiceDecodeLength = "int ";

			for (AsnElementType sequenceElement : sequenceElements) {

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

						write("subCodeLength += new BerLength().decode(is);");
						explicitEncoding = "null";
					}
					else {
						explicitEncoding = "berIdentifier";
					}

					write(getSequenceElementName(sequenceElement) + " = new "
							+ getClassNameOfStructureElement(sequenceElement) + "();");

					write(initChoiceDecodeLength + "choiceDecodeLength = " + getSequenceElementName(sequenceElement)
							+ ".decode(is, " + explicitEncoding + ");");
					initChoiceDecodeLength = "";
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

					explicitEncoding = "false";

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

						if ((getUniversalType(sequenceElement) instanceof AsnAny)
								|| (getUniversalType(sequenceElement) instanceof AsnAnyNoDecode)) {
							if (!hasExplicitTag(sequenceElement)) {
								throw new IOException("ANY within SEQUENCE has no tag: "
										+ getClassNameOfStructureElement(sequenceElement));
							}
						}
						else if (hasExplicitTag(sequenceElement)) {
							write("subCodeLength += new BerLength().decode(is);");
							explicitEncoding = "true";
						}
					}
					else {
						write("if (berIdentifier.equals(" + getClassNameOfStructureElement(sequenceElement)
								+ ".identifier)) {");
					}

					write(getSequenceElementName(sequenceElement) + " = new "
							+ getClassNameOfStructureElement(sequenceElement) + "();");

					write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, "
							+ explicitEncoding + ");");

					write("subCodeLength += berIdentifier.decode(is);");

					write("}");

				}

				if (!isOptional(sequenceElement) && ((!(getUniversalType(sequenceElement) instanceof AsnChoice))
						|| hasExplicitTag(sequenceElement))) {
					write("else {");
					write("throw new IOException(\"Identifier does not match required sequence element identifer.\");");
					write("}");
				}

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
			AsnElementType sequenceElement = sequenceElements.get(sequenceElements.size() - 1 - j);
			if (!isOptional(sequenceElement)) {
				lastNoneOptionalFieldIndex = sequenceElements.size() - 1 - j;
				break;
			}
		}

		if (lastNoneOptionalFieldIndex == -1) {
			write("if (length.val == 0) {");
			write("return codeLength;");
			write("}");
		}

		write("subCodeLength += berIdentifier.decode(is);");

		String initChoiceDecodeLength = "int ";

		for (int j = 0; j < sequenceElements.size(); j++) {
			AsnElementType sequenceElement = sequenceElements.get(j);

			String explicitEncoding;

			if (getUniversalType(sequenceElement) instanceof AsnChoice) {
				if (hasExplicitTag(sequenceElement)) {
					write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

					write("subCodeLength += new BerLength().decode(is);");
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
							write("if (subCodeLength == length.val) {");
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
						write("if (subCodeLength == length.val) {");
						write("return codeLength;");
						write("}");
					}
				}
				else {
					write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, "
							+ explicitEncoding + ");");
					if (j != (sequenceElements.size() - 1)) {
						if (lastNoneOptionalFieldIndex <= j) {
							write("if (subCodeLength == length.val) {");
							write("return codeLength;");
							write("}");
						}
						write("subCodeLength += berIdentifier.decode(is);");
					}
					else {
						write("if (subCodeLength == length.val) {");
						write("return codeLength;");
						write("}");
					}
				}

				if (hasExplicitTag(sequenceElement)) {
					write("}");
				}

				if (j == (sequenceElements.size() - 1)) {
					write("throw new IOException(\"Unexpected end of sequence, length tag: \" + length.val + \", actual sequence length: \" + subCodeLength);\n");
				}
				else if (hasExplicitTag(sequenceElement) && !isOptional(sequenceElement)) {
					write("else {");
					write("throw new IOException(\"Identifier does not match required sequence element identifer.\");");
					write("}");
				}

			}
			else {

				explicitEncoding = "false";

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

					if (hasExplicitTag(sequenceElement) && !(getUniversalType(sequenceElement) instanceof AsnAny)
							&& !(getUniversalType(sequenceElement) instanceof AsnAnyNoDecode)) {
						write("subCodeLength += new BerLength().decode(is);");
						explicitEncoding = "true";
					}
				}
				else {
					write("if (berIdentifier.equals(" + getClassNameOfStructureElement(sequenceElement)
							+ ".identifier)) {");
				}

				write(getSequenceElementName(sequenceElement) + " = new "
						+ getClassNameOfStructureElement(sequenceElement) + "();");

				write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(is, " + explicitEncoding
						+ ");");

				if (lastNoneOptionalFieldIndex <= j) {
					write("if (subCodeLength == length.val) {");
					write("return codeLength;");
					write("}");
				}
				if (j != (sequenceElements.size() - 1)) {
					write("subCodeLength += berIdentifier.decode(is);");
				}

				write("}");

				if (j == (sequenceElements.size() - 1)) {
					write("throw new IOException(\"Unexpected end of sequence, length tag: \" + length.val + \", actual sequence length: \" + subCodeLength);\n");
				}
				else if (!isOptional(sequenceElement)) {
					write("else {");
					write("throw new IOException(\"Identifier does not match the mandatory sequence element identifer.\");");
					write("}");
				}

			}

			write("");

		}

		write("}\n");

	}

	private void writeSequenceOrSetToStringFunction(List<AsnElementType> sequenceElements) throws IOException {
		write("public String toString() {");

		write("StringBuilder sb = new StringBuilder(\"SEQUENCE{\");");

		// boolean firstSelectedElementInitialized = false;

		boolean checkIfFirstSelectedElement = true;

		int j = 0;

		for (AsnElementType sequenceElement : sequenceElements) {

			if (isOptional(sequenceElement)) {
				if (j == 0) {
					write("boolean firstSelectedElement = true;");
					// firstSelectedElementInitialized = true;
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

	private void writeSequenceOrSetEncodeFunction(List<AsnElementType> sequenceElements) throws IOException {
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

			AsnElementType sequenceElement = sequenceElements.get(j);

			if (isOptional(sequenceElement)) {
				write("if (" + getSequenceElementName(sequenceElement) + " != null) {");
			}

			String explicitEncoding = "true";
			if (hasImplicitTag(sequenceElement)) {
				explicitEncoding = "false";
			}

			if (hasExplicitTag(sequenceElement)) {
				write("sublength = " + getSequenceElementName(sequenceElement) + ".encode(os, " + explicitEncoding
						+ ");");
				write("codeLength += sublength;");
				write("codeLength += BerLength.encodeLength(os, sublength);");
			}
			else {
				write("codeLength += " + getSequenceElementName(sequenceElement) + ".encode(os, " + explicitEncoding
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

		write("// write tag {" + tagClass + ", " + cpString + ", " + tagNum + "}");
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

	private boolean isPrimitive(AsnType asnType) throws IOException {
		return isPrimitive(asnType, module);
	}

	/**
	 * Is called to find out if an implicit TAG should be CONSTRUCTED or PRIMITIVE. Is therefore never called choices.
	 */
	private boolean isPrimitive(AsnType asnType, AsnModule module) throws IOException {

		if ((asnType instanceof AsnSequenceSet) || (asnType instanceof AsnSequenceOf)) {
			return false;
		}
		else if (asnType instanceof AsnTaggedType) {
			AsnTaggedType asnTaggedType = (AsnTaggedType) asnType;

			if (isExplicit(asnTaggedType.tagType)) {
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
			throw new IllegalStateException("This function is not applicable to CHOICEs");
		}
		else {
			return true;
		}
	}

	private boolean isPrimitive(String typeName, AsnModule module) throws IOException {

		AsnType asnType = module.asnTypes.typesByName.get(typeName);
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

		AsnType asnType = module.asnTypes.typesByName.get(typeName);
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

		AsnType asnType = module.asnTypes.typesByName.get(typeName);

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

	private String getSequenceElementName(AsnElementType sequenceElement) {
		return cleanUpName(sequenceElement.name);
	}

	private boolean isOptional(AsnElementType sequenceElement) {
		return (sequenceElement.isOptional || sequenceElement.isDefault);
	}

	private boolean hasExplicitTag(AsnTaggedType taggedType) throws IOException {
		if (!hasTag(taggedType)) {
			return false;
		}

		// TODO make this part of the is a directchoice function:
		if ((!taggedType.typeName.isEmpty() && isADirectChoice(taggedType.typeName))
				|| ((taggedType.typeReference != null) && (taggedType.typeReference instanceof AsnChoice))) {
			return true;
		}

		return isExplicit(taggedType.tagType);

	}

	private boolean hasImplicitTag(AsnTaggedType taggedType) throws IOException {
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

	private boolean hasTag(AsnTaggedType taggedType) {
		return taggedType.tag != null;
	}

	private void writeEncodeConstructor(String className, List<AsnElementType> sequenceElements) throws IOException {
		writeEncodeConstructor(className, sequenceElements, false);
	}

	private void writeEncodeConstructor(String className, List<AsnElementType> sequenceElements, boolean isChoice)
			throws IOException {
		String line = "public " + className + "(";

		int j = 0;

		for (AsnElementType sequenceElement : sequenceElements) {

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

		for (AsnElementType sequenceElement : sequenceElements) {

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

	private void writePublicMembers(BufferedWriter out, List<AsnElementType> sequenceElements) throws IOException {
		for (AsnElementType element : sequenceElements) {
			write("public " + getClassNameOfStructureElement(element) + " " + cleanUpName(element.name) + " = null;\n");

		}
	}

	private String getClassNameOfSequenceOfElement(AsnSequenceOf asnElementType) throws IOException {
		if (asnElementType.typeReference == null) {
			return cleanUpName(asnElementType.typeName);
		}
		else {

			AsnType typeDefinition = (AsnType) asnElementType.typeReference;

			if (typeDefinition instanceof AsnConstructedType) {

				String subClassName;

				if (typeDefinition instanceof AsnSequenceSet) {

					if (((AsnSequenceSet) typeDefinition).isSequence) {
						subClassName = "SubSeq";
					}
					else {
						subClassName = "SubSet";
					}

				}
				else if (typeDefinition instanceof AsnSequenceOf) {

					if (((AsnSequenceOf) typeDefinition).isSequenceOf) {
						subClassName = "SubSeqOf";
					}
					else {
						subClassName = "SubSetOf";
					}

				}
				else {
					subClassName = "SubChoice";
				}

				return cleanUpName(subClassName);

			}

			return getBerType(typeDefinition);

		}
	}

	private String getClassNameOfStructureElement(AsnElementType asnElementType) throws IOException {

		if (asnElementType.typeReference == null) {
			return cleanUpName(asnElementType.typeName);
		}
		else {

			AsnType typeDefinition = (AsnType) asnElementType.typeReference;

			if (typeDefinition instanceof AsnConstructedType) {

				String subClassName;

				if (typeDefinition instanceof AsnSequenceSet) {

					if (((AsnSequenceSet) typeDefinition).isSequence) {
						subClassName = "SubSeq";
					}
					else {
						subClassName = "SubSet";
					}

				}
				else if (typeDefinition instanceof AsnSequenceOf) {

					if (((AsnSequenceOf) typeDefinition).isSequenceOf) {
						subClassName = "SubSeqOf";
					}
					else {
						subClassName = "SubSetOf";
					}

				}
				else {
					subClassName = "SubChoice";
				}

				subClassName += "_" + asnElementType.name;

				return cleanUpName(subClassName);

			}

			return getBerType(typeDefinition);

		}
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

		boolean assignedTypeIsDirectChoice = isADirectChoice(assignedTypeName);

		// if the assigned type is a direct choice (not a tagged choice) then the tag is always explicit.
		if (assignedTypeIsDirectChoice && !tagNum.isEmpty()) {
			explicit = true;
		}

		write("public class " + typeName + " extends " + assignedTypeName + " {\n");

		if (!tagNum.isEmpty()) {

			String primitiveOrConstructedString = "PRIMITIVE";

			if (explicit || !isPrimitive(typeDefinition)) {
				primitiveOrConstructedString = "CONSTRUCTED";
			}

			write("public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
					+ ", BerIdentifier." + primitiveOrConstructedString + ", " + tagNum + ");\n");

			if (explicit) {
				write("protected BerIdentifier id;\n");

				write("public byte[] code = null;\n");
			}
		}

		boolean assignedTypeIsChoice = (getUniversalType(typeDefinition) instanceof AsnChoice);

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

		if (constructorParameters.length != 0) {
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

			if (assignedTypeIsChoice) {
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
		else {
			throw new IllegalStateException("type of unknown class: " + typeDefinition.name);
		}

	}

	private String[] getConstructorParametersFromConstructedElement(AsnConstructedType assignedTypeDefinition)
			throws IOException {

		List<AsnElementType> sequenceElements;

		if (assignedTypeDefinition instanceof AsnSequenceSet) {

			sequenceElements = ((AsnSequenceSet) assignedTypeDefinition).elementTypeList.elements;
		}
		else {
			sequenceElements = ((AsnChoice) assignedTypeDefinition).elementTypeList.elements;
		}

		String[] constructorParameters = new String[sequenceElements.size() * 2];

		for (int j = 0; j < sequenceElements.size(); j++) {
			AsnElementType sequenceElement = sequenceElements.get(j);

			constructorParameters[j * 2] = getClassNameOfStructureElement(sequenceElement);
			constructorParameters[j * 2 + 1] = cleanUpName(sequenceElement.name);

		}
		return constructorParameters;
	}

	private boolean isADirectChoice(String typeName) throws IOException {
		return isADirectChoice(typeName, module);
	}

	private boolean isADirectChoice(String typeName, AsnModule module) throws IOException {
		if (typeName.startsWith("Ber")) {
			return false;
		}
		else {

			AsnType asnType = module.asnTypes.typesByName.get(typeName);

			if (asnType != null) {
				if (asnType instanceof AsnDefinedType) {
					return isADirectChoice(((AsnDefinedType) asnType).typeName, module);
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
						return isADirectChoice(typeName, modulesByName.get(symbolsFromModule.modref));
					}
				}
			}
			throw new IllegalStateException("Type definition \"" + typeName + "\" was not found in module \""
					+ module.moduleIdentifier.name + "\"");

		}
	}

	private String getTagNum(AsnTaggedType asnTaggedType) {

		if (asnTaggedType.tag == null) {
			throw new IllegalStateException("tag is null");
		}

		Integer tagNum = asnTaggedType.tag.classNumber.num;

		if (tagNum == null) {
			throw new IllegalStateException("tag number is null");
		}

		return tagNum.toString();
	}

	private String getTagClass(AsnTaggedType asnTaggedType) {
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
