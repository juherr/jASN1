import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import generated.ChildInformation;
import generated.Date;
import generated.EmployeeNumber;
import generated.Name;
import generated.PersonnelRecord;

public class EncodeDecodeSample {

	public static void main(String[] args) throws IOException {

		BerByteArrayOutputStream berOS = new BerByteArrayOutputStream(1000);

		// Name name = new Name(new BerVisibleString("John"), new
		// BerVisibleString("P"), new BerVisibleString("Smith"));
		// instead of creating the Name object as in previous statement you can
		// assign the byte code directly as in the following statement. The
		// encode function of the name object will then simply insert this byte
		// array in the BerOutputStream. This can speed up things if the code
		// for certain structures is known and does not change.
		Name name = new Name();
		name.code = new byte[] { (byte) 0x10, (byte) 0x1A, (byte) 0x04, (byte) 0x4a, (byte) 0x6f, (byte) 0x68,
				(byte) 0x6e, (byte) 0x1A, (byte) 0x01, (byte) 0x50, (byte) 0x1A, (byte) 0x05, (byte) 0x53, (byte) 0x6d,
				(byte) 0x69, (byte) 0x74, (byte) 0x68 };

		BerVisibleString title = new BerVisibleString("Director".getBytes("US-ASCII"));
		EmployeeNumber number = new EmployeeNumber(51);
		Date dateOfHire = new Date("19710917");
		Name nameOfSpouse = new Name(new BerVisibleString("Mary"), new BerVisibleString("T"), new BerVisibleString(
				"Smith"));

		ChildInformation child1 = new ChildInformation(new Name(new BerVisibleString("Ralph"),
				new BerVisibleString("T"), new BerVisibleString("Smith")), new Date("19571111"));
		ChildInformation child2 = new ChildInformation(new Name(new BerVisibleString("Susan"),
				new BerVisibleString("B"), new BerVisibleString("Jones")), new Date("19590717"));

		List<ChildInformation> childList = new ArrayList<ChildInformation>(2);
		childList.add(child1);

		// encodeAndSave will start the encoding and save the result in
		// child1.code. This is usefull if the same structure will have to be
		// encoded several times as part of different structures. Using this
		// function will make sure that the real encoding is only done once.
		child1.encodeAndSave(80);

		childList.add(child2);

		PersonnelRecord.SubSeqOf_children childrenSeq = new PersonnelRecord.SubSeqOf_children(childList);

		PersonnelRecord personnelRecord = new PersonnelRecord(name, title, number, dateOfHire, nameOfSpouse,
				childrenSeq);

		personnelRecord.encode(berOS, true);

		System.out.println("result:");
		System.out.println(getByteArrayString(berOS.getArray()));

		ByteArrayInputStream bais = new ByteArrayInputStream(berOS.getArray());

		PersonnelRecord personnelRecord_decoded = new PersonnelRecord();
		personnelRecord_decoded.decode(bais, true);

		System.out.println("");
		System.out.println("PersonnelRecord.name.givenName = " + personnelRecord_decoded.name.givenName);

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
