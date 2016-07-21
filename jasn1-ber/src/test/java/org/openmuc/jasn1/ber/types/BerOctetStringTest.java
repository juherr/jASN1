package org.openmuc.jasn1.ber.types;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerOctetString;


public class BerOctetStringTest {
	
	@Test
	public void explicitEncoding() throws IOException {
		BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

		byte[] byteArray = new byte[] {0x01,0x02,0x03};
		BerOctetString asn1OctetString = new BerOctetString(byteArray);
		int length = asn1OctetString.encode(berStream, true);
		Assert.assertEquals(5, length);
		
		byte[] expectedBytes = new byte[]{0x04,0x03,0x01,0x02,0x03};
		Assert.assertArrayEquals(expectedBytes, berStream.getArray());
		
	}
	
}
