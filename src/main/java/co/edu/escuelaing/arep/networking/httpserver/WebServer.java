package co.edu.escuelaing.arep.networking.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer {

	private static final WebServer _instance = new WebServer();

	public static WebServer getInstance() {

		return _instance;
	}

	private WebServer() {

	}

	public void startSocket(String[] args, int port) throws IOException {
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

	public void serverConnection(Socket clientSocket) throws IOException {
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
		String uriStr = request.toString().split(" ")[1];
		URI resourceURI;

		try {
			resourceURI = new URI(uriStr);
			outputLine = getResource(resourceURI);
			out.println(outputLine);
		} catch (URISyntaxException e) {
			Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, e);
		}

		out.close();
		in.close();
		clientSocket.close();
	}

	public String getResource(URI resourceURI) throws IOException {
		// System.out.println("Received URI path: " + resourceURI.getPath());
		// System.out.println("Received URI query: " + resourceURI.getQuery());

		File htmlFile;
		// System.out.println("res: "+resourceURI.getPath().length());
		if (resourceURI.getPath().length() != 1) {
			htmlFile = new File("target/classes/public" + resourceURI.getPath());
		} else {
			htmlFile = new File("target/classes/public/index.html");
		}
		BufferedReader in = new BufferedReader(new FileReader(htmlFile));

		String str, type = null;
		type = setMimeTypeContent(resourceURI.getPath());
		StringBuilder sb = new StringBuilder("HTTP/1.1 200 OK\r\n" + "Content-Type: " + type + "\r\n"); // Define
																										// MimeType of
																										// file

		while ((str = in.readLine()) != null) {
			sb.append(str + "\n");
		}

		return sb.toString();
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

	private String computeDefaultResponse() {
		String outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>\n"
				+ "<html>\n" + "	<head>\n" + "		<meta charset=\"UTF-8\">\n"
				+ "		<title>Title of the document</title>\n" + "	</head>\n" + "	<body>\n"
				+ "		My Web Site Space!!\n" + "	</body>\n" + "</html>\n";
		return outputLine;
	}
}
