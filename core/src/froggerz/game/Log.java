package froggerz.game;

public class Log extends Actor {
	
	public Log(Game game) {
		super(game);
		
		mMoveComp = new MoveComponent(this);
		
		mMoveComp.setForwardSpeed(20.0f);
		
		mCollComp = new CollisionComponent(this);
		mCollComp.SetSize(80, 19);
	}
}
