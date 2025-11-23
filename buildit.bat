rem COMPILING WITH JAVA 1.7 FOR BACKWARD COMPATIBILITY, BUT EMBEDDED JRE WILL BE 21

@echo off
set JAVA_HOME=d:\java64\jdk1.7.0_79
rem set JAVA_HOME=d:\java64\jdk-21.0.8

set path=d:\apache-ant-1.7.1\bin;%JAVA_HOME%\bin;%path%

ant -f build-all.xml