package froggerz.game;

public class Log extends Actor {
	
	public Log(FroggerzGame froggerzGame) {
		super(froggerzGame);
		
		mMoveComp = new MoveComponent(this);
		
		mMoveComp.setForwardSpeed(20.0f);
		
		mCollComp = new CollisionComponent(this);
		mCollComp.SetSize(80, 19);
	}
}
