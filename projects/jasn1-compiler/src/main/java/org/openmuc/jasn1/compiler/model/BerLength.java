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
package org.openmuc.jasn1.compiler.model;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

public class BerLength {

    public int val;

    public BerLength() {
    }

    public int decode(InputStream is) throws IOException {

        val = is.read();
        if (val == -1) {
            throw new EOFException("Unexpected end of input stream.");
        }
        int length = 1;

        if ((val & 0x80) != 0) {
            int lengthLength = val & 0x7f;

            // check for indefinite length
            if (lengthLength == 0) {
                val = -1;
                return 1;
            }

            if (lengthLength > 4) {
                throw new IOException("Length is out of bound!");
            }

            val = 0;

            length += lengthLength;

            for (int i = 0; i < lengthLength; i++) {
                int nextByte = is.read();
                if (nextByte == -1) {
                    throw new EOFException("Unexpected end of input stream.");
                }
                val |= nextByte << (8 * (lengthLength - i - 1));
            }
        }

        return length;
    }

    public static int encodeLength(BerByteArrayOutputStream os, int length) throws IOException {
        // the indefinite form is ignored for now

        if (length <= 127) {
            // this is the short form, it is coded differently than the long
            // form for values > 127
            os.write((byte) length);
            return 1;
        }
        else {
            int numLengthBytes = 1;

            while (((int) (Math.pow(2, 8 * numLengthBytes) - 1)) < length) {
                numLengthBytes++;
            }

            for (int i = 0; i < numLengthBytes; i++) {
                os.write((length >> 8 * i) & 0xff);
            }

            os.write(0x80 | numLengthBytes);

            return 1 + numLengthBytes;
        }

    }

}
