package de.efghaigerseelbach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Johannes Gilbert
 *
 */
public class Main {

	/**
	 * Read the content of 'behavior.json'
	 * @return content of 'behavior.json' as string
	 * @throws IOException if an I/O error occurs reading from the file or a malformed orunmappable byte sequence is read (see also {@link Files.readAllLines})
	 */
	public String getBehaviorStr() throws IOException {
		Path behaviorJson = Paths.get("behavior.json");
		if(!Files.exists(behaviorJson, LinkOption.NOFOLLOW_LINKS)) {
			System.err.println("File 'behavior.json' not found! This file must be located in the same directory.");
			return "";
		}
		return String.join("\n", Files.readAllLines(behaviorJson));
	}

	/**
	 * Read 'behavior.json' as JSON-array
	 * @return a JSON-array containing the desired behavior description of the sudo mock or {@code null} in case 'behavior.json' is empty
	 * @throws IOException if an I/O error occurs reading from the file or a malformed orunmappable byte sequence is read (see also {@link Files.readAllLines})
	 */
	public JSONArray getBehavior() throws IOException {
		String behaviorStr = getBehaviorStr();
		if(behaviorStr.isEmpty()) {
			return null;
		}
		return new JSONArray(behaviorStr);
	}

	/**
	 * Check if the given {@code command} is defined
	 * @param command the command to check
	 * @return {@code true} is case a behavior is defined for the given command, else {@code false}
	 */
	public boolean isCommandDefined(String command) {
		for (int i = 0; i < behaviorArray.length(); i++) {
			JSONObject behavior = behaviorArray.getJSONObject(i);
			if (behavior.getString("command").equals(command)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if an output is defined for the given {@code command}
	 * @param command the command to check
	 * @return {code true} is case an output is defined (property "output"), else {@code false}
	 */
	public boolean hasOutput(String command) {
		if (!isCommandDefined(command)) {
			throw new RuntimeException("No such command! Did you call isCommandDefined() before?!? Change you coding!");
		}
		for (int i = 0; i < behaviorArray.length(); i++) {
			JSONObject behavior = behaviorArray.getJSONObject(i);
			if (behavior.getString("command").equals(command)) {
				return behavior.has("output");
			}
		}
		return false; // should never be reached
	}

	/**
	 * Determine the output for the given {@code command}. Make sure to call {@link isCommandDefined} a-priori. If the given
	 * {@code command} is not defined an exception is raised.
	 * @param command the command to get the output for
	 * @return the output as string
	 */
	public String getOutput(String command) {
		if (!isCommandDefined(command)) {
			throw new RuntimeException("No such command! Did you call isCommandDefined() before?!? Change you coding!");
		}
		boolean hasOutput = hasOutput(command);
		if (!hasOutput) {
			System.err.println(
					"For command '" + command + "' property 'output' is NOT defined! Correct behavior.json first!");
			return "";
		}
		for (int i = 0; i < behaviorArray.length(); i++) {
			JSONObject behavior = behaviorArray.getJSONObject(i);
			if (behavior.getString("command").equals(command)) {
				return behavior.get("output").toString(); 
			}
		}
		return ""; // should never be reached
	}
	
	/**
	 * Content of 'behavior.json' as JSON-array.
	 */
	public JSONArray behaviorArray;

	/**
	 * Putting all ends together...
	 * @param args the program arguments
	 */
	public void run(String[] args) {
		try {
			behaviorArray = getBehavior();
			if(behaviorArray == null) {
				return;
			}
			String command = String.join(" ", args).trim();
			if (!isCommandDefined(command)) {
				System.err.println("No definition found for command '" + command + "' in behavior.json!");
				return;
			}
			String output = getOutput(command);
			System.out.println(output);
		} catch (IOException e) {
			System.err.println("An error occurred while reading behavior.json");
			e.printStackTrace();
			return;
		}
	}

	public static void main(String[] args) {
		new Main().run(args);
	}
}
