package assets;

import java.awt.Color;
import java.util.LinkedList;

import assets.Node.Value;
import game.Direction;
import game.Drawable;
import ui.GridPanel;
import worker.Action;
import worker.LocalInformation;
import worker.Action.Type;


/**
 * This is the snake class for an individual snake. It arranges everything for a snake.
 * @author Burak
 *
 */
public class Snake implements Drawable {

	/**
	 * Value to set the max size of a snake before reproducing
	 */
	private static final int DIVIDING_SIZE = 8;
	/**
	 * Size for the snake. Helps reproduction for the snake.
	 */
	private int size;

	/**
	 * LinkedList that keeps every node of the snake. Main arrangements for the game are done on the list.
	 */
	private LinkedList<Node> snake = new LinkedList<Node>();

	/**
	 * Constructor for the first created snake. It takes x,y, and size for the sake of the game. Therefore, it 
	 * only creates horizontal direction. Not vertical or other. 
	 * @param x x coordinate of the first snake
	 * @param y y coordinate of the first snake
	 * @param size size for the first created snake
	 */
	public Snake(int x, int y, int size) {
		Node head = new Node(Value.SNAKE,true,x,y);
		snake.add(head);
		for(int i = 1; i < size ; i++) {
			snake.add(new Node(Value.SNAKE,false,x-i,y));
		}
		this.size = size;
	}

	/**
	 * Constructor for the new snakes. It creates new snake with the help of LinkedList so it can create at any direction.
	 * @param snake LinkedList for the newborn snake.
	 */
	public Snake(LinkedList<Node> snake) {
		this.snake = snake;
		this.size = snake.size();
	}

	/**
	 * Divides size, and arranges LinkedList's both for current snake and newborn snake.
	 * @return Calls the constructor of the snake, returns the new snake
	 */
	public Snake reproduce() {
		this.size = this.size/2;
		LinkedList<Node> sn = new LinkedList<Node>();
		// Adds nodes to the new snake's list while removing them from the current snake's list
		for(int i = 0; i < this.size ; i++) {
			sn.addLast(snake.removeLast());
		}
		sn.getFirst().setHead(true);
		return new Snake(sn);
	}


	/**
	 * Getter for the current snake's size
	 * @return the size of the current snake
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Getter for the current snake's LinkedList. It is necessary for most snake related arrangements.
	 * @return LinkedList of the current snake
	 */
	public LinkedList<Node> getList(){
		return this.snake;
	}

	/**
	 * Stay method for the snake, if there is no better option, it comes here and does nothing
	 */
	public void stay() {	
	}

	/**
	 * Makes the snake move 1 box further by using the proper direction
	 * @param dir The direction of the new move, the free and wise direction.
	 */
	public void move(Direction dir) {
		int x=0, y=0;
		if(dir == Direction.DOWN) y++;
		if(dir == Direction.UP) y--;
		if(dir == Direction.LEFT) x--;
		if(dir == Direction.RIGHT) x++;
		// the head node now becomes body, so that snake moves
		snake.getFirst().setHead(false);
		// new head is added to the next direction
		snake.addFirst(new Node(Value.SNAKE, true, snake.getFirst().getX() + x, snake.getFirst().getY() + y));
		// makes the last node empty so that other snakes not crashes to this node
		snake.getLast().setValue(Value.EMPTY);
	}

	/**
	 * Eating method for the snake, it makes snake bigger, and arranges food node for the next spawn
	 * @param food Node for the food object
	 */
	public void eat(Node food) {
		// sets head to body so that new head can spawn
		snake.getFirst().setHead(false);
		// adds the new node to the list, it is the head for the snake
		food.setHead(true);
		food.setValue(Value.SNAKE);
		snake.addFirst(food);
		//size increments by 1
		this.size++;
	}

	/**
	 * It uses some information and makes proper decisions for the snake's head, also return the action.
	 * @param information Information for the snake, in particular snake's head
	 * @return Proper action to do
	 */
	public Action chooseAction(LocalInformation information) {
		Direction eat = null;
		if(getSize() >= DIVIDING_SIZE) {
			return new Action(Type.REPRODUCE);
		}else { 
			// makes the direction possible that includes a food
			if(information.getNodeDown().getValue() == Value.FOOD) eat = Direction.DOWN;
			else if(information.getNodeUp().getValue() == Value.FOOD) eat = Direction.UP;
			else if(information.getNodeRight().getValue() == Value.FOOD)  eat = Direction.RIGHT;
			else if(information.getNodeLeft().getValue() == Value.FOOD)  eat = Direction.LEFT;
		}
		// checks if there is a food around snake's head, if not goes for the move
		if(eat == null){
			Direction dir = LocalInformation.getRandomDirection(information.getFreeDirections());
			if(information.getNextDirection() != null) {
				return new Action(Type.MOVE, information.getNextDirection());
			}
			else if( dir != null) {
				return new Action(Type.MOVE, dir);
			}else {
				return new Action(Type.STAY);
			}

		}else {
			return new Action(Type.EAT, eat);
		}
	}

	/**
	 * Draws the snake's each node, head is blue and others are red. 
	 * It can be arranged to variable colors by using size, but it may crash to the game
	 */
	public void draw(GridPanel panel) {
		for(Node n : snake) {
			if(n.isHead()) panel.drawSquare(n.getX(), n.getY(), Color.blue);
			else panel.drawSquare(n.getX(), n.getY(), new Color(255 , 0 , 0));
		}
		}
}
