package pathfinding2;

public class Node {
	
	Node parent;
	public int col;
	public int row;
	int gCost;
	int hCost;
	int fCost;
	boolean solid, open, checked;
	
	public Node(int col, int row) {
		this.row = row;
		this.col = col;
	}

}
