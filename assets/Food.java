package assets;

import assets.Node.Value;

/**
 * Food, bait whatever you say class for the snake game
 * @author Burak
 *
 */
public class Food {

	/**
	 * Food node for the food object
	 */
	private Node food;
	
	/**
	 * Constructor for the food object, it sets value of the node to food and coordinates in right way 
	 * @param x X Coordinate of the node 
	 * @param y Y coordinate of the node
	 */
	public Food(int x, int y) {
		this.food = new Node(Value.FOOD, false, x,y);
	}
	
	/**
	 * Getter for the food node
	 * @return the node for food object
	 */
	public Node getFood() {
		return this.food;
	}	
}
