/**
 * This file was automatically produced by jasn1-complier (http://www.openmuc.org)
 */

package x690_ber_example;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.LinkedList;
import java.io.UnsupportedEncodingException;
import org.openmuc.jasn1.ber.*;
import org.openmuc.jasn1.ber.types.*;

public class EmployeeNumber extends BerInteger {

	public final static BerIdentifier identifier = new BerIdentifier(BerIdentifier.APPLICATION_CLASS, BerIdentifier.PRIMITIVE, 2);

	public EmployeeNumber() {
		id = identifier;
	}

	public EmployeeNumber(long val) {
		id = identifier;
		this.val = val;
	}

}
