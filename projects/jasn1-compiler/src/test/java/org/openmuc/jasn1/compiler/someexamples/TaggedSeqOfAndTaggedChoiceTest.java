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
package org.openmuc.jasn1.compiler.someexamples;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerInteger;
import org.openmuc.jasn1.compiler.someexamples.generated.some_example.TaggedSeqOf;

public class TaggedSeqOfAndTaggedChoiceTest {

    @Test
    public void taggedSeqOfTest() throws Exception {

        List<BerInteger> integerList = new ArrayList<>();
        integerList.add(new BerInteger(3));
        integerList.add(new BerInteger(4));

        TaggedSeqOf taggedSeqOf = new TaggedSeqOf(integerList);

        BerByteArrayOutputStream berByteArrayOutputStream = new BerByteArrayOutputStream(1000);
        taggedSeqOf.encode(berByteArrayOutputStream, true);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("300AA303020103A303020104"),
                berByteArrayOutputStream.getArray());

        taggedSeqOf = new TaggedSeqOf(integerList);
        taggedSeqOf.decode(new ByteArrayInputStream(berByteArrayOutputStream.getArray()), true);

    }

}
