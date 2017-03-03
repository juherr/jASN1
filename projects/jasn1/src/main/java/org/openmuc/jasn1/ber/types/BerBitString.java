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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerLength;
import org.openmuc.jasn1.ber.BerTag;

public class BerBitString {

    public final static BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.PRIMITIVE, BerTag.BIT_STRING_TAG);

    public byte[] code = null;

    public byte[] value;
    public int numBits;

    public BerBitString() {
    }

    public BerBitString(byte[] value, int numBits) {
        if ((numBits < (((value.length - 1) * 8) + 1)) || (numBits > (value.length * 8))) {
            throw new IllegalArgumentException("numBits out of bound.");
        }

        this.value = value;
        this.numBits = numBits;

    }

    public BerBitString(byte[] code) {
        this.code = code;
    }

    public int encode(BerByteArrayOutputStream os) throws IOException {
        return encode(os, true);
    }

    public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {

        if (code != null) {
            for (int i = code.length - 1; i >= 0; i--) {
                os.write(code[i]);
            }
            if (withTag) {
                return tag.encode(os) + code.length;
            }
            return code.length;
        }

        for (int i = (value.length - 1); i >= 0; i--) {
            os.write(value[i]);
        }
        os.write(value.length * 8 - numBits);

        int codeLength = value.length + 1;

        codeLength += BerLength.encodeLength(os, codeLength);

        if (withTag) {
            codeLength += tag.encode(os);
        }

        return codeLength;
    }

    public int decode(InputStream is) throws IOException {
        return decode(is, true);
    }

    public int decode(InputStream is, boolean withTag) throws IOException {
        // could be encoded in primitiv and constructed mode
        // only primitiv mode is implemented

        int codeLength = 0;

        if (withTag) {
            codeLength += tag.decodeAndCheck(is);
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
