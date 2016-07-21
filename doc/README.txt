
jASN1 can be used for ASN.1 BER encoding/decoding using Java. It
consists of two projects: jasn1-compiler (licensed under GPLv3) and
jasn1-ber (licensed under LGPLv2.1). The jasn1-compiler is an
application that creates Java classes from ASN.1 syntax. These
generated classes can then be used together with the jasn1-ber library
to efficiently encode and decode messages using the Basic Encoding
Rules (BER).

The software has been tested under Linux and Windows.

For the latest release of this software visit http://www.openmuc.org .


Dependencies and Notices:
-------------------------

The compiler uses code from the excellent ASN.1 to XML converter from
the BinaryNotes ASN.1 Framework (http://bnotes.sourceforge.net/
licensed under the Apache 2.0 license).  Copyright 2006-2011 Abdulla
Abdurakhmanov (abdulla@latestbit.com)

Dependecies of the jasn1-compiler are:

- logback-core and logback-classic, License: EPLv1.0 and LGPLv2.1,
  http://logback.qos.ch

- slf4j-api, License: MIT, http://www.slf4j.org

- lineargs, License: LGPLv2, http://sourceforge.net/projects/lineargs/
  Copyright 2006 Abdulla G. Abdurakhmanov (abdulla.abdurakhmanov@gmail.com)

- antlr, License: The BSD License, http://www.antlr.org 
  Copyright (c) 2012 Terence Parr and Sam Harwell
  All rights reserved.


Example:
--------

For an example on how to use jASN1 see the sample folder.


Notes about the ASN1 syntax:
----------------------------

explicit Tags in the beginning of an ASN1 element definition such as:
MyInt ::= [2] EXPLICIT INTEGER 
are not support by this compiler because they are inefficient and
usually not needed.

For the same reason statements like the following are not supported:
MyElement ::= [2] IMPLICIT MyElement2 
but statements like the following are supported:
MyInt ::= [2] IMPLICIT INTEGER

In some cases it could be favorable not to decode elements of type ANY
because it would involve big array copying. Therefor the compiler
supports the alternative element type called ANY_NODECODE which can be
used inside the ASN.1 file in order to generate Java code that will
not decod nor encode elements of these types.


Develop jASN1:
--------------

We use the Gradle build automation tool. The distribution contains a
fully functional gradle build file ("build.gradle"). Thus if you
changed code and want to rebuild a library you can do it easily with
Gradle. Also if you want to import our software into Eclipse you can
easily create Eclipse project files using Gradle. Just follow these
instructions (for the most up to date version of these instructions
visit http://www.openmuc.org/index.php?id=28):

Install Gradle: 

- Download Gradle from the website: www.gradle.org

- Set the PATH variable: e.g. in Linux add to your .bashrc: export
  PATH=$PATH:/home/user/path/to/gradle-version/bin

- If you're behind a proxy you can set the proxy options in the
  gradle.properties file as explained here:
  http://www.gradle.org/docs/current/userguide/build_environment.html.

- Setting "org.gradle.daemon=true" in gradle.properties will speed up
  Gradle

Create Eclipse project files using Gradle:

- with the command "gradle eclipse" you can generate Eclipse project
  files

- It is important to add the GRADLE_USER_HOME variable in Eclipse:
  Window->Preferences->Java->Build Path->Classpath Variable. Set it to
  the path of the .gradle folder in your home directory
  (e.g. /home/someuser/.gradle (Unix) or C:/Documents and
  Settings/someuser/.gradle (Windows))

Rebuild a library:

- After you have modified the code you can completely rebuild the code
  using the command "gradle clean build" This will also execute the
  junit tests.

- You can also assemble a new distribution tar file: the command
  "gradle clean tar" will build everything and put a new distribution
  file in the folder "build/distribution".


ASN.1 and BER standards:
------------------------

The Abstract Syntax Notation One (ASN.1) is defined in the ITU-T
Standard X.680. The Basic Encoding Rules (BER) are specified in the
ITU-T Standard X.690.

