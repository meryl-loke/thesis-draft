package stream;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStream {
	
	private static twitter4j.TwitterStream twitterStream = null;
	private static int count = 1;
	private static long tStart = 0;
	private static final String FILENAME = "C:/Users/user/Desktop/UserID.txt";
	private static final String FILENAME2 = "C:/Users/user/Desktop/Tweets.txt";
	
	public static void stream(){
			// Builder class that can be used to construct a twitter4j configuration
			ConfigurationBuilder configBuild = new ConfigurationBuilder();
			configBuild.setDebugEnabled(true);
			configBuild.setOAuthConsumerKey("j7ievKiDKDYTn1iLfne5RxFvW");
			configBuild.setOAuthConsumerSecret("ZLQ4uAFZ2VTNrPaKdIMyKVSKVefupVg4rRR8XayzfRzLNWuWw2");
			configBuild.setOAuthAccessToken("863724886413398017-okEZv0EaUnIjY5AKgxYv6f6rCbnTKTl");
			configBuild.setOAuthAccessTokenSecret("xH9Uc5lkw5NDVjXNiMIPQh97DbLPClEUB7cKWJHgJHPyu");

			//Build a twitter stream based on the configuration above
			twitterStream = new TwitterStreamFactory(configBuild.build()).getInstance();
			tStart = System.currentTimeMillis();
			
			// A listener to retrieve the tweets
			StatusListener listener = new StatusListener() {
            public void onException(Exception arg0) { }
            public void onDeletionNotice(StatusDeletionNotice arg0) { }
            public void onScrubGeo(long arg0, long arg1) { }

            //Status is the tweet of the user
            public void onStatus(Status status) {
            	User user = status.getUser(); 	
                BufferedWriter idWriter = null;
                BufferedWriter tweetWriter = null;
                	try {    
                	     idWriter = new BufferedWriter(new FileWriter(FILENAME,true));
                	     tweetWriter = new BufferedWriter(new FileWriter(FILENAME2,true));
                	     long userId = user.getId();               	                	      
                	     long tweetId = status.getId();
                	     long tweetTime = status.getCreatedAt().getTime();
                	     idWriter.write(userId + "," + tweetId);
                	     idWriter.newLine();
                	     String content = status.getText().replace("\n", "");
                	     tweetWriter.write(tweetId + "," + tweetTime + "," + content.replace("\r", ""));
                	     tweetWriter.newLine(); 
                	     count +=1;
                	     System.out.print("Number of tweets: " + count + "\r");
                	     long tEnd = System.currentTimeMillis();
                   	     long elapse = tEnd - tStart;
                	     double elapsedSeconds = elapse / 1000.0/ 60.0;
                	     System.out.print("Time:" + elapsedSeconds + "\r");

                	   } catch (FileNotFoundException ex) { 
                		   ex.printStackTrace();
                		   
                	   } catch (IOException ex) {   
                		   ex.printStackTrace();
                	   
                	   } finally {
                	      //Closing the BufferedWriter
                	      try {
                	    	  if (idWriter != null && tweetWriter != null) {
                	    		  idWriter.flush();
                	        	  tweetWriter.flush();
                	        	  idWriter.close();
                	        	  tweetWriter.close();
                	         }
                	      } 
                	      catch (IOException ex) {
                	         
                	    	  ex.printStackTrace();
                	      }
                	   }           
	        }

            public void onTrackLimitationNotice(int arg0) {
            	System.out.println("Limit: " + arg0);
            
            }

			public void onStallWarning(StallWarning arg0) {
				System.out.println(arg0);
			
			}
        };
        
        	twitterStream.addRateLimitStatusListener( new RateLimitStatusListener() {
  
            public void onRateLimitStatus( RateLimitStatusEvent event ) {
                System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
            }

          
            public void onRateLimitReached( RateLimitStatusEvent event ) {
                System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
            }
        });
        twitterStream.addListener(listener);
        twitterStream.sample("en");
        
}	
	public static void main(String args[]) {
	
		stream();
		
   }

}
