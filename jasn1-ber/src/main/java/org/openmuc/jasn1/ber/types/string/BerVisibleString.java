/*
 * Copyright 2011-14 Fraunhofer ISE
 *
 * This file is part of jasn1-ber.
 * For more information visit http://www.openmuc.org
 *
 * jasn1-ber is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jasn1-ber is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jasn1-ber.  If not, see <http://www.gnu.org/licenses/>.
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

	public byte[] octetString;

	public BerVisibleString() {
		id = identifier;
	}

	public BerVisibleString(byte[] octetString) {
		id = identifier;
		this.octetString = octetString;
	}

	public BerVisibleString(String string) {
		id = identifier;
		octetString = string.getBytes();
	}

	public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {

		berOStream.write(octetString);
		int codeLength = octetString.length;

		codeLength += BerLength.encodeLength(berOStream, codeLength);

		if (explicit) {
			codeLength += id.encode(berOStream);
		}

		return codeLength;
	}

	public int decode(InputStream iStream, boolean explicit) throws IOException {

		int codeLength = 0;

		if (explicit) {
			codeLength += id.decodeAndCheck(iStream);
		}

		BerLength length = new BerLength();
		codeLength += length.decode(iStream);

		octetString = new byte[length.val];

		if (length.val != 0) {
			if (iStream.read(octetString, 0, length.val) < length.val) {
				throw new IOException("Error Decoding BerVisibleString");
			}
			codeLength += length.val;
		}

		return codeLength;

	}

	@Override
	public String toString() {
		return new String(octetString);
	}

}
