package org.openmuc.jasn1.compiler.berexample;

import java.io.File;

import org.junit.Test;
import org.openmuc.jasn1.compiler.Compiler;

public class X690BerExampleCompilingTest {

	@Test
	public void compiling() throws Exception {

		System.out.println(new File(".").getAbsolutePath());

		String[] args = new String[] { "-f", "src/test/resources/x690BerExample.asn", "-o",
				"src/test/java/org/openmuc/jasn1/compiler/berexample/generated", "-ns",
				"org.openmuc.jasn1.compiler.berexample.generated" };
		Compiler.main(args);

	}

}
