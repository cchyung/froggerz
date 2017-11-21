package froggerz.websockets;

/**
 * Java interface for JavaScript WebSocket listener
 */
public interface GameSocketListener {
	
	/**
	 * What the listener should do when closed
	 * @param event
	 */
	void onClose(CloseEvent event);

	/**
	 * What the listener should do when a message is received
	 * @param message
	 */
    void onMessage(String message);

    /**
     * What the listener should do when opened
     */
    void onOpen();
}
