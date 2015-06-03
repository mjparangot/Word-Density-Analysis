import java.util.HashMap;

public class WordNode implements Comparable<Object> {

	private String value;
	private int size;
	private HashMap<String, WordNode> children;
	
	public WordNode() {
		value = "";
		size = 0;
		children = new HashMap<String, WordNode>();
	}
	
	public WordNode(String val) {
		value = val;
		size = 1;
		children = new HashMap<String, WordNode>();
	}
	
	public WordNode(String val, int s) {
		value = val;
		size = s;
		children = new HashMap<String, WordNode>();
	}
	
	/*
	 * Adds a new child WordNode. If child already exists then increments the child's size.
	 * 
	 * Returns the child WordNode
	 */
	public WordNode add(String child) {
		if (!children.containsKey(child))
			children.put(child, new WordNode(child));
		else
			children.get(child).increment();
		
		return children.get(child);
	}
	
	/*
	 * Returns the specified child of this WordNode
	 */
	public WordNode get(String child) {
		return children.get(child);
	}
	
	/*
	 * Increments the WordNode's size
	 */
	public void increment() {
		size += 1;
	}
	
	/*
	 * Returns the value of the WordNode
	 */
	public String getValue() {
		return value;
	}
	
	/*
	 * Returns the size of the WordNode
	 */
	public int getSize() {
		return size;
	}
	
	/*
	 * Returns a HashMap of containing all children strings and their respective WordNodes
	 */
	public HashMap<String, WordNode> getChildren() {
		return children;
	}
	
	/*
	 * Returns true if the WordNode has children
	 */
	public boolean hasChildren() {
		return children.size() != 0;
	}

	@Override
	public int compareTo(Object arg0) {
		WordNode a = this;
		WordNode b = (WordNode) arg0;
		
		if (a.getSize() > b.getSize())
			return -1;
		else if (a.getSize() < b.getSize())
			return 1;
		else
			return 0;
	}
}
