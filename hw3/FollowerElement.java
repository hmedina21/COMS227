package hw3;

import api.AbstractElement;

/**
 * A follower element is one that is associated with another "base" element
 * such as a {@link PlatformElement} or {@link LiftElement}. The follower's
 * position tracks the base element's position: when the base moves, the
 * follower moves with it. However, unlike an {@link AttachedElement}, the
 * follower is not always at a fixed location relative to the base. When the
 * follower's horizontal velocity is set to a non-zero value, the follower
 * will oscillate back and forth between the left and right edges of the
 * base element it is associated with.
 * <p>
 * The boundaries for oscillation are automatically updated each frame to
 * match the base element's current left and right edges.
 * 
 * @author Hugo Medina
 */
public class FollowerElement extends VelocityElement {
	private double min;
	private double max;
	private int initialOffset;
	private AbstractElement base;
	private double offset;

	/**
	 * Constructs a new FollowerElement with the given dimensions and initial
	 * horizontal offset. Before being added to a base element, the x and y
	 * coordinates are zero. When a base element is set via
	 * {@link #setBase(AbstractElement)}, the initial x-coordinate becomes the
	 * base's x-coordinate plus the given offset, and the y-coordinate becomes
	 * the base's y-coordinate minus this element's height.
	 * 
	 * @param width         element's width in pixels
	 * @param height        element's height in pixels
	 * @param initialOffset when added to a base, this amount is added to the
	 *                      base's x-coordinate to calculate this element's
	 *                      initial x-coordinate
	 */
	public FollowerElement(int width, int height, int initialOffset) {
		super(0, 0, width, height);
		this.min = 0;
		this.max = width;
		this.initialOffset = initialOffset;
		offset = 0;
	}

	/**
	 * Sets the oscillation boundaries for this follower. These are
	 * automatically updated each frame to match the base element's
	 * left and right edges.
	 * 
	 * @param lower the left boundary (minimum x, i.e., base's left edge)
	 * @param upper the right boundary (maximum x, i.e., base's right edge)
	 */
	public void setBounds(double lower, double upper) {
		this.min = lower;
		this.max = upper;
	}

	/**
	 * Returns the current left boundary for oscillation.
	 * 
	 * @return the minimum x boundary (base's left edge)
	 */
	public double getMin() {
		return min;
	}

	/**
	 * Returns the current right boundary for oscillation.
	 * 
	 * @return the maximum x boundary (base's right edge)
	 */
	public double getMax() {
		return max;
	}

	/**
	 * {@inheritDoc}
	 * Increments the frame count, recalculates the oscillation boundaries
	 * based on the base element's current position, moves the follower
	 * relative to the base by applying the horizontal velocity to its
	 * offset, and reverses the horizontal velocity if a boundary is reached.
	 * The stored offset is updated to reflect the follower's current
	 * position relative to the base.
	 */
	@Override
	public void update() {
		incrementFrameCount();
		setBounds(base.getXReal(), base.getXReal() + base.getWidth());
		setPosition(base.getXReal() + offset + getDeltaX(), base.getYReal() - getHeight());
		
		if (getXReal() + getWidth() >= getMax()) {
			setPosition(max - getWidth(), getYReal());
			setVelocity(-getDeltaX(), getDeltaY());
		} else if (getXReal() <= min) {
			setPosition(min, getYReal());
			setVelocity(-getDeltaX(), getDeltaY());
		}
		
		offset = getXReal() - base.getXReal();
	}
	
	/**
	 * Sets the base element that this follower is associated with and
	 * initializes the follower's position and boundaries. The boundaries
	 * are set to the base element's left and right edges, the offset is
	 * set to the initial offset, and the position is calculated as:
	 * <ul>
	 *   <li>x = base.getXReal() + initialOffset</li>
	 *   <li>y = base.getYReal() - height</li>
	 * </ul>
	 * This method is typically called by
	 * {@link PlatformElement#addAssociated(FollowerElement)} or
	 * {@link LiftElement#addAssociated(FollowerElement)}.
	 * 
	 * @param base the element this follower should track
	 */
	public void setBase(AbstractElement base) {
		this.base = base;
		setBounds(base.getXReal(), base.getXReal() + base.getWidth());
		offset = initialOffset;
		setPosition(base.getXReal() + offset, base.getYReal() - getHeight());
	}
}
