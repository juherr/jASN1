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
import java.math.BigInteger;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerTag;

public class BerEnum extends BerInteger {

    public final static BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.PRIMITIVE, BerTag.ENUMERATED_TAG);

    public BerEnum() {
    }

    public BerEnum(byte[] code) {
        this.code = code;
    }

    public BerEnum(BigInteger val) {
        this.value = val;
    }

    public BerEnum(long val) {
        this.value = BigInteger.valueOf(val);
    }

    @Override
    public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {

        int codeLength = super.encode(os, false);

        if (withTag) {
            codeLength += tag.encode(os);
        }

        return codeLength;
    }

    @Override
    public int decode(InputStream is, boolean withTag) throws IOException {

        int codeLength = 0;

        if (withTag) {
            codeLength += tag.decodeAndCheck(is);
        }

        codeLength += super.decode(is, false);

        return codeLength;
    }

}
