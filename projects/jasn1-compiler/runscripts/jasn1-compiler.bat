::BATCH file to windows
ECHO compiling

set BATDIR=%~dp0
set LIBDIR=%BATDIR%..\build\libsdeps

java -Djava.ext.dirs=%LIBDIR% org.openmuc.jasn1.compiler.Compiler %*
