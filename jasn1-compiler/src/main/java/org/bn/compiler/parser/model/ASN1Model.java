package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

//~--- classes ----------------------------------------------------------------

@XmlRootElement
public class ASN1Model implements Serializable {
	public String moduleDirectory;
	public String outputDirectory;
	public String[] runtimeArguments;
	public String moduleNS;
	public ASNModule module;
}
