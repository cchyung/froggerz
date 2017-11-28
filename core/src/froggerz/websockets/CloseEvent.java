package froggerz.websockets;

/**
 * Provides a Java implementation of the JavaScript CloseEvent
 */
public class CloseEvent {
	private final short code;  // Close code to sent by the server
	private final String reason;  // DOMString the reason why the server closed the connection
	private final boolean wasClean;  // True if the connection was cleanly closed, false otherwise
	
	public CloseEvent(short code, String reason, boolean wasClean) {
        this.code = code;
        this.reason = reason;
        this.wasClean = wasClean;
    }

    public short code() {
        return code;
    }

    public String reason() {
        return reason;
    }

    public boolean wasClean() {
        return wasClean;
    }
}
