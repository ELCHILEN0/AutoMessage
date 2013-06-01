#! /bin/bash

clear
echo "Building and Packaging Project"

mvn package

echo "JAR file packaged sucessfully!"

while getopts o:c option
do
        case "${option}"
        in
                o) OUTPUT=${OPTARG};;
                c) CLEAR=true;;
                s) SHADED=true;;
        esac
done

if [ -z "$OUTPUT" ]
  then
    echo "No file path specified.  Skipping copy."
  else
    echo "Copying packaged file..."

    if [ $SHADED ]
      then
        echo "target/*-standalone.jar";
        cp target/*-standalone.jar $OUTPUT
      else
                echo "target/*.jar";
        cp target/*.jar $OUTPUT
    fi
fi

if [ $CLEAR ]
  then
    clear
fi
