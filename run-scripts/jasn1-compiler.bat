::BATCH file to windows
ECHO compiling

set BATDIR=%~dp0
set LIBDIR=%BATDIR%..\projects\jasn1-compiler\build\libs-all

java -Djava.ext.dirs=%LIBDIR% org.openmuc.jasn1.compiler.Compiler %*
