cd xom
call mvn clean install
call mvn javadoc:javadoc
rem pause
cd ..\lib
call mvn clean install
rem pause
cd ..\common
call mvn clean install
rem pause
cd ..\tmlib
call mvn clean install
pause
