import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author Matt
 *
 */
public class KeywordParser {
	
	// Site URL
	private URL url;
	
	// WordTree structure containing every possible keyword
	private WordTree tree;
	
	// Set containing words to ignore
	private HashSet<String> stopwords;
	
	public KeywordParser() {
		try {
			url = new URL("http://www.cnn.com/2013/06/10/politics/edward-snowden-profile/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		tree = new WordTree();
		stopwords = new HashSet<String>();
		
		init();
	}
	
	public KeywordParser(String siteURL) {
		try {
			url = new URL(siteURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		tree = new WordTree();
		stopwords = new HashSet<String>();
		
		init();
	}
	
	/*
	 *  Init function called by constructor
	 */
	private void init() {
		parseStopwords();
		parseKeywords();
	}
	
	/*
	 * Generates the set of stopwords used to find breaks between keywords
	 */
	private void parseStopwords() {
		Scanner sc = null;
		sc = new Scanner(getClass().getResourceAsStream("stopwords.txt"));
		
		while (sc.hasNext())
			stopwords.add(sc.next());
	}
	
	/*
	 * Returns true if a string is a stopword
	 */
	private boolean stopword(String word) {
		return word.endsWith(",") || stopwords.contains(word); 
	}
	
	/*
	 * Returns true if a string begins with an upper case character
	 */
	private boolean isUpper(String s)
	{
	    for (char c : s.toCharArray()) {
	        if(!Character.isUpperCase(c))
	            return false;
	    }
	    return true;
	}
	
	/*
	 * Returns a string with all unnecessary symbols removed
	 */
	private String trimSymbols(String inputText) {
		
		// Trim extra whitespace
		String text = inputText.trim();
		
		// Remove plurals
		text = text.replace("'s", "");
		
		// Remove other symbols
		text = text.replace("!", "");
		text = text.replace("?", "");
		text = text.replace("\"", "");
		text = text.replace(",", "");
		text = text.replace(":", "");
		text = text.replace("", "");
		
		// Remove apostrophes
		if (text.startsWith("'") || text.endsWith("'"))
			text = text.replace("'", "");
		
		// Remove periods
		if (text.endsWith(".") && !isUpper(text.substring(text.length()-2, text.length()-1)))
			text = text.replace(".", "");
		
		return text;
	}
	
	/*
	 * Connects to the site URL and parses the entire content of the page. 
	 * Constructs a WordTree with every keyword phrase found.
	 */
	private void parseKeywords() {
		
		// Connect to site
		Document doc = null;
		try {
			doc = Jsoup.connect(url.toString()).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Store all content inside html element
		Elements ps = doc.select("body");
		
		// Scanner for parsing each individual string in document
		Scanner sc = new Scanner(doc.text());
		
		// Next string in document text
		String word = "";
		
		// Longest keyword phrase found before stopword or punctuation
		String topic = "";
		
		// Temporary string to hold next value of 'word'
		String temp = "";
		
		// True if the last keyword phrase ended with punctuation
		boolean endKeyword = false;
		
		// Iterate through entire document text
		while (sc.hasNext()) {

			// Stores next word in document
			word = sc.next();
			
			// True if the word ends with punctuation
			boolean endPunc = (word.endsWith(".") || word.endsWith(",") || word.endsWith("!") || word.endsWith("?"));
			
			// True if the word is a stopword
			boolean stopword = stopword(trimSymbols(word.toLowerCase()));
			
			/*
			 *  If the next word is the end of a topic phrase then the topic is broken down into individual
			 *  keywords and added to the WordTree
			 */
			if (endPunc || stopword) {
				//  If word ended with punctuation then remove all of it and add word to topic string
				if (endPunc) {
					topic += trimSymbols(word) + " ";
				}
				if (topic != " " && topic != "") {
					String[] keywords = topic.trim().split(" ");
					tree.add(keywords);
					topic = "";
				}
			}
			else {
				topic += trimSymbols(word) + " ";
			}
		}
	}
	
	// Returns a set of all single keywords stored in the tree
	public Set<String> getKeyset() {
		return tree.getKeyset();
	}
	
	// Returns a HashMap containing all keyword phrases starting with string 'str' and their respective count
	public HashMap<String, Integer> get(String str) {
		return tree.getKeywords(str);
	}
	
	// Returns a HashMap containing all keyword phrases and their respective count
	public HashMap<String, Integer> getAll() {
		return tree.getAll();
	}
}
