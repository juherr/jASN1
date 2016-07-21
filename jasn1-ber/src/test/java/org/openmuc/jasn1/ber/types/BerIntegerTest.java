package org.openmuc.jasn1.ber.types;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.BerIdentifier;
import org.openmuc.jasn1.ber.BerLength;

public class BerIntegerTest {

	public class IntegerUnivPrim extends BerInteger {

		// in the final version identifier needs to be static
		protected final BerIdentifier identifier = new BerIdentifier(BerIdentifier.APPLICATION_CLASS,
				BerIdentifier.PRIMITIVE, 2);

		IntegerUnivPrim(long val) {
			super(val);
		}

		@Override
		public int encode(BerByteArrayOutputStream berBAOStream, boolean explicit) throws IOException {
			int codeLength = super.encode(berBAOStream, false);
			if (explicit) {
				codeLength += BerLength.encodeLength(berBAOStream, codeLength);
				codeLength += identifier.encode(berBAOStream);
			}

			return codeLength;
		}

	}

	@Test
	public void implicitEncoding1() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		// 51 is the example in X.690
		IntegerUnivPrim testInteger = new IntegerUnivPrim(51);
		int length = testInteger.encode(berBAOStream, false);
		Assert.assertEquals(2, length);

		byte[] expectedBytes = new byte[] { 0x01, 0x33 };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());

	}

	@Test
	public void implicitEncoding2() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		IntegerUnivPrim testInteger = new IntegerUnivPrim(256);
		int length = testInteger.encode(berBAOStream, false);
		Assert.assertEquals(3, length);

		byte[] expectedBytes = new byte[] { 0x02, 0x01, 0x00 };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());

	}

	@Test
	public void implicitEncoding3() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		IntegerUnivPrim testInteger = new IntegerUnivPrim(0);
		int length = testInteger.encode(berBAOStream, false);
		Assert.assertEquals(2, length);

		byte[] expectedBytes = new byte[] { 0x01, 0x00 };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());

	}

	@Test
	public void implicitEncoding4() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		IntegerUnivPrim testInteger = new IntegerUnivPrim(127);
		int length = testInteger.encode(berBAOStream, false);
		Assert.assertEquals(2, length);

		byte[] expectedBytes = new byte[] { 0x01, 0x7f };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());

	}

	@Test
	public void implicitEncoding5() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		IntegerUnivPrim testInteger = new IntegerUnivPrim(128);
		int length = testInteger.encode(berBAOStream, false);
		Assert.assertEquals(3, length);

		byte[] expectedBytes = new byte[] { 0x02, 0x00, (byte) 0x80 };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());

	}

	@Test
	public void implicitEncoding6() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		IntegerUnivPrim testInteger = new IntegerUnivPrim(-128);
		int length = testInteger.encode(berBAOStream, false);
		Assert.assertEquals(2, length);

		byte[] expectedBytes = new byte[] { 0x01, (byte) 0x80 };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());

	}

	@Test
	public void implicitEncoding7() throws IOException {
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(50);

		IntegerUnivPrim testInteger = new IntegerUnivPrim(-129);
		int length = testInteger.encode(berBAOStream, false);
		Assert.assertEquals(3, length);

		byte[] expectedBytes = new byte[] { 0x02, (byte) 0xff, (byte) 0x7f };
		Assert.assertArrayEquals(expectedBytes, berBAOStream.getArray());

	}

	@Test
	public void explicitEncoding() throws IOException {
		BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

		// 51 is the example in X.690
		BerInteger testInteger = new BerInteger(51);
		int length = testInteger.encode(berStream, true);
		Assert.assertEquals(3, length);

		byte[] expectedBytes = new byte[] { 0x02, 0x01, 0x33 };
		Assert.assertArrayEquals(expectedBytes, berStream.getArray());
	}

	@Test
	public void explicitDecoding() throws IOException {
		byte[] byteCode = new byte[] { 0x02, 0x01, 0x33 };
		ByteArrayInputStream berInputStream = new ByteArrayInputStream(byteCode);
		BerInteger asn1Integer = new BerInteger();
		asn1Integer.decode(berInputStream, true);
		Assert.assertEquals(51, asn1Integer.val);
	}

	@Test
	public void explicitEncoding2() throws IOException {
		BerByteArrayOutputStream berStream = new BerByteArrayOutputStream(50);

		BerInteger testInteger = new BerInteger(5555);
		int length = testInteger.encode(berStream, true);
		Assert.assertEquals(4, length);

		// System.out.println(getByteArrayString(berStream.getArray()));

		byte[] expectedBytes = new byte[] { 0x02, 0x02, 0x15, (byte) 0xb3 };
		Assert.assertArrayEquals(expectedBytes, berStream.getArray());
	}

	@Test
	public void explicitDecoding2() throws IOException {
		byte[] byteCode = new byte[] { 0x02, 0x02, 0x15, (byte) 0xb3 };
		ByteArrayInputStream berInputStream = new ByteArrayInputStream(byteCode);
		BerInteger asn1Integer = new BerInteger();
		asn1Integer.decode(berInputStream, true);
		Assert.assertEquals(5555, asn1Integer.val);
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
