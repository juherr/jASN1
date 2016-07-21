package org.openmuc.jasn1.compiler.presentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerAny;
import org.openmuc.jasn1.ber.types.BerInteger;
import org.openmuc.jasn1.ber.types.BerObjectIdentifier;
import org.openmuc.jasn1.ber.types.BerOctetString;
import org.openmuc.jasn1.compiler.presentation.generated.CP_type;
import org.openmuc.jasn1.compiler.presentation.generated.Context_list;
import org.openmuc.jasn1.compiler.presentation.generated.Fully_encoded_data;
import org.openmuc.jasn1.compiler.presentation.generated.Mode_selector;
import org.openmuc.jasn1.compiler.presentation.generated.PDV_list;
import org.openmuc.jasn1.compiler.presentation.generated.User_data;

public class PresentationLayerTest {

	@Test
	public void encodingDecoding() throws IOException {

		BerByteArrayOutputStream berOS = new BerByteArrayOutputStream(1000);

		List<BerObjectIdentifier> berObjectIdentifierList = new ArrayList<BerObjectIdentifier>(1);
		berObjectIdentifierList.add(new BerObjectIdentifier(new int[] { 2, 1, 1 }));

		Context_list.SubSeq.SubSeqOf_transfer_syntax_name_list tsnl = new Context_list.SubSeq.SubSeqOf_transfer_syntax_name_list(
				berObjectIdentifierList);

		Context_list.SubSeq context_listSubSeq = new Context_list.SubSeq(new BerInteger(1), new BerObjectIdentifier(
				new int[] { 2, 2, 1, 0, 1 }), tsnl);

		Context_list.SubSeq context_listSubSeq2 = new Context_list.SubSeq(new BerInteger(3), new BerObjectIdentifier(
				new int[] { 1, 0, 9506, 2, 1 }), tsnl);

		List<Context_list.SubSeq> context_listSubSeqList = new ArrayList<Context_list.SubSeq>(2);

		context_listSubSeqList.add(context_listSubSeq);
		context_listSubSeqList.add(context_listSubSeq2);

		Context_list context_list = new Context_list(context_listSubSeqList);

		PDV_list.SubChoice_presentation_data_values presDataValues = new PDV_list.SubChoice_presentation_data_values(
				new BerAny(91), null, null);
		PDV_list pdvList = new PDV_list(null, new BerInteger(1), presDataValues);
		List<PDV_list> pdvListList = new ArrayList<PDV_list>(1);
		pdvListList.add(pdvList);
		Fully_encoded_data fullyEncodedData = new Fully_encoded_data(pdvListList);
		User_data userData = new User_data(null, fullyEncodedData);

		CP_type.SubSeq_normal_mode_parameters normalModeParameter = new CP_type.SubSeq_normal_mode_parameters(null,
				new BerOctetString(new byte[] { 0, 0, 0, 1 }), new BerOctetString(new byte[] { 0, 0, 0, 1 }),
				context_list, null, null, null, userData);

		Mode_selector modeSelector = new Mode_selector(new BerInteger(1));

		CP_type cpType = new CP_type(modeSelector, normalModeParameter);

		cpType.encode(berOS, true);

		byte[] expectedBytes = new byte[] { (byte) 0x31, (byte) 0x81, (byte) 0x9d, (byte) 0xa0, (byte) 0x03,
				(byte) 0x80, (byte) 0x01, (byte) 0x01, (byte) 0xa2, (byte) 0x81, (byte) 0x95, (byte) 0x81, (byte) 0x04,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x82, (byte) 0x04, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x01, (byte) 0xa4, (byte) 0x23, (byte) 0x30, (byte) 0x0f, (byte) 0x02, (byte) 0x01,
				(byte) 0x01, (byte) 0x06, (byte) 0x04, (byte) 0x52, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x30,
				(byte) 0x04, (byte) 0x06, (byte) 0x02, (byte) 0x51, (byte) 0x01, (byte) 0x30, (byte) 0x10, (byte) 0x02,
				(byte) 0x01, (byte) 0x03, (byte) 0x06, (byte) 0x05, (byte) 0x28, (byte) 0xca, (byte) 0x22, (byte) 0x02,
				(byte) 0x01, (byte) 0x30, (byte) 0x04, (byte) 0x06, (byte) 0x02, (byte) 0x51, (byte) 0x01, (byte) 0x61,
				(byte) 0x62, (byte) 0x30, (byte) 0x60, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0xa0, (byte) 0x5b };

		// System.out.println(getByteArrayString(berOS.getArray()));
		// System.out.println(getByteArrayString(expectedBytes));

		Assert.assertArrayEquals(expectedBytes, berOS.getArray());

		ByteArrayInputStream bais = new ByteArrayInputStream(berOS.getArray());

		CP_type cpType_decoded = new CP_type();
		cpType_decoded.decode(bais, true);

		Assert.assertEquals(
				"2.2.1.0.1",
				cpType_decoded.normal_mode_parameters.presentation_context_definition_list.seqOf.get(0).abstract_syntax_name
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
			if ((l != 1) && ((l - 1) % 8 == 0))
				builder.append(' ');
			if ((l != 1) && ((l - 1) % 16 == 0))
				builder.append('\n');
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
