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
package org.openmuc.jasn1.ber.types;

import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;

public class BerBitString {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.BIT_STRING_TAG);
	protected BerIdentifier id;

	public byte[] code = null;

	public byte[] bitString;
	public int numBits;

	public BerBitString() {
		id = identifier;
	}

	public BerBitString(byte[] bitString, int numBits) {
		id = identifier;
		if ((numBits <= (((bitString.length - 1) * 8) + 1)) || (numBits > (bitString.length * 8))) {
			throw new IllegalArgumentException("numBits out of bound.");
		}

		this.bitString = bitString;
		this.numBits = numBits;

	}

	public BerBitString(byte[] code) {
		id = identifier;
		this.code = code;
	}

	public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {

		int codeLength;

		if (code != null) {
			codeLength = code.length;
			for (int i = code.length - 1; i >= 0; i--) {
				berOStream.write(code[i]);
			}
		}
		else {

			for (int i = (bitString.length - 1); i >= 0; i--) {
				berOStream.write(bitString[i]);
			}
			berOStream.write(bitString.length * 8 - numBits);

			codeLength = bitString.length + 1;

			codeLength += BerLength.encodeLength(berOStream, codeLength);

		}

		if (explicit) {
			codeLength += id.encode(berOStream);
		}

		return codeLength;
	}

	public int decode(InputStream iStream, boolean explicit) throws IOException {
		// could be encoded in primitiv and constructed mode
		// only primitiv mode is implemented

		int codeLength = 0;

		if (explicit) {
			codeLength += id.decodeAndCheck(iStream);
		}

		BerLength length = new BerLength();
		codeLength += length.decode(iStream);

		bitString = new byte[length.val - 1];
		numBits = (bitString.length * 8) - iStream.read();

		if (bitString.length > 0) {
			if (iStream.read(bitString, 0, bitString.length) < bitString.length) {
				throw new IOException("Error Decoding BerBitString");
			}
		}

		codeLength += bitString.length + 1;

		return codeLength;

	}
}
