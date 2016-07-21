/*
 * Copyright Fraunhofer ISE, 2011
 * Author(s): Stefan Feuerhahn
 *    
 * This file is part of jASN1.
 * For more information visit http://www.openmuc.org
 * 
 * jASN1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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
package org.openmuc.jasn1.ber.types;

import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;

public class BerInteger {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.INTEGER_TAG);
	public BerIdentifier id;

	public byte[] code = null;

	public long val;

	public BerInteger() {
		id = identifier;
	}

	public BerInteger(byte[] code) {
		id = identifier;
		this.code = code;
	}

	public BerInteger(long val) {
		id = identifier;
		this.val = val;
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

			codeLength = 1;

			while (val > (Math.pow(2, (8 * codeLength) - 1) - 1) || val < Math.pow(-2, (8 * codeLength) - 1)
					&& codeLength < 8) {
				codeLength++;
			}

			for (int i = 0; i < codeLength; i++) {
				berOStream.write(((int) (val >> 8 * (i))) & 0xff);
			}

			codeLength += BerLength.encodeLength(berOStream, codeLength);
		}

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

		if (length.val < 1 || length.val > 8) {
			throw new IOException("Decoded length of BerInteger is not correct");
		}

		byte[] byteCode = new byte[length.val];
		if (iStream.read(byteCode, 0, length.val) < length.val) {
			throw new IOException("Error Decoding BerInteger");
		}

		codeLength += length.val;

		val = 0;
		for (int i = 0; i < length.val; i++) {
			val |= (byteCode[i] & 0xff) << (8 * (length.val - i - 1));
		}

		return codeLength;
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(encodingSizeGuess);
		encode(berOStream, false);
		code = berOStream.getArray();
	}
}
