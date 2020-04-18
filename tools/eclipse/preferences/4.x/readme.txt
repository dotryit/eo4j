SETTINGS FOR NEW WORKSPACE
==========================

Java -> Compiler
  Compiler Compliance Level

Java -> Compiler -> Errors/Warnings
  Potential Programming Problems
     Boxing and unboxing conversions: Error
     Serializable class without SerialVersionUID: Ignore
  Unnecessary Code
     Unnecessary declaration of thrown exception: Warning
        Uncheck "Ignore in overriding & implementing methods"
        Uncheck "Ignore Exceptions documented with ..."
        Uncheck "Ignore 'Exception' and 'Throwable'"

Java -> Code Style -> Code Templates
  Import codetemplates.xml
  
Java -> Code Style -> Formatters
  Import formatter.xml

General -> Editors -> Text Editors
  Displayed tab width: 3
  Insert spaces for tabs: True

Ant -> Editor -> Formatter
  Maximum line width: 140
  Wrap long element tags: True
  Tab Size: 3
  Use tab character instead of spaces: False

XML -> Xml Files -> Editor
  Line width: 140
  Indent using spaces: True
  Indentation size: 3

General -> Workspace
  Text file encoding -> UTF-8
  Text file line ending -> Unix
