package game;
/**
 * Data to be sent from the server to the client
 */
public class GameDataJSON {
	private String command = "";  // Any specific command from the server
	private String positions = "";  // Positions of players that is not this player
	private String data = "";
	
	/////////////////////////////////////// Setters/Getters ///////////////////////////////////////
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void setPositions(String positions) {
		this.positions = positions;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getPositions() {
		return positions;
	}
	
	public String getData() {
		return data;
	}
}
