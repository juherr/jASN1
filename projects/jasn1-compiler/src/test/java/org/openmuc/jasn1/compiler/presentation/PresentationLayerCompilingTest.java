package org.openmuc.jasn1.compiler.presentation;

import java.io.File;

import org.junit.Test;
import org.openmuc.jasn1.compiler.Compiler;

public class PresentationLayerCompilingTest {

	@Test
	public void compiling() throws Exception {

		System.out.println(new File(".").getAbsolutePath());

		String[] args = new String[] { "-f", "src/test/resources/isoPresentationLayer.asn", "-o",
				"src/test/java/org/openmuc/jasn1/compiler/presentation/generated", "-ns",
				"org.openmuc.jasn1.compiler.presentation.generated" };
		Compiler.main(args);

	}

}
