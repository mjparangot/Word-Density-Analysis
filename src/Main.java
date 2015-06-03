import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		// COMMENT IF EXPORTING JAR
		//String url = "http://www.amazon.com/Cuisinart-CPT-122-Compact-2-Slice-Toaster/dp/B009GQ034C/ref=sr_1_1?s=kitchen&ie=UTF8&qid=1431620315&sr=1-1&keywords=toaster";
		//String url = "http://blog.rei.com/camp/how-to-introduce-your-indoorsy-friend-to-the-outdoors/";
		
		// URL from argument parameters. COMMENT IF RUNNING IN IDE
		String url = args[0];
		
		// Structure that holds the WordTree with all possible keywords
		KeywordParser parser = null;
		try {
			parser = new KeywordParser(url);
		}
		catch (Exception e) {
			System.out.println("\nCONNECTION ERROR.\n\nTry running command again or check your URL or connection.\n");
			return;
		}
		
		// Sort keywords by descending count
		PriorityQueue<WordNode> pq = new PriorityQueue<WordNode>();
		
		Scanner sc = new Scanner(System.in);
		
		// Default count threshold
		String input = "2";
		
		// Whether or not the user entered the close command
		boolean closed = false;
		
		
		while (!closed) {
			if (!closed) {
				System.out.println("URL: " + url + "\n");
				System.out.println("Keywords found (count on left):\n");
				
				// Get keyset of single-word keywords and all keywords stemming from each key in the keyset
				for (String str : parser.getKeyset()) {
					
					HashMap<String, Integer> keywords = parser.get(str);
					
					for (String keyword : keywords.keySet()) {
						
						// Only add keyword to priority queue if the count is greater than the threshold
						int threshold = Integer.parseInt(input);
						if (threshold < 2)
							threshold = 2;
						if (keywords.get(keyword) >= threshold) {
							pq.add(new WordNode(keyword, keywords.get(keyword)));
						}
						
					}
				}
				
				// Print out keywords and count in descending order
				while (!pq.isEmpty()) {
					WordNode node = pq.remove();
					System.out.println(node.getSize() + " " + node.getValue());
				}
				System.out.println();
				
				// Get input for new threshold or if user wants to exit
				System.out.print("Set threshold for number of keywords (Minimum = 1 | 'c' to close'): ");
				input = sc.next();
				closed = (input.equals("c") || input.equals("C"));
				while (!closed && !isNumeric(input)) {
					System.out.print("Set threshold for number of keywords (Minimum = 1 | 'c' to close'): ");
					input = sc.next();
					closed = (input.equals("c") || input.equals("C"));
				}
				
				System.out.println();
			}
		}
		
		
		/*
		 * Basic uses of WordTree
		 * 
		 
		WordTree tree = new WordTree();
		
		tree.add(new String[]{"safeguard"});
		tree.add(new String[]{"safeguard", "pretty"});
		tree.add(new String[]{"safeguard", "privacy"});
		tree.add(new String[]{"safeguard", "privacy", "liberty"});
		
		//System.out.println(tree.getKeywords("safeguard"));
		
		for (String keyword : tree.getKeyset()) {
			//System.out.println(tree.get(keyword).getSize() + " " + keyword);
		}
		*/
	}
	
	/*
	 *  Returns true if the string is a numeric value
	 */
	public static boolean isNumeric(String str)  {  
		try  {  
			double d = Double.parseDouble(str);
		}  
		catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}
}
