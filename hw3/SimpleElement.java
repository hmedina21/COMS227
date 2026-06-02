package hw3;


/**
 * Minimal concrete extension of AbstractElement. The <code>update</code> method
 * in this implementation just increments the frame count. This element has no
 * built-in movement or other behaviors; it simply exists at a fixed position
 * and can be drawn, repositioned, and checked for collisions.
 * 
 * @author Hugo Medina
 */
public class SimpleElement extends BaseElement {

	/**
	 * Constructs a new SimpleElement at the given position with the given
	 * dimensions. The frame count is initially zero and the element is not
	 * marked for deletion.
	 * 
	 * @param x      x-coordinate of upper left corner
	 * @param y      y-coordinate of upper left corner
	 * @param width  element's width in pixels
	 * @param height element's height in pixels
	 */
	public SimpleElement(double x, double y, int width, int height) {
		super(x, y, width, height);
	}

	/**
	 * {@inheritDoc}
	 * increment or increase frame count is the only action in this implementation
	 */
	@Override
	public void update() {
		incrementFrameCount();
	}
}
