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
import java.io.InputStream;

public class BerLength {

	public int val;

	public BerLength() {
	}

	public int decode(InputStream iStream) throws IOException {

		val = iStream.read();
		int length = 1;

		if ((val & 0x80) != 0) {
			int lengthLength = val & 0x7f;

			if (lengthLength == 0) {
				val = -1;
				return 1;
			}

			if (lengthLength > 4) {
				throw new IOException("Length is out of bound!");
			}

			val = 0;
			byte[] byteCode = new byte[lengthLength];
			if (iStream.read(byteCode, 0, lengthLength) == -1) {
				throw new IOException("Error Decoding ASN1Integer");
			}
			length += lengthLength;

			for (int i = 0; i < lengthLength; i++) {
				val |= (byteCode[i] & 0xff) << (8 * (lengthLength - i - 1));
			}

		}

		return length;
	}

	public static int encodeLength(BerByteArrayOutputStream berOStream, int length) throws IOException {
		// the indefinite form is ignored for now

		if (length <= 127) {
			// this is the short form, it is coded differently than the long
			// form for values > 127
			berOStream.write((byte) length);
			return 1;
		}
		else {
			int numLengthBytes = 1;

			while (((int) (Math.pow(2, 8 * numLengthBytes) - 1)) < length) {
				numLengthBytes++;
			}

			for (int i = 0; i < numLengthBytes; i++) {
				berOStream.write((length >> 8 * i) & 0xff);
			}

			berOStream.write(0x80 | numLengthBytes);

			return 1 + numLengthBytes;
		}

	}

}
