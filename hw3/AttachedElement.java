
package hw3;

import api.AbstractElement;

/**
 * An attached element is one that is associated with another "base" element
 * such as a {@link PlatformElement} or a {@link LiftElement}. Specifically,
 * the attached element's position is determined entirely by the position of
 * the base element — it always remains at a fixed offset horizontally and
 * a fixed distance above the base (controlled by the hover amount).
 * <p>
 * The position is recalculated on every call to {@link #update()} and
 * also when the base is first set via {@link #setBase(AbstractElement)}.
 * 
 * @author Hugo Medina
 */
public class AttachedElement extends BaseElement {
	private int offset;
	private int hover;
	private AbstractElement base;

	/**
	 * Constructs a new AttachedElement with the given dimensions, horizontal
	 * offset, and hover distance. Before being added to a base element, the
	 * x and y coordinates are initialized to zero. When the base is set via
	 * {@link #setBase(AbstractElement)}, the position is calculated as:
	 * <ul>
	 *   <li>x = base.getXReal() + offset</li>
	 *   <li>y = base.getYReal() - height - hover</li>
	 * </ul>
	 * 
	 * @param width  element's width in pixels
	 * @param height element's height in pixels
	 * @param offset horizontal offset from the base element's x-coordinate
	 * @param hover  vertical gap between this element's bottom edge and the
	 *               base element's top edge
	 */
	public AttachedElement(int width, int height, int offset, int hover) {
		super(0, 0, width, height);
		this.offset = offset;
		this.hover = hover;
	}


	/**
	 * {@inheritDoc}
	 * Increments the frame count and then repositions this element relative
	 * to its base element using the configured offset and hover values.
	 */
	@Override
	public void update() {
		incrementFrameCount();
		updatePosition();
	}

	/**
	 * Sets the base element that this element is attached to and immediately
	 * repositions this element relative to the base using the configured
	 * offset and hover values. This method is typically called by
	 * {@link PlatformElement#addAssociated(AttachedElement)} or
	 * {@link LiftElement#addAssociated(AttachedElement)}.
	 * 
	 * @param base the element to attach to
	 */
	public void setBase(AbstractElement base) {
		this.base = base;
		updatePosition();
	}

	/**
	 * Recalculates this element's position based on the base element's
	 * current position, the horizontal offset, and the hover distance.
	 * The resulting position is:
	 * <ul>
	 *   <li>x = base.getXReal() + offset</li>
	 *   <li>y = base.getYReal() - height - hover</li>
	 * </ul>
	 */
	private void updatePosition() {
		setPosition(base.getXReal() + offset, base.getYReal() - getHeight() - hover);
	}
}
