/*
 * Copyright 2011-14 Fraunhofer ISE
 *
 * This file is part of jasn1.
 * For more information visit http://www.openmuc.org
 *
 * jasn1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jasn1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jasn1.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jasn1.ber.types;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

public class BerRealTest {

	@Test
	public void explicitEncodingZero() throws IOException {
		BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

		BerReal berReal = new BerReal(0);
		berReal.encode(berStream, true);

		ByteArrayInputStream berInputStream = new ByteArrayInputStream(berStream.getArray());
		BerReal berRealDecoded = new BerReal();
		berRealDecoded.decode(berInputStream, true);

		Assert.assertEquals(0, berRealDecoded.val, 0.01);
	}

	@Test
	public void explicitEncoding() throws IOException {
		BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

		BerReal berReal = new BerReal(1.5);
		berReal.encode(berStream, true);

		ByteArrayInputStream berInputStream = new ByteArrayInputStream(berStream.getArray());
		BerReal berRealDecoded = new BerReal();
		berRealDecoded.decode(berInputStream, true);

		Assert.assertEquals(1.5, berRealDecoded.val, 0.01);
	}

	@Test
	public void explicitEncodingNegInf() throws IOException {
		BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

		BerReal berReal = new BerReal(Double.NEGATIVE_INFINITY);
		berReal.encode(berStream, true);

		ByteArrayInputStream berInputStream = new ByteArrayInputStream(berStream.getArray());
		BerReal berRealDecoded = new BerReal();
		berRealDecoded.decode(berInputStream, true);

		Assert.assertEquals(Double.NEGATIVE_INFINITY, berRealDecoded.val, 0.01);
	}

	public static String getByteArrayString(byte[] byteArray) {
		StringBuilder builder = new StringBuilder();
		int l = 1;
		for (byte b : byteArray) {
			if ((l != 1) && ((l - 1) % 8 == 0)) {
				builder.append(' ');
			}
			if ((l != 1) && ((l - 1) % 16 == 0)) {
				builder.append('\n');
			}
			l++;
			builder.append("0x");
			String hexString = Integer.toHexString(b & 0xff);
			if (hexString.length() == 1) {
				builder.append(0);
			}
			builder.append(hexString + " ");
		}
		return builder.toString();
	}

	// @Test
	public void explicitDecoding() throws IOException {
		byte[] byteCode = new byte[] { 0x02, 0x01, 0x33 };
		ByteArrayInputStream berInputStream = new ByteArrayInputStream(byteCode);
		BerInteger asn1Integer = new BerInteger();
		asn1Integer.decode(berInputStream, true);
		Assert.assertEquals(51, asn1Integer.val);
	}

}
