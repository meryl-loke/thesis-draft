package clean;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TweetTest {

	private static final String FILENAME = "C:/Users/user/Desktop/data1/Tweets.txt";
	private static final String FILENAME2 = "C:/Users/user/Desktop/data1/Tweets(Test).txt";
	private static BufferedReader tweetReader;
	private static BufferedWriter tweetWriter;
	private static long tStart = 0;
	private static String rawData = null;
	private static String tweetID = null;
	private static String tweetData = null;
	private static String tweetTime = null;
	private static String processedData = null;
	private static String[] splitData = null;
	private static int count = 0;
	
	public static void main(String[] args) throws IOException {
		tStart = System.currentTimeMillis();
		try {
			tweetReader = new BufferedReader(new FileReader(FILENAME));
			tweetWriter = new BufferedWriter(new FileWriter(FILENAME2));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while ((rawData = tweetReader.readLine()) != null){
			splitData = rawData.split(",",3);
			try{
				tweetID = splitData[0];
				tweetTime = splitData[1];
				tweetData = splitData[2];
			}
			catch(ArrayIndexOutOfBoundsException e){
				rawData = tweetReader.readLine();
			}
			processedData = tweetID + "," + tweetTime + "," + tweetData;
			System.out.println(processedData);
			tweetWriter.write(processedData);
			tweetWriter.newLine();
			count++;
		     
		    if(count == 5100000){
		    	break;
		    }
		}
		 long tEnd = System.currentTimeMillis();
   	     long elapse = tEnd - tStart;
	     double elapsedSeconds = elapse / 1000.0/ 60.0;
	     System.out.print("Time:" + elapsedSeconds + "\r");
	}

}
