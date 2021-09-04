package co.edu.escuelaing.arep.networking.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import co.edu.escuelaing.arep.networking.httpserver.myspring.Service;

public class WebServer {

	private static final WebServer _instance = new WebServer();

	public static WebServer getInstance() {

		return _instance;
	}

	private WebServer() {

	}

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

	public void serverConnection(Socket clientSocket) throws IOException, URISyntaxException {
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // envío de msgs al Cliente.
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // recibir msgs
																										// del Cliente

		String inputLine, outputLine;
		StringBuilder request = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			// System.out.println("Received: " + inputLine);
			request.append(inputLine);
			if (!in.ready()) {
				break;
			}
		}

		String uriStr;
		System.out.println("request: " + request.toString());
		if (request.toString().equals("")) {
			System.out.println("entra solo la primera vez");
			uriStr = "/";
		}else {
			uriStr = request.toString().split(" ")[1];
		}
		System.out.println("uriStr: " + uriStr);
		URI resourceURI = new URI(uriStr);

		// System.out.println("Received URI path: " + resourceURI.getPath());
		// System.out.println("Received URI query: " + resourceURI.getQuery());

		if (resourceURI.toString().startsWith("/appuser")) {
			outputLine = getComponentResource(resourceURI);
		} else {
			outputLine = getResource(resourceURI);
		}
		out.println(outputLine);

		out.close();
		in.close();
		clientSocket.close();
	}

	// Probar
	// http://localhost:35000/appuser/co.edu.escuelaing.arep.networking.httpserver.webapp.Square?5,
	// este muestra 4.0
	private String getComponentResource(URI resourceURI) {
		String response = default404Response();
		try {
			String classPath = resourceURI.getPath().toString().replaceAll("/appuser/", "");
			Class component = Class.forName(classPath);
			for (Method m : component.getDeclaredMethods()) {
				if (m.isAnnotationPresent(Service.class)) {
					response = m.invoke(null).toString();
					response = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + response;
				}
			}
		} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException
				| IllegalArgumentException e) {
			Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, e);
			response = default404Response();
		}
		return response;
	}

	// Probar localhost:35000/demo.html, este lee el .html, .css y .js
	public String getResource(URI resourceURI) throws IOException {

		StringBuilder response = new StringBuilder();
		Charset charset = Charset.forName("UTF-8");
		Path htmlFile = Paths.get("target/classes/public" + resourceURI.getPath());

		try (BufferedReader in = Files.newBufferedReader(htmlFile, charset)) {

			String str, type = null;
			type = setMimeTypeContent(resourceURI.getPath());
			response = new StringBuilder("HTTP/1.1 200 OK\r\n" + "Content-Type: " + type + "\r\n"); // Define
																									// MimeType
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

	private String default404Response() {
		String outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>\n"
				+ "<html>\n" + "	<head>\n" + "		<meta charset=\"UTF-8\">\n" + "		<title>Error</title>\n"
				+ "	</head>\n" + "	<body>\n" + "		<h1>NOT FOUND 404</h1>\n" + "	</body>\n" + "</html>\n";
		return outputLine;
	}
}