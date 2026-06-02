package hw3;

import java.awt.Rectangle;
import api.AbstractElement;

/**
 * This is an abstract class that provides shared data and behavior to all game objects in the hw3 package.
 * It stores things like positions(x,y), size (width, height), a frame counter, and a flag to mark when
 * something should be removed, along with the methods to use them.
 * 
 * 
 * All specific fame object classes in hw3 extend this class directly or indirectly, so they don't have to
 * define these fields and methods themselves.
 * 
 * 
 * @author Hugo Medina
 */

public abstract class BaseElement extends AbstractElement {
	
	/** x position of top left corner */
	private double x;
	
	/** y position of top left corner */
	private double y;
	
	/** width of pixels */
	private int width;
	
	/** Height in pixels */
	private int height;
	
	/** Number of frames since it was created */
	private int countFrame;
	
	/** return true if the element has been marked for deletion */
	private boolean marked;
	
	
	/**
	 * Constructs a BaseElement with a position and size.
	 * The frame count starts at 0, and it is not marked for deletion.
	 * 
	 * @param x  x-coordinate of the top left corner
	 * @param y  y-coordinate of the top left corner
	 * @param width   element's width in pixels
	 * @param height  element's height in pixels
	 */
	protected BaseElement(double x, double y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.countFrame = 0;
		this.marked = false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getXInt() {
		return (int) Math.round(x);
	}
	
	/**
	 * {inheritDoc}
	 */
	@Override
	public int getYInt() {
		return (int) Math.round(y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth() {
		return width;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight() {
		return height;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getRect() {
		return new Rectangle(getXInt(), getYInt(), width, height);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPosition(double newX, double newY) {
		x = newX;
		y = newY;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public double getXReal() {
		return x;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public double getYReal() {
		return y;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFrameCount() {
		return countFrame;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMarked() {
		return marked;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void markForDeletion() {
		marked = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean collides(AbstractElement other) {
		return getRect().intersects(other.getRect());
	}
	
	/**
	 * increase the frame counter by one.
	 * Subclasses should call this at the start of their update() method
	 * 
	 */
	protected void incrementFrameCount() {
		countFrame++;
	}

}
