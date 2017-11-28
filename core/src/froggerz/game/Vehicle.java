package froggerz.game;

public class Vehicle extends Actor{
	
	public Vehicle(Game game) {
		super(game);
		
		mMoveComp = new MoveComponent(this);
		
		mCollComp = new CollisionComponent(this);
		mCollComp.SetSize(43, 28);
	}
}