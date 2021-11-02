package de.efghaigerseelbach;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

	public String getBehaviorStr() throws IOException {
		if(!Files.exists(Paths.get("behavior.json"), LinkOption.NOFOLLOW_LINKS)) {
			System.err.println("File 'behavior.json' not found! This file must be located in the same directory.");
			return "";
		}
		BufferedReader br = new BufferedReader(new FileReader("behavior.json"));
		String line = null;
		StringBuffer content = new StringBuffer();
		while ((line = br.readLine()) != null) {
			content.append(line);
		}
		br.close();
		return content.toString();
	}

	public JSONArray getBehavior() throws IOException {
		String behaviorStr = getBehaviorStr();
		if(behaviorStr.isEmpty()) {
			return null;
		}
		return new JSONArray(behaviorStr);
	}

	public boolean isCommandDefined(String command) {
		for (int i = 0; i < behaviorArray.length(); i++) {
			JSONObject behavior = behaviorArray.getJSONObject(i);
			if (behavior.getString("command").equals(command)) {
				return true;
			}
		}
		return false;
	}

	public String getArgsAsString(String[] args) {
		StringBuffer result = new StringBuffer();
		for (String arg : args) {
			result.append(" " + arg);
		}
		return result.toString().trim();
	}

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

	public JSONArray behaviorArray;

	public void run(String[] args) {
		try {
			behaviorArray = getBehavior();
			if(behaviorArray == null) {
				return;
			}
			String command = getArgsAsString(args);
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
