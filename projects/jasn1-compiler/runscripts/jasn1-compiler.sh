#!/bin/bash

# got this from the gradle start up script:
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/.."
JASN1_COMP_HOME="`pwd -P`"
cd "$SAVED"

LIB=$JASN1_COMP_HOME/build/libsdeps

CLASSPATH=$(JARS=("$LIB"/*.jar); IFS=:; echo "${JARS[*]}")

exec java -classpath "$CLASSPATH" org.openmuc.jasn1.compiler.Compiler "$@"
