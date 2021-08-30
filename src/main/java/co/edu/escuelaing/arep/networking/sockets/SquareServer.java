package co.edu.escuelaing.arep.networking.sockets;

import java.net.*;
import java.io.*;

/**
 * Clase servidor que regresa el mismo mensaje que lee. Multiusuario (no
 * concurrente) Uno después del otro.
 * 
 * @author aleja
 *
 */
public class SquareServer {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		try {
			// Obtener una canal de conexión con el cliente(s)
			serverSocket = new ServerSocket(35000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 35000.");
			System.exit(1);
		}
		boolean running = true;
		//Mientras esté corriendo permita hacer una nueva conexión
		while (running) {
			Socket clientSocket = null;
			try {
				// Acepta conexión
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			// Flujo de salida msgs (concurrencia el bloque de abajo para su acceso concurrente)
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			// Flujo de entrada msgs
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine, outputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("El número recibido fue: " + inputLine);
				outputLine = "Respuesta desde el server: El cuadrado es " + Math.pow(Double.parseDouble(inputLine), 2);
				out.println(outputLine);
				//Sale del ciclo pero se mantiene escuchando
				if (outputLine.equals("Respuesta desde el server: Bye."))
					break;
			}
			// Cerramos conexiones
			out.close();
			in.close();
			clientSocket.close(); //Hasta aquí. (concurrencia)
		}
		serverSocket.close();

	}
}