package co.edu.escuelaing.arep.networking.sockets;

import java.net.Socket;

import java.io.*;
import java.net.*;

/**
 * Client socket, que envía datos y recibe respuestas.
 * 
 * @author aleja
 *
 */

public class SquareClient {
	public static void main(String[] args) throws IOException {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			// Obtener una conexión
			echoSocket = new Socket("127.0.0.1", 35000);
			// Flujo de salida msgs
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			// Flujo de entrada msgs
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don’t know about host!.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn’t get I/O for " + "the connection to: localhost.");
			System.exit(1);
		}
		// Lee desde la consola el mensaje a transmitir
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput, serverResponse;
		System.out.println("Ingrese un numero: ");
		while ((userInput = stdIn.readLine()) != null) {
			out.println(userInput);
			serverResponse = in.readLine();
			System.out.println(serverResponse);
			if (serverResponse.equals("Respuesta desde el server: Bye."))
				break;
		}
		// Cerramos conexiones
		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
	}
}