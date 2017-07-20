@echo off

if not exist setvars.bat goto nofile

call setvars.bat

if "%PROE_COMMON%"=="" goto nocommon

if "%PROE_ENV%"=="" goto noenv

if "%JAVA_HOME%"=="" goto nojava

if "%JSON_PORT%"=="" goto noport

set PRO_COMM_MSG_EXE=%PROE_COMMON%\%PROE_ENV%\obj\pro_comm_msg.exe

set opath=%path%
set path=%PROE_COMMON%\%PROE_ENV%\lib;%PROE_COMMON%\%PROE_ENV%\obj;%path%

set cp=.
setlocal enabledelayedexpansion
for %%x in (*.jar) DO (
  set cp=!cp!;%%x
)
set cp=%cp%;%PROE_COMMON%\text\java\pfcasync.jar
rem echo cp = %cp%
rem echo .

"%JAVA_HOME%\bin\java" -classpath "%cp%" -Dsli.jlink.timeout=200 -Dsli.socket.port=%JSON_PORT% com.simplifiedlogic.nitro.jshell.MainServer

set path=%opath%

goto end

:nofile
echo The setvars.bat file does not exist, run CreosonSetup.exe to create it.
pause
goto end

:nocommon
echo The PRO_COMMON variable has not been defined, run CreosonSetup.exe to set it.
pause
goto end

:noenv
echo The PRO_ENV variable has not been defined, run CreosonSetup.exe to set it.
pause
goto end

:nojava
echo The JAVA_HOME variable has not been defined, run CreosonSetup.exe to set it.
pause
goto end

:noport
echo The JSON_PORT variable has not been defined, run CreosonSetup.exe to set it.
pause
goto end

:end
