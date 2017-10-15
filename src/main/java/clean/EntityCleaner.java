package clean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EntityCleaner {

	private static BufferedReader reader;
	private static BufferedWriter writer;
	private static final String READFILE = "C:/Users/user/Desktop/tweet_1.0_data/extracted/entities1.1.txt";
	private static final String WRITEFILE = "C:/Users/user/Desktop/tweet_1.0_data/cleaned/entities1.1(c).txt";
	
	/*
	public ArrayList<String> textFinder(String initialString) throws IOException{

		ArrayList<String> stringCombi = new ArrayList<String>();
		String firstString = "";
		String secondString = "";
		if(!initialString.contains(" ")){
		    for(int i = 0; i < initialString.length(); i ++){
		    	firstString = initialString.substring(0,i+1);
		    	secondString = initialString.substring(i+1, initialString.length());
		    	System.out.print(firstString + " ");
		    	System.out.print(secondString);
		    	stringCombi.add(firstString + " " + secondString);
		    	stringCombi.toString();
		    	System.out.println("");
		    }				
		}	
		return stringCombi;
	}
	*/
	
	
	//accept a string, like aCamelString
	//return a list containing strings e.g. a Camel String
	public static String splitCamelCaseString(String s){

	    String split = "";
	    if(s.length() > 9 && s.codePoints().filter(c-> c>='A' && c<='Z').count() < 5 ){
	    	
	    	for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
	    		split += w + " ";
	    	}    
	    }
	    return split;    
	}

	public static void reader() throws IOException{
		String data;
		String[] dataArray;
		String splitData = "";
		reader = new BufferedReader(new FileReader(READFILE));
		writer = new BufferedWriter(new FileWriter(WRITEFILE));
		while((data = reader.readLine()) != null){
			ArrayList<String>joinDataList = new ArrayList<String>();
			dataArray = data.split(",");
			for(int i = 1; i < dataArray.length; i++){
				dataArray[i] = dataArray[i].replace("_", " ");
				dataArray[i] = dataArray[i].replace("_", " ");
				if((splitCamelCaseString(dataArray[i])).equals("")){
					dataArray[i] = dataArray[i].trim();
					joinDataList.add(dataArray[i]);
				}
				else{
					splitData = splitCamelCaseString(dataArray[i]);
					joinDataList.add(splitData.trim());
				}
				splitData ="";
			}	
			writer.write(dataArray[0] + "," + joinDataList.toString().trim().replace(", ", ",").replaceAll("[\\t\\n\\r]+","").
		    		replace("[", "").replace("]", "").trim().replace("  ", " "));
				
			writer.newLine();
			writer.flush();	
		}
	}
	public static void main(String[] args) throws IOException {
		reader();
	
	}
}
