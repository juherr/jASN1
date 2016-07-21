package org.openmuc.jasn1.ber;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class BerIdentifierTest {

	@Test
	public void twoByteEncoding() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		BerIdentifier berIdentifier = new BerIdentifier(BerIdentifier.APPLICATION_CLASS, BerIdentifier.PRIMITIVE, 31);

		int length = berIdentifier.encode(berBAOStream);
		Assert.assertEquals(2, length);

		byte[] expectedBytes = new byte[] { 0x5f, 0x1f };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());
	}

}
