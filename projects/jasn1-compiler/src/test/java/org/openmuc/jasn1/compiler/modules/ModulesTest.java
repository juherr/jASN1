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
package org.openmuc.jasn1.compiler.modules;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;
import org.openmuc.jasn1.compiler.HexConverter;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.ChildInformation;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.Date;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.EmployeeNumber;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.MyBitString;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.MyDate1;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.MyInt;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.MyInt2;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.Name;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.PersonnelRecord;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.PersonnelRecord.SubSeqOf_children;
import org.openmuc.jasn1.compiler.modules.generated.x690.ber_example.TestChoice;

public class ModulesTest {

	@Test
	public void encodingDecoding() throws IOException {

		BerByteArrayOutputStream berOS = new BerByteArrayOutputStream(1000);

		MyDate1 dateOfHire = new MyDate1();
		// MyDate1 dateOfHire = new MyDate1("19710917");
		dateOfHire.value = new String("19710917").getBytes();

		dateOfHire.encode(berOS, true);

		MyInt2 myInt2Encode = new MyInt2(2);
		berOS.reset();
		myInt2Encode.encode(berOS, true);
		System.out.println("myInt2: " + HexConverter.toShortHexString(berOS.getArray()));

		MyInt2 myInt2Decode = new MyInt2();
		byte[] code = HexConverter.fromShortHexString("a303020102");
		InputStream is = new ByteArrayInputStream(code);
		myInt2Decode.decode(is, true);
		Assert.assertEquals(myInt2Decode.value, 2l);

		PersonnelRecord pr = new PersonnelRecord();

		pr.name = new Name();
		pr.name.givenName = new BerVisibleString("givenName".getBytes());
		pr.name.familyName = new BerVisibleString("familyName".getBytes());
		pr.name.initial = new BerVisibleString("initial".getBytes());

		pr.title = new BerVisibleString("title".getBytes());

		pr.number = new EmployeeNumber(1);

		pr.dateOfHire = new Date("23121981".getBytes());

		pr.nameOfSpouse = pr.name;

		ChildInformation child = new ChildInformation();
		child.name = new Name("child name".getBytes());
		child.dateOfBirth = new Date("12121912".getBytes());
		List<ChildInformation> children = new ArrayList<>();
		children.add(child);
		children.add(child);
		pr.children = new SubSeqOf_children(children);

		pr.testBitString = new MyBitString(new byte[] { (byte) 0x80, (byte) 0xff }, 10);

		pr.test = new MyInt(3);

		TestChoice testChoice = new TestChoice(child, null);

		pr.test2 = testChoice;

		pr.test3 = testChoice;

		pr.test4 = testChoice;

		pr.test5 = testChoice;

		pr.test6 = testChoice;

		System.out.println("pr: " + pr);
	}

}
