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

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

public class BerGeneralizedTimeTest {

    @Test
    public void explicitEncoding() throws IOException {
        BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

        byte[] byteArray = new byte[] { 0x01, 0x02, 0x03 };
        BerGeneralizedTime berGeneralizedTime = new BerGeneralizedTime(byteArray);
        int length = berGeneralizedTime.encode(berStream, true);
        Assert.assertEquals(5, length);

        byte[] expectedBytes = new byte[] { 24, 0x03, 0x01, 0x02, 0x03 };
        Assert.assertArrayEquals(expectedBytes, berStream.getArray());

    }

}
