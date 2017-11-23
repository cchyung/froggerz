package froggerz.server;

import java.io.IOException;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/server")
public class ServerSocket {
	private static Vector<Session> sessionVector = new Vector<Session>();
	private RunningGame runningGame = null;

	// Can change method names, but cannot change annotations because that is what identifies what to call
	// These are called "Callback methods"
	@OnOpen
	public void open(Session session) {
		System.out.println("Player Connected!");
		
		if(runningGame == null) {
			runningGame = new RunningGame(session);
			runningGame.start();
			System.out.println("Player added to an empty game!");
		}
		else if (runningGame) {
			System.out.println("Client Connected!");
		}
		else {
			// TODO create a new lobby
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		
	}

	@OnClose
	public void close(Session session) {
		System.out.println("Client Disconnected!");
		sessionVector.remove(session);
	}

	@OnError
	public void onError(Throwable error) {
		System.out.println("Error!");
	}
}
