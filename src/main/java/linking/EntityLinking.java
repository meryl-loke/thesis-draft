package linking;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Copyright Mark Watson 2008-2010. All Rights Reserved.
 * License: LGPL version 3 (http://www.gnu.org/licenses/lgpl-3.0.txt)
 */

// Use Georgi Kobilarov's DBpedia lookup web service
//    ref: http://lookup.dbpedia.org/api/search.asmx?op=KeywordSearch
//    example: http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryString=Flagstaff&QueryClass=XML&MaxHits=10

/**
 * Searches return results that contain any of the search terms. I am going to filter
 * the results to ignore results that do not contain all search terms.
 */


public class EntityLinking extends DefaultHandler {
	public EntityLinking(String query) throws Exception {
		this.query = query;
		//System.out.println("\n query: " + query);
		HttpClient client = new HttpClient();
		String query2 = query.replaceAll(" ", "+"); // URLEncoder.encode(query, "utf-8");
		//System.out.println("\n query2: " + query2);
			HttpMethod method = new GetMethod
					("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?MaxHits=1&QueryString=" + query2);
			try {
			//System.out.println("\n method: " + method.getURI());
			client.executeMethod(method);
			// System.out.println(method);
			InputStream ins = method.getResponseBodyAsStream();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sax = factory.newSAXParser();
			sax.parse(ins, this);
			//System.out.println("TEST" +ins);
		} catch (HttpException he) {
			System.err.println("Http error connecting to lookup.dbpedia.org");
		} catch (IOException ioe) {
			System.err.println("Unable to connect to lookup.dbpedia.org");
		} 
		method.releaseConnection();
	}

	private List<Map<String, String>> variableBindings = new ArrayList<Map<String, String>>();
	private Map<String, String> tempBinding = null;
	private String lastElementName = null;

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//System.out.println("startElement " + qName);
		if (qName.equalsIgnoreCase("result")) {
			tempBinding = new HashMap<String, String>();
		}
		lastElementName = qName;
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
    //System.out.println("endElement " + qName);
		if (qName.equalsIgnoreCase("result")) {
			try {
				if (!variableBindings.contains(tempBinding) && containsSearchTerms(tempBinding))
					variableBindings.add(tempBinding);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
	  
    String s = new String(ch, start, length).trim();
    //System.out.println("characters (lastElementName='" + lastElementName + "'): " + s);
    
    //Get the description of the entity
    if (s.length() > 0) {
    	/*
      if ("Description".equals(lastElementName)) {
        if (tempBinding.get("Description") == null) {
          tempBinding.put("Description", s);
        }
        tempBinding.put("Description", "" + tempBinding.get("Description") + " " + s);
      }
      */
      // Get the URI of the entity
      //if ("URI".equals(lastElementName)) tempBinding.put("URI", s);
      if ("URI".equals(lastElementName) && s.indexOf("Category")==-1 && tempBinding.get("URI") == null) {
        tempBinding.put("URI", s);
      }
      //GET the Label
      //if ("Label".equals(lastElementName)) tempBinding.put("Label", s);
    }
  }
	public List<Map<String, String>> variableBindings() {
		return variableBindings;
	}
  
	//Check if the query contains the search terms
	private boolean containsSearchTerms(Map<String, String> bindings) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (String value : bindings.values()) sb.append(value);  // do not need white space
		// the entire text e.g. james b. comey, jr. (born...)
		String text = sb.toString().toLowerCase();
		// Tokenized the entity extracted from the text file
		StringTokenizer st = new StringTokenizer(this.query);
    		while (st.hasMoreTokens()) {
    			if (text.indexOf(st.nextToken().toLowerCase()) == -1) {
    				return false;
    			}
    		}    
    		return true;
	}
	private String query = "";
}