package linking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Map;

/**
 * Copyright Mark Watson 2008-2010. All Rights Reserved.
 * License: LGPL version 3 (http://www.gnu.org/licenses/lgpl-3.0.txt)
 */

public class EntityLinkingClient {
	private static final String READFILE = "C:/Users/user/Desktop/tweet_1.0_data/extracted/entities1(cut).txt";
	private static final String WRITEFILE = "C:/Users/user/Desktop/tweet_1.0_data/linked/linked2.txt";
	private static BufferedWriter uriWriter = null;
	private static BufferedReader entityReader = null;
	
	public static void main(String[] args) throws Exception {
		String rawData;
		String[] entities;
		EntityLinking lookup = null;
		entityReader = new BufferedReader(new FileReader(READFILE));
		uriWriter = new BufferedWriter(new FileWriter(WRITEFILE));
		while((rawData = entityReader.readLine()) != null){
			entities = rawData.split(",");
			LinkedList<String> linkedList = new LinkedList<String>();
			//read each entity in a line
			for(int i = 1; i < entities.length; i++){	
				// DBpedia lookup to get the uri
				lookup = new EntityLinking(entities[i]);
				for (Map<String, String> bindings : lookup.variableBindings()) {
					for (String variableName : bindings.keySet()) {
						linkedList.add(bindings.get(variableName));
					}
				}
			}
			if(linkedList.isEmpty() == false){
				uriWriter.write(entities[0] + "," + linkedList.toString().replace("[", "").replace("]", ""));
				uriWriter.flush();
				uriWriter.newLine();
			}
		}	
	}
}