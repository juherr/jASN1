/*
 * Copyright 2011-13 Fraunhofer ISE
 * Author(s): Stefan Feuerhahn
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlToJavaTranslator {

	private static Logger logger = LoggerFactory.getLogger(XmlToJavaTranslator.class);
	private final Document doc;
	private final String outputDir;
	private final XPath xPath;
	private final Element asnTypesElement;
	private final String packageName;
	private int indentNum = 0;
	BufferedWriter out;
	private boolean defaultExplicit = true;
	private boolean supportIndefiniteLength = false;

	XmlToJavaTranslator(InputStream xmlInputStream, String outputDir, boolean supportIndefiniteLength)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();

		this.supportIndefiniteLength = supportIndefiniteLength;

		doc = builder.parse(xmlInputStream);
		this.outputDir = outputDir;

		XPathFactory xPathFactory = XPathFactory.newInstance();
		xPath = xPathFactory.newXPath();

		Element asn1Model = (Element) doc.getElementsByTagName("asn1Model").item(0);

		packageName = ((Element) asn1Model.getElementsByTagName("moduleNS").item(0)).getTextContent();

		asnTypesElement = (Element) doc.getElementsByTagName("asnTypes").item(0);

		if (xPath.evaluate("module/tagDefault", asn1Model).equals("IMPLICIT")) {
			defaultExplicit = false;
		}
	}

	public void translate() throws XPathExpressionException, IOException {

		NodeList asnTypes = asnTypesElement.getChildNodes();

		for (int i = 0; i < asnTypes.getLength(); i++) {

			if (asnTypes.item(i).getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element asn1TypeElement = (Element) asnTypes.item(i);

			String tagNum = "";
			String tagClass = "";
			String className = "";

			if (asn1TypeElement.getAttribute("xsi:type").equals("asnTaggedType")) {

				if ((!xPath.evaluate("tagDefault", asn1TypeElement).equals("IMPLICIT")) && defaultExplicit) {
					throw new IOException("ASN1 element " + xPath.evaluate("name", asn1TypeElement)
							+ " has an explicit tag which is inefficient and not supported by this compiler");
				}

				if (xPath.evaluate("typeReference", asn1TypeElement).equals("")) {
					throw new IOException(
							"ASN1 element "
									+ xPath.evaluate("name", asn1TypeElement)
									+ ": this kind of definition where an element equals aother element with an implicit tag is not supported by this compiler");
				}

				tagClass = xPath.evaluate("tag/clazz", asn1TypeElement);
				if (tagClass != "") {
					tagClass += "_CLASS";
				}
				tagNum = xPath.evaluate("tag/classNumber/num", asn1TypeElement);
				// className = xPath.evaluate("name", asn1TypeElement);
				className = xPath.evaluate("name", asn1TypeElement).replace('-', '_');

				Element oldAsn1TypeElement = asn1TypeElement;

				asn1TypeElement = (Element) xPath.evaluate("typeReference", asn1TypeElement, XPathConstants.NODE);
				String asn1TypeElementString = getASNType(asn1TypeElement);

				if (asn1TypeElementString.equals("asnInteger")
						|| (asn1TypeElementString.startsWith("asn") && asn1TypeElementString.endsWith("String"))) {

					FileWriter fstream = new FileWriter(outputDir + "/" + className + ".java");
					out = new BufferedWriter(fstream);
					writeHeader(new String[] { "import java.util.List;", "import java.util.LinkedList;",
							"import java.io.UnsupportedEncodingException;" });

					if (tagClass.equals("")) {
						tagClass = "CONTEXT_CLASS";
					}

					write("public final class " + className + " extends " + getElementType(oldAsn1TypeElement, true)
							+ " {\n");

					write("public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
							+ ", BerIdentifier.PRIMITIVE, " + tagNum + ");\n");

					write("public " + className + "() {");
					write("id = identifier;");
					write("}\n");

					// if (!getASNType(asn1TypeElement).equals("asnNull")) {
					// write("public " + className + "(byte[] code) {");
					// write("id = identifier;");
					// write("this.code = code;");
					// write("}\n");
					// }

					if (asn1TypeElementString.equals("asnInteger")) {
						write("public " + className + "(long val) {");
						write("id = identifier;");
						write("this.val = val;");
						write("}\n");
					}
					else if (asn1TypeElementString.equals("asnReal")) {
						write("public " + className + "(double val) {");
						write("id = identifier;");
						write("this.val = val;");
						write("}\n");
					}
					else if (asn1TypeElementString.equals("asnBoolean")) {
						write("public " + className + "(boolean val) {");
						write("id = identifier;");
						write("this.val = val;");
						write("}\n");
					}
					else if (asn1TypeElementString.equals("asnObjectIdentifier")) {
						write("public " + className + "(int[] objectIdentifierComponents) {");
						write("id = identifier;");
						write("this.objectIdentifierComponents = objectIdentifierComponents;");
						write("}\n");
					}
					else if (asn1TypeElementString.equals("asnEnum")) {
						write("public " + className + "(long val) {");
						write("id = identifier;");
						write("this.val = val;");
						write("}\n");
					}
					else if (asn1TypeElementString.equals("asnBitString")) {
						write("public " + className + "(byte[] bitString, int numBits) {");
						write("id = identifier;");
						write("if ((numBits <= (((bitString.length - 1) * 8) + 1)) || (numBits > (bitString.length * 8))) {");
						write("throw new IllegalArgumentException(\"numBits out of bound.\");");
						write("}\n");

						write("this.bitString = bitString;");
						write("this.numBits = numBits;");
						write("}\n");
					}
					else if ((asn1TypeElementString.startsWith("asn") && asn1TypeElementString.endsWith("String"))
							|| asn1TypeElementString.equals("asnGeneralizedTime")) {
						write("public " + className + "(byte[] octetString) {");
						write("id = identifier;");
						write("this.octetString = octetString;");
						write("}\n");

						if (asn1TypeElementString.equals("asnVisibleString")) {

							write("public " + className
									+ "(String visibleString) throws UnsupportedEncodingException {");
							write("id = identifier;");
							write("this.octetString = visibleString.getBytes(\"US-ASCII\");");
							write("}\n");
						}
						else if (asn1TypeElementString.equals("asnUTF8String")) {

							write("public " + className
									+ "(String visibleString) throws UnsupportedEncodingException {");
							write("id = identifier;");
							write("this.octetString = visibleString.getBytes(\"UTF-8\");");
							write("}\n");
						}
					}

					write("}");

					out.close();
				}

			}

			if (!asn1TypeElement.getAttribute("xsi:type").equals("asnSequenceSet")
					&& !asn1TypeElement.getAttribute("xsi:type").equals("asnSequenceOf")
					&& !asn1TypeElement.getAttribute("xsi:type").equals("asnChoice")) {
				continue;
			}

			if (className.equals("")) {
				// className = xPath.evaluate("name", asn1TypeElement);
				className = xPath.evaluate("name", asn1TypeElement).replace('-', '_');
			}

			FileWriter fstream = new FileWriter(outputDir + "/" + className + ".java");
			out = new BufferedWriter(fstream);
			writeHeader(new String[] { "import java.util.List;", "import java.util.LinkedList;" });

			writeClass(asn1TypeElement, tagNum, tagClass, className, false);

			out.close();
		}

	}

	private void writeClass(Element asn1TypeElement, String tagNum, String tagClass, String className, boolean isStatic)
			throws IOException, XPathExpressionException {
		if (asn1TypeElement.getAttribute("xsi:type").equals("asnSequenceSet")) {

			writeSequenceOrSetClass(asn1TypeElement, tagNum, tagClass, className, isStatic);

		}
		else if (asn1TypeElement.getAttribute("xsi:type").equals("asnSequenceOf")) {

			writeSequenceOfClass(asn1TypeElement, tagNum, tagClass, className, isStatic);

		}
		else if (asn1TypeElement.getAttribute("xsi:type").equals("asnChoice")) {
			writeChoiceClass(asn1TypeElement, tagNum, tagClass, className, isStatic);

		}
	}

	private void writeChoiceClass(Element asn1TypeElement, String tagNum, String tagClass, String className,
			boolean isStatic) throws IOException, XPathExpressionException {

		if (className.equals("")) {
			className = xPath.evaluate("name", asn1TypeElement);
		}

		String isStaticStr = "";
		if (isStatic) {
			isStaticStr = " static";
		}

		write("public final" + isStaticStr + " class " + className + " {\n");

		write("public byte[] code = null;");

		NodeList sequenceElements = (NodeList) xPath.evaluate("elementTypeList/elements", asn1TypeElement,
				XPathConstants.NODESET);

		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);

			Element subElementRef = (Element) xPath.evaluate("typeReference", sequenceElement, XPathConstants.NODE);

			if (subElementRef != null) {
				String subClassName = getElementType(sequenceElement, true);
				writeClass(subElementRef, "", "", subClassName, true);
			}
		}

		writePublicMembers(out, sequenceElements);

		writeEmptyConstructor(className, true);

		writeEncodeConstructor(className, sequenceElements, true);

		writeChoiceEncodeFunction(sequenceElements);

		writeChoiceDecodeFunction(sequenceElements);

		writeEncodeAndSaveFunction();

		write("}\n");

	}

	private void writeChoiceDecodeFunction(NodeList sequenceElements) throws IOException, XPathExpressionException {
		write("public int decode(InputStream iStream, BerIdentifier berIdentifier) throws IOException {");
		write("int codeLength = 0;");
		write("int choiceDecodeLength = 0;");
		write("BerIdentifier passedIdentifier = berIdentifier;");
		write("if (berIdentifier == null) {");
		write("berIdentifier = new BerIdentifier();");
		write("codeLength += berIdentifier.decode(iStream);");
		write("}");

		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);

			String explicitEncoding = "false";
			if (hasTag(sequenceElement)) {

				if (getASNType(sequenceElement).equals("asnChoice")) {

					if (hasExplicitTag(sequenceElement)) {
						write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
								+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

						write("codeLength += new BerLength().decode(iStream);");
						explicitEncoding = "null";
					}
					else {
						explicitEncoding = "berIdentifier";
						throw new IOException("Element " + getSequenceElementName(sequenceElement)
								+ " is a CHOICE and has an implicit tag");
					}

				}
				else {

					if (hasExplicitTag(sequenceElement) || !isPrimitive(getASNType(sequenceElement))) {
						write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
								+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");
					}
					else {
						write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
								+ ", BerIdentifier.PRIMITIVE" + ", " + getTagNum(sequenceElement) + ")) {");
					}

					if (getASNType(sequenceElement).equals("asnAny")) {
						if (hasExplicitTag(sequenceElement)) {
							write("BerLength tempLength = new BerLength();");
							write("codeLength += tempLength.decode(iStream);");
						}
						else {
							throw new IOException("ANY within CHOICE has no tag: "
									+ getElementType(sequenceElement, true));
						}
					}
					else {

						if (hasExplicitTag(sequenceElement)) {

							write("codeLength += new BerLength().decode(iStream);");
							explicitEncoding = "true";
						}
					}
				}

				if (getASNType(sequenceElement).equals("asnAny")) {
					write("codeLength += tempLength.val;");
				}
				else {
					write(getSequenceElementName(sequenceElement) + " = new " + getElementType(sequenceElement, true)
							+ "();");

					write("codeLength += " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
							+ explicitEncoding + ");");
				}

				write("return codeLength;");

				write("}\n");

			}
			else {
				if (getASNType(sequenceElement).equals("asnAny")) {
					throw new IOException("ANY within CHOICE has no tag: " + getElementType(sequenceElement, true));
				}

				if (getASNType(sequenceElement).equals("asnChoice")) {

					logger.info("CHOICE without TAG within another CHOICE: "
							+ getElementType(sequenceElement, true)
							+ " You could consider integrating the inner CHOICE in the parent CHOICE in order to reduce the number of Java classes/objects.");

					explicitEncoding = "berIdentifier";

					write(getSequenceElementName(sequenceElement) + " = new " + getElementType(sequenceElement, true)
							+ "();");

					write("choiceDecodeLength = " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
							+ explicitEncoding + ");");
					write("if (choiceDecodeLength != 0) {");
					write("codeLength += choiceDecodeLength;");
					write("return codeLength;");
					write("}");
					write("else {");
					write(getSequenceElementName(sequenceElement) + " = null;");
					write("}\n");

				}
				else {

					write("if (berIdentifier.equals(" + getElementType(sequenceElement, true) + ".identifier)) {");

					if (getASNType(sequenceElement).equals("asnAny")) {
						write("codeLength += tempLength.val;");
					}
					else {
						write(getSequenceElementName(sequenceElement) + " = new "
								+ getElementType(sequenceElement, true) + "();");

						write("codeLength += " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
								+ explicitEncoding + ");");
					}

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

	private void writeChoiceEncodeFunction(NodeList sequenceElements) throws IOException, XPathExpressionException {
		write("public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {");

		write("if (code != null) {");

		write("for (int i = code.length - 1; i >= 0; i--) {");
		write("berOStream.write(code[i]);");
		write("}");
		write("return code.length;\n");
		write("}");

		write("int codeLength = 0;");

		for (int j = sequenceElements.getLength() - 1; j >= 0; j--) {
			if (hasExplicitTag((Element) sequenceElements.item(j))) {
				write("int sublength;\n");
				break;
			}
		}

		for (int j = sequenceElements.getLength() - 1; j >= 0; j--) {

			Element sequenceElement = (Element) sequenceElements.item(j);

			write("if (" + getSequenceElementName(sequenceElement) + " != null) {");

			String explicitEncoding = "true";
			if (hasImplicitTag(sequenceElement)) {
				explicitEncoding = "false";
			}

			if (hasExplicitTag(sequenceElement)) {
				write("sublength = " + getSequenceElementName(sequenceElement) + ".encode(berOStream, " + "true" + ");");
				write("codeLength += sublength;");
				write("codeLength += BerLength.encodeLength(berOStream, sublength);");
			}
			else {
				write("codeLength += " + getSequenceElementName(sequenceElement) + ".encode(berOStream, "
						+ explicitEncoding + ");");
			}

			if (hasTag(sequenceElement)) {
				if (hasExplicitTag(sequenceElement) || !isPrimitive(getASNType(sequenceElement))) {
					write("codeLength += (new BerIdentifier(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.CONSTRUCTED, " + getTagNum(sequenceElement) + ")).encode(berOStream);");

				}
				else {
					write("codeLength += (new BerIdentifier(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.PRIMITIVE, " + getTagNum(sequenceElement) + ")).encode(berOStream);");
				}
			}

			write("return codeLength;\n");

			write("}");

			write("");

		}

		write("throw new IOException(\"Error encoding BerChoice: No item in choice was selected.\");");

		write("}\n");

	}

	private void writeSequenceOrSetClass(Element asn1TypeElement, String tagNum, String tagClass, String className,
			boolean isStatic) throws IOException, XPathExpressionException {

		if (tagClass.equals("")) {
			tagClass = "UNIVERSAL_CLASS";
		}
		if (tagNum.equals("")) {
			if (xPath.evaluate("isSequence", asn1TypeElement).equals("true")) {
				tagNum = "16";
			}
			else {
				tagNum = "17";
			}
		}
		if (className.equals("")) {
			className = xPath.evaluate("name", asn1TypeElement);
		}

		String isStaticStr = "";
		if (isStatic) {
			isStaticStr = " static";
		}

		write("public final" + isStaticStr + " class " + className + " {\n");

		NodeList sequenceElements = (NodeList) xPath.evaluate("elementTypeList/elements", asn1TypeElement,
				XPathConstants.NODESET);

		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);

			Element subElementRef = (Element) xPath.evaluate("typeReference", sequenceElement, XPathConstants.NODE);

			if (subElementRef != null) {
				String subClassName = getElementType(sequenceElement, true);
				writeClass(subElementRef, "", "", subClassName, true);
			}
		}

		write("public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
				+ ", BerIdentifier.CONSTRUCTED, " + tagNum + ");");

		write("protected BerIdentifier id;\n");

		write("public byte[] code = null;");

		writePublicMembers(out, sequenceElements);

		writeEmptyConstructor(className);

		writeEncodeConstructor(className, sequenceElements);

		writeSequenceEncodeFunction(sequenceElements);

		if (xPath.evaluate("isSequence", asn1TypeElement).equals("true")) {

			writeSequenceDecodeFunction(sequenceElements);
		}
		else {
			writeSetDecodeFunction(sequenceElements);
		}

		writeEncodeAndSaveFunction();

		write("}\n");

	}

	private void writeSequenceOfClass(Element asn1TypeElement, String tagNum, String tagClass, String className,
			boolean isStatic) throws IOException, XPathExpressionException {

		if (tagClass.equals("")) {
			tagClass = "UNIVERSAL_CLASS";
		}
		if (tagNum.equals("")) {
			if (xPath.evaluate("isSequenceOf", asn1TypeElement).equals("true")) {
				tagNum = "16";
			}
			else {
				tagNum = "17";
			}
		}

		String isStaticStr = "";
		if (isStatic) {
			isStaticStr = " static";
		}

		write("public final" + isStaticStr + " class " + className + " {\n");

		Element subElementRef = (Element) xPath.evaluate("typeReference", asn1TypeElement, XPathConstants.NODE);

		if (subElementRef != null) {
			String subClassName = getElementType(asn1TypeElement, false);
			writeClass(subElementRef, "", "", subClassName, true);
		}

		write("public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier." + tagClass
				+ ", BerIdentifier.CONSTRUCTED, " + tagNum + ");");
		write("protected BerIdentifier id;\n");

		write("public byte[] code = null;");

		write("public List<" + getElementType(asn1TypeElement, false) + "> seqOf = null;\n");

		writeEmptyConstructor(className);

		write("public " + className + "(List<" + getElementType(asn1TypeElement, false) + "> seqOf) {");
		write("id = identifier;");
		write("this.seqOf = seqOf;");
		write("}\n");

		writeSequenceOfEncodeFunction();

		writeSequenceOfDecodeFunction(asn1TypeElement);

		writeEncodeAndSaveFunction();

		write("}\n");

	}

	private void writeSequenceOfDecodeFunction(Element element) throws IOException, XPathExpressionException {
		write("public int decode(InputStream iStream, boolean explicit) throws IOException {");
		write("int codeLength = 0;");
		write("int subCodeLength = 0;");
		write("seqOf = new LinkedList<" + getElementType(element, false) + ">();\n");

		write("if (explicit) {");
		write("codeLength += id.decodeAndCheck(iStream);");
		write("}\n");

		write("BerLength length = new BerLength();");
		write("codeLength += length.decode(iStream);\n");

		// indefinite length
		if (supportIndefiniteLength == true) {

			write("if (length.val == -1) {");
			write("BerIdentifier berIdentifier = new BerIdentifier();");
			write("while (true) {");
			write("subCodeLength += berIdentifier.decode(iStream);\n");

			write("if (berIdentifier.tagNumber == 0 && berIdentifier.identifierClass == 0 && berIdentifier.primitive == 0) {");
			write("if (iStream.read() != 0) {");
			write("throw new IOException(\"Decoded sequence has wrong end of contents octets\");");
			write("}");
			write("codeLength += subCodeLength + 1;");
			write("return codeLength;");
			write("}\n");

			write(getElementType(element, false) + " element = new " + getElementType(element, false) + "();");

			String asnType;
			if (!xPath.evaluate("typeReference/@type", element).equals("")) {
				asnType = getASNType((Element) xPath.evaluate("typeReference", element, XPathConstants.NODE));
			}
			else {
				Element name = (Element) xPath.evaluate("*/name[.='" + xPath.evaluate("typeName", element) + "']",
						asnTypesElement, XPathConstants.NODE);
				asnType = getASNType((Element) name.getParentNode());
			}

			if (asnType.equals("asnChoice")) {
				write("subCodeLength += element.decode(iStream, berIdentifier);");
			}
			else {
				write("subCodeLength += element.decode(iStream, false);");
			}
			write("seqOf.add(element);");
			write("}");

			write("}");

		}

		// definite length
		write("while (subCodeLength < length.val) {");
		write(getElementType(element, false) + " element = new " + getElementType(element, false) + "();");

		String asnType;
		if (!xPath.evaluate("typeReference/@type", element).equals("")) {
			asnType = getASNType((Element) xPath.evaluate("typeReference", element, XPathConstants.NODE));
		}
		else {
			Element name = (Element) xPath.evaluate("*/name[.='" + xPath.evaluate("typeName", element) + "']",
					asnTypesElement, XPathConstants.NODE);
			asnType = getASNType((Element) name.getParentNode());
		}

		if (asnType.equals("asnChoice")) {
			write("subCodeLength += element.decode(iStream, null);");
		}
		else {
			write("subCodeLength += element.decode(iStream, true);");
		}
		write("seqOf.add(element);");
		write("}");
		write("if (subCodeLength != length.val) {");
		write("throw new IOException(\"Decoded SequenceOf or SetOf has wrong length tag\");\n");
		write("}");
		write("codeLength += subCodeLength;\n");

		write("return codeLength;");
		write("}");

	}

	private void writeSequenceOfEncodeFunction() throws IOException {
		write("public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {");
		write("int codeLength;\n");

		write("if (code != null) {");
		write("codeLength = code.length;");
		write("for (int i = code.length - 1; i >= 0; i--) {");
		write("berOStream.write(code[i]);");
		write("}");
		write("}");
		write("else {");

		write("codeLength = 0;");

		write("for (int i = (seqOf.size() - 1); i >= 0; i--) {");
		write("codeLength += seqOf.get(i).encode(berOStream, true);");
		write("}\n");

		write("codeLength += BerLength.encodeLength(berOStream, codeLength);\n");

		write("}\n");

		write("if (explicit) {");
		write("codeLength += id.encode(berOStream);");
		write("}\n");

		write("return codeLength;");
		write("}\n");

	}

	private void writeSetDecodeFunction(NodeList sequenceElements) throws IOException, XPathExpressionException {
		write("public int decode(InputStream iStream, boolean explicit) throws IOException {");
		write("int codeLength = 0;");
		write("int subCodeLength = 0;");
		write("BerIdentifier berIdentifier = new BerIdentifier();\n");

		write("if (explicit) {");
		write("codeLength += id.decodeAndCheck(iStream);");
		write("}\n");
		write("BerLength length = new BerLength();");
		write("codeLength += length.decode(iStream);\n");

		write("while (subCodeLength < length.val) {");
		write("subCodeLength += berIdentifier.decode(iStream);");

		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);

			String explicitEncoding = "false";

			if (hasTag(sequenceElement)) {
				if (hasExplicitTag(sequenceElement) || !isPrimitive(getASNType(sequenceElement))) {
					write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.CONSTRUCTED, " + getTagNum(sequenceElement) + ")) {");
				}
				else {
					write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.PRIMITIVE, " + getTagNum(sequenceElement) + ")) {");
				}
				if (hasExplicitTag(sequenceElement)) {
					write("subCodeLength += new BerLength().decode(iStream);");
					explicitEncoding = "true";
				}
			}
			else {
				write("if (berIdentifier.equals(" + getElementType(sequenceElement, true) + ".identifier)) {");
			}

			write(getSequenceElementName(sequenceElement) + " = new " + getElementType(sequenceElement, true) + "();");

			write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
					+ explicitEncoding + ");");

			write("}");

		}

		write("}");

		write("if (subCodeLength != length.val) {");
		write("throw new IOException(\"Decoded sequence has wrong length tag\");\n");
		write("}");
		write("codeLength += subCodeLength;\n");

		write("return codeLength;");
		write("}\n");
	}

	private void writeSequenceDecodeFunction(NodeList sequenceElements) throws IOException, XPathExpressionException {
		write("public int decode(InputStream iStream, boolean explicit) throws IOException {");
		write("int codeLength = 0;");
		write("int subCodeLength = 0;");
		write("int choiceDecodeLength = 0;");
		write("BerIdentifier berIdentifier = new BerIdentifier();");
		write("boolean decodedIdentifier = false;\n");

		write("if (explicit) {");
		write("codeLength += id.decodeAndCheck(iStream);");
		write("}\n");
		write("BerLength length = new BerLength();");
		write("codeLength += length.decode(iStream);\n");

		// indefinite length

		if (supportIndefiniteLength == true) {

			write("if (length.val == -1) {");
			write("subCodeLength += berIdentifier.decode(iStream);\n");

			for (int j = 0; j < sequenceElements.getLength(); j++) {
				Element sequenceElement = (Element) sequenceElements.item(j);

				write("if (berIdentifier.tagNumber == 0 && berIdentifier.identifierClass == 0 && berIdentifier.primitive == 0) {");
				write("if (iStream.read() != 0) {");
				write("throw new IOException(\"Decoded sequence has wrong end of contents octets\");");
				write("}");
				write("codeLength += subCodeLength + 1;");
				write("return codeLength;");
				write("}");

				// write("if (berIdentifier.identifierClass != 0 || berIdentifier.tagNumber != 0 || berIdentifier.primitive != 0) {");

				// write("if (decodedIdentifier == false) {");
				// write("subCodeLength += berIdentifier.decode(iStream);");
				// write("decodedIdentifier = true;");
				// write("}");

				String explicitEncoding;

				if (getASNType(sequenceElement).equals("asnAny")) {

					if (hasExplicitTag(sequenceElement)) {
						write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
								+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

						write("BerLength tempLength = new BerLength();");
						write("codeLength += tempLength.decode(iStream);");
						write("}");
					}
					else {
						throw new IOException("ANY within SEQUENCE has no explicit tag: "
								+ getElementType(sequenceElement, true));
					}
				}
				else {

					if (getASNType(sequenceElement).equals("asnChoice")) {
						if (hasExplicitTag(sequenceElement)) {
							write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
									+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

							write("subCodeLength += new BerLength().decode(iStream);");
							explicitEncoding = "null";
						}
						else {
							explicitEncoding = "berIdentifier";
						}

						write(getSequenceElementName(sequenceElement) + " = new "
								+ getElementType(sequenceElement, true) + "();");

						write("choiceDecodeLength = " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
								+ explicitEncoding + ");");
						write("if (choiceDecodeLength != 0) {");
						write("subCodeLength += choiceDecodeLength;");

						write("subCodeLength += berIdentifier.decode(iStream);");
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
							if (hasExplicitTag(sequenceElement) || !isPrimitive(getASNType(sequenceElement))) {
								write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
										+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");
							}
							else {
								write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
										+ ", BerIdentifier.PRIMITIVE" + ", " + getTagNum(sequenceElement) + ")) {");
							}
							if (hasExplicitTag(sequenceElement)) {
								write("subCodeLength += new BerLength().decode(iStream);");
								explicitEncoding = "true";
							}
						}
						else {
							write("if (berIdentifier.equals(" + getElementType(sequenceElement, true)
									+ ".identifier)) {");
						}

						write(getSequenceElementName(sequenceElement) + " = new "
								+ getElementType(sequenceElement, true) + "();");

						write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
								+ explicitEncoding + ");");

						write("subCodeLength += berIdentifier.decode(iStream);");

						write("}");

					}

				}

				if (!isOptional(sequenceElement)
						&& (!getASNType(sequenceElement).equals("asnChoice") || hasExplicitTag(sequenceElement))) {
					write("else {");
					write("throw new IOException(\"Identifier does not macht required sequence element identifer.\");");
					write("}");
				}

			}

			write("if (berIdentifier.tagNumber != 0 || berIdentifier.identifierClass != 0 || berIdentifier.primitive != 0");
			write("|| iStream.read() != 0) {");
			write("throw new IOException(\"Decoded sequence has wrong end of contents octets\");");
			write("}");
			write("codeLength += subCodeLength + 1;");

			write("return codeLength;");
			write("}\n");

		}

		// definite length

		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);

			write("if (subCodeLength < length.val) {");

			write("if (decodedIdentifier == false) {");
			write("subCodeLength += berIdentifier.decode(iStream);");
			write("decodedIdentifier = true;");
			write("}");

			String explicitEncoding;
			if (getASNType(sequenceElement).equals("asnAny")) {

				if (hasExplicitTag(sequenceElement)) {
					write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

					write("BerLength tempLength = new BerLength();");
					write("codeLength += tempLength.decode(iStream);");
					write("}");
				}
				else {
					throw new IOException("ANY within SEQUENCE has no explicit tag: "
							+ getElementType(sequenceElement, true));
				}
			}
			else {

				if (getASNType(sequenceElement).equals("asnChoice")) {
					if (hasExplicitTag(sequenceElement)) {
						write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
								+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");

						write("subCodeLength += new BerLength().decode(iStream);");
						explicitEncoding = "null";
					}
					else {
						explicitEncoding = "berIdentifier";
					}

					write(getSequenceElementName(sequenceElement) + " = new " + getElementType(sequenceElement, true)
							+ "();");

					write("choiceDecodeLength = " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
							+ explicitEncoding + ");");
					write("if (choiceDecodeLength != 0) {");
					write("decodedIdentifier = false;");
					write("subCodeLength += choiceDecodeLength;");

					write("}");

					if (hasExplicitTag(sequenceElement)) {
						write("}");
					}

				}
				else {

					explicitEncoding = "false";

					if (hasTag(sequenceElement)) {
						if (hasExplicitTag(sequenceElement) || !isPrimitive(getASNType(sequenceElement))) {
							write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
									+ ", BerIdentifier.CONSTRUCTED" + ", " + getTagNum(sequenceElement) + ")) {");
						}
						else {
							write("if (berIdentifier.equals(BerIdentifier." + getTagClass(sequenceElement)
									+ ", BerIdentifier.PRIMITIVE" + ", " + getTagNum(sequenceElement) + ")) {");
						}
						if (hasExplicitTag(sequenceElement)) {
							write("subCodeLength += new BerLength().decode(iStream);");
							explicitEncoding = "true";
						}
					}
					else {
						write("if (berIdentifier.equals(" + getElementType(sequenceElement, true) + ".identifier)) {");
					}

					write(getSequenceElementName(sequenceElement) + " = new " + getElementType(sequenceElement, true)
							+ "();");

					write("subCodeLength += " + getSequenceElementName(sequenceElement) + ".decode(iStream, "
							+ explicitEncoding + ");");

					write("decodedIdentifier = false;");

					write("}");

				}

			}

			if (!isOptional(sequenceElement)
					&& (!getASNType(sequenceElement).equals("asnChoice") || hasExplicitTag(sequenceElement))) {
				write("else {");
				write("throw new IOException(\"Identifier does not macht required sequence element identifer.\");");
				write("}");
			}

			write("}");

		}

		write("if (subCodeLength != length.val) {");
		write("throw new IOException(\"Decoded sequence has wrong length tag\");\n");
		write("}");
		write("codeLength += subCodeLength;\n");

		write("return codeLength;");
		write("}\n");

	}

	private void writeSequenceEncodeFunction(NodeList sequenceElements) throws IOException, XPathExpressionException {
		write("public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {\n");

		write("int codeLength;\n");

		write("if (code != null) {");
		write("codeLength = code.length;");
		write("for (int i = code.length - 1; i >= 0; i--) {");
		write("berOStream.write(code[i]);");
		write("}");
		write("}");
		write("else {");

		write("codeLength = 0;");

		for (int j = sequenceElements.getLength() - 1; j >= 0; j--) {
			if (hasExplicitTag((Element) sequenceElements.item(j))) {
				write("int sublength;\n");
				break;
			}
		}

		for (int j = sequenceElements.getLength() - 1; j >= 0; j--) {

			Element sequenceElement = (Element) sequenceElements.item(j);

			if (isOptional(sequenceElement)) {
				write("if (" + getSequenceElementName(sequenceElement) + " != null) {");
			}

			String explicitEncoding = "true";
			if (hasImplicitTag(sequenceElement)) {
				explicitEncoding = "false";
			}

			if (hasExplicitTag(sequenceElement)) {
				write("sublength = " + getSequenceElementName(sequenceElement) + ".encode(berOStream, "
						+ explicitEncoding + ");");
				write("codeLength += sublength;");
				write("codeLength += BerLength.encodeLength(berOStream, sublength);");
			}
			else {
				write("codeLength += " + getSequenceElementName(sequenceElement) + ".encode(berOStream, "
						+ explicitEncoding + ");");
			}

			if (hasTag(sequenceElement)) {
				if (hasExplicitTag(sequenceElement) || !isPrimitive(getASNType(sequenceElement))) {
					write("codeLength += (new BerIdentifier(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.CONSTRUCTED, " + getTagNum(sequenceElement) + ")).encode(berOStream);");

				}
				else {
					write("codeLength += (new BerIdentifier(BerIdentifier." + getTagClass(sequenceElement)
							+ ", BerIdentifier.PRIMITIVE, " + getTagNum(sequenceElement) + ")).encode(berOStream);");
				}
			}
			if (isOptional(sequenceElement)) {
				write("}");
			}

			write("");

		}

		write("codeLength += BerLength.encodeLength(berOStream, codeLength);");
		write("}\n");
		write("if (explicit) {");
		write("codeLength += id.encode(berOStream);");
		write("}\n");

		write("return codeLength;\n");

		write("}\n");
	}

	private void writeHeader(String[] additionalImports) throws IOException {

		write("/**");
		write(" * This class file was automatically generated by jASN1 (http://www.openmuc.org)\n */\n");
		write("package " + packageName + ";\n");

		write("import java.io.IOException;");
		write("import java.io.InputStream;");
		if (additionalImports != null) {
			for (String importStatement : additionalImports) {
				write(importStatement);
			}
		}
		write("import org.openmuc.jasn1.ber.*;");
		write("import org.openmuc.jasn1.ber.types.*;");
		write("import org.openmuc.jasn1.ber.types.string.*;\n");

	}

	private void writePublicMembers(BufferedWriter out, NodeList sequenceElements) throws XPathExpressionException,
			IOException {
		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);
			write("public " + getElementType(sequenceElement, true) + " "
					+ xPath.evaluate("name", sequenceElement).replace('-', '_') + " = null;\n");

		}
	}

	private void writeEmptyConstructor(String className) throws IOException {
		writeEmptyConstructor(className, false);
	}

	private void writeEncodeAndSaveFunction() throws IOException {
		write("public void encodeAndSave(int encodingSizeGuess) throws IOException {");
		write("BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(encodingSizeGuess);");
		write("encode(berOStream, false);");
		write("code = berOStream.getArray();");
		write("}");
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

	private void writeEncodeConstructor(String className, NodeList sequenceElements) throws IOException,
			XPathExpressionException {
		writeEncodeConstructor(className, sequenceElements, false);
	}

	private void writeEncodeConstructor(String className, NodeList sequenceElements, boolean isChoice)
			throws IOException, XPathExpressionException {
		String line = "public " + className + "(";

		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);

			if (j != 0) {
				line += ", ";
			}
			line += (getElementType(sequenceElement, true) + " " + xPath.evaluate("name", sequenceElement).replace('-',
					'_'));

		}

		write(line + ") {");
		if (!isChoice) {
			write("id = identifier;");
		}

		for (int j = 0; j < sequenceElements.getLength(); j++) {
			Element sequenceElement = (Element) sequenceElements.item(j);
			write("this." + xPath.evaluate("name", sequenceElement).replace('-', '_') + " = "
					+ xPath.evaluate("name", sequenceElement).replace('-', '_') + ";");

		}

		write("}\n");
	}

	private boolean isPrimitive(String asnType) {
		if (asnType.equals("asnChoice") || asnType.equals("asnSequenceSet") || asnType.equals("asnSequenceOf")) {
			return false;
		}
		else {
			return true;
		}
	}

	private String getASNType(Element element) throws XPathExpressionException, IOException {
		if (element == null) {
			throw new IOException("");
		}
		if (element.getAttribute("xsi:type").equals("asnElementType")
				|| element.getAttribute("xsi:type").equals("asnDefinedType")
				|| element.getAttribute("xsi:type").equals("asnTaggedType")) {
			if (!xPath.evaluate("typeReference/@type", element).equals("")) {
				return getASNType((Element) xPath.evaluate("typeReference", element, XPathConstants.NODE));
			}
			else {
				Element name = (Element) xPath.evaluate("*/name[.='" + xPath.evaluate("typeName", element) + "']",
						asnTypesElement, XPathConstants.NODE);
				return getASNType((Element) name.getParentNode());
			}
		}
		else {
			if (element.getAttribute("xsi:type").equals("asnCharacterString")) {
				return "asn" + xPath.evaluate("stringtype", element);
			}
			return element.getAttribute("xsi:type");
		}

	}

	private String getElementType(Element seqElement, boolean appendName) throws XPathExpressionException, IOException {

		String asnType = xPath.evaluate("typeReference/@type", seqElement);
		if (asnType.equals("")) {
			return simplifyTypeName(xPath.evaluate("typeName", seqElement)).replace('-', '_');
		}
		else {

			if (asnType.equals("asnSequenceSet")) {

				String subClassName = "";
				if (xPath.evaluate("typeReference/isSequence", seqElement).equals("true")) {

					subClassName += "SubSeq";
				}
				else {
					subClassName += "SubSet";
				}

				if (appendName) {
					subClassName += "_" + xPath.evaluate("name", seqElement);
				}

				return subClassName.replace('-', '_');

			}

			else if (asnType.equals("asnSequenceOf")) {
				String subClassName = "";
				if (xPath.evaluate("typeReference/isSequenceOf", seqElement).equals("true")) {

					subClassName += "SubSeqOf";
				}
				else {
					subClassName += "SubSetOf";
				}

				if (appendName) {
					subClassName += "_" + xPath.evaluate("name", seqElement);
				}

				return subClassName.replace('-', '_');
			}
			else if (asnType.equals("asnChoice")) {

				String subClassName = "SubChoice";

				if (appendName) {
					subClassName += "_" + xPath.evaluate("name", seqElement);
				}

				return subClassName.replace('-', '_');

			}

			return getBerType((Element) xPath.evaluate("typeReference", seqElement, XPathConstants.NODE));

		}
	}

	private String simplifyTypeName(String typeName) throws XPathExpressionException, IOException {
		Element name = (Element) xPath.evaluate("*/name[.='" + typeName + "']", asnTypesElement, XPathConstants.NODE);
		if (name == null) {
			throw new IOException("No asntype of name: " + typeName + " was found.");
		}

		Element newASNElement = (Element) name.getParentNode();
		String newASNType = newASNElement.getAttribute("xsi:type");
		if (newASNType.equals("asnSequenceSet") || newASNType.equals("asnSequenceOf") || newASNType.equals("asnChoice")
				|| newASNType.equals("asnTaggedType")) {
			return typeName;
		}
		else {
			if (newASNType.equals("asnDefinedType")) {
				return simplifyTypeName(xPath.evaluate("typeName", name.getParentNode()));
			}

			return getBerType(newASNElement);

		}
	}

	private String getBerType(Element asnTypeElement) throws XPathExpressionException {
		String newASNType = asnTypeElement.getAttribute("xsi:type");
		if (newASNType.equals("asnCharacterString")) {
			return "Ber" + xPath.evaluate("stringtype", asnTypeElement);
		}
		return "Ber" + newASNType.substring(3);
	}

	private String getSequenceElementName(Element sequenceElement) throws XPathExpressionException {
		return xPath.evaluate("name", sequenceElement).replace('-', '_');
	}

	private boolean hasTag(Element sequenceElement) throws XPathExpressionException {
		if (xPath.evaluate("isTag", sequenceElement).equals("true")) {
			return true;
		}
		return false;
	}

	private boolean isOptional(Element sequenceElement) throws XPathExpressionException {
		if (xPath.evaluate("isOptional", sequenceElement).equals("true")
				|| xPath.evaluate("isDefault", sequenceElement).equals("true")) {
			return true;
		}
		return false;
	}

	private boolean hasExplicitTag(Element sequenceElement) throws XPathExpressionException {
		if (hasTag(sequenceElement)) {
			if (xPath.evaluate("typeTagDefault", sequenceElement).equals("IMPLICIT")) {
				return false;
			}
			else if (xPath.evaluate("typeTagDefault", sequenceElement).equals("EXPLICIT")) {
				return true;
			}
			else {
				return defaultExplicit;
			}
		}
		return false;
	}

	private boolean hasImplicitTag(Element sequenceElement) throws XPathExpressionException {
		if (hasTag(sequenceElement)) {
			if (xPath.evaluate("typeTagDefault", sequenceElement).equals("IMPLICIT")) {
				return true;
			}
			else if (xPath.evaluate("typeTagDefault", sequenceElement).equals("EXPLICIT")) {
				return false;
			}
			else {
				return !defaultExplicit;
			}
		}
		return false;
	}

	private String getTagNum(Element sequenceElement) throws XPathExpressionException {
		String subTagNum = xPath.evaluate("tag/classNumber/num", sequenceElement);

		if (subTagNum.equals("")) {
			subTagNum = "0";
		}
		return subTagNum;
	}

	private String getTagClass(Element sequenceElement) throws XPathExpressionException {
		String subTagClass = xPath.evaluate("tag/clazz", sequenceElement);
		if (subTagClass.equals("")) {
			subTagClass = "CONTEXT";
		}

		subTagClass += "_CLASS";

		return subTagClass;
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
