/**
 * This file was automatically produced by jasn1-complier (http://www.openmuc.org)
 */

package x690_ber_example;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.LinkedList;
import org.openmuc.jasn1.ber.*;
import org.openmuc.jasn1.ber.types.*;

public class PersonnelRecord {

	public static class SubSeqOf_children {

		public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS, BerIdentifier.CONSTRUCTED, 16);

		public List<ChildInformation> seqOf = null;

		public SubSeqOf_children() {
		}

		public SubSeqOf_children(List<ChildInformation> seqOf) {
			this.seqOf = seqOf;
		}

		public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {
			int codeLength = 0;

			for (int i = (seqOf.size() - 1); i >= 0; i--) {
				codeLength += seqOf.get(i).encode(berOStream, true);
			}

			codeLength += BerLength.encodeLength(berOStream, codeLength);

			if (explicit) {
				codeLength += identifier.encode(berOStream);
			}

			return codeLength;
		}

		public int decode(InputStream iStream, boolean explicit) throws IOException {
			int codeLength = 0;
			int subCodeLength = 0;
			seqOf = new LinkedList<ChildInformation>();

			if (explicit) {
				codeLength += identifier.decodeAndCheck(iStream);
			}

			BerLength length = new BerLength();
			codeLength += length.decode(iStream);

			while (subCodeLength < length.val) {
				ChildInformation element = new ChildInformation();
				subCodeLength += element.decode(iStream, true);
				seqOf.add(element);
			}
			if (subCodeLength != length.val) {
				throw new IOException("Decoded SequenceOf or SetOf has wrong length tag");

			}
			codeLength += subCodeLength;

			return codeLength;
		}
	}

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.APPLICATION_CLASS, BerIdentifier.CONSTRUCTED, 0);

	public Name name = null;

	public BerVisibleString title = null;

	public EmployeeNumber number = null;

	public Date dateOfHire = null;

	public Name nameOfSpouse = null;

	public SubSeqOf_children children = null;

	public PersonnelRecord() {
	}

	public PersonnelRecord(Name name, BerVisibleString title, EmployeeNumber number, Date dateOfHire, Name nameOfSpouse, SubSeqOf_children children) {
		this.name = name;
		this.title = title;
		this.number = number;
		this.dateOfHire = dateOfHire;
		this.nameOfSpouse = nameOfSpouse;
		this.children = children;
	}

	public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {
		int codeLength = 0;
		int sublength;

		if (children != null) {
			codeLength += children.encode(berOStream, false);
			codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 3)).encode(berOStream);
		}
		
		sublength = nameOfSpouse.encode(berOStream, true);
		codeLength += sublength;
		codeLength += BerLength.encodeLength(berOStream, sublength);
		codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 2)).encode(berOStream);
		
		sublength = dateOfHire.encode(berOStream, true);
		codeLength += sublength;
		codeLength += BerLength.encodeLength(berOStream, sublength);
		codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 1)).encode(berOStream);
		
		codeLength += number.encode(berOStream, true);
		
		sublength = title.encode(berOStream, true);
		codeLength += sublength;
		codeLength += BerLength.encodeLength(berOStream, sublength);
		codeLength += (new BerIdentifier(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 0)).encode(berOStream);
		
		codeLength += name.encode(berOStream, true);
		
		codeLength += BerLength.encodeLength(berOStream, codeLength);
		if (explicit) {
			codeLength += identifier.encode(berOStream);
		}

		return codeLength;

	}

	public int decode(InputStream iStream, boolean explicit) throws IOException {
		int codeLength = 0;
		int subCodeLength = 0;
		BerIdentifier berIdentifier = new BerIdentifier();

		if (explicit) {
			codeLength += identifier.decodeAndCheck(iStream);
		}

		BerLength length = new BerLength();
		codeLength += length.decode(iStream);

		while (subCodeLength < length.val) {
			subCodeLength += berIdentifier.decode(iStream);
			if (berIdentifier.equals(Name.identifier)) {
				name = new Name();
				subCodeLength += name.decode(iStream, false);
			}
			if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 0)) {
				subCodeLength += new BerLength().decode(iStream);
				title = new BerVisibleString();
				subCodeLength += title.decode(iStream, true);
			}
			if (berIdentifier.equals(EmployeeNumber.identifier)) {
				number = new EmployeeNumber();
				subCodeLength += number.decode(iStream, false);
			}
			if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 1)) {
				subCodeLength += new BerLength().decode(iStream);
				dateOfHire = new Date();
				subCodeLength += dateOfHire.decode(iStream, true);
			}
			if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 2)) {
				subCodeLength += new BerLength().decode(iStream);
				nameOfSpouse = new Name();
				subCodeLength += nameOfSpouse.decode(iStream, true);
			}
			if (berIdentifier.equals(BerIdentifier.CONTEXT_CLASS, BerIdentifier.CONSTRUCTED, 3)) {
				children = new SubSeqOf_children();
				subCodeLength += children.decode(iStream, false);
			}
		}
		if (subCodeLength != length.val) {
			throw new IOException("Decoded sequence has wrong length tag");

		}
		codeLength += subCodeLength;

		return codeLength;
	}

}

