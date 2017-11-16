package froggerz.game;

public class PoliceCar extends Vehicle
{
	public PoliceCar(Game game) {
		super(game);
		
		mMoveComp.setForwardSpeed(-60.0f);
	}
}
