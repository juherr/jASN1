
jASN1 can be used for ASN.1 BER encoding/decoding using Java. It
consists of two projects: jasn1-compiler (licensed under GPLv3) and
the jasn1 library (licensed under LGPLv2.1). The jasn1-compiler is an
application that creates Java classes from ASN.1 syntax. These
generated classes can then be used together with the jasn1 library to
efficiently encode and decode messages using the Basic Encoding Rules
(BER).

The software has been tested under Linux and Windows.

For the latest release of this software visit http://www.openmuc.org .


Dependencies:
-------------

Dependency of the jasn1-compiler:

- ANTLR, License: The BSD License, http://www.antlr.org Copyright (c)
  2012 Terence Parr and Sam Harwell All rights reserved.

  The jasn1-compiler uses ANTLR to parse the ASN.1
  definitions. The excellent grammar to generate the ANTLR parser was
  taken from the BinaryNotes ASN.1 Framework
  (http://bnotes.sourceforge.net/ licensed under the Apache 2.0
  license).  Copyright 2006-2011 Abdulla Abdurakhmanov
  (abdulla@latestbit.com)



Example:
--------

For an example on how to use jASN1 see the sample folder.

Notes about the ASN1 syntax:
----------------------------

In some cases it could be favorable not to decode elements of type ANY
because it would involve big array copying. Therefor the compiler
supports the alternative element type called ANY_NODECODE which can be
used inside the ASN.1 file in order to generate Java code that will
not decode nor encode elements of these types.


ASN.1 and BER standards:
------------------------

The Abstract Syntax Notation One (ASN.1) is defined in the ITU-T
Standard X.680. The Basic Encoding Rules (BER) are specified in the
ITU-T Standard X.690.

