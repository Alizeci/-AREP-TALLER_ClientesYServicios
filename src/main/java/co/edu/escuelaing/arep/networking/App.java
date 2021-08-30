package co.edu.escuelaing.arep.networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.escuelaing.arep.networking.httpserver.WebServer;

public class App {
	
	private static int port = 35000;

	public static void main(String[] args) {
		port = getPort();
		//System.out.println("Server is starting...");

		WebServer httpServer = WebServer.getInstance();
		try {
			httpServer.startSocket(args, port);
		} catch (IOException e) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * This method reads the default port as specified by the PORT variable in the
	 * environment.
	 *
	 * Heroku provides the port automatically so you need this to run the project on
	 * Heroku.
	 */
	static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 35000; // returns default port if heroku-port isn't set (i.e. on localhost)
	}
}