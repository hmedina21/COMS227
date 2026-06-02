package hw3;


/**
 * A PlatformElement is an element with two distinctive behaviors. First, it
 * can be set up to move horizontally within a fixed set of boundaries. On
 * reaching a boundary, the x-component of its velocity is reversed so the
 * platform "bounces" back and forth. Second, it maintains a list of
 * <em>associated</em> elements (such as {@link AttachedElement} or
 * {@link FollowerElement}) whose motion occurs relative to this platform.
 * When the platform's {@link #update()} is called, all associated elements
 * are also updated.
 * 
 * @author Hugo Medina
 */
public class PlatformElement extends BoundedContainerElement {

	/**
	 * Constructs a new PlatformElement at the given position with the given
	 * dimensions. The velocity defaults to zero. The initial horizontal
	 * boundaries are set to [x, y + width].
	 * 
	 * @param x      x-coordinate of initial position of upper left corner
	 * @param y      y-coordinate of initial position of upper left corner
	 * @param width  object's width
	 * @param height object's height
	 */
	public PlatformElement(double x, double y, int width, int height) {
		super(x, y, width, height);
	}
	
	/**
	 * {inheritDoc}
	 * Reverses the x velocity if the platform has reached its horizontal boundary.
	 */
	@Override
	protected void bounceIfNeeded() {
		if (getXReal() + getWidth() >= getMax()) {
			setPosition(getMax() - getWidth(), getYReal());
			setVelocity(-getDeltaX(), getDeltaY());
		} else if (getXReal() <= getMin()) {
			setPosition(getMin(), getYReal());
			setVelocity(-getDeltaX(), getDeltaY());
		}
	}

}