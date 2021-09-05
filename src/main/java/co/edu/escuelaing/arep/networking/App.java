package co.edu.escuelaing.arep.networking;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.escuelaing.arep.networking.httpserver.WebServer;

/**
 * Clase que contiene las caracteristicas principales de comunicación para Heroku usando librerías para manejo de red de java
 * @author aleja
 *
 */
public class App {
	
	/**
	 * Puente de comunicación
	 */
	public static final Integer PORT = 35000;
	
	/**
	 * Inicia el WebServer
	 * @param args - conexión al servidor
	 */
	public static void main(String[] args) {

		WebServer httpServer = WebServer._instance;
		try {
			httpServer.startSocket(args, getPort());
		} catch (IOException | URISyntaxException e) {
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
		return PORT; // returns default port if heroku-port isn't set (i.e. on localhost)
	}
}