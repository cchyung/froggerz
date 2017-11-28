package froggerz.game;

public class PoliceCar extends Vehicle
{
	public PoliceCar(FroggerzGame froggerzGame) {
		super(froggerzGame);
		mCollComp = new CollisionComponent(this);
		mCollComp.SetSize(65, 28);
	}
}
