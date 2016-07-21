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
package org.openmuc.jasn1.ber;

import java.io.IOException;
import java.io.InputStream;

public class BerIdentifier {

	public static final int UNIVERSAL_CLASS = 0x00;
	public static final int APPLICATION_CLASS = 0x40;
	public static final int CONTEXT_CLASS = 0x80;
	public static final int PRIVATE_CLASS = 0xc0;

	public static final int PRIMITIVE = 0x00;
	public static final int CONSTRUCTED = 0x20;

	public static final int BOOLEAN_TAG = 1;
	public static final int INTEGER_TAG = 2;
	public static final int BIT_STRING_TAG = 3;
	public static final int OCTET_STRING_TAG = 4;
	public static final int NULL_TAG = 5;
	public static final int OBJECT_IDENTIFIER_TAG = 6;
	public static final int REAL_TAG = 9;
	public static final int ENUMERATED_TAG = 10;
	public static final int UTF8_STRING_TAG = 12;
	public static final int NUMERIC_STRING_TAG = 18;
	public static final int PRINTABLE_STRING_TAG = 19;
	public static final int TELETEX_STRING_TAG = 20;
	public static final int VIDEOTEX_STRING_TAG = 21;
	public static final int IA5_STRING_TAG = 22;
	public static final int GENERALIZED_TIME_TAG = 24;
	public static final int GRAPHIC_STRING_TAG = 25;
	public static final int VISIBLE_STRING_TAG = 26;
	public static final int GENERAL_STRING_TAG = 27;
	public static final int UNIVERSAL_STRING_TAG = 28;
	public static final int BMP_STRING_TAG = 30;

	public byte[] identifier = null;
	public int identifierClass;
	public int primitive;
	public int tagNumber;

	public BerIdentifier(int identifierClass, int primitive, int tagNumber) {
		this.identifierClass = identifierClass;
		this.primitive = primitive;
		this.tagNumber = tagNumber;
		code();
	}

	public BerIdentifier() {
	}

	private void code() {
		if (tagNumber < 31) {
			identifier = new byte[1];
			identifier[0] = (byte) (identifierClass | primitive | tagNumber);
		}
		else {
			int tagLength = 1;
			while (tagNumber > (Math.pow(2, (7 * tagLength)) - 1)) {
				tagLength++;
			}

			identifier = new byte[1 + tagLength];
			identifier[0] = (byte) (identifierClass | primitive | 31);

			for (int j = 1; j <= (tagLength - 1); j++) {
				identifier[j] = (byte) (((tagNumber >> (7 * (tagLength - j))) & 0xff) | 0x80);
			}

			identifier[tagLength] = (byte) (tagNumber & 0x7f);

		}
	}

	public int encode(BerByteArrayOutputStream berOStream) throws IOException {
		for (int i = (identifier.length - 1); i >= 0; i--) {
			berOStream.write(identifier[i]);
		}
		return identifier.length;
	}

	public int decode(InputStream iStream) throws IOException {
		byte nextByte = (byte) iStream.read();
		identifierClass = nextByte & 0xC0;
		primitive = nextByte & 0x20;
		tagNumber = nextByte & 0x1f;

		int codeLength = 1;

		if (tagNumber == 0x1f) {
			tagNumber = 0;

			int counter = 0;

			do {
				nextByte = (byte) iStream.read();
				codeLength++;
				if (counter >= 6) {
					throw new IOException("Invalid Tag");
				}
				tagNumber = tagNumber << 7;
				tagNumber |= (nextByte & 0x7f);
				counter++;
			} while ((nextByte & 0x80) == 0x80);

		}

		return codeLength;
	}

	/**
	 * Decodes the Identifier from the ByteArrayInputStream and throws an
	 * Exception if it is not equal to itself. Returns the number of bytes read
	 * from the InputStream.
	 */
	public int decodeAndCheck(InputStream iStream) throws IOException {

		for (Byte myByte : identifier) {
			if (iStream.read() != (myByte & 0xff)) {
				throw new IOException("Identifier does not match!");
			}
		}
		return identifier.length;
	}

	public boolean equals(int identifierClass, int primitive, int tagNumber) {
		return (this.identifierClass == identifierClass && this.primitive == primitive && this.tagNumber == tagNumber);
	}

	public boolean equals(BerIdentifier berIdentifier) {
		return (this.identifierClass == berIdentifier.identifierClass && this.primitive == berIdentifier.primitive && this.tagNumber == berIdentifier.tagNumber);
	}

	public String toString() {
		return "identifierClass: " + identifierClass + " primitive: " + primitive + "Tag Number: " + tagNumber;
	}

}
