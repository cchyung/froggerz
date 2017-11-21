package froggerz.db;

public class User
{
	private String mUsername;
	private String mPassword;
	private int mWins;
	private int mNumUnlockedSkins;
	
	public User()
	{
		mUsername = null;
		mPassword = null;
		mWins = 0;
		mNumUnlockedSkins = 0;
	}
	
	public void unlockSkin(int wins)
	{
		mWins = wins;
		// KPTODO create some type of "this many wins unlocks this skin" deal
	}
	
}
