# sudo-mock

Mocking sudo on non-unix OS

## Build jar file

Using a maven build-run configuration a jar file can be built. Use goal `package`. 

## EXE-packaging

The actual source code is organized as maven project and will build a jar file. To wrap it into an executable [lauch4j](http://launch4j.sourceforge.net/) is used. The launch4j configuration is stored in `launch4j.xml`.

## Usage

Make sure a JSON-file named `behavior.json` is stored at the same directory as `sudo.exe`.

## Issues

* File `behavior.json` does not exist or is not located in the same directory as `sudo.exe`. In this case you will get error message `File 'behavior.json' not found! This file must be located in the same directory.`. Create a file named `behavior.json`. You might want to copy the same available in this repository.
* File `behavior.json` does not contain a specific command (e.g. `ABC`). In this case you will get error message `No definition found for command 'ABC' in behavior.json!`. Edit file `behavior.json` and an object like the following one:
```JSON
	{
		"command": "ABC",
		"output": "some output"
	},
```

* File `behavior.json` does not contain any definition for a certain command (e.g. `ABC`). In the case you will get error message `For command 'ABC' property 'output' is NOT defined! Correct behavior.json first!`. Edit file `behavior.json` and add a property `output` on the JSON-object of the respective command. The property name must be `output`(as already mentioned). The property's value can be any valid JSON (e.g. string, object, ...).

## Restrictions

* The command **must not** contain the following characters: `<` `>`