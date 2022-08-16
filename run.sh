#!/usr/bin/env bash
export JAVA_PROGRAM_ARGS=`echo "$@"`
mvn exec:java -Dexec.mainClass="xyz.fantastixus.socnet.App" -Dexec.args="$JAVA_PROGRAM_ARGS"