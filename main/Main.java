package main;

import java.awt.EventQueue;

import assets.Food;
import assets.Snake;
import ui.ApplicationWindow;
import worker.SnakeSimulator;

/**
 * This is the beginning class of the self-played game.
 * @author Burak
 */
public class Main {

	/**
	 * It keeps a final value so that the first spawning snake not stuck to the world.
	 * It should not be bigger than the grid size minus one because it cannot spawn to the world correctly
	 */
	private static final int FIRST_SNAKE_SIZE = 4;
	
	/**
	 * Value to keep windows square, in other words to eliminate creation of the not square game panel
	 * If the grid size becomes too big, then ,in game, food may not be seen due to the drawing code of it, if so you
	 * can try to lower grid size, to see it. In short, drawing code of the food also cuts some parts of the boxes that 
	 * it draw.
	 */
	private static final int GRID_SIZE = 25;
	
	/**
	 * Value to set the game speed
	 */
	private static final int GAME_SPEED = 10;
	
	/**
	 * This is the main part of the snake game. Game deviation begins in here.
	 *
	 * @param args application arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				// Create game
				// You can change the world width and height, size of each grid square in pixels or the game speed
				SnakeSimulator game = new SnakeSimulator(GRID_SIZE, GRID_SIZE, 100 * 8/GRID_SIZE, GAME_SPEED);		
				// Create and add snake
				game.addSnake(new Snake(FIRST_SNAKE_SIZE,1,FIRST_SNAKE_SIZE));
				// Create and add food
				// get special x an y so that it will not overlap with the snake
				int x = game.getX();
				int y = game.getY();
				while(x == 1 || x == 2 || x == 3 || x == 4 && y == 1) {
					x = game.getX();
					y = game.getY();
				}
				game.addFood(new Food(x, y));

				// Create application window that contains the game panel
				ApplicationWindow window = new ApplicationWindow(game.getGamePanel());
				window.getFrame().setVisible(true);

				// Start game
				game.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
