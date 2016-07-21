/*
 * Copyright 2011-15 Fraunhofer ISE
 *
 * This file is part of jASN1.
 * For more information visit http://www.openmuc.org
 *
 * jASN1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jASN1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jASN1.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jasn1.ber.types.string;

import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;

public class BerVisibleString {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.VISIBLE_STRING_TAG);

	protected BerIdentifier id;

	public byte[] value;

	public BerVisibleString() {
		id = identifier;
	}

	public BerVisibleString(byte[] value) {
		id = identifier;
		this.value = value;
	}

	public BerVisibleString(String valueAsString) {
		id = identifier;
		value = valueAsString.getBytes();
	}

	public int encode(BerByteArrayOutputStream os, boolean explicit) throws IOException {

		os.write(value);
		int codeLength = value.length;

		codeLength += BerLength.encodeLength(os, codeLength);

		if (explicit) {
			codeLength += id.encode(os);
		}

		return codeLength;
	}

	public int decode(InputStream is, boolean explicit) throws IOException {

		int codeLength = 0;

		if (explicit) {
			codeLength += id.decodeAndCheck(is);
		}

		BerLength length = new BerLength();
		codeLength += length.decode(is);

		value = new byte[length.val];

		if (length.val != 0) {
			if (is.read(value, 0, length.val) < length.val) {
				throw new IOException("Error Decoding BerVisibleString");
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
