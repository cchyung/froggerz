package froggerz.websockets;

import froggerz.game.Game;

/**
 * Java interface for JavaScript WebSocket listener
 */
public class GameSocketListener {
	private Game game = null;
	
	/**
	 * What the listener should do when closed
	 * @param event
	 */
	public void onClose(CloseEvent event){}

	/**
	 * What the listener should do when a message is received
	 * @param message
	 */
	public void onMessage(String message){
    	game.addQueue(message);
    }
    
    /**
     * What the listener should do when opened
     */
	public void onOpen(){}
    
    public void setGame(Game game) {
    	this.game = game;
    }
}
