@echo off

set cp=.
setlocal enabledelayedexpansion
for %%x in (..\out\*.jar) DO (
  set cp=!cp!;%%x
)
rem set cp=..\out\creoson-core*.jar
rem set cp=%cp%;..\out\creoson-intf*.jar
set cp=%cp%;d:\Program Files\ptc\Creo 9.0.0.0\Common Files\text\java\pfcasync.jar

set outdir=jshell_native

set package=com.simplifiedlogic.nitro.jlink.cpp

rem echo Generating JCGlobal
rem javah -cp "%cp%" -d %outdir% %package%.JCGlobal

echo Generating JCConnection
javah -cp "%cp%" -d %outdir% %package%.JCConnection

echo Generating JCFeature
javah -cp "%cp%" -d %outdir% %package%.JCFeature

echo Generating JCFamilyTable
javah -cp "%cp%" -d %outdir% %package%.JCFamilyTable

echo Generating JCFile
javah -cp "%cp%" -d %outdir% %package%.JCFile

