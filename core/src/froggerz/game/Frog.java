package froggerz.game;

public class Frog extends Actor{
	
	/**
	 * Constructor
	 * @param froggerzGame
	 */
	public Frog(FroggerzGame froggerzGame) {
		super(froggerzGame);
		
		mMoveComp = new FrogMove(this);
		mCollComp = new FrogCollide(this);
		mCollComp.SetSize(30, 23);
	}
	
	
	
}