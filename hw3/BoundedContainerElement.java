package hw3;

import java.util.ArrayList;

import api.AbstractElement;

/**
 * Class that extends to VelocityElement and adds two shared behaviors used by
 * LiftElement and PlatformElement:
 * 
 * 1.Bounded movement - The element bounces back when it hits a min or max boundary.
 *   Subclasses decide if the bouncing is vertical (lift) or horizontal (platform).
 *   
 * 2.Associated elements - the element keeps a list of AttachedElement and FollowerElement
 * 	 Objects that move in relation to this element. Each time update() is called, all elements
 *   in the list are updated too.
 * 
 * @author Hugo Medina
 */

public abstract class BoundedContainerElement extends VelocityElement {
	/** Movement for lower boundary */
	private double min;
	
	/** Movement for upper boundary */
	private double max;
	
	/** List of elements whose motion is related to this element */
	private ArrayList<AbstractElement> associated;
	
	
	/**
	 * Creates a BoundedCountainerElement with given position and dimensions.
	 * Initial boundaries are set to [x, x + width].
	 * 
	 * 
	 * @param x  x-coordinate of upper left corner
	 * @param y  y-coordinate of upper left corner
	 * @param width  element's width in pixels
	 * @param height element's height in pixels
	 */
	protected BoundedContainerElement(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.min = x;
		this.max = x + width;
		this.associated = new ArrayList<AbstractElement>();
	}
	
	
	/**
	 * Sets movement boundaries for this element
	 * 
	 * @param lower  lower boundary
	 * @param upper  upper boundary
	 */
	public void setBounds(double lower, double upper) {
		this.min = lower;
		this.max = upper;
	}
	
	
	/**
	 * Returns lower boundary.
	 * 
	 * @return the minimum boundary value
	 */
	public double getMin() {
		return min;
	}
	
	
	/**
	 * Returns upper boundary.
	 * 
	 * @return the minimum boundary value
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * Returns list of elements related or associated with this element
	 * 
	 * @return list of associated elements
	 */
	public ArrayList<AbstractElement> getAssociated() {
		return associated;
	}
	
	/**
	 * Adds an AttachedElemetns to the list and sets this elements as its base.
	 * 
	 * @param e  attachedElement to add
	 */
	public void addAssociated(AttachedElement e) {
		associated.add(e);
		e.setBase(this);
	}
	
	/**
	 * Adds a FollowerElement to the list and sets this element as its base.
	 * 
	 * @param e FollowerElement to add
	 */
	public void addAssociated(FollowerElement e) {
		associated.add(e);
		e.setBase(this);
	}
	
	/**
	 * Removes elements in the list that have been marked for deletion.
	 * 
	 */
	public void deleteMarkedAssociated() {
		for (int i = associated.size() - 1; i >= 0; i--) {
			if (associated.get(i).isMarked()) {
				associated.remove(i);
			}
		}
	}
	
	
	/**
	 * Bounces back element if it hits a boundary.
	 * Subclasses decide if bouncing is vertical (lift) or horizontal (platform).
	 */
	protected abstract void bounceIfNeeded();
	
	/**
	 * {@inheritDoc}
	 * Moves element, checks boundaries, and updates all elements in the list. 
	 * 
	 */
	@Override
	public void update() {
		incrementFrameCount();
		setPosition(getXReal() + getDeltaX(), getYReal() + getDeltaY());
		bounceIfNeeded();
		for (AbstractElement a : associated) {
			a.update();
		}
	}

}
