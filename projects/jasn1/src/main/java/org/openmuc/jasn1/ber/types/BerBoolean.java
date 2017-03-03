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

public class BerBoolean {

    public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS,
            BerIdentifier.PRIMITIVE, BerIdentifier.BOOLEAN_TAG);
    public BerIdentifier id;

    public byte[] code = null;

    public boolean value;

    public BerBoolean() {
        id = identifier;
    }

    public BerBoolean(byte[] code) {
        id = identifier;
        this.code = code;
    }

    public BerBoolean(boolean value) {
        id = identifier;
        this.value = value;
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

            codeLength = 1;

            if (value) {
                os.write(0xff);
            }
            else {
                os.write(0);
            }

            codeLength += BerLength.encodeLength(os, codeLength);
        }

        if (explicit) {
            codeLength += id.encode(os);
        }

        return codeLength;
    }

    public int decode(InputStream is, boolean explicit) throws IOException {

        int codeLength = 0;

        if (explicit) {
            codeLength += id.decodeAndCheck(is);
        }

        BerLength length = new BerLength();
        codeLength += length.decode(is);

        if (length.val != 1) {
            throw new IOException("Decoded length of BerBoolean is not correct");
        }

        int nextByte = is.read();
        if (nextByte == -1) {
            throw new EOFException("Unexpected end of input stream.");
        }

        codeLength++;
        if (nextByte == 0) {
            value = false;
        }
        else {
            value = true;
        }

        return codeLength;
    }

    public void encodeAndSave(int encodingSizeGuess) throws IOException {
        BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);
        encode(os, false);
        code = os.getArray();
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
