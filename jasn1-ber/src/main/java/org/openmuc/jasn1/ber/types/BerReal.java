/*
 * Copyright 2011-13 Fraunhofer ISE
 * Author(s): Stefan Feuerhahn
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
package org.openmuc.jasn1.ber.types;

import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;

public class BerReal {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.REAL_TAG);
	public BerIdentifier id;

	public byte[] code = null;

	public double val;

	public BerReal() {
		id = identifier;
	}

	public BerReal(byte[] code) {
		id = identifier;
		this.code = code;
	}

	public BerReal(double val) {
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

			codeLength = 0;

			// explained in Annex C and Ch. 8.5 of X.690

			// we use binary encoding, with base 2 and F==0
			// F is only needed when encoding with base 8 or 16

			Long longVal = Double.doubleToLongBits(val);

			byte sign = 0;
			if (val < 0) {
				sign = 0x40;
			}
			byte exponentFormat = 0;

			int exponent = ((int) (longVal >> 52)) & 0x7ff;
			long mantissa = (longVal & 0x000fffffffffffffL) | 0x0010000000000000L;

			if (exponent == 0x7ff) {
				if (mantissa == 0x0010000000000000L) {
					if (sign == 0) {
						berOStream.write(0x40);
					}
					else {
						berOStream.write(0x41);
					}
					codeLength++;
				}
				else {
					throw new IOException("NAN not supported");
				}
			}
			else if (!(exponent == 0 && mantissa == 0x0010000000000000L)) {
				exponent -= 1075;
				int exponentIncr = 0;
				while (((longVal >> exponentIncr) & 0xff) == 0x00) {
					exponentIncr += 8;
				}
				while (((longVal >> exponentIncr) & 0x01) == 0x00) {
					exponentIncr++;
				}

				exponent += exponentIncr;

				mantissa >>= exponentIncr;

				int mantissaLength = 1;

				// TODO remove -1?
				while (mantissa > (Math.pow(2, (8 * mantissaLength) - 1) - 1)) {
					mantissaLength++;
				}
				for (int i = 0; i < mantissaLength; i++) {
					berOStream.write(((int) (mantissa >> 8 * (i))) & 0xff);
				}
				codeLength += mantissaLength;

				int expLength = 1;
				while (exponent > (Math.pow(2, (8 * expLength) - 1) - 1)
						|| exponent < Math.pow(-2, (8 * expLength) - 1) && expLength < 8) {
					expLength++;
				}
				for (int i = 0; i < expLength; i++) {
					berOStream.write((exponent >> 8 * (i)) & 0xff);
				}
				codeLength += expLength;

				if (expLength < 4) {
					exponentFormat = (byte) (expLength - 1);
				}
				else {
					berOStream.write(expLength);
					codeLength++;
					exponentFormat = 0x03;
				}

				berOStream.write(0x80 | sign | exponentFormat);

				codeLength++;
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

		if (length.val == 0) {
			val = 0;
			return codeLength;
		}

		if (length.val == 1) {
			int myByte = iStream.read();
			if (myByte == 0x40) {
				val = Double.POSITIVE_INFINITY;
			}
			else if (myByte == 0x41) {
				val = Double.NEGATIVE_INFINITY;
			}
			else {
				throw new IOException("invalid real encoding");
			}
			return codeLength + 1;
		}

		byte[] byteCode = new byte[length.val];
		if (iStream.read(byteCode, 0, length.val) < length.val) {
			throw new IOException("Error Decoding BerInteger");
		}

		codeLength += length.val;

		int tempLength = 1;

		int sign = 1;
		if ((byteCode[0] & 0x40) == 0x40) {
			sign = -1;
		}

		int exponentLength = (byteCode[0] & 0x03) + 1;
		if (exponentLength == 4) {
			exponentLength = byteCode[1];
			tempLength++;
		}

		tempLength += exponentLength;

		int exponent = 0;
		for (int i = 0; i < exponentLength; i++) {
			exponent |= byteCode[1 + i] << (8 * (exponentLength - i - 1));
		}
		int mantissa = 0;
		for (int i = 0; i < length.val - tempLength; i++) {
			mantissa |= byteCode[i + tempLength] << (8 * (length.val - tempLength - i - 1));
		}

		val = sign * mantissa * Math.pow(2, exponent);

		return codeLength;
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		BerByteArrayOutputStream berOStream = new BerByteArrayOutputStream(encodingSizeGuess);
		encode(berOStream, false);
		code = berOStream.getArray();
	}
}
