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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

public class BerRealTest {

    @Test
    public void codingZero() throws IOException {
        BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

        BerReal berReal = new BerReal(0);
        berReal.encode(berStream, true);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("0900"), berStream.getArray());

        ByteArrayInputStream berInputStream = new ByteArrayInputStream(berStream.getArray());
        BerReal berRealDecoded = new BerReal();
        berRealDecoded.decode(berInputStream, true);

        Assert.assertEquals(0, berRealDecoded.value, 0.01);
    }

    @Test
    public void codingNegInf() throws IOException {
        BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

        BerReal berReal = new BerReal(Double.NEGATIVE_INFINITY);
        berReal.encode(berStream, true);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("090141"), berStream.getArray());

        ByteArrayInputStream berInputStream = new ByteArrayInputStream(berStream.getArray());
        BerReal berRealDecoded = new BerReal();
        berRealDecoded.decode(berInputStream, true);

        Assert.assertEquals(Double.NEGATIVE_INFINITY, berRealDecoded.value, 0.01);
    }

    @Test
    public void coding1dot5() throws IOException {
        BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

        BerReal berReal = new BerReal(1.5);
        berReal.encode(berStream, true);

        // System.out.println(DatatypeConverter.printHexBinary(berStream.getArray()));
        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("090380FF03"), berStream.getArray());

        ByteArrayInputStream berInputStream = new ByteArrayInputStream(berStream.getArray());
        BerReal berRealDecoded = new BerReal();
        berRealDecoded.decode(berInputStream, true);

        Assert.assertEquals(1.5, berRealDecoded.value, 0.01);
    }

    @Test
    public void coding0dot7() throws IOException {
        final BerReal orig = new BerReal(0.7);
        final BerByteArrayOutputStream baos = new BerByteArrayOutputStream(100, true);
        orig.encode(baos, true);

        // System.out.println(DatatypeConverter.printHexBinary(baos.getArray()));
        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("090980CC0B333333333333"), baos.getArray());

        final BerReal decoded = new BerReal();
        decoded.decode(new ByteArrayInputStream(baos.getArray()), true);

        Assert.assertEquals(orig.value, decoded.value, 0.001);
    }

}
