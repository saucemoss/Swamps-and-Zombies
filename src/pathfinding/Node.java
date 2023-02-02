package pathfinding;

public class Node extends ExampleNode{

	public Node(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		// TODO Auto-generated constructor stub
	}
    @Override
	public void sethCosts(AbstractNode endNode) {
//        this.sethCosts((absolute(this.getxPosition() - endNode.getxPosition())
//                + absolute(this.getyPosition() - endNode.getyPosition()))
//                * BASICMOVEMENTCOST);

    	//Euclidean distance
        int xDiff = absolute(this.getxPosition() - endNode.getxPosition());
        int yDiff = absolute(this.getyPosition() - endNode.getyPosition());

        this.sethCosts( (int) Math.hypot(xDiff, yDiff)*BASICMOVEMENTCOST);

    }

    private int absolute(int a) {
        return a > 0 ? a : -a;
    }
}
