@echo off

"C:\LH\Tools\Java\openlogic-openjdk-17.0.10+7-windows-x64\bin\java.exe" -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar C:\LH\Tools\EclipseIDE\workspace\SQLite\SQLite\target\SQLite-0.0.1-SNAPSHOT-jar-with-dependencies.jar

pause