package froggerz.db;

public class User
{
	private String mUsername;
	private int mWins;
	
	// A registered user for use in the current game
	public User(String username)
	{
		mUsername = username;
	}
	
	// A non-registered user for use in the current game
	public User()
	{
		mUsername = "Guest";
	}
	
	public String getUsername()
	{
		return mUsername;
	}
	
	public void unlockSkin(int wins)
	{
		mWins = wins;
		// TODO we need to set some sort of threshold for "x wins unlocks a new skin"
	}
}
