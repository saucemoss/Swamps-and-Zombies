package pathfinding2;

import java.util.ArrayList;

import main.GamePanel;

public class Pathfinder {
	GamePanel gp;
	Node[][] node;
	ArrayList<Node> openList = new ArrayList<>();
	public ArrayList<Node> pathList = new ArrayList<>();
	Node startNode, currentNode, goalNode;
	boolean goalReached;
	int step = 0;

	public Pathfinder(GamePanel gp) {
		this.gp = gp;
		instantiateNodes();

	}

	public void instantiateNodes() {
		node = new Node[gp.tileM.dimension][gp.tileM.dimension];
		int col = 0;
		int row = 0;

		while (col < gp.tileM.dimension && row < gp.tileM.dimension) {
			node[col][row] = new Node(col, row);
			col++;
			if (col == gp.tileM.dimension) {
				col = 0;
				row++;
			}
		}
	}

	public void resetNodes() {
		int col = 0;
		int row = 0;

		while (col < gp.tileM.dimension && row < gp.tileM.dimension) {
			node[col][row].open = false;
			node[col][row].checked = false;
			node[col][row].solid = false;
			col++;
			if (col == gp.tileM.dimension) {
				col = 0;
				row++;
			}
		}
		openList.clear();
		pathList.clear();
		goalReached = false;
		step = 0;
	}

	public void setNodes(int startRow, int startCol, int goalRow, int goalCol) {

		resetNodes();
		
		startNode = node[startRow][startCol];
		currentNode = startNode;
		goalNode = node[goalRow][goalCol];

		int col = 0;
		int row = 0;

		openList.add(currentNode);
		while (col < gp.tileM.dimension && row < gp.tileM.dimension) {
			int tileNum = gp.tileM.mapTileNum[col][row];
			if (gp.tileM.tile[tileNum].collision) {
				node[col][row].solid = true;
			}
			for(int i = 0; i < gp.obj.size(); i ++) {
				if(gp.aSetter.getObjFromCoords(col, row) != null && gp.aSetter.getObjFromCoords(col, row).collision) {
					node[col][row].solid = true;
				}
			}

			getCost(node[col][row]);
			col++;
			if(col == gp.tileM.dimension) {
				col = 0;
				row++;
				
			}
		}
		
	}

	private void getCost(Node node) {
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;
		node.fCost = node.gCost + node.hCost;
		
	}
	
	public boolean search() {
		while (goalReached == false && step < 500) {
			int col = currentNode.col;
			int row = currentNode.row;
			currentNode.checked = true;
			openList.remove(currentNode);
			
			if(row - 1 >= 0) {
				openNode(node[col][row-1]);
			}
			if(col - 1 >= 0) {
				openNode(node[col-1][row]);
			}
			if(row + 1 < gp.tileM.dimension) {
				openNode(node[col][row+1]);
			}
			if(col + 1 < gp.tileM.dimension) {
				openNode(node[col+1][row]);
			}
			int bestNodeIndex = 0;
			int bestNodeFCost = 999;
			
			for (int i = 0; i < openList.size(); i++) {
				if(openList.get(i).fCost < bestNodeFCost) {
					bestNodeIndex = i;
					bestNodeFCost = openList.get(i).fCost;
				}else if(openList.get(i).fCost == bestNodeFCost) {
					if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i ;
					}
				}
			}
			
			if(openList.size() == 0) {
				break;
			}
			
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			step ++;
		}
		
		return goalReached;
	}

	private void trackThePath() {
		Node current = goalNode;
		while(current != startNode) {
			pathList.add(0,current);
			current = current.parent;
		}
		
	}

	private void openNode(Node node) {
		if(node.open == false && node.checked == false && node.solid == false) {
			node.open = true;
			node.parent = currentNode;
			openList.add(node);
		}
		
	}
	

}
