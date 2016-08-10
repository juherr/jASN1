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
package org.openmuc.jasn1.ber;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class BerLengthTest {

	@Test
	public void encodeLength() throws IOException {
		BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(50);

		int codedLength = BerLength.encodeLength(berOStream, 128);

		Assert.assertEquals(2, codedLength);

		byte[] expectedBytes = new byte[] { (byte) 0x81, (byte) 128 };

		Assert.assertArrayEquals(expectedBytes, berOStream.getArray());
	}

	@Test
	public void encodeLength2() throws IOException {
		BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(50);

		int codedLength = BerLength.encodeLength(berOStream, 128);

		Assert.assertEquals(2, codedLength);

		byte[] expectedBytes = new byte[] { (byte) 0x81, (byte) 128 };

		Assert.assertArrayEquals(expectedBytes, berOStream.getArray());
	}

	@Test
	public void explicitDecoding() throws IOException {
		byte[] byteCode = new byte[] { (byte) 0x81, (byte) 128 };
		ByteArrayInputStream berInputStream = new ByteArrayInputStream(byteCode);
		BerLength berLength = new BerLength();
		berLength.decode(berInputStream);
		Assert.assertEquals(128, berLength.val);
	}

}
