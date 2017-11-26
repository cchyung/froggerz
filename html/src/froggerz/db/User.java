package froggerz.db;

public class User
{
	private String mUsername;
	private String mPassword;
	private int mWins;
	public User()
	{
		mUsername = null;
		mPassword = null;
		mWins = 0;
	}

	/**
	 * @param username
	 * @param password
	 * @param numWins
	 */
	public User(String username, String password, int numWins) {
		mUsername = username;
		mPassword = password;
		mWins = numWins;
	}

	public String getmUsername() {
		return mUsername;
	}

	public void setmUsername(String mUsername) {
		this.mUsername = mUsername;
	}

	public String getmPassword() {
		return mPassword;
	}

	public void setmPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public int getmWins() {
		return mWins;
	}

	public void setmWins(int mWins) {
		this.mWins = mWins;
	}
	
	
}
