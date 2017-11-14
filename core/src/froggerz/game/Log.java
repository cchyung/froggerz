package froggerz.game;

public class Log extends Actor {
	
	public Log(Game game) {
		super(game);
		
		mMoveComp = new MoveComponent(this);
	}
}
