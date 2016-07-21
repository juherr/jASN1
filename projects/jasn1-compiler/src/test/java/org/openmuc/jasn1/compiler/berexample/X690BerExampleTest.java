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
package org.openmuc.jasn1.compiler.berexample;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;
import org.openmuc.jasn1.compiler.HexConverter;
import org.openmuc.jasn1.compiler.berexample.generated.x690_ber_example.ChildInformation;
import org.openmuc.jasn1.compiler.berexample.generated.x690_ber_example.Date;
import org.openmuc.jasn1.compiler.berexample.generated.x690_ber_example.EmployeeNumber;
import org.openmuc.jasn1.compiler.berexample.generated.x690_ber_example.Name;
import org.openmuc.jasn1.compiler.berexample.generated.x690_ber_example.PersonnelRecord;

public class X690BerExampleTest {

	@Test
	public void encodingDecoding() throws IOException {

		BerByteArrayOutputStream berOS = new BerByteArrayOutputStream(1000);

		// Name name = new Name(new BerVisibleString("John"), new
		// BerVisibleString("P"), new BerVisibleString("Smith"));
		Name name = new Name();
		name.code = new byte[] { (byte) 0x10, (byte) 0x1A, (byte) 0x04, (byte) 0x4a, (byte) 0x6f, (byte) 0x68,
				(byte) 0x6e, (byte) 0x1A, (byte) 0x01, (byte) 0x50, (byte) 0x1A, (byte) 0x05, (byte) 0x53, (byte) 0x6d,
				(byte) 0x69, (byte) 0x74, (byte) 0x68 };
		BerVisibleString title = new BerVisibleString("Director".getBytes("US-ASCII"));
		// EmployeeNumber number = new EmployeeNumber(51);
		EmployeeNumber number = new EmployeeNumber();
		number.code = new byte[] { 0x01, 0x33 };
		Date dateOfHire = new Date("19710917".getBytes());
		Name nameOfSpouse = new Name(new BerVisibleString("Mary"), new BerVisibleString("T"),
				new BerVisibleString("Smith"));

		ChildInformation child1 = new ChildInformation(
				new Name(new BerVisibleString("Ralph"), new BerVisibleString("T"), new BerVisibleString("Smith")),
				new Date("19571111".getBytes()));

		System.out.println("192: " + HexConverter.toShortHexString("19571111".getBytes()));

		child1.encodeAndSave(80);

		System.out.println("geneCode: " + HexConverter.toShortHexString(child1.code));
		System.out.println("realCode: " + HexConverter.toShortHexString(new byte[] { (byte) 0x1f, (byte) 0x61,
				(byte) 0x11, (byte) 0x1A, (byte) 0x05, (byte) 0x52, (byte) 0x61, (byte) 0x6c, (byte) 0x70, (byte) 0x68,
				(byte) 0x1A, (byte) 0x01, (byte) 0x54, (byte) 0x1A, (byte) 0x05, (byte) 0x53, (byte) 0x6d, (byte) 0x69,
				(byte) 0x74, (byte) 0x68, (byte) 0xa0, (byte) 0x0a, (byte) 0x43, (byte) 0x08, (byte) 0x31, (byte) 0x39,
				(byte) 0x35, (byte) 0x37, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31 }));

		Assert.assertArrayEquals(new byte[] { (byte) 0x1f, (byte) 0x61, (byte) 0x11, (byte) 0x1A, (byte) 0x05,
				(byte) 0x52, (byte) 0x61, (byte) 0x6c, (byte) 0x70, (byte) 0x68, (byte) 0x1A, (byte) 0x01, (byte) 0x54,
				(byte) 0x1A, (byte) 0x05, (byte) 0x53, (byte) 0x6d, (byte) 0x69, (byte) 0x74, (byte) 0x68, (byte) 0xa0,
				(byte) 0x0a, (byte) 0x43, (byte) 0x08, (byte) 0x31, (byte) 0x39, (byte) 0x35, (byte) 0x37, (byte) 0x31,
				(byte) 0x31, (byte) 0x31, (byte) 0x31 }, child1.code);

		ChildInformation child2 = new ChildInformation(
				new Name(new BerVisibleString("Susan"), new BerVisibleString("B"), new BerVisibleString("Jones")),
				new Date("19590717".getBytes()));

		List<ChildInformation> childList = new ArrayList<ChildInformation>(2);
		childList.add(child1);
		childList.add(child2);

		PersonnelRecord.SubSeqOf_children childrenSeq = new PersonnelRecord.SubSeqOf_children(childList);

		PersonnelRecord personnelRecord = new PersonnelRecord(name, title, number, dateOfHire, nameOfSpouse,
				childrenSeq);

		personnelRecord.encode(berOS, true);

		byte[] expectedBytes = new byte[] { (byte) 0x60, (byte) 0x81, (byte) 0x85, (byte) 0x61, (byte) 0x10,
				(byte) 0x1A, (byte) 0x04, (byte) 0x4a, (byte) 0x6f, (byte) 0x68, (byte) 0x6e, (byte) 0x1A, (byte) 0x01,
				(byte) 0x50, (byte) 0x1A, (byte) 0x05, (byte) 0x53, (byte) 0x6d, (byte) 0x69, (byte) 0x74, (byte) 0x68,
				(byte) 0xa0, (byte) 0x0a, (byte) 0x1A, (byte) 0x08, (byte) 0x44, (byte) 0x69, (byte) 0x72, (byte) 0x65,
				(byte) 0x63, (byte) 0x74, (byte) 0x6f, (byte) 0x72, (byte) 0x42, (byte) 0x01, (byte) 0x33, (byte) 0xa1,
				(byte) 0x0a, (byte) 0x43, (byte) 0x08, (byte) 0x31, (byte) 0x39, (byte) 0x37, (byte) 0x31, (byte) 0x30,
				(byte) 0x39, (byte) 0x31, (byte) 0x37, (byte) 0xa2, (byte) 0x12, (byte) 0x61, (byte) 0x10, (byte) 0x1A,
				(byte) 0x04, (byte) 0x4d, (byte) 0x61, (byte) 0x72, (byte) 0x79, (byte) 0x1A, (byte) 0x01, (byte) 0x54,
				(byte) 0x1A, (byte) 0x05, (byte) 0x53, (byte) 0x6d, (byte) 0x69, (byte) 0x74, (byte) 0x68, (byte) 0xa3,
				(byte) 0x42, (byte) 0x31, (byte) 0x1f, (byte) 0x61, (byte) 0x11, (byte) 0x1A, (byte) 0x05, (byte) 0x52,
				(byte) 0x61, (byte) 0x6c, (byte) 0x70, (byte) 0x68, (byte) 0x1A, (byte) 0x01, (byte) 0x54, (byte) 0x1A,
				(byte) 0x05, (byte) 0x53, (byte) 0x6d, (byte) 0x69, (byte) 0x74, (byte) 0x68, (byte) 0xa0, (byte) 0x0a,
				(byte) 0x43, (byte) 0x08, (byte) 0x31, (byte) 0x39, (byte) 0x35, (byte) 0x37, (byte) 0x31, (byte) 0x31,
				(byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x1f, (byte) 0x61, (byte) 0x11, (byte) 0x1A, (byte) 0x05,
				(byte) 0x53, (byte) 0x75, (byte) 0x73, (byte) 0x61, (byte) 0x6e, (byte) 0x1A, (byte) 0x01, (byte) 0x42,
				(byte) 0x1A, (byte) 0x05, (byte) 0x4a, (byte) 0x6f, (byte) 0x6e, (byte) 0x65, (byte) 0x73, (byte) 0xa0,
				(byte) 0x0a, (byte) 0x43, (byte) 0x08, (byte) 0x31, (byte) 0x39, (byte) 0x35, (byte) 0x39, (byte) 0x30,
				(byte) 0x37, (byte) 0x31, (byte) 0x37 };

		System.out.println("encoded structure:");
		System.out.println(getByteArrayString(berOS.getArray()));

		Assert.assertArrayEquals(expectedBytes, berOS.getArray());

		ByteBuffer byteBuffer = berOS.getByteBuffer();
		Assert.assertEquals((byte) 0x60, byteBuffer.get());
		Assert.assertEquals((byte) 0x37, byteBuffer.get(byteBuffer.limit() - 1));

		ByteArrayInputStream bais = new ByteArrayInputStream(berOS.getArray());

		PersonnelRecord personnelRecord_decoded = new PersonnelRecord();
		personnelRecord_decoded.decode(bais, true);

		Assert.assertEquals("John", new String(personnelRecord_decoded.name.givenName.value));

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
