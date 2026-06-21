/*
 * Copyright 2011-17 Fraunhofer ISE
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
import java.io.Serializable;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerLength;
import org.openmuc.jasn1.ber.BerTag;
import org.openmuc.jasn1.util.HexConverter;

public class BerAny implements Serializable {

    private static final long serialVersionUID = 1L;

    public byte[] value;

    public BerAny() {
    }

    public BerAny(byte[] value) {
        this.value = value;
    }

    public int encode(BerByteArrayOutputStream os) throws IOException {
        os.write(value);
        return value.length;
    }

    public int decode(InputStream is) throws IOException {

        return decode(is, null);
    }

    public int decode(InputStream is, BerTag tag) throws IOException {

        int decodedLength = 0;

        int tagLength;

        if (tag == null) {
            tag = new BerTag();
            tagLength = tag.decode(is);
            decodedLength += tagLength;
        }
        else {
            tagLength = tag.encode(new BerByteArrayOutputStream(10));
        }

        BerLength lengthField = new BerLength();
        int lengthLength = lengthField.decode(is);
        decodedLength += lengthLength + lengthField.val;

        value = new byte[tagLength + lengthLength + lengthField.val];

        Util.readFully(is, value, tagLength + lengthLength, lengthField.val);
        BerByteArrayOutputStream os = new BerByteArrayOutputStream(value, tagLength + lengthLength - 1);
        BerLength.encodeLength(os, lengthField.val);
        tag.encode(os);

        return decodedLength;
    }

    @Override
    public String toString() {
        return HexConverter.toShortHexString(value);
    }

}
