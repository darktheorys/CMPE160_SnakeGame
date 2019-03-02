package assets;

import java.awt.Color;
import game.Drawable;
import ui.GridPanel;

/**
 * Node class for the game, in basic it is the every little box on the game window
 * @author Burak
 */
public class Node implements Drawable{
	/**
	 * Boolean value for the snake's head, set false for default nodes
	 */
	private boolean isHead;
	/**
	 * Value for each node, empty for default nodes, snake for the snake, food for the food node
	 */
	private Value value;
	/**
	 * Coordinates for the particular nodes
	 */
	private int x,y;
	/**
	 * Boolean value for the information about if the node is inside or outside, default true
	 */
	private boolean inside = true;
	
	/**
	 * Constructor for the node
	 * @param value Node's value about its niche
	 * @param isHead if the node is head or not
	 * @param x coordinate for the node
	 * @param y coordinate for the node
	 */
	public Node(Value value, boolean isHead, int x, int y)  {
		this.value = value;
		this.isHead = isHead;
		this.x = x;
		this.y = y;
	}
	/**
	 * Getter for the isHead boolean value
	 * @return The boolean value of isHead field
	 */
	public boolean isHead() {
		return isHead;
	}

	/**
	 * Enum to keep necessary information in a node
	 * @author Burak
	 *
	 */
	public enum Value {
		EMPTY,
		SNAKE,
		FOOD,
	}
	
	/**
	 * Special constructor for the outside nodes, it helps to choose proper direction to move or eat
	 * @param st Any string value, just for the use right constructor
	 */
	public Node(String st) {
		this.inside = false;
	}
	
	/**
	 * Getter for the isInside field
	 * @return The boolean value of the node if is inside or not
	 */
	public boolean isInside() {
		return this.inside;
	}

	/**
	 * Getter for the x coordinate of the node
	 * @return the integer value of the x coordinate
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Getter for the y coordinate of the node
	 * @return the integer value of the y coordinate
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Setter for the Value of the node
	 * @param value Value to be set
	 */
	public void setValue(Value value) {
		this.value = value;
	}
	/**
	 * Setter for the x coordinate of the node
	 * @param x x coordinate to be set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * Setter for the y coordinate of the node
	 * @param y y coordinate to be set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * Setter for the isHead field
	 * @param isHead Boolean value to be set
	 */
	public void setHead(boolean isHead) {
		this.isHead = isHead;
	}

	/**
	 * Getter for the value of the node
	 * @return the value of the current node
	 */
	public Value getValue() {
		return this.value;
	}


	/**
	 * Draws the food valued nodes, other, in particular snake, nodes is drawn in Snake class, not here
	 */
	public void draw(GridPanel panel) {
		if(this.value == Value.FOOD) panel.drawSmallOval(getX(), getY(), new Color(0,100,0), Color.ORANGE);
	}
}
