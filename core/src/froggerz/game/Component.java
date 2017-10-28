package froggerz.game;

public class Component {
	protected Actor mOwner = null;
	
	/**
	 * Constructor
	 */
	public Component(Actor owner)
	{ 
		mOwner = owner;
	}
	
	/**
	 * Performed when the object is no longer needed, unloads any data
	 */
	public void destroy() {};
	
	/**
	 * Update this component by deltaTime
	 */
	public void update(float deltaTime) {};
	
	/**
	 * Process input for this component (if needed)
	 */
	public void processInput() {};
}
