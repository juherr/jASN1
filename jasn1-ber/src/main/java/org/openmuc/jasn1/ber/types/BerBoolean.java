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

public class BerBoolean {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.BOOLEAN_TAG);
	public BerIdentifier id;

	public byte[] code = null;

	public boolean val;

	public BerBoolean() {
		id = identifier;
	}

	public BerBoolean(byte[] code) {
		id = identifier;
		this.code = code;
	}

	public BerBoolean(boolean val) {
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

			if (val) {
				berOStream.write(1);
			}
			else {
				berOStream.write(0);
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

		if (length.val != 1) {
			throw new IOException("Decoded length of BerBoolean is not correct");
		}

		int readVal = iStream.read();
		codeLength++;
		if (readVal == 0) {
			val = false;
		}
		else {
			val = true;
		}

		return codeLength;
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(encodingSizeGuess);
		encode(berOStream, false);
		code = berOStream.getArray();
	}
}
