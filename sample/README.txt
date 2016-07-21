
This example shall demonstrate how to use jASN1:

The file x690BerExample.asn contains an ASN.1 example from the X.690
standard document:
http://www.itu.int/ITU-T/studygroups/com17/languages/X.690-0207.pdf

jASN1 was used to parse this file and generate the Java class files
ChildInformation.java, Date.java, EmployeeNumber.java, Name.java, and
PersonnelRecord.java in the folder named "generated". This generation
can be repeated using the following command:

../jasn1-compiler/run-scripts/jasn1-compiler.sh -f x690BerExample.asn -o "generated/" -ns "generated"

jasn1-compiler uses the ASN.1 to XML converter from BinaryNotes. The
XML is then converted to the Java classes. If you want to see the XML
that is generated you can execute jasn1-compiler using the -x flag.

These generated classes can then be used together with the jasn1-ber
library to encode and decode the structures using BER. This is
demonstrated in the EncodeDecodeSample.java file.

To compile and run the EncodeDecodeSample do something like the
following:

javac -cp ../jasn1-ber/lib/jasn1-ber-<version>.jar generated/*.java *.java
java -cp "../jasn1-ber/lib/jasn1-ber-<version>.jar:./" EncodeDecodeSample
