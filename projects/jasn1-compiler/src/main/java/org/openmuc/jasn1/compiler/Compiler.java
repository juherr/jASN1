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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openmuc.jasn1.compiler.cli.CliParameter;
import org.openmuc.jasn1.compiler.cli.CliParameterBuilder;
import org.openmuc.jasn1.compiler.cli.CliParseException;
import org.openmuc.jasn1.compiler.cli.CliParser;
import org.openmuc.jasn1.compiler.cli.FlagCliParameter;
import org.openmuc.jasn1.compiler.cli.StringCliParameter;
import org.openmuc.jasn1.compiler.model.AsnModel;
import org.openmuc.jasn1.compiler.model.AsnModule;
import org.openmuc.jasn1.compiler.parser.ASNLexer;
import org.openmuc.jasn1.compiler.parser.ASNParser;

public class Compiler {

    public final static String VERSION = "1.7.1";

    public static void main(String args[]) throws Exception {

        StringCliParameter outputBaseDir = new CliParameterBuilder("-o")
                .setDescription(
                        "The base directory for the generated Java classes. The class files will be saved in subfolders of the base directory corresponding to the name of the defined modules.")
                .buildStringParameter("output_base_dir", "./");

        StringCliParameter basePackageName = new CliParameterBuilder("-p")
                .setDescription("The base package name. Added to this will be a name generated from the module name.")
                .buildStringParameter("package_base_name", "");

        FlagCliParameter supportIndefiniteLength = new CliParameterBuilder("-il")
                .setDescription(
                        "Add support for decoding indefinite length in generated classes. This feature is not yet fully implemented and should be used with caution.")
                .buildFlagParameter();

        FlagCliParameter legacyMode = new CliParameterBuilder("-l")
                .setDescription(
                        "Enable legacy mode. Earlier versions of the jASN1 compiler generated classes that had public member variables instead of getters and setters. This flag enables the old kind of classes.")
                .buildFlagParameter();

        StringCliParameter asn1Files = new CliParameterBuilder("-f").setMandatory()
                .setDescription("Comma separated ASN.1 files defining one or more modules.")
                .buildStringParameter("asn1_files");

        List<CliParameter> cliParameters = new ArrayList<>();
        cliParameters.add(outputBaseDir);
        cliParameters.add(basePackageName);
        cliParameters.add(supportIndefiniteLength);
        cliParameters.add(legacyMode);
        cliParameters.add(asn1Files);

        CliParser cliParser = new CliParser("jasn1-compiler",
                "The compiler reads the ASN.1 definitions from the given files and generates corresponding Java classes that can be used to conveniently encode and decode BER data.");
        cliParser.addParameters(cliParameters);

        try {
            cliParser.parseArguments(args);
        } catch (CliParseException e1) {
            System.err.println("Error parsing command line parameters: " + e1.getMessage());
            System.out.println(cliParser.getUsageString());
            System.exit(1);
        }

        System.out.println("Generated code will be saved in: " + outputBaseDir.getValue());
        if (supportIndefiniteLength.isSelected()) {
            System.out.println("Java classes will support decoding indefinite length.");
        }

        HashMap<String, AsnModule> modulesByName = new HashMap<>();

        for (String inputFile : asn1Files.getValue().split(",")) {
            System.out.println("Parsing file: " + inputFile);
            AsnModel model = getJavaModelFromAsn1File(inputFile);
            for (String modName : model.modulesByName.keySet()) {
                System.out.println("modname: " + modName);
            }
            modulesByName.putAll(model.modulesByName);
        }

        BerClassWriter classWriter = new BerClassWriter(modulesByName, outputBaseDir.getValue(),
                basePackageName.getValue(), !legacyMode.isSelected(), supportIndefiniteLength.isSelected());

        System.out.println("Generating Java classes...");

        classWriter.translate();
        System.out.println("done");
    }

    private static AsnModel getJavaModelFromAsn1File(String inputFileName) throws Exception {
        InputStream stream = new FileInputStream(inputFileName);
        ASNLexer lexer = new ASNLexer(stream);
        ASNParser parser = new ASNParser(lexer);

        AsnModel model = new AsnModel();
        parser.module_definitions(model);

        return model;
    }

}
