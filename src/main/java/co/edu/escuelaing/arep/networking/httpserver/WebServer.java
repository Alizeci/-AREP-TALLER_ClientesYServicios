package co.edu.escuelaing.arep.networking.httpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase que contiene todas las características del Webserver.
 * @author aleja
 * 05/09/2021
 */
public class WebServer {

	/**
	 * Atributo que define el WebServer
	 */
	public static final WebServer _instance = new WebServer();
	
	
	public WebServer() {

	}

	/**
	 * Preparando la comunicación para el intercambio de mensajes.
	 * @param args - peticiones del cliente
	 * @param port - puerto de comunicación
	 * @throws IOException - Cuando no es posible establecer la comunicación
	 * @throws URISyntaxException - Cuando no es posible interpretar la URI
	 */
	public void startSocket(String[] args, int port) throws IOException, URISyntaxException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 35000.");
			System.exit(1);
		}

		boolean running = true;
		while (running) {
			Socket clientSocket = null;
			try {
				System.out.println("Listo para recibir ...");
				clientSocket = serverSocket.accept();

			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			serverConnection(clientSocket);
		}
		serverSocket.close();
	}
	
	/**
	 * Conectando el Cliente con el Servidor y atendiendo su petición(es).
	 * @param clientSocket - brinda comunicación al cliente
	 * @throws IOException - Cuando no es posible establecer la comunicación
	 * @throws URISyntaxException - Cuando no es posible interpretar la URI
	 */
	public void serverConnection(Socket clientSocket) throws IOException, URISyntaxException {
		if (clientSocket != null) {

			PrintWriter out;
			BufferedReader in;
			OutputStream los_outputStream;
			InputStream lis_inputStream;

			los_outputStream = clientSocket.getOutputStream();
			lis_inputStream = clientSocket.getInputStream();

			if ((los_outputStream != null) && (lis_inputStream != null)) {

				String inputLine, outputLine;
				StringBuilder request;
				InputStreamReader lisr_inputStreamReader;

				request = new StringBuilder();
				lisr_inputStreamReader = new InputStreamReader(lis_inputStream);
				out = new PrintWriter(los_outputStream, true); // envío de msgs al Cliente.

				if (lisr_inputStreamReader != null) {
					in = new BufferedReader(lisr_inputStreamReader); // recibir msgs del Cliente

					if (in != null && in.ready()) {

						while ((inputLine = in.readLine()) != null) {
							// System.out.println("Received: " + inputLine);
							request.append(inputLine);
							if (!in.ready()) {
								break;
							}
						}
					}
					String ls_request;
					ls_request = request.toString();

					if ((ls_request != null) && (!ls_request.isEmpty())) {

						String ls_uriStr;
						String[] las_request;

						las_request = ls_request.split(" ");

						if ((las_request != null)) {
							ls_uriStr = las_request[1];

							if ((ls_uriStr != null) && (!ls_uriStr.isEmpty())) {
								URI resourceURI;
								resourceURI = new URI(ls_uriStr);

								outputLine = getResource(resourceURI);
								out.println(outputLine);
							}
						}
					}
					out.close();
					in.close();
				}
			} else {
				throw new IOException("ServerConnection BufferReader input vacío o nulo!");

			}
			clientSocket.close();
		} else {
			throw new IOException("ServerConnection Socket no puede ser nulo");
		}
	}

	// Probar localhost:35000/demo.html, este lee el .html, .css y .js
	/**
	 * Permite leer un recurso de tipo .html, .css y .js
	 * @param resourceURI - Ruta del recurso requerido.
	 * @return El contenido del recurso.
	 * @throws IOException - Cuando no es posible leer el recurso.
	 */
	public String getResource(URI resourceURI) throws IOException {
		StringBuilder response = new StringBuilder();

		Charset charset = Charset.forName("UTF-8");
		Path htmlFile = Paths.get("target/classes/public" + resourceURI.getPath());

		try (BufferedReader in = Files.newBufferedReader(htmlFile, charset)) {
			String str, type = null;

			type = setMimeTypeContent(resourceURI.getPath());
			response = new StringBuilder("HTTP/1.1 200 OK\r\n" + "Content-Type: " + type + "\r\n" + "\r\n"); // Define MimeType
																									// of file
			while ((str = in.readLine()) != null) {
				response.append(str + "\n");
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
			return default404Response();
		}
		return response.toString();
	}
	
	/**
	 * Permite conocer el tipo de recurso
	 * @param path - recurso
	 * @return El tipo del recurso
	 */
	private String setMimeTypeContent(String path) {
		String type = null;
		if (path.contains(".css")) {
			type = "text/css";
		} else if (path.contains(".html")) {
			type = "text/html";
		} else if (path.contains(".js")) {
			type = "text/javascript";
		}
		return type;
	}
	
	/**
	 * Página por defecto al intentar conectar con el servidor
	 * @return la página por defecto en html
	 */
	private String default404Response() {
		String outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>\n"
				+ "<html>\n" + "	<head>\n" + "		<meta charset=\"UTF-8\">\n" + "		<title>Inicio</title>\n"
				+ "	</head>\n" + "	<body>\n" + "		<h1>NOT FOUND 404</h1>\n" + "	</body>\n" + "</html>\n";
		return outputLine;
	}
}