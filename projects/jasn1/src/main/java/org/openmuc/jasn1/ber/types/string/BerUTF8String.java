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

    public BerUTF8String(byte[] value) {
        id = identifier;
        this.value = value;
    }

    public BerUTF8String(String valueAsString) throws UnsupportedEncodingException {
        id = identifier;
        value = valueAsString.getBytes("UTF-8");
    }

    @Override
    public String toString() {
        try {
            return new String(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "Unsupported Encoding";
        }
    }

}
