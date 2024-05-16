@echo off
ECHO =====================================
ECHO Android App Build
ECHO =====================================

call gradlew clean assemble bundle

:: echo copying %~dp0app\build\outputs\apk to %~dp0production_build\apk
 Xcopy "%~dp0app\build\outputs\" "%~dp0production_build" /e /i /y /j
:: Xcopy "%~dp0app\build\outputs\apk" "%~dp0production_build/apk" /e /i /y /j
::Xcopy "%~dp0app\build\outputs\apk\free\release\app-free-release.apk" "%~dp0production_build" /s /y

::Xcopy "%~dp0app\build\outputs\apk\pro\release\app-pro-release.apk" "%~dp0production_build" /s /y

::Xcopy "%~dp0app\build\outputs\bundle\freeRelease\app-free-release.aab" "%~dp0production_build" /s /y

:: Xcopy "%~dp0app\build\outputs\bundle\proRelease\app-pro-release.aab" "%~dp0production_build" /s /y

::Xcopy "%~dp0app\build\outputs\*.aab" "%~dp0production_build" /s /y

:: echo copying %~dp0app\build\outputs\bundle to %~dp0production_build\bundle

:: Xcopy "%~dp0app\build\outputs\bundle" "%~dp0production_build/bundle" /e /i /y /j
:: Xcopy "%~dp0app\build\outputs\bundle\*.aab" "%~dp0production_build" /e /i /y /j

ECHO " "
ECHO " "
ECHO =====================================
ECHO Android App Build completed :)
ECHO =====================================



::pause