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
package org.openmuc.jasn1.ber.types.string;

import java.io.UnsupportedEncodingException;

import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.types.BerOctetString;

public class BerUTF8String extends BerOctetString {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.UTF8_STRING_TAG);

	public BerUTF8String() {
		id = identifier;
	}

	public BerUTF8String(byte[] octetString) {
		id = identifier;
		this.octetString = octetString;
	}

	public BerUTF8String(String visibleString) throws UnsupportedEncodingException {
		id = identifier;
		octetString = visibleString.getBytes("UTF-8");
	}

	@Override
	public String toString() {
		try {
			return new String(octetString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

}
