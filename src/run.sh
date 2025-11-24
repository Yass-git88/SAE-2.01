#!/bin/bash

# Compilation
echo "Compilation..."
javac -encoding UTF-8 @compile.list -d ../class
if [ $? -ne 0 ]; then
  echo "Erreur de compilation."
  exit 1
fi

# Création / mise à jour de la javadoc (depuis src)
echo "Génération de la Javadoc..."
javadoc -d ../javadoc -encoding UTF-8 -charset UTF-8 -subpackages mpm > /dev/null 2>&1

# Aller dans le dossier class
cd ../class || { echo "Le dossier class n'existe pas"; exit 1; }

# Exécution
echo "Exécution..."
java mpm.Controleur &