package froggerz.game;

public class Vehicle extends Actor{
	
	public Vehicle(FroggerzGame froggerzGame) {
		super(froggerzGame);
		
		mMoveComp = new MoveComponent(this);
		
		mCollComp = new CollisionComponent(this);
		mCollComp.SetSize(43, 28);
	}
}
