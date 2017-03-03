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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerAnyNoDecode;
import org.openmuc.jasn1.ber.types.BerInteger;
import org.openmuc.jasn1.compiler.iso8823_presentation.AbstractSyntaxName;
import org.openmuc.jasn1.compiler.iso8823_presentation.CPType;
import org.openmuc.jasn1.compiler.iso8823_presentation.CalledPresentationSelector;
import org.openmuc.jasn1.compiler.iso8823_presentation.CallingPresentationSelector;
import org.openmuc.jasn1.compiler.iso8823_presentation.ContextList;
import org.openmuc.jasn1.compiler.iso8823_presentation.FullyEncodedData;
import org.openmuc.jasn1.compiler.iso8823_presentation.ModeSelector;
import org.openmuc.jasn1.compiler.iso8823_presentation.PDVList;
import org.openmuc.jasn1.compiler.iso8823_presentation.PresentationContextDefinitionList;
import org.openmuc.jasn1.compiler.iso8823_presentation.PresentationContextIdentifier;
import org.openmuc.jasn1.compiler.iso8823_presentation.TransferSyntaxName;
import org.openmuc.jasn1.compiler.iso8823_presentation.UserData;

public class PresentationLayerTest {

    @Test
    public void encodingDecoding() throws IOException {

        BerByteArrayOutputStream berOS = new BerByteArrayOutputStream(1000);

        List<TransferSyntaxName> berObjectIdentifierList = new ArrayList<>(1);
        berObjectIdentifierList.add(new TransferSyntaxName(new int[] { 2, 1, 1 }));

        ContextList.SEQUENCE.TransferSyntaxNameList tsnl = new ContextList.SEQUENCE.TransferSyntaxNameList(
                berObjectIdentifierList);

        ContextList.SEQUENCE context_listSubSeq = new ContextList.SEQUENCE(new PresentationContextIdentifier(1),
                new AbstractSyntaxName(new int[] { 2, 2, 1, 0, 1 }), tsnl);

        ContextList.SEQUENCE context_listSubSeq2 = new ContextList.SEQUENCE(new PresentationContextIdentifier(3),
                new AbstractSyntaxName(new int[] { 1, 0, 9506, 2, 1 }), tsnl);

        List<ContextList.SEQUENCE> context_listSubSeqList = new ArrayList<>(2);

        context_listSubSeqList.add(context_listSubSeq);
        context_listSubSeqList.add(context_listSubSeq2);

        PresentationContextDefinitionList context_list = new PresentationContextDefinitionList(context_listSubSeqList);

        PDVList.PresentationDataValues presDataValues = new PDVList.PresentationDataValues(new BerAnyNoDecode(91), null,
                null);
        PDVList pdvList = new PDVList(null, new PresentationContextIdentifier(1), presDataValues);
        List<PDVList> pdvListList = new ArrayList<>(1);
        pdvListList.add(pdvList);
        FullyEncodedData fullyEncodedData = new FullyEncodedData(pdvListList);
        UserData userData = new UserData(null, fullyEncodedData);

        CPType.NormalModeParameters normalModeParameter = new CPType.NormalModeParameters(null,
                new CallingPresentationSelector(new byte[] { 0, 0, 0, 1 }),
                new CalledPresentationSelector(new byte[] { 0, 0, 0, 1 }), context_list, null, null, null, userData);

        ModeSelector modeSelector = new ModeSelector(new BerInteger(1));

        CPType cpType = new CPType(modeSelector, normalModeParameter);

        cpType.encode(berOS, true);

        byte[] expectedBytes = new byte[] { (byte) 0x31, (byte) 0x81, (byte) 0x9d, (byte) 0xa0, (byte) 0x03,
                (byte) 0x80, (byte) 0x01, (byte) 0x01, (byte) 0xa2, (byte) 0x81, (byte) 0x95, (byte) 0x81, (byte) 0x04,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x82, (byte) 0x04, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01, (byte) 0xa4, (byte) 0x23, (byte) 0x30, (byte) 0x0f, (byte) 0x02, (byte) 0x01,
                (byte) 0x01, (byte) 0x06, (byte) 0x04, (byte) 0x52, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x30,
                (byte) 0x04, (byte) 0x06, (byte) 0x02, (byte) 0x51, (byte) 0x01, (byte) 0x30, (byte) 0x10, (byte) 0x02,
                (byte) 0x01, (byte) 0x03, (byte) 0x06, (byte) 0x05, (byte) 0x28, (byte) 0xca, (byte) 0x22, (byte) 0x02,
                (byte) 0x01, (byte) 0x30, (byte) 0x04, (byte) 0x06, (byte) 0x02, (byte) 0x51, (byte) 0x01, (byte) 0x61,
                (byte) 0x62, (byte) 0x30, (byte) 0x60, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0xa0,
                (byte) 0x5b };

        // System.out.println(getByteArrayString(berOS.getArray()));
        // System.out.println(getByteArrayString(expectedBytes));

        Assert.assertArrayEquals(expectedBytes, berOS.getArray());

        ByteArrayInputStream bais = new ByteArrayInputStream(berOS.getArray());

        CPType cpType_decoded = new CPType();
        cpType_decoded.decode(bais, true);

        Assert.assertEquals("2.2.1.0.1",
                cpType_decoded.normalModeParameters.presentationContextDefinitionList.seqOf.get(0).abstractSyntaxName
                        .toString());

        // System.out
        // .println("presentation_context_identifier= "
        // +
        // cpType_decoded.normal_mode_parameters.presentation_context_definition_list.seqOf.get(0).abstract_syntax_name);

    }

    public static String getByteArrayString(byte[] byteArray) {
        StringBuilder builder = new StringBuilder();
        int l = 1;
        for (byte b : byteArray) {
            if ((l != 1) && ((l - 1) % 8 == 0)) {
                builder.append(' ');
            }
            if ((l != 1) && ((l - 1) % 16 == 0)) {
                builder.append('\n');
            }
            l++;
            builder.append("0x");
            String hexString = Integer.toHexString(b & 0xff);
            if (hexString.length() == 1) {
                builder.append(0);
            }
            builder.append(hexString + " ");
        }
        return builder.toString();
    }
}
