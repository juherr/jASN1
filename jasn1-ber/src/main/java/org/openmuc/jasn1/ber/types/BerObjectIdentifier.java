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
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;

public class BerObjectIdentifier {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.OBJECT_IDENTIFIER_TAG);
	protected BerIdentifier id;

	public byte[] code = null;

	public int[] objectIdentifierComponents;

	public BerObjectIdentifier() {
		id = identifier;
	}

	public BerObjectIdentifier(byte[] code) {
		id = identifier;
		this.code = code;
	}

	public BerObjectIdentifier(int[] objectIdentifierComponents) {
		id = identifier;
		if ((objectIdentifierComponents.length < 2)
				|| ((objectIdentifierComponents[0] == 0 || objectIdentifierComponents[0] == 1) && (objectIdentifierComponents[1] > 39))
				|| objectIdentifierComponents[0] > 2) {
			throw new IllegalArgumentException("invalid object identifier components");
		}
		for (int objectIdentifierComponent : objectIdentifierComponents) {
			if (objectIdentifierComponent < 0) {
				throw new IllegalArgumentException("invalid object identifier components");
			}
		}

		this.objectIdentifierComponents = objectIdentifierComponents;

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

			int firstSubidentifier = 40 * objectIdentifierComponents[0] + objectIdentifierComponents[1];

			int subidentifier;

			codeLength = 0;

			for (int i = (objectIdentifierComponents.length - 1); i > 0; i--) {

				if (i == 1) {
					subidentifier = firstSubidentifier;
				}
				else {
					subidentifier = objectIdentifierComponents[i];
				}

				// get length of subidentifier
				int subIDLength = 1;
				while (subidentifier > (Math.pow(2, (7 * subIDLength)) - 1)) {
					subIDLength++;
				}

				berOStream.write(subidentifier & 0x7f);

				for (int j = 1; j <= (subIDLength - 1); j++) {
					berOStream.write(((subidentifier >> (7 * j)) & 0xff) | 0x80);
				}

				codeLength += subIDLength;
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
			objectIdentifierComponents = new int[0];
			return codeLength;
		}

		byte[] byteCode = new byte[length.val];
		if (iStream.read(byteCode, 0, length.val) == -1) {
			throw new IOException("Error Decoding BerObjectIdentifier");
		}
		codeLength += length.val;

		List<Integer> objectIdentifierComponentsList = new ArrayList<Integer>();

		int subIDEndIndex = 0;
		while ((byteCode[subIDEndIndex] & 0x80) == 0x80) {
			if (subIDEndIndex >= (length.val - 1)) {
				throw new IOException("Invalid Object Identifier");
			}
			subIDEndIndex++;
		}

		int subidentifier = 0;
		for (int i = 0; i <= subIDEndIndex; i++) {
			subidentifier |= (byteCode[i] << ((subIDEndIndex - i) * 7));
		}

		if (subidentifier < 40) {
			objectIdentifierComponentsList.add(0);
			objectIdentifierComponentsList.add(subidentifier);
		}
		else if (subidentifier < 80) {
			objectIdentifierComponentsList.add(1);
			objectIdentifierComponentsList.add(subidentifier - 40);
		}
		else {
			objectIdentifierComponentsList.add(2);
			objectIdentifierComponentsList.add(subidentifier - 80);
		}

		subIDEndIndex++;

		while (subIDEndIndex < length.val) {
			int subIDStartIndex = subIDEndIndex;

			while ((byteCode[subIDEndIndex] & 0x80) == 0x80) {
				if (subIDEndIndex == (length.val - 1)) {
					throw new IOException("Invalid Object Identifier");
				}
				subIDEndIndex++;
			}
			subidentifier = 0;
			for (int j = subIDStartIndex; j <= subIDEndIndex; j++) {
				subidentifier |= ((byteCode[j] & 0x7f) << ((subIDEndIndex - j) * 7));
			}
			objectIdentifierComponentsList.add(subidentifier);
			subIDEndIndex++;
		}

		objectIdentifierComponents = new int[objectIdentifierComponentsList.size()];
		for (int i = 0; i < objectIdentifierComponentsList.size(); i++) {
			objectIdentifierComponents[i] = objectIdentifierComponentsList.get(i);
		}

		return codeLength;

	}

	@Override
	public String toString() {
		if (objectIdentifierComponents == null || objectIdentifierComponents.length == 0) {
			return "";
		}

		String objIDString = "";
		objIDString += objectIdentifierComponents[0];
		for (int i = 1; i < objectIdentifierComponents.length; i++) {
			objIDString += "." + objectIdentifierComponents[i];
		}
		return objIDString;
	}

}
