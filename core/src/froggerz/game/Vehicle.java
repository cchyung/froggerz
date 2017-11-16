package froggerz.game;

public class Vehicle extends Actor{
	
	public Vehicle(Game game) {
		super(game);
		
		mMoveComp = new MoveComponent(this);
		
		//Move to the left across the screen
		mMoveComp.setForwardSpeed(-40.0f);
	}
}
