package froggerz.jsonobjects;

/**
 * Data to be sent from the server to the client
 */
public class GameDataJSON {
	private String command = "";  // Any specific command from the server
	private float deltaTime;  // To update all non-player actors
	private String data = "";
	
	/////////////////////////////////////// Setters/Getters ///////////////////////////////////////
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void setDeltaTime(float deltaTime) {
		this.deltaTime = deltaTime;
	}
	
	public String getCommand() {
		return command;
	}
	
	public float getDeltaTime() {
		return deltaTime;
	}
}
