#!/bin/sh

# gets the current directory of the script (assumes in project directory)
DIRECTORY=$(cd `dirname $0` && pwd)

# removes target directory for a clean build
rm -rf $DIRECTORY/target/

# compiles the code into taget
javac -d $DIRECTORY/target -classpath "$DIRECTORY/." $DIRECTORY/src/net/thesyndicate/utilities/*.java

# runs the compiled code
java -ea -classpath "$DIRECTORY/target/." net.thesyndicate.utilities.Bencode

# package into a jar file
mkdir $DIRECTORY/jar
jar -cfev $DIRECTORY/jar/Bencode.jar net.thesyndicate.utilities.Bencode -C $DIRECTORY/target net/thesyndicate/utilities/

# runs the package code
java -jar $DIRECTORY/jar/Bencode.jar

