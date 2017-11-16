package froggerz.game;

public class Alligator extends Actor
{
	public Alligator(Game game) 
	{
		super(game);
		
		mMoveComp = new MoveComponent(this);
	}
}
