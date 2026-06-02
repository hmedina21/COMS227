package hw3;


/**
 * A moving element in which the vertical velocity is adjusted each frame by a
 * gravitational constant to simulate gravity. The element can be set to
 * "grounded", meaning gravity will no longer influence its vertical velocity.
 * <p>
 * By default, a new FlyingElement is grounded with zero gravity. To enable
 * gravity, call {@link #setGravity(double)} with a positive value and
 * {@link #setGrounded(boolean)} with {@code false}.
 * 
 * @author Hugo Medina
 */
public class FlyingElement extends VelocityElement {
	private boolean grounded;
	private double gravity;

	/**
	 * Constructs a new FlyingElement at the given position with the given
	 * dimensions. By default the element is grounded (gravity has no effect)
	 * and gravity is zero.
	 * 
	 * @param x      x-coordinate of upper left corner
	 * @param y      y-coordinate of upper left corner
	 * @param width  element's width
	 * @param height element's height
	 */
	public FlyingElement(double x, double y, int width, int height) {
		super(x, y, width, height);
		grounded = true;
		gravity = 0;
	}


	/**
	 * {@inheritDoc}
	 * Increments the frame count, moves the position by the current velocity
	 * vector, and then — if the element is not grounded — adds the gravity
	 * constant to the vertical velocity. When grounded, the velocity is left
	 * unchanged after movement.
	 */
	@Override
	public void update() {
		incrementFrameCount();
		setPosition(getXReal() + getDeltaX(), getYReal() + getDeltaY());
		if (!grounded) {
			setVelocity(getDeltaX(), getDeltaY() + gravity);
		}
	}


	/**
	 * Sets the gravitational constant applied to the vertical velocity
	 * each frame when this element is not grounded. A positive value
	 * accelerates the element downward (increasing y).
	 * 
	 * @param gravity gravitational acceleration in pixels per frame squared
	 */
	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	/**
	 * Sets whether this element is grounded. When grounded, the gravity
	 * constant is not applied to the vertical velocity during
	 * {@link #update()}.
	 * 
	 * @param grounded {@code true} to disable gravity, {@code false} to
	 *                 enable it
	 */
	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	/**
	 * Returns whether this element is currently grounded.
	 * 
	 * @return {@code true} if the element is grounded (gravity disabled),
	 *         {@code false} otherwise
	 */
	public boolean isGrounded() {
		return grounded;
	}
}
