package clean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileSplitter {
	// Counter to change the name of the text file written (e.g. data1.txt, data2.txt...)
	private static int count = 1;
	// Counter to count the number of lines
	private static int numLine = 0;
	// Select the number of lines per text file to be written
	private static int initialNum = 50000;
	// Set number same as initialNum variable
	private static final int LINENUM = 50000;
	// File to be read
	private static final String READFILE = "C:/Users/user/Desktop/data_by_5/tweet_1.0.txt";
	// Path of the file to be written
	private static final String PATHNAME = "C:/Users/user/Desktop/data_by_5/data"; 
	// A timer for the program
	private static long tStart = 0;
	private static BufferedReader lineReader;
	private static BufferedWriter dataWriter;

	public static void fileReader() throws IOException{
		String rawData;
		try {
			lineReader = new BufferedReader(new FileReader(READFILE));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dataWriter = new BufferedWriter(new FileWriter(PATHNAME + count + ".txt"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while ((rawData = lineReader.readLine()) != null){
			dataWriter.write(rawData);
			if(rawData != null){
				numLine++;		
			}
			// if the number of lines read is equal to the number set(e.g.250000
			// write to a new file
			if(numLine == initialNum ){
				dataWriter.write(rawData);
				dataWriter.newLine();
				dataWriter.flush();
				count++;
				initialNum  += LINENUM;	
				try {
					dataWriter = new BufferedWriter(new FileWriter(PATHNAME + count + ".txt"));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				dataWriter.newLine();
				dataWriter.flush();
			}
		}
	}	
	public static void main(String[] args) throws IOException {
		// start the timer
		tStart = System.currentTimeMillis();
		fileReader();
		lineReader.close();
		dataWriter.close();
		// end the timer
		long tEnd = System.currentTimeMillis();
  	    long elapse = tEnd - tStart;
	    double elapsedSeconds = elapse / 1000.0/ 60.0;
	    System.out.print("Time:" + elapsedSeconds + "\r");	
	}
}
