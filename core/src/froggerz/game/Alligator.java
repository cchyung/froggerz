package froggerz.game;

public class Alligator extends Actor
{
	public Alligator(FroggerzGame froggerzGame) 
	{
		super(froggerzGame);
		
		mMoveComp = new MoveComponent(this);
		
		mMoveComp.setForwardSpeed(20.0f);
		
		mCollComp = new CollisionComponent(this);
		mCollComp.SetSize(100, 19);
	}
}
