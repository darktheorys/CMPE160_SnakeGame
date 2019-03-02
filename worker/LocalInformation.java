package worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import assets.Food;
import assets.Node;
import assets.Node.Value;
import game.Direction;

/**
 * Class to create and keep information about a snake in particular its head
 * @author Burak
 *
 */
public class LocalInformation {

	/**
	 * Width of the game panel
	 */
	private int gridWidth;
	/**
	 * Height of the game panel
	 */
	private int gridHeight;
	/**
	 * the current food
	 */
	private Food food;
	/**
	 * Node of the current node, the information is created about it
	 */
	private Node head;
	/**
	 * HashMap To keep nodes around the head node of the snake
	 */
	private HashMap<Direction, Node> nodes;
	/**
	 * List to keep free directions to move
	 */
	private List<Direction> freeDirections;

	/**
	 * Constructor for the local information object to help snake arrangements
	 * @param gridWidth Width of the game
	 * @param gridHeight Height of the game
	 * @param nodes HashMap of the nodes around head node
	 * @param freeDirections Directions that is free to move
	 * @param food Parameter for the food node
	 * @param head Parameter for the head node
	 */
	LocalInformation(int gridWidth, int gridHeight,
			HashMap<Direction, Node> nodes, List<Direction> freeDirections,Food food, Node head) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.nodes = nodes;
		this.freeDirections = freeDirections;
		this.food = food;
		this.head = head;
	}

	/**
	 * Getter for the grid width
	 * @return Game width
	 */
	public int getGridWidth() {
		return gridWidth;
	}

	/**
	 * Getter for the height of the grid world.
	 * Can be used to assess the boundaries of the world.
	 * @return number of grid squares along the height
	 */
	public int getGridHeight() {
		return gridHeight;
	}

	/**
	 * Returns the neighbor Node one square up
	 * @return node or null if no node exists
	 */
	public Node getNodeUp() {
		return nodes.get(Direction.UP);
	}

	/**
	 * Returns the neighbor Node one square down
	 * @return node or null if no node exists
	 */
	public Node getNodeDown() {
		return nodes.get(Direction.DOWN);
	}

	/**
	 * Returns the neighbor Node one square left
	 * @return node or null if no node exists
	 */
	public Node getNodeLeft() {
		return nodes.get(Direction.LEFT);
	}

	/**
	 * Returns the neighbor node one square right
	 * @return node or null if no node exists
	 */
	public Node getNodeRight() {
		return nodes.get(Direction.RIGHT);
	}

	/**
	 * Returns the list of free directions around the current position.
	 * The list does not contain directions out of bounds or containing a node.
	 * Can be used to determine the directions available to move or reproduce.
	 * @return directions that is free to move
	 */
	public List<Direction> getFreeDirections() {
		return freeDirections;
	}

	/**
	 * Utility function to get a randomly selected direction among multiple directions.
	 * The selection is uniform random: All directions in the list have an equal chance to be chosen.
	 * @param possibleDirections list of possible directions
	 * @return direction randomly selected from the list of possible directions
	 */
	public static Direction getRandomDirection(List<Direction> possibleDirections) {
		if (possibleDirections.isEmpty()) {
			return null;
		}
		int randomIndex = (int)(Math.random() * possibleDirections.size());
		return possibleDirections.get(randomIndex);
	}

	/**
	 * Method to creation the right, free direction for the snake
	 * @return Direction to get food, to try
	 */
	public Direction getNextDirection() {
		Node food = this.food.getFood();
		ArrayList<Direction> nextDir = new ArrayList<Direction>();
		if(food.getX() > head.getX()) nextDir.add(Direction.RIGHT);
		if(food.getX() < head.getX()) nextDir.add(Direction.LEFT);
		if(food.getY() > head.getY()) nextDir.add(Direction.DOWN);
		if(food.getY() < head.getY()) nextDir.add(Direction.UP);
		freeDirections.retainAll(nextDir);
		return getRandomDirection(freeDirections);
	}
}
