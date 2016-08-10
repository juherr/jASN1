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
