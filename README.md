# sudo-mock

Mocking sudo on non-unix OS

## Build jar file

Using a maven build-run configuration a jar file can be built. Use goal `package`. 

## EXE-packaging

The actual source code is organized as maven project and will build a jar file. To wrap it into an executable [lauch4j](http://launch4j.sourceforge.net/) is used. The launch4j configuration is stored in `launch4j.xml`.

## Usage

Make sure a JSON-file named `behavior.json` is stored at the same directory as `sudo.exe`.
