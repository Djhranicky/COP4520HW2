@ECHO OFF
IF %1 == bp GOTO BIRTHDAYPARTY
:AFTERPARTY

IF %1 == cv GOTO CRYSTALVASE
:AFTERVASE

IF %1 == rm GOTO REMOVE
:AFTERREMOVE

GOTO END

:BIRTHDAYPARTY
javac BirthdayParty.java
java BirthdayParty
GOTO AFTERPARTY

:CRYSTALVASE
javac CrystalVase.java
java CrystalVase
GOTO AFTERVASE

:REMOVE
del /f *.class
GOTO AFTERREMOVE

:END