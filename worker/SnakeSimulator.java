package worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import assets.Node;
import assets.Node.Value;
import assets.Snake;
import assets.Food;
import game.Direction;
import game.GridGame;
/**
 * Most important and clever class of the game and project
 * Arranges most of the game
 * @author Burak
 *
 */
public class SnakeSimulator extends GridGame {
	/**
	 * Multi-Dimension array of node to keep map in a right way
	 */
	private Node[][] map;
	/**
	 * Food for the game
	 */
	private Food food;
	/**
	 * Snakes in the game is kept in this field
	 */
	private ArrayList<Snake> snakes;
	/**
	 * Iterator to add/remove node without any error
	 */
	private ListIterator<Snake> it;

	/**
	 * Constructor for the snake simulator game. Also, calls the constructor of the GridGame class
	 * @param gridWidth Width of the game
	 * @param gridHeight Height of the game
	 * @param gridSquareSize Size of the one square
	 * @param frameRate rate of the game
	 */
	public SnakeSimulator(int gridWidth, int gridHeight, int gridSquareSize, int frameRate) {
		super(gridWidth, gridHeight, gridSquareSize, frameRate);
		map = new Node[gridWidth][gridHeight];
		snakes = new ArrayList<Snake>();
		it = snakes.listIterator();
		for(int i = 0; i < map.length ; i++) {
			for(int j = 0; j < map[i].length; j++) {
				map[i][j] = new Node( Value.EMPTY, false,i,j);	
			}
		}
	}


	@Override
	protected void timerTick() {
		//iterator
		it = snakes.listIterator();
		while(it.hasNext()){
			Snake copy = it.next();
			Action action = copy.chooseAction(createLocalInformationForSnake(copy));
			// Execute action
			if (action != null) {
				if (action.getType() == Action.Type.STAY) {
					// STAY
					copy.stay();
				} else if (action.getType() == Action.Type.REPRODUCE) {
					// REPRODUCE
					addSnake(copy.reproduce());
				} else if (action.getType() == Action.Type.MOVE) {
					copy.move(action.getDirection());
					// moving snake's old tail is updated and removed here
					updateMap(copy.getList().getLast().getX(),copy.getList().getLast().getY(),copy.getList().removeLast());
				} else if (action.getType() == Action.Type.EAT) {
					// EAT
					Node food = getNodeAtDirection(copy.getList().getFirst().getX(), copy.getList().getFirst().getY(), action.getDirection());
					if (food.getValue() == Value.FOOD) {
						copy.eat(food);
						removeDrawable(food);
						int x = getX();
						int y = getY();
						//to get a free and empty spawn point for the food
						while(map[x][y].getValue() != Value.EMPTY) {
							x = getX();
							y = getY();
						}
						addFood(new Food(x, y));
					}
				}
			}

			for(Node n : copy.getList()) {
				updateMap(n.getX(),n.getY(), n);
			}
		}
	}


	/**
	 * Method to add food, in this project no further purpose needed
	 * @param food food to add the game
	 */
	public void addFood(Food food) {
		this.food = food;
		Node n = food.getFood();
		if (isPositionInsideGrid(n.getX(), n.getY())) {
			if (map[n.getX()][n.getY()].getValue()== Value.EMPTY) {
				addDrawable(n);
				updateMap(n.getX(),n.getY(), n);
			} 
		} 
	}


	/**
	 * Method to add newborn snake to the game
	 * @param sn snake to add
	 */
	public void addSnake(Snake sn) {
		// iterator keeps going
		while(it.hasNext()) {
			it.next();
		}
		it.add(sn);
		addDrawable(sn);
		for(Node n : sn.getList()) {
			if (n != null) {
				if (isPositionInsideGrid(n.getX(), n.getY())) {
					if (map[n.getX()][n.getY()].getValue() == Value.EMPTY) {
						updateMap(n.getX(),n.getY(), n);
					} 
				} 
			}
		}
	}

	/**
	 * Method to check whether position is inside grid or not
	 * @param x x coordinate to check
	 * @param y y coordinate to check
	 * @return whether position is inside grid or not
	 */
	private boolean isPositionInsideGrid(int x, int y) {
		return (x >= 0 && x < getGridWidth()) && (y >= 0 && y < getGridHeight());
	}

	/**
	 * Method to update map in every iteration of the game
	 * @param x x to update
	 * @param y y to update
	 * @param n node to update
	 */
	private void updateMap(int x, int y, Node n) {
		if (isPositionInsideGrid(x, y)) {
			if(n == null) {
				map[x][y].setX(x);
				map[x][y].setY(y);
				map[x][y].setHead(false);
				map[x][y].setValue(Value.EMPTY);
			}
			else {
				map[x][y] = n;
			}
		}
	}

	/**
	 * To get node at a given coordinate
	 * @param x coordinate to get node
	 * @param y coordinate to get node
	 * @return the node that is at a a given coordinate
	 */
	private Node getNodeAtPosition(int x, int y) {
		if (!isPositionInsideGrid(x, y)) {
			// check node class for this speacial case
			return new Node("outsideGrid");
		}
		return map[x][y];
	}

	/**
	 * To get a node at a given direction from the head node
	 * @param x coordinate of the head node
	 * @param y coordinate of the head node
	 * @param direction direction to get node
	 * @return the node at the given direction
	 */
	private Node getNodeAtDirection(int x, int y, Direction direction) {
		if (direction == null) {
			return null;
		}
		int xTarget = x;
		int yTarget = y;
		if (direction == Direction.UP) {
			yTarget--;
		} else if (direction == Direction.DOWN) {
			yTarget++;
		} else if (direction == Direction.LEFT) {
			xTarget--;
		} else if (direction == Direction.RIGHT) {
			xTarget++;
		}
		return getNodeAtPosition(xTarget, yTarget);
	}

	/**
	 * To create most of the information about environment for the head node of the snake
	 * @param snake snake which the information will be about
	 * @return the environment information about the given snake
	 */
	private LocalInformation createLocalInformationForSnake(Snake snake) {
		//head node it taken here
		Node n = snake.getList().getFirst();
		int x =n.getX();
		int y = n.getY();
		HashMap<Direction, Node> nodes = new HashMap<>();
		nodes.put(Direction.UP, getNodeAtPosition(x, y - 1));
		nodes.put(Direction.DOWN, getNodeAtPosition(x, y + 1));
		nodes.put(Direction.LEFT, getNodeAtPosition(x - 1, y));
		nodes.put(Direction.RIGHT, getNodeAtPosition(x + 1, y));

		ArrayList<Direction> freeDirections = new ArrayList<>();
		if (nodes.containsKey(Direction.UP) && isPositionInsideGrid(x, y - 1)) {
			if(nodes.get(Direction.UP).getValue() == Value.EMPTY) freeDirections.add(Direction.UP);
		}
		if (nodes.containsKey(Direction.DOWN)&& isPositionInsideGrid(x, y + 1)) {
			if(nodes.get(Direction.DOWN).getValue() == Value.EMPTY) freeDirections.add(Direction.DOWN);
		}
		if (nodes.containsKey(Direction.LEFT) && isPositionInsideGrid(x - 1, y)) {
			if(nodes.get(Direction.LEFT).getValue() == Value.EMPTY) freeDirections.add(Direction.LEFT);
		}
		if (nodes.containsKey(Direction.RIGHT)&& isPositionInsideGrid(x + 1, y)) {
			if(nodes.get(Direction.RIGHT).getValue() == Value.EMPTY) freeDirections.add(Direction.RIGHT);
		}

		return new LocalInformation(getGridWidth(), getGridHeight(), nodes, freeDirections, food, n);
	}

	/**
	 * A random x coordinate generator method
	 * @return a random x coordinate - integer
	 */
	public int getX() {
		return (int) (Math.random() * getGridWidth());
	}
	/**
	 * A random y coordinate generator method
	 * @return a random x coordinate - integer
	 */
	public int getY() {
		return (int) (Math.random() * getGridWidth());
	}

}
