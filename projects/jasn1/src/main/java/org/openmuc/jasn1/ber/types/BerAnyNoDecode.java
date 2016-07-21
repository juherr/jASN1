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
package org.openmuc.jasn1.ber.types;

import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerLength;

public class BerAnyNoDecode {

	public int length;

	public BerAnyNoDecode() {
	}

	public BerAnyNoDecode(int length) {
		this.length = length;
	}

	public int encode(BerByteArrayOutputStream os, boolean explicit) throws IOException {
		if (!explicit) {
			return length + BerLength.encodeLength(os, length);
		}
		return length;
	}

	public int decode(InputStream is, boolean explicit) throws IOException {

		BerLength length = new BerLength();
		int codeLength = length.decode(is);
		this.length = length.val;

		return codeLength + length.val;

	}

	@Override
	public String toString() {
		return "ANY not decoded";
	}
}
