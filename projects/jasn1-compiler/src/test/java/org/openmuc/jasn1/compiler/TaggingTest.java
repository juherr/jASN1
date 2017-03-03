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
package org.openmuc.jasn1.compiler;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerAny;
import org.openmuc.jasn1.ber.types.BerBoolean;
import org.openmuc.jasn1.ber.types.BerInteger;
import org.openmuc.jasn1.compiler.tagging_test.ExplicitlyTaggedSeqOf;
import org.openmuc.jasn1.compiler.tagging_test.ExplicitlyTaggedSequence;
import org.openmuc.jasn1.compiler.tagging_test.ExplicitlyTaggedSet;
import org.openmuc.jasn1.compiler.tagging_test.ExplicitlyTaggedSetOf;
import org.openmuc.jasn1.compiler.tagging_test.ImplicitlyRetaggedTaggedChoice;
import org.openmuc.jasn1.compiler.tagging_test.ImplicitlyTaggedInteger;
import org.openmuc.jasn1.compiler.tagging_test.RetaggedUntaggedChoice;
import org.openmuc.jasn1.compiler.tagging_test.SeqOfExplicitlyTaggedType;
import org.openmuc.jasn1.compiler.tagging_test.SequenceOfDirectTypes;
import org.openmuc.jasn1.compiler.tagging_test.SequenceOfDirectTypes.UntaggedChoice;
import org.openmuc.jasn1.compiler.tagging_test.TaggedChoice;

public class TaggingTest {

    @Test
    public void seqOfExplicitlyTaggedTypeTest() throws Exception {

        SeqOfExplicitlyTaggedType seqOf = new SeqOfExplicitlyTaggedType();
        List<BerInteger> integerList = seqOf.getBerInteger();

        integerList.add(new BerInteger(3));
        integerList.add(new BerInteger(4));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        seqOf.encode(os);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("300AA303020103A303020104"), os.getArray());

        seqOf = new SeqOfExplicitlyTaggedType();
        seqOf.decode(new ByteArrayInputStream(os.getArray()));

    }

    @Test
    public void retaggedChoiceTest() throws Exception {

        RetaggedUntaggedChoice choice = new RetaggedUntaggedChoice();
        choice.setMyInteger(new BerInteger(1));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        choice.encode(os);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("BF2103830101"), os.getArray());

        choice = new RetaggedUntaggedChoice();
        choice.decode(new ByteArrayInputStream(os.getArray()));

        Assert.assertNotNull(choice.getMyInteger());
        Assert.assertNull(choice.getMyBoolean());

    }

    @Test
    public void explicitlyTaggedSequenceTest() throws Exception {

        ExplicitlyTaggedSequence sequence = new ExplicitlyTaggedSequence();
        sequence.setMyInteger(new BerInteger(1));
        sequence.setMyBoolean(new BerBoolean(true));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        sequence.encode(os);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("BF210830060201010101FF"), os.getArray());

        sequence = new ExplicitlyTaggedSequence();
        sequence.decode(new ByteArrayInputStream(os.getArray()));

        Assert.assertNotNull(sequence.getMyInteger());
        Assert.assertNotNull(sequence.getMyBoolean());

    }

    @Test
    public void explicitlyTaggedSetTest() throws Exception {

        ExplicitlyTaggedSet set = new ExplicitlyTaggedSet();
        set.setMyInteger(new BerInteger(1));
        set.setMyBoolean(new BerBoolean(true));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        set.encode(os);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("BF210831060201010101FF"), os.getArray());

        set = new ExplicitlyTaggedSet();
        set.decode(new ByteArrayInputStream(os.getArray()));

        Assert.assertNotNull(set.getMyInteger());
        Assert.assertNotNull(set.getMyBoolean());
    }

    @Test
    public void explicitlyTaggedSeqOfTest() throws Exception {

        ExplicitlyTaggedSeqOf seqOf = new ExplicitlyTaggedSeqOf();
        List<BerInteger> integerList = seqOf.getBerInteger();

        integerList.add(new BerInteger(3));
        integerList.add(new BerInteger(4));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        seqOf.encode(os);

        System.out.println("seqOf : " + DatatypeConverter.printHexBinary(os.getArray()));

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("BF21083006020103020104"), os.getArray());

        seqOf = new ExplicitlyTaggedSeqOf();
        seqOf.decode(new ByteArrayInputStream(os.getArray()));
    }

    @Test
    public void explicitlyTaggedSetOfTest() throws Exception {

        ExplicitlyTaggedSetOf setOf = new ExplicitlyTaggedSetOf();
        List<BerInteger> integerList = setOf.getBerInteger();

        integerList.add(new BerInteger(3));
        integerList.add(new BerInteger(4));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        setOf.encode(os);

        System.out.println("setOf : " + DatatypeConverter.printHexBinary(os.getArray()));

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("BF21083106020103020104"), os.getArray());

        setOf = new ExplicitlyTaggedSetOf();
        setOf.decode(new ByteArrayInputStream(os.getArray()));
    }

    @Test
    public void taggedChoiceTest() throws Exception {

        TaggedChoice choice = new TaggedChoice();
        choice.setMyInteger(new BerInteger(1));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        choice.encode(os);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("BF2203020101"), os.getArray());

        choice = new TaggedChoice();
        choice.decode(new ByteArrayInputStream(os.getArray()));

        Assert.assertNotNull(choice.getMyInteger());
        Assert.assertNull(choice.getMyBoolean());

    }

    @Test
    public void implicitlyTaggedIntegerTest() throws Exception {

        ImplicitlyTaggedInteger implicitlyTaggedInteger = new ImplicitlyTaggedInteger(1);

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        implicitlyTaggedInteger.encode(os);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("9F210101"), os.getArray());

    }

    @Test
    public void implicitlyRetaggedTaggedChoiceTest() throws Exception {

        ImplicitlyRetaggedTaggedChoice choice = new ImplicitlyRetaggedTaggedChoice();
        choice.setMyInteger(new BerInteger(1));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        choice.encode(os);

        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("A303020101"), os.getArray());

    }

    @Test
    public void sequenceOfDirectTypesTest() throws Exception {

        SequenceOfDirectTypes sequence = new SequenceOfDirectTypes();
        sequence.setUntaggedInt(new BerInteger(1));
        sequence.setExplicitlyTaggedInt(new BerInteger(2));
        sequence.setImplicitlyTaggedInt(new BerInteger(3));

        UntaggedChoice untaggedChoice = new UntaggedChoice();
        untaggedChoice.setMyBoolean(new BerBoolean(true));
        sequence.setUntaggedChoice(untaggedChoice);

        SequenceOfDirectTypes.TaggedChoice taggedChoice = new SequenceOfDirectTypes.TaggedChoice();
        taggedChoice.setMyInteger(new BerInteger(4));
        sequence.setTaggedChoice(taggedChoice);

        sequence.setTaggedAny(new BerAny(new byte[] { 2, 1, 1 }));

        BerByteArrayOutputStream os = new BerByteArrayOutputStream(1000);
        sequence.encode(os);

        Assert.assertArrayEquals(
                DatatypeConverter.parseHexBinary("BF2B18020101A1030201028201038401FFA503830104A603020101"),
                os.getArray());

        sequence = new SequenceOfDirectTypes();
        sequence.decode(new ByteArrayInputStream(os.getArray()));

        Assert.assertEquals(1, sequence.getUntaggedInt().value.intValue());
        Assert.assertEquals(2, sequence.getExplicitlyTaggedInt().value.intValue());
        Assert.assertEquals(3, sequence.getImplicitlyTaggedInt().value.intValue());
        Assert.assertEquals(true, untaggedChoice.getMyBoolean().value);
        Assert.assertEquals(4, sequence.getTaggedChoice().getMyInteger().value.intValue());
        Assert.assertArrayEquals(DatatypeConverter.parseHexBinary("020101"), sequence.getTaggedAny().value);
        Assert.assertNull(sequence.getUntaggedChoice2());

    }

}
