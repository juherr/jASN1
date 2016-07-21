/*
 * Copyright 2011-14 Fraunhofer ISE
 *
 * This file is part of jasn1-compiler.
 * For more information visit http://www.openmuc.org
 *
 * jasn1-compiler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jasn1-compiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jasn1-compiler.  If not, see <http://www.gnu.org/licenses/>.
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 * 
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

import org.lineargs.Option;
import org.lineargs.constraints.RegexConstraint;

public class CompilerArgs {

	@Option(name = "--outputDir", shortName = "-o", description = "Output directory name", isOptional = true)
	@RegexConstraint(mask = ".+")
	private String outputDir = "output/";

	@Option(name = "--fileName", shortName = "-f", description = "Input ASN.1 filename")
	@RegexConstraint(mask = ".+")
	private String inputFileName = null;

	@Option(name = "--namespace", shortName = "-ns", description = "Generate classes with specified namespace/package name", isOptional = true)
	@RegexConstraint(mask = ".+")
	private String namespace = null;

	@Option(name = "--model-only", shortName = "-x", description = "Generate only the ASN.1 model (as XML)", isOptional = true)
	private Boolean generateModelOnly = false;

	@Option(name = "--with-indefinite-length", shortName = "-il", description = "Create Java classes that can also decode the indefinite length fields", isOptional = true)
	private Boolean supportIndefiniteLength = false;

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Boolean getGenerateModelOnly() {
		return generateModelOnly;
	}

	public void setGenerateModelOnly(Boolean generateModelOnly) {
		this.generateModelOnly = generateModelOnly;
	}

	public Boolean getSupportIndefiniteLength() {
		return supportIndefiniteLength;
	}

	public void setSupportIndefiniteLength(Boolean supportIndefiniteLength) {
		this.supportIndefiniteLength = supportIndefiniteLength;
	}

}
