import java.util.HashMap;
import java.util.Set;

public class WordTree {

	private HashMap<String, WordNode> tree;
	
	public WordTree() {
		tree = new HashMap<String, WordNode>();
	}
	
	/*
	 * Adds the specified keyword string to the tree. If keyword already exists then increments the WordNode's size
	 */
	public void add(String keyword) {
		if (!tree.containsKey(keyword))
			tree.put(keyword, new WordNode(keyword));
		else {
			get(keyword).increment();
		}
	}
	
	/*
	 * Adds a keyword phrase that is split up into individual keywords stored in an array.
	 * 
	 * Example:
	 * 
	 * Adding these four keywords {"safeguard", "safeguard pretty", "safeguard privacy", "safeguard privacy liberty"} would look like this:
	 * 
	 * tree.add(new String[]{"safeguard"});
	 * tree.add(new String[]{"safeguard", "pretty"});
	 * tree.add(new String[]{"safeguard", "privacy"});
	 * tree.add(new String[]{"safeguard", "privacy", "liberty"});
	 * 
	 * And the tree looks like this:
	 * 
	 * ----------------------------------------------------
	 * ("safeguard", 4)	-->	("pretty", 1)
	 * 					-->	("privacy" 2)	-->	("liberty", 1)
	 * 
	 * ("pretty, 1)
	 * 
	 * ("privacy" 2)	-->	("liberty", 1)
	 * 
	 * ("liberty", 1)
	 * ----------------------------------------------------
	 * 
	 * In parentheses are WordNodes with structure (value, size). The arrows represent all children of the WordNode
	 * The left-most nodes represent the top level of the WordTree
	 * 
	 * If you added these new keywords {"safeguard pretty liberty"} the tree would look like this:
	 * 
	 * ----------------------------------------------------
	 * ("safeguard", 5)	-->	("pretty", 2)	--> ("liberty", 1)
	 * 					-->	("privacy", 2)	--> ("liberty", 1)
	 * 
	 * ("pretty, 2)		-->	("liberty", 1) 
	 * 
	 * ("privacy" 2)	-->	("liberty", 1)
	 * 
	 * ("liberty", 2)
	 * ----------------------------------------------------
	 * 
	 * Example interpretations of this tree:
	 * 
	 * 5 instances of "safeguard"
	 * 2 instances of "safeguard privacy"
	 * 1 instance of "safeguard privacy liberty"
	 * 2 instances of "privacy"
	 * 1 instance of "privacy liberty"
	 * 2 instances of "liberty"
	 * 
	 */
	public void add(String[] keywords) {
		
		int index = 1;
		for (String keyword : keywords) {
			add(keyword);
			addHelper(keywords, index, tree.get(keyword));
			index++;
		}
	}
	
	/*
	 * Recursive helper function for add method
	 */
	private void addHelper(String[] keywords, int index, WordNode parent) {
		if (index != keywords.length) {
			parent.add(keywords[index]);
			addHelper(keywords, index + 1, parent.get(keywords[index]));
		}
	}
	
	/*
	 * Returns the WordNode associated with the string 'keyword'
	 */
	public WordNode get(String keyword) {
		return tree.get(keyword);
	}
	
	/*
	 * Returns a Set of all keys in the tree HashMap
	 */
	public Set<String> getKeyset() {
		return tree.keySet();
	}
	
	/*
	 * Returns a HashMap containing each keyword phrase starting with string 'str' and their respective size
	 */
	public HashMap<String, Integer> getKeywords(String str) {
		return getKeywords(get(str));
	}
	
	/*
	 * Helper function for getKeywords(String str)
	 */
	private HashMap<String, Integer> map = null;
	private HashMap<String, Integer> getKeywords(WordNode startNode) {
		
		map = new HashMap<String, Integer>();
		getKeywordsHelper(startNode, startNode.getValue());
		
		return map;
	}
	
	/*
	 * Recursive helper function for getKeywords(WordNde startNode)
	 */
	private String getKeywordsHelper(WordNode node, String keywords) {
		
		if (node.hasChildren()) {
			HashMap<String, WordNode> ch = node.getChildren();
			for (String child : ch.keySet()) {
				getKeywordsHelper(ch.get(child), keywords + " " + child);
			}
		}
		
		map.put(keywords, node.getSize());
		
		return keywords.trim();
	}
	
	/*
	 * Returns a HashMap containing every possible keyword in the tree and their respective size
	 */
	public HashMap<String, Integer> getAll() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (String startKey : getKeyset()) {
			HashMap<String, Integer> keywords = getKeywords(startKey);
			for (String key : keywords.keySet()) {
				map.put(key, keywords.get(key));
			}
		}
		return map;
	}
		
}
