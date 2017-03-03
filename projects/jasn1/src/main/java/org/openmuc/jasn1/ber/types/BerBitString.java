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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;

public class BerBitString {

    public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
            BerIdentifier.PRIMITIVE, BerIdentifier.BIT_STRING_TAG);
    protected BerIdentifier id;

    public byte[] code = null;

    public byte[] value;
    public int numBits;

    public BerBitString() {
        id = identifier;
    }

    public BerBitString(byte[] value, int numBits) {
        id = identifier;
        if ((numBits < (((value.length - 1) * 8) + 1)) || (numBits > (value.length * 8))) {
            throw new IllegalArgumentException("numBits out of bound.");
        }

        this.value = value;
        this.numBits = numBits;

    }

    public BerBitString(byte[] code) {
        id = identifier;
        this.code = code;
    }

    public int encode(BerByteArrayOutputStream os, boolean explicit) throws IOException {

        int codeLength;

        if (code != null) {
            codeLength = code.length;
            for (int i = code.length - 1; i >= 0; i--) {
                os.write(code[i]);
            }
        }
        else {

            for (int i = (value.length - 1); i >= 0; i--) {
                os.write(value[i]);
            }
            os.write(value.length * 8 - numBits);

            codeLength = value.length + 1;

            codeLength += BerLength.encodeLength(os, codeLength);

        }

        if (explicit) {
            codeLength += id.encode(os);
        }

        return codeLength;
    }

    public int decode(InputStream is, boolean explicit) throws IOException {
        // could be encoded in primitiv and constructed mode
        // only primitiv mode is implemented

        int codeLength = 0;

        if (explicit) {
            codeLength += id.decodeAndCheck(is);
        }

        BerLength length = new BerLength();
        codeLength += length.decode(is);

        value = new byte[length.val - 1];

        int nextByte = is.read();
        if (nextByte == -1) {
            throw new EOFException("Unexpected end of input stream.");
        }

        numBits = (value.length * 8) - nextByte;

        if (value.length > 0) {
            Util.readFully(is, value);
        }

        codeLength += value.length + 1;

        return codeLength;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numBits; i++) {
            if (((value[i / 8] & 0xff) & (0x80 >> (i % 8))) == (0x80 >> (i % 8))) {
                sb.append('1');
            }
            else {
                sb.append('0');
            }
        }
        return sb.toString();
    }
}
