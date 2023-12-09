@echo off

set dllname=x64\release\jshellnative.dll

call protk_unlock.bat -c %dllname%

rem **** copy %dllname% ..\com.simplifiedlogic.nitro.jshell.cpp.win64

copy %dllname% ..\com.simplifiedlogic.nitro.jshell.osgi\src
rem copy %dllname% ..\com.simplifiedlogic.nitro.jshell.json

copy %dllname% ..\JShellServer
