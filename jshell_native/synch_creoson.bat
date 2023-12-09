@echo off

set dllname=x64\release\jshellnative.dll

call protk_unlock.bat -c %dllname%

copy %dllname% ..\creoson-server
