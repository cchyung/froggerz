package froggerz.game;

public class Frog extends Actor{
	
	/**
	 * Constructor
	 * @param game
	 */
	public Frog(Game game) {
		super(game);
		
		mMoveComp = new FrogMove(this);
		mCollComp = new FrogCollide(this);
		mCollComp.SetSize(30, 23);
	}
	
	
	
}