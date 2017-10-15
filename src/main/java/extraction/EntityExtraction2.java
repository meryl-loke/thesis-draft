package extraction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.Utils;
import gate.creole.ExecutionException;
import gate.util.GateException;
import gate.util.Out;
import gate.util.persistence.PersistenceManager;

public class EntityExtraction2 {
	
	// The file with the tweetID, tweetTime and tweetData
	
	private static final String FILENAME = "C:/Users/user/Desktop/tweet_1.0_data/Tweet_1/data20/data20(new).txt";
	// File with twetID, entities extracted and extra information
	private static final String FILENAME2 = "C:/Users/user/Desktop/tweet_1.0_data/Tweet_1/data20/entities_extra20(new).txt";
	// File with tweetID, and entities extracted only
	private static final String FILENAME3 = "C:/Users/user/Desktop/tweet_1.0_data/Tweet_1/data20/entities20(new).txt";
	private static BufferedReader tweetReader = null;
	private static BufferedWriter entityWriter = null;
	private static BufferedWriter enWriter = null;
	private static String rawData = null;
	private static String splitData[] = null;
	private static Document document = null;
	private static CorpusController twitieController;
	private static long tStart = 0;
	
	// to initialise the TwitIE
	public void initTwitie() throws GateException, IOException {
		System.out.println("TwitIE initalising...");
		File pluginsHome = Gate.getPluginsHome();
		File twitiePlugin = new File(pluginsHome, "Twitter");
		File twitieGapp = new File(twitiePlugin, "/resources/twitie-english-only.gapp");
		twitieController = (CorpusController) PersistenceManager.loadObjectFromFile(twitieGapp);
		System.out.println("...TwitIE loaded");
	}
	
	// execute the twitIE
	private void execute() throws ExecutionException {
		Out.prln("Running ANNIE...");
		twitieController.execute();
		Out.prln("...ANNIE complete");
	}
	
	// set the Corpus
	public void setCorpus(Corpus corpus){
		twitieController.setCorpus(corpus);
	}
	
	
	public static void entityExtractor() throws IOException, GateException{
		// Create a new document for the corpus
		tweetReader = new BufferedReader(new FileReader(FILENAME));
		entityWriter = new BufferedWriter(new FileWriter(FILENAME2));
		enWriter = new BufferedWriter(new FileWriter(FILENAME3));
	
    	// Set and initialise GATE
    	Gate.setGateHome(new File("D:\\GATE_Developer_8.4.1"));
        Gate.init();
        EntityExtraction2 twitie = new EntityExtraction2();
        twitie.initTwitie();
       //Create a new corpus// 
        Corpus corpus = Factory.newCorpus("corpus");

	    while ((rawData = tweetReader.readLine()) != null){
	    	String tweetID;
	    	ArrayList <String> entityList = new ArrayList<String>();
	    	ArrayList <String> entity = new ArrayList<String>();
	    	
	    	try{
	    		splitData = rawData.split(",",3);
	    	}
	    	catch(ArrayIndexOutOfBoundsException e){
				rawData = tweetReader.readLine();
	    	}
	    	
	    	tweetID = splitData[0];
    		document = Factory.newDocument(splitData[2]);
		    
    		//Add the document to the corpus 
		    corpus.add(document); 
		    //set the corpus
		    twitie.setCorpus(corpus); 
		    twitie.execute();
		    
		    //Get the named entities from the corpus
		    document.getAnnotations().get(new HashSet<>(Arrays.asList("Person", "Organization", "Location","Event", "Unknown")))
		    	.parallelStream().forEach(a -> entityList.add(String.format("%d  %d %s %s - %s\n"
		    			, a.getStartNode().getOffset(), a.getEndNode().getOffset()
		    			, a.getFeatures(), a.getType(), Utils.stringFor(document, a).replaceAll("\\W", ""),entity.add(Utils.stringFor(document, a).replaceAll("\\W", ""))).trim()));

		  
		    if(entityList.isEmpty() == false){
		    	entityWriter.write(tweetID + "," + entityList.toString().replaceAll("[\\t\\n\\r]+","").
		    			replace("[", "").replace("]", "").replace("null, ", ""));
		    	enWriter.write(tweetID + "," + entity.toString().replaceAll("[\\t\\n\\r]+","").
		    			replace("[", "").replace("]", "").replace("null, ", ""));
		    	entityWriter.flush();
		    	enWriter.flush();
		    	enWriter.newLine();
		    	entityWriter.newLine();    	
		    }   
		    
	    }
	    //Release GATE resources 
        Factory.deleteResource(document); Factory.deleteResource(corpus); Factory.deleteResource(twitieController);
        tweetReader.close();    
	}
	
	public static void main(String[] args) throws Exception {
		tStart = System.currentTimeMillis();
		entityExtractor();
	    long tEnd = System.currentTimeMillis();
	    long elapse = tEnd - tStart;
	    double elapsedSeconds = elapse / 1000.0/ 60.0;
	    System.out.print("Time:" + elapsedSeconds + "\r");
	    
    }
}