package froggerz.websockets;

import com.badlogic.gdx.utils.ObjectSet;

/**
 * Wrapper for gwt WebSockets
 */
public class GameSocket {
	private static int socketNum = 0;  // Support for multiple sockets from one client
	
	private final String name;
	private final String url;
	private final ObjectSet<GameSocketListener> listeners = new ObjectSet<GameSocketListener>();  // Support for multiple listeners
	
	public GameSocket(String url) {
		this.url = url;
		this.name = "gwtgs-" + ++socketNum;
	}
	
	/////////////////////////////// Native function names begin with native ///////////////////////////////
	
	/**
	 * Native version
	 * Open a JavaScript web socket
	 * @param gs GameSocket to open
	 * @param name Name of the GameSocket opening
	 * @param url Url of the server the GameSocket is connecting to
	 */
	private native void nativeOpen(GameSocket gs, String name, String url) /*-{
		alert("STARTING THE WEBSOCKET");
	    $wnd[name] = new WebSocket(url);
	    $wnd[name].onopen = function() { gs.@froggerz.websockets.GameSocket::onOpen()(); };
	    $wnd[name].onclose = function(event) { gs.@froggerz.websockets.GameSocket::onClose(SLjava/lang/String;Z)(event.code, event.reason, event.wasClean); };    
	    $wnd[name].onmessage = function(message) { alert("HEY YOU GOT A MESSAGE"); gs.@froggerz.websockets.GameSocket::onMessage(Ljava/lang/String;)(message.data); }
	}-*/;
	// For error callback $wnd[name].onerror = function() { gs.@froggerz.websockets.GameSocket::onError()(); };
	
	/**
	 * Native version
	 * @param name Name of the GameSocket to close
	 */
	private native void nativeClose(String name) /*-{
	    $wnd[name].close();
	}-*/;
	
	/**
	 * Native version
	 * @param name Name of the GameSocket sending
	 * @param message Message to be sent
	 */
	private native void nativeSend(String name, String message) /*-{
		$wnd[name].send(message);
	}-*/;
	
	/////////////////////////////// Non-native method counterparts ///////////////////////////////
	
	/**
	 * Non-native version
	 */
	public void close() {
        nativeClose(this.name);
    }
	
	/**
	 * Non-native version
	 */
	public void open() {
        nativeOpen(this, this.name, this.url);
    }
	
	/**
	 * Non-native version
	 * @param message Message to send to all listeners
	 */
	public void send(String message) {
        nativeSend(this.name, message);
    }
	
	/////////////////////////////// Callback functions ///////////////////////////////
	
	/**
	 * Callback function for receiving a message. Calls for all gameListeners 
	 * @param message Message to be sent
	 */
	protected void onMessage(String message) {
		debug("before sending listeners");
        for (GameSocketListener listener : listeners) {
        	debug("about to send message to listener");
        	listener.onMessage(message);
        	debug("sent message to listener");
        }
        debug("after sending listeners");
    }
	
	/**
	 * Callback function for closing. Calls for all gameListeners
	 * @param code Close code to sent by the server
	 * @param reason DOMString the reason why the server closed the connection
	 * @param wasClean True if the connection was cleanly closed, false otherwise
	 */
	protected void onClose(short code, String reason, boolean wasClean) {
        CloseEvent event = new CloseEvent(code, reason, wasClean);
        for (GameSocketListener listener : listeners)
        	listener.onClose(event);
    }
	
	/**
	 * Callback function for opening. Calls for all gameListeners
	 */
	protected void onOpen() {
        for (GameSocketListener listener : listeners)
        	listener.onOpen();
    }
	
	/////////////////////////////// Listener functions ///////////////////////////////
	
	/**
	 * @param listener GameSocketListener to add to this GameSocket
	 */
	public void addListener(GameSocketListener listener) {
		listeners.add(listener);
    }
	
	/**
	 * @param listener GameSocketListener to remove from this GameSocket
	 */
    public void removeListener(GameSocketListener listener) {
    	listeners.remove(listener);
    }
    
    private native void debug(String alert) /*-{
		alert(alert);
	}-*/;
}