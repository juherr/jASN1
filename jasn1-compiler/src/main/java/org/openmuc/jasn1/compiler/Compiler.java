/*
 Copyright 2011 Stefan Feuerhahn, Fraunhofer ISE
 Copyright 2006-2011 Abdulla Abdurakhmanov (abdulla@latestbit.com)
 Original sources are available at www.latestbit.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.openmuc.jasn1.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.bn.compiler.parser.ASNLexer;
import org.bn.compiler.parser.ASNParser;
import org.bn.compiler.parser.model.ASN1Model;
import org.bn.compiler.parser.model.ASNModule;
import org.lineargs.LineArgsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Compiler {

	private static Logger logger = LoggerFactory.getLogger(Compiler.class);

	static private ByteArrayOutputStream getXMLStream(String outputDir, String nameSpace, String inputFileName)
			throws PropertyException, Exception, JAXBException {

		ByteArrayOutputStream outputXml = new ByteArrayOutputStream(65535);

		JAXBContext jc = JAXBContext.newInstance("org.bn.compiler.parser.model");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ASN1Model model = createModel(inputFileName);

		model.outputDirectory = outputDir;
		if (nameSpace != null) {
			model.moduleNS = nameSpace;
		}
		else {
			model.moduleNS = model.module.moduleIdentifier.name.toLowerCase();
		}

		marshaller.marshal(model, outputXml);

		return outputXml;

	}

	static private ASN1Model createModel(String inputFileName) throws Exception {
		InputStream stream = new FileInputStream(inputFileName);
		ASNLexer lexer = new ASNLexer(stream);
		ASNParser parser = new ASNParser(lexer);
		ASNModule module = new ASNModule();

		parser.module_definition(module);

		ASN1Model model = new ASN1Model();

		model.module = module;

		return model;
	}

	public static void main(String args[]) throws Exception {

		LineArgsParser parser = new LineArgsParser();

		if (args.length == 0) {
			parser.printHelp(CompilerArgs.class, System.out);
		}
		CompilerArgs arguments = parser.parse(CompilerArgs.class, args);

		logger.info("outputDir: " + arguments.getOutputDir());
		logger.info("Compiling file: " + arguments.getInputFileName());
		if (arguments.getSupportIndefiniteLength() == true) {
			logger.info("Java classes will support decoding indefinite length.");
		}

		ByteArrayOutputStream outputXml = getXMLStream(arguments.getOutputDir(), arguments.getNamespace(),
				arguments.getInputFileName());
		InputStream stream = new ByteArrayInputStream(outputXml.toByteArray());

		if (arguments.getGenerateModelOnly()) {
			System.out.println(new String(outputXml.toByteArray()));
			return;
		}

		XmlToJavaTranslator xmlToJavaTranslator = new XmlToJavaTranslator(stream, arguments.getOutputDir(),
				arguments.getSupportIndefiniteLength());
		xmlToJavaTranslator.translate();

	}

}
