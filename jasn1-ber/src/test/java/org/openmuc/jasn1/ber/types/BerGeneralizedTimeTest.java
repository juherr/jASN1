package org.openmuc.jasn1.ber.types;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

public class BerGeneralizedTimeTest {

	@Test
	public void explicitEncoding() throws IOException {
		BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

		byte[] byteArray = new byte[] { 0x01, 0x02, 0x03 };
		BerGeneralizedTime berGeneralizedTime = new BerGeneralizedTime(byteArray);
		int length = berGeneralizedTime.encode(berStream, true);
		Assert.assertEquals(5, length);

		byte[] expectedBytes = new byte[] { 24, 0x03, 0x01, 0x02, 0x03 };
		Assert.assertArrayEquals(expectedBytes, berStream.getArray());

	}

}
