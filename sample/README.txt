
This example shall demonstrate how to use jASN1:

The file x690BerExample.asn contains an ASN.1 example from the X.690
standard document:
http://www.itu.int/ITU-T/studygroups/com17/languages/X.690-0207.pdf

jASN1 was used to parse this file and generate the Java class files
ChildInformation.java, Date.java, EmployeeNumber.java, Name.java, and
PersonnelRecord.java in the folder named "generated". This generation
can be repeated using the following command:

Linux:
../projects/jasn1-compiler/run-scripts/jasn1-compiler.sh -o "generated/" -p "generated" x690BerExample.asn

Windows:
..\projects\jasn1-compiler\run-scripts\jasn1-compiler.bat -o "generated/" -p "generated" x690BerExample.asn

If you need to decode the indefinite length then you can use the -il flag.

These generated classes can then be used together with the jasn1
library to encode and decode the structures using BER. This is
demonstrated in the EncodeDecodeSample.java file.

To compile and run the EncodeDecodeSample do something like the
following:

Linux:
javac -cp ../projects/jasn1/build/libs-all/jasn1-<version>.jar generated/*.java *.java
java -cp "../projects/jasn1/build/libs-all/jasn1-<version>.jar:./" EncodeDecodeSample

Windows:
javac -cp ../projects/jasn1/build/libs-all/jasn1-<version>.jar generated/*.java *.java
java -cp "../projects/jasn1/build/libs-all/jasn1-<version>.jar;./" EncodeDecodeSample
