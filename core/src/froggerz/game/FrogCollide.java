package froggerz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;

public class FrogCollide extends CollisionComponent
{
	public FrogCollide(Actor owner) 
	{
		super(owner);
	}
	
	@Override
	public void processInput()
	{
		boolean gotReset = false;
		boolean onFloaty = false;
		
		//Handling finish line collision
		if (mOwner.getPosition().y >= 448)
		{
			System.out.println("End");
			
			//End game...better way to do this probably
			Gdx.app.exit();
		}
		
		for (int i = 0; i < mOwner.getGame().getActors().size; i++)
		{
			Actor currActor = mOwner.getGame().getActors().get(i);
						
			//Tiles are Actors too, so we need to make sure we're only checking for collision against actual objects like vehicles
			if (!currActor.getClass().getSimpleName().equals("Actor") && !currActor.getClass().getSimpleName().equals("Frog"))
			{	
				if (currActor.getCollision() != null)
				{
					if (Intersect(currActor.getCollision()))
					{
						gotReset = true;
						//Handling log collision
						if (currActor.getClass().getSimpleName().equals("Log"))
						{
							onFloaty = true; 
							
							//Hop onto the gator's back at a decent distance from where you were
							if (mOwner.getPosition().x < currActor.getPosition().x + 10)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 5, currActor.getPosition().y));
							}
							else if (mOwner.getPosition().x < currActor.getPosition().x + 20)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 15, currActor.getPosition().y));
							}
							else if (mOwner.getPosition().x < currActor.getPosition().x + 30)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 25, currActor.getPosition().y));
							}
							else if (mOwner.getPosition().x < currActor.getPosition().x + 40)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 35, currActor.getPosition().y));
							}
							else if (mOwner.getPosition().x < currActor.getPosition().x + 50)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 45, currActor.getPosition().y));
							}
							else if (mOwner.getPosition().x < currActor.getPosition().x + 60)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 55, currActor.getPosition().y));
							}
							else if (mOwner.getPosition().x < currActor.getPosition().x + 70)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 65, currActor.getPosition().y));
							}
							else if (mOwner.getPosition().x < currActor.getPosition().x + 80)
							{
								mOwner.setPosition(new Vector2(currActor.getPosition().x + 75, currActor.getPosition().y));
							}
						}
						
						//Handling alligator collision
						if (currActor.getClass().getSimpleName().equals("Alligator"))
						{
							onFloaty = true; 
							
							//Safe part of gator
							if (mOwner.getPosition().x > currActor.getPosition().x + 30) 
							{								
								//Hop onto the gator's back at a decent distance from where you were
								if (mOwner.getPosition().x < currActor.getPosition().x + 40)
								{
									mOwner.setPosition(new Vector2(currActor.getPosition().x + 35, currActor.getPosition().y));
								}
								else if (mOwner.getPosition().x < currActor.getPosition().x + 50)
								{
									mOwner.setPosition(new Vector2(currActor.getPosition().x + 45, currActor.getPosition().y));
								}
								else if (mOwner.getPosition().x < currActor.getPosition().x + 60)
								{
									mOwner.setPosition(new Vector2(currActor.getPosition().x + 55, currActor.getPosition().y));
								}
								else if (mOwner.getPosition().x < currActor.getPosition().x + 70)
								{
									mOwner.setPosition(new Vector2(currActor.getPosition().x + 65, currActor.getPosition().y));
								}
								else if (mOwner.getPosition().x < currActor.getPosition().x + 80)
								{
									mOwner.setPosition(new Vector2(currActor.getPosition().x + 75, currActor.getPosition().y));
								}
								else if (mOwner.getPosition().x < currActor.getPosition().x + 90)
								{
									mOwner.setPosition(new Vector2(currActor.getPosition().x + 85, currActor.getPosition().y));
								}
								else if (mOwner.getPosition().x < currActor.getPosition().x + 100)
								{
									mOwner.setPosition(new Vector2(currActor.getPosition().x + 95, currActor.getPosition().y));
								}
							}
							
							//In alligator's mouth
							else
							{
								//Frames 11-18 the alligator's mouth is open
								if (currActor.getSprite().getCurrentFrame() > 10 && currActor.getSprite().getCurrentFrame() < 19)
								{
									mOwner.setPosition(new Vector2(mOwner.getStartPos()));
								}
							}
						}
						
						//Handling vehicle collision
						if (currActor.getClass().getSimpleName().equals("Vehicle") || currActor.getClass().getSimpleName().equals("PoliceCar"))
						{
							mOwner.setPosition(new Vector2(mOwner.getStartPos()));
						}
					}
					
				}
				
			}
			
		}
		
		//Handling water collision
		if (!onFloaty && mOwner.getPosition().y >= 256.0 && mOwner.getPosition().y <= 288.0 + 32)
		{
			mOwner.setPosition(new Vector2(mOwner.getStartPos()));
			gotReset = true;
		}
		
		if(gotReset) {
			Client gameSocket = mOwner.getGame().getClient();
			PositionPacket data = new PositionPacket();
			data.playerNum = mOwner.getGame().getPlayerNum();
			data.vector2 = mOwner.getPosition().toString();
			gameSocket.sendTCP(data);
		}
	}
}
