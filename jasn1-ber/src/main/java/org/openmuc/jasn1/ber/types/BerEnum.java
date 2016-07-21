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

import org.openmuc.jasn1.ber.BerIdentifier;

public class BerEnum extends BerInteger {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
			BerIdentifier.PRIMITIVE, BerIdentifier.ENUMERATED_TAG);

	public BerEnum() {
		id = identifier;
	}

	public BerEnum(byte[] code) {
		id = identifier;
		this.code = code;
	}

	public BerEnum(long val) {
		id = identifier;
		this.val = val;
	}

}
