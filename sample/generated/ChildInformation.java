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

public class ChildInformation {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.UNIVERSAL_CLASS, BerIdentifier.CONSTRUCTED, 17);

	public Name name = null;

	public Date dateOfBirth = null;

	public ChildInformation() {
	}

	public ChildInformation(Name name, Date dateOfBirth) {
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}

	public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {
		int codeLength = 0;
		int sublength;

		sublength = dateOfBirth.encode(berOStream, true);
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
				dateOfBirth = new Date();
				subCodeLength += dateOfBirth.decode(iStream, true);
			}
		}
		if (subCodeLength != length.val) {
			throw new IOException("Decoded sequence has wrong length tag");

		}
		codeLength += subCodeLength;

		return codeLength;
	}

}

