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

import javax.xml.bind.DatatypeConverter;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

public class BerAny {

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

    public int decode(InputStream is, int length) throws IOException {
        value = new byte[length];

        if (length != 0) {
            Util.readFully(is, value);
        }
        return length;
    }

    @Override
    public String toString() {
        return DatatypeConverter.printHexBinary(value);
    }

}
