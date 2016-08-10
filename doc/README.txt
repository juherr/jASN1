/*
 * Copyright Fraunhofer ISE, 2011
 * Author(s): Stefan Feuerhahn
 *    
 * This file is part of jASN1.
 * For more information visit http://www.openmuc.org
 * 
 * jASN1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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


jASN1 is a Java ASN.1 BER encoding/decoding library. It consists of a
compiler (jasn-compiler) that creates Java classes from ASN.1
syntax. These generated classes can then be used together with the
jasn-ber library to efficiently encode and decode messages using the
Basic Encoding Rules (BER). Other encodings such as DER or CER are not
supported at the moment. The compiler uses the excellent ASN.1 to XML
converter from the BinaryNotes ASN.1 Framework
(http://bnotes.sourceforge.net/).

Some of the restricted character string types are not part of the
jasn1-ber lib. But you can easily use them by adding the appropriate
ASN1 syntax:

The software has been tested under Linux and Windows.

-The library and its dependencies are found in the build/lib folder
-Javadocs can be found in the build/docs folder

For the latest release of this software visit http://www.openmuc.org .

Notes about the ASN1 syntax:
----------------------------

Choices should not contain other Choices that do not have tags. If
this is the case in your ASN1 code then you should copy the contents
of that inner Choice in the parent Choice.

explicit Tags in the beginning of an ASN1 element definition such as:
MyInt ::= [2] EXPLICIT INTEGER 
are not support by this compiler because they are inefficient and
usually not needed.

At the moment a statement like the following is not supported:
MyElement ::= [2] IMPLICIT MyElement2 
but statements like the following are supported:
MyInt ::= [2] IMPLICIT INTEGER


Example:
--------

For an example on how to use jASN1 see the sample folder.

Rebuild jASN1:
--------------

After modifying the jASN1 code it can be rebuild using Gradle. The
Gradle build files are included with this distribution. Install Gradle
first (at least version 1.0-milestone-7) . Then run "gradle clean
build" to rebuild everything.

Please send us any code improvements so we can integrate them in our
distribution.

ASN.1 and BER standards:
------------------------

The Abstract Syntax Notation One (ASN.1) is defined in the ITU-T
Standard X.680. The Basic Encoding Rules (BER) are specified in the
ITU-T Standard X.690.

