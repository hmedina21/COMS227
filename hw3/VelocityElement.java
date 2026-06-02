package hw3;


/**
 * Class that extends BaseElement and adds a velocity vector such as deltaX or deltaY
 * and it;s methods. This is the superclass for all elements that move using a velocity
 * vector: FlyElement, MovingElement, LiftElement, PlatformElement, FollowerElement.
 * 
 * @author Hugo Medina
 */

public abstract class VelocityElement extends BaseElement{
	
	private double deltaX;
	private double deltaY;
	
	
	/**
	 * Constructs a VelocityElement at the given position and size.
	 * Speed starts at zero in both directions.
	 * 
	 * @param x   x-coordinate of upper left corner
	 * @param y   y-coordinate of upper left corner
	 * @param width  element's width in pixels
	 * @param height element's height in pixels
	 */
	protected VelocityElement(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.deltaX = 0;
		this.deltaY = 0;
	}
	
	
	/**
	 * Sets the velocity for this element.
	 * 
	 * @param deltaX horizontal velocity in pixels per frame
	 * @param deltaY vertical velocity speed in pixels per frame
	 */
	public void setVelocity(double deltaX, double deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	/**
	 * Returns current horizontal velocity (speed).
	 * 
	 * @return horizontal speed in pixels per frame
	 */
	public double getDeltaX() {
		return deltaX;
	}
	
	
	/**
	 * Returns current vertical velocity (speed).
	 * 
	 * @return vertical velocity in pixels per frame
	 */
	public double getDeltaY() {
		return deltaY;
	}
}
