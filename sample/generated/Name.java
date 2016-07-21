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

public class Name {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.APPLICATION_CLASS, BerIdentifier.CONSTRUCTED, 1);

	public BerVisibleString givenName = null;

	public BerVisibleString initial = null;

	public BerVisibleString familyName = null;

	public Name() {
	}

	public Name(BerVisibleString givenName, BerVisibleString initial, BerVisibleString familyName) {
		this.givenName = givenName;
		this.initial = initial;
		this.familyName = familyName;
	}

	public int encode(BerByteArrayOutputStream berOStream, boolean explicit) throws IOException {
		int codeLength = 0;
		codeLength += familyName.encode(berOStream, true);
		
		codeLength += initial.encode(berOStream, true);
		
		codeLength += givenName.encode(berOStream, true);
		
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
		boolean decodedIdentifier = false;

		if (explicit) {
			codeLength += identifier.decodeAndCheck(iStream);
		}

		BerLength length = new BerLength();
		codeLength += length.decode(iStream);

		if (subCodeLength < length.val) {
			if (decodedIdentifier == false) {
				subCodeLength += berIdentifier.decode(iStream);
				decodedIdentifier = true;
			}
			if (berIdentifier.equals(BerVisibleString.identifier)) {
				givenName = new BerVisibleString();
				subCodeLength += givenName.decode(iStream, false);
				decodedIdentifier = false;
			}
			else {
				throw new IOException("Identifier does not macht required sequence element identifer.");
			}
		}
		if (subCodeLength < length.val) {
			if (decodedIdentifier == false) {
				subCodeLength += berIdentifier.decode(iStream);
				decodedIdentifier = true;
			}
			if (berIdentifier.equals(BerVisibleString.identifier)) {
				initial = new BerVisibleString();
				subCodeLength += initial.decode(iStream, false);
				decodedIdentifier = false;
			}
			else {
				throw new IOException("Identifier does not macht required sequence element identifer.");
			}
		}
		if (subCodeLength < length.val) {
			if (decodedIdentifier == false) {
				subCodeLength += berIdentifier.decode(iStream);
				decodedIdentifier = true;
			}
			if (berIdentifier.equals(BerVisibleString.identifier)) {
				familyName = new BerVisibleString();
				subCodeLength += familyName.decode(iStream, false);
				decodedIdentifier = false;
			}
			else {
				throw new IOException("Identifier does not macht required sequence element identifer.");
			}
		}
		if (subCodeLength != length.val) {
			throw new IOException("Decoded sequence has wrong length tag");

		}
		codeLength += subCodeLength;

		return codeLength;
	}

}

