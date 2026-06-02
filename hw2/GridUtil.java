package hw2;

import static api.Orientation.*;
import static api.CellType.*;

import java.util.ArrayList;

import api.Boulder;
import api.Cell;

/**
 * Utilities for parsing string descriptions of a grid.
 * 
 * @author Hugo Medina
 */
public class GridUtil {
	/**
	 * Constructs a 2D grid of Cell objects given a 2D array of cell descriptions.
	 * String descriptions are a single character and have the following meaning.
	 * <ul>
	 * <li>"*" represents a wall.</li>
	 * <li>"e" represents an exit.</li>
	 * <li>"." represents a floor.</li>
	 * <li>"[", "]", "^", "v", or "#" represent a part of a block. A block is not a
	 * type of cell, it is something placed on a cell floor. For these descriptions
	 * a cell is created with CellType of FLOOR. This method does not create any
	 * blocks or set blocks on cells.</li>
	 * </ul>
	 * The method only creates cells and not blocks. See the other utility method
	 * findBlocks which is used to create the blocks.
	 * 
	 * @param desc a 2D array of strings describing the grid
	 * @return a 2D array of cells the represent the grid without any blocks present
	 */
	public static Cell[][] createGrid(String[][] desc) {
		int rows = desc.length;
		int cols = desc[0].length;
		Cell[][] grid = new Cell[rows][cols];
		
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String s = desc[r][c];
				
				//walls get WALL type, exits get EXIT type, and everything else is GROUND
				//Ground cells: ".", "[", "]", "^", "v", "#"
				if (s.equals("*")) {
					grid[r][c] = new Cell(r, c, WALL);
				} else if (s.equals("e")) {
					grid[r][c] = new Cell(r, c, EXIT);
				} else {
					grid[r][c] = new Cell(r, c, GROUND);
				}
			}
		}
		
		return grid;
	}

	/**
	 * Returns a list of blocks that are constructed from a given 2D array of cell
	 * descriptions. String descriptions are a single character and have the
	 * following meanings.
	 * <ul>
	 * <li>"[" the start (left most column) of a horizontal block</li>
	 * <li>"]" the end (right most column) of a horizontal block</li>
	 * <li>"^" the start (top most row) of a vertical block</li>
	 * <li>"v" the end (bottom most column) of a vertical block</li>
	 * <li>"#" inner segments of a block, these are always placed between the start
	 * and end of the block</li>
	 * <li>"*", ".", and "e" symbols that describe cell types, meaning there is not
	 * block currently over the cell</li>
	 * </ul>
	 * 
	 * @param desc a 2D array of strings describing the grid
	 * @return a list of blocks found in the given grid description
	 */
	public static ArrayList<Boulder> findBoulders(String[][] desc) {
		int rows = desc.length;
		int cols = desc[0].length;
		ArrayList<Boulder> boulders = new ArrayList<>();
		
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String s = desc[r][c];
				
				//"[" is the starting point of a horizontal boulder
				if (s.equals("[")) {
					int length = 1;
					
					//count segments until we hit "]"
					while (!desc[r][c + length].equals("]")) {
						length++;
					}
					length++;
					boulders.add(new Boulder(r, c, length, HORIZONTAL));
				}
				
				//"^" is the starting point of a vertical boulder
				if (s.equals("^")) {
					int length = 1;
					
					while (!desc[r + length][c].equals("v")) {
						length++;
					}
					length++;
					boulders.add(new Boulder(r, c, length, VERTICAL));
				}
			}
		}
		

		return boulders;
	}
}
