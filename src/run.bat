@echo off

REM Compilation
echo Compilation...
javac -encoding UTF-8 @compile.list -d ..\class
if errorlevel 1 (
	echo Erreur de compilation.
	exit /b 1
)

REM Création / mise à jour de la javadoc (depuis src)
echo Génération de la Javadoc...
javadoc -d ..\javadoc -encoding UTF-8 -charset UTF-8 -subpackages mpm >nul 2>&1

REM Aller dans le dossier class
cd ..\class
if errorlevel 1 (
	echo Le dossier class n'existe pas
	exit /b 1
)

REM Exécution
echo Exécution...
java mpm.Controleur