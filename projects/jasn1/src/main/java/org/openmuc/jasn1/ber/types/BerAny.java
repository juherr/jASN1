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

import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerLength;

public class BerAny {

	public byte[] value;

	public BerAny() {
	}

	public BerAny(byte[] value) {
		this.value = value;
	}

	public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {

		berOStream.write(value);
		int codeLength = value.length;

		codeLength += BerLength.encodeLength(berOStream, codeLength);

		return codeLength;
	}

	public int decode(InputStream iStream, boolean explicit) throws IOException {

		int codeLength = 0;

		BerLength length = new BerLength();
		codeLength += length.decode(iStream);

		value = new byte[length.val];

		if (length.val != 0) {
			if (iStream.read(value, 0, length.val) < length.val) {
				throw new IOException("Error Decoding BerAny");
			}
			codeLength += length.val;
		}

		return codeLength;

	}

	@Override
	public String toString() {
		return new String(value);
	}

}
