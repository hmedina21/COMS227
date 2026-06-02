package hw2;

import static api.Direction.*;
import static api.Orientation.*;

import java.util.ArrayList;

import api.Boulder;
import api.Cell;
import api.Direction;
import api.Move;

/**
 * Represents a board in the game. A board contains a 2D grid of cells and a
 * list of boulders that slide over the cells.
 * 
 * @author Hugo Medina
 */
public class Board {
	/**
	 * 2D array of cells, the indexes signify (row, column) with (0, 0) representing
	 * the upper-left corner of the board.
	 */
	private Cell[][] grid;

	/**
	 * A list of boulders that are positioned on the board.
	 */
	private ArrayList<Boulder> boulders;

	/**
	 * A list of moves that have been made in order to get to the current position.
	 * of boulders on the board.
	 */
	private ArrayList<Move> moveHistory;
	
	/*
	 * The boulder that the user is currently holding. Null means nothing is being grabbed.
	 */
	private Boulder grabbedBoulder;
	
	/*
	 * Determines when the game is over. returns true when a boulder reached an exit and puzzle is solved.
	 * 
	 */
	private boolean gameOver;
	
	/*
	 * Keeps track of the number of successful moves the player has made so far.
	 */
	private int moveCount;
	
	
	/**
	 * Constructs a new board from a given 2D array of cells and list of boulders. The
	 * cells of the grid should be updated to indicate which cells have boulders
	 * placed over them (i.e., placeBoulder() method of Cell). The move history should
	 * be initialized as empty.
	 * 
	 * @param grid   a 2D array of cells which is expected to be a rectangular shape
	 * @param boulders list of boulders already containing row-column position which
	 *               should be placed on the board
	 */
	public Board(Cell[][] grid, ArrayList<Boulder> boulders) {
		this.grid = grid;
		this.boulders = boulders;
		this.moveHistory = new ArrayList<>();
		this.grabbedBoulder = null;
		this.moveCount = 0;
		this.gameOver = false;
		
		//puts each boulder on the starting cells
		for (int i = 0; i < boulders.size(); i++) {
			placeBoulderOnGrid(boulders.get(i));
		}
		
	}

	/**
	 * DO NOT MODIFY THIS CONSTRUCTOR
	 * <p>
	 * Constructs a new board from a given 2D array of String descriptions.
	 * 
	 * @param desc 2D array of descriptions
	 */
	public Board(String[][] desc) {
		this(GridUtil.createGrid(desc), GridUtil.findBoulders(desc));
	}

	/**
	 * Returns the number of rows of the board.
	 * 
	 * @return number of rows
	 */
	public int getRowSize() {
		return grid.length;
	}

	/**
	 * Returns the number of columns of the board.
	 * 
	 * @return number of columns
	 */
	public int getColSize() {
		return grid[0].length;
	}

	/**
	 * Returns the cell located at a given row and column.
	 * 
	 * @param row the given row
	 * @param col the given column
	 * @return the cell at the specified location
	 */
	public Cell getCellAt(int row, int col) {
		return grid[row][col];
	}

	/**
	 * Returns the total number of moves (calls to moveGrabbedBoulder which
	 * resulted in a boulder being moved) made so far in the game.
	 * 
	 * @return the number of moves
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * Returns a list of all boulders on the board.
	 * 
	 * @return a list of all boulders
	 */
	public ArrayList<Boulder> getBoulders() {
		return boulders;
	}

	/**
	 * Returns true if the player has completed the puzzle by positioning a boulder
	 * over an exit, false otherwise.
	 * 
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
	/**
	 * Models the user grabbing (mouse button down) a boulder over the given row and
	 * column. The purpose of grabbing a boulder is for the user to be able to drag
	 * the boulder to a new position, which is performed by calling
	 * moveGrabbedBoulder().
	 * <p>
	 * This method should find which boulder has been grabbed (if any) and record
	 * that boulder as grabbed in some way.
	 * 
	 * @param row row to grab the boulder from
	 * @param col column to grab the boulder from
	 */
	public void grabBoulderAt(int row, int col) {
		Boulder bd = grid[row][col].getBoulder();
		if (bd != null) {
			grabbedBoulder = bd;
		}
	}

	/**
	 * Models the user releasing (mouse button up) the currently grabbed boulder
	 * (if any). Update the object accordingly to indicate no boulder is
	 * currently being grabbed.
	 */
	public void releaseBoulder() {
		grabbedBoulder = null;
	}

	/**
	 * Returns the currently grabbed boulder. If there is no currently grabbed
	 * boulder the method return null.
	 * 
	 * @return the currently grabbed boulder or null if none
	 */
	public Boulder getGrabbedBoulder() {
		return grabbedBoulder;
	}

	/**
	 * Returns true if the cell at the given row and column is available for a
	 * boulder to be placed over it. Boulders can only be placed over ground
	 * and exits. Additionally, a boulder cannot be placed over a cell that is
	 * already occupied by another boulder.
	 * 
	 * @param row row location of the cell
	 * @param col column location of the cell
	 * @return true if the cell is available for a boulder, otherwise false
	 */
	public boolean isAvailable(int row, int col) {
		if (!inBounds(row, col)) {
			return false;
		}
		
		Cell c = grid[row][col];
		return (c.isGround() || c.isExit()) && c.getBoulder() == null;
	}

	/**
	 * Moves the currently grabbed boulder by one cell in the given direction. A
	 * horizontal boulder is only allowed to move right and left and a vertical boulder
	 * is only allowed to move up and down. A boulder can only move over a cell that
	 * is a floor or exit and is not already occupied by another boulder. The method
	 * does nothing under any of the following conditions:
	 * <ul>
	 * <li>The game is over.</li>
	 * <li>No boulder is currently grabbed by the user.</li>
	 * <li>A boulder is currently grabbed by the user, but the boulder is not allowed to
	 * move in the given direction.</li>
	 * </ul>
	 * If none of the above conditions are meet, the method does at least the following:
	 * <ul>
	 * <li>Moves the boulder object by calling its move() method.</li>
	 * <li>Calls placeBoulder() for the grid cell that the boulder is being moved into.</li>
	 * <li>Calls removeBoulder() for the grid cell that the boulder is being moved out of.</li>
	 * <li>Adds the move (as a Move object) to the end of the move history list.</li>
	 * <li>Increments the count of total moves made in the game.</li>
	 * </ul>
	 * 
	 * @param dir the direction to move
	 */
	public void moveGrabbedBoulder(Direction dir) {
		// does nothing if the game is over or there's nothing to be grabbed
		if (gameOver || grabbedBoulder == null) {
			return;
		}
		
		Boulder b = grabbedBoulder;
		int row = b.getFirstRow();
		int col = b.getFirstCol();
		int length = b.getLength();
		
		
		//Horizontal boulders
		if (b.getOrientation() == HORIZONTAL && (dir == UP || dir == DOWN)) {
			return;
		}
		
		//Vertical boulders
		if (b.getOrientation() == VERTICAL && (dir == LEFT || dir == RIGHT)) {
			return;
		}
		
		if (dir == LEFT) {
			int newCol = col -1;
			if (!isAvailable(row, newCol)) {
				return;
			}
			//slides left
			grid[row][col + length - 1].removeBoulder();
			b.move(LEFT);
			grid[row][newCol].placeBoulder(b);
		
		} else if (dir == RIGHT) {
			int newCol = col + length;
			if (!isAvailable(row, newCol)) {
				return;
			}
			//slides right
			grid[row][col].removeBoulder();
			b.move(RIGHT);
			grid[row][newCol].placeBoulder(b);
		
		} else if (dir == UP) {
			int newRow = row - 1;
			if (!isAvailable(newRow, col)) {
				return;
			}
			//slides up
			grid[row + length - 1][col].removeBoulder();
			b.move(UP);
			grid[newRow][col].placeBoulder(b);
		} else {
			int newRow = row + length;
			if (!isAvailable(newRow, col)) {
				return;
			}
			//slides down
			grid[row][col].removeBoulder();
			b.move(DOWN);
			grid[newRow][col].placeBoulder(b);
		}
		
		//counts and records the move
		moveHistory.add(new Move(b, dir));
		moveCount++;
		
		//returns if the puzzle is solved and the boulder is on an exit
		if (boulderOnExit(b)) {
			gameOver = true;
		}
	}

	/**
	 * Resets the state of the game back to the start, which includes the move
	 * count, the move history, and whether the game is over. The method calls the
	 * reset method of each boulder object. It also updates each grid cells by calling
	 * their placeBoulder method to either set a boulder if one is located over the cell
	 * or set null if no boulder is located over the cell.
	 */
	public void reset() {
		//reset each boulder to its original position
		for (int i = 0; i < boulders.size(); i++) {
			boulders.get(i).reset();
		}
		
		//Clears all boulder references from the grid
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r][c].removeBoulder();
			}
		}
		
		//Replaces all boulders to their starting positions
		for (int i = 0; i < boulders.size(); i++) {
			placeBoulderOnGrid(boulders.get(i));
		}
		
		// reset game to the beginning
		moveCount = 0;
		gameOver = false;
		moveHistory.clear();
		grabbedBoulder = null;
	}
	

	/**
	 * Returns a list of all legal moves that can be made by any boulder on the
	 * current board.
	 * 
	 * @return a list of legal moves
	 */
	public ArrayList<Move> getAllPossibleMoves() {
		ArrayList<Move> moves = new ArrayList<>();
		
		for (int i = 0; i < boulders.size(); i++) {
			Boulder b = boulders.get(i);
			int row = b.getFirstRow();
			int col = b.getFirstCol();
			int length = b.getLength();
			
			//The following checks if it can slide left, right, up and down
			if (b.getOrientation() == HORIZONTAL) {
				if (isAvailable(row, col - 1)) {
					moves.add(new Move(b, LEFT));
				}
				
				if (isAvailable(row, col + length)) {
					moves.add(new Move(b, RIGHT));
				}
			} else {
				if (isAvailable(row - 1, col)) {
					moves.add(new Move(b, UP));
				}
				
				if (isAvailable(row + length, col)) {
					moves.add(new Move(b, DOWN));
				}
			}
		}
		
		return moves;
	}

	/**
	 * Gets the list of all moves performed to get to the current position on the
	 * board.
	 * 
	 * @return a list of moves performed to get to the current position
	 */
	public ArrayList<Move> getMoveHistory() {
		return moveHistory;
	}

	/**
	 * This method is only used by the Solver.
	 * <p>
	 * Undo the previous move. The method gets the last move on the moveHistory list
	 * and performs the opposite actions of that move, which are the following:
	 * <ul>
	 * <li>grabs the moved boulder and calls moveGrabbedBoulder passing the opposite
	 * direction</li>
	 * <li>decreases the total move count by two to undo the effect of calling
	 * moveGrabbedBoulder twice</li>
	 * <li>if required, sets is game over to false</li>
	 * <li>removes the move from the moveHistory list</li>
	 * </ul>
	 * If the moveHistory list is empty this method does nothing.
	 */
	public void undoMove() {
		if (moveHistory.isEmpty()) {
			return;
		}
		
		//get the last move that was made
		Move last = moveHistory.get(moveHistory.size() - 1);
		
		//grabs the boulder and moves it the opposite way
		grabBoulderAt(last.getBoulder().getFirstRow(), last.getBoulder().getFirstCol());
		moveGrabbedBoulder(oppositeDirection(last.getDirection()));
		
		//moveGrabbedBoulder added 1 to moveCount, and we want to remove the original
		//move too, so we subtract 2
		moveCount -= 2;
		
		//the game is not over anymore if we undid the winning move
		gameOver = false;
		
		//remove undo move that was just added by moveGrabbedBoulder and then removes the original move
		moveHistory.remove(moveHistory.size() - 1);
		moveHistory.remove(moveHistory.size() - 1);
		
		releaseBoulder();
		
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		for (Cell row[] : grid) {
			if (!first) {
				buff.append("\n");
			} else {
				first = false;
			}
			for (Cell cell : row) {
				buff.append(cell.toString());
				buff.append(" ");
			}
		}
		return buff.toString();
	}
	
	
	/*
	 * Private Helper methods
	 */
	
	/**
	 * Places the boulder onto every cell it has based on its orientation, length, and position.
	 * 
	 * @param b the boulder in place
	 */
	private void placeBoulderOnGrid(Boulder b) {
		int row = b.getFirstRow();
		int col = b.getFirstCol();
		int length = b.getLength();
		
		
		if (b.getOrientation() == HORIZONTAL) {
			//fill cells from left to right
			for (int c = col; c < col + length; c++) {
				grid[row][c].placeBoulder(b);
			}
		}else {
			//fill cells from top to bottom
			for (int r = row; r < row + length; r++) {
				grid[r][col].placeBoulder(b);
			}
		}
	}
	
	
	/**
	 * Checks if there is any cell the boulder is sitting on that is an exit.
	 * 
	 * @param b boulder to check
	 * @return true if boulder overlaps exit cell
	 */
	private boolean boulderOnExit(Boulder b) {
		int row = b.getFirstRow();
		int col = b.getFirstCol();
		int length = b.getLength();
		
		if (b.getOrientation() == HORIZONTAL) {
			for (int c = col; c < col + length; c++) {
				if (grid[row][c].isExit()) {
					return true;
				}
			}
		} else {
			for (int r = row; r < row + length; r++) {
				if (grid[r][col].isExit()) {
					return true;
				}
			}
		}
		return false;
		
	}
	
	
	/**
	 * Checks if the given column and row are inside the grid.
	 * This helps prevent the array out of bound errors when checking nearby cells.
	 * 
	 * @param row row to check
	 * @param col column to check
	 * @return true if the position is inside the grid
	 * 
	 */
	private boolean inBounds(int row, int col) {
		return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
	}
	
	
	/**
	 * Returns opposite direction of the original direction.
	 * 
	 * @param dir original direction
	 * @param the opposite direction
	 */
	private Direction oppositeDirection(Direction dir) {
		if (dir == LEFT) {
			return RIGHT;
		} else if (dir == RIGHT) {
			return LEFT;
		} else if (dir == UP) {
			return DOWN;
		} else {
			return UP;
		}
	}
}
