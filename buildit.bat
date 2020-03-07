rem MUST COMPILE WITH JAVA 1.7 OR LOWER BECAUSE THAT'S WHAT THE CUSTOMER HAS INSTALLED

@echo off
set path=d:\apache-ant-1.7.1\bin;d:\java64\jdk1.7.0_79\bin;%path%

set JAVA_HOME=d:\java64\jdk1.7.0_79

ant -f build-all.xml