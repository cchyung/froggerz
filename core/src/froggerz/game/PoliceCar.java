package froggerz.game;

public class PoliceCar extends Vehicle
{
	public PoliceCar(Game game) {
		super(game);
		
		mCollComp = new CollisionComponent(this);
		mCollComp.SetSize(65, 28);
	}
}