package co.edu.escuelaing.arep.networking.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import co.edu.escuelaing.arep.networking.App;
import co.edu.escuelaing.arep.networking.httpserver.myspring.Service;

public class WebServerToFramework {

	private static final WebServerToFramework _instance = new WebServerToFramework();

	public static WebServerToFramework getInstance() {

		return _instance;
	}

	private WebServerToFramework() {

	}

	public void startSocket(String[] args, int port) throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
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
			try {
				serverConnection(clientSocket);
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		serverSocket.close();
	}

	public void serverConnection(Socket clientSocket) throws IOException, URISyntaxException{
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // envío de msgs al Cliente.
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // recibir msgs
																										// del Cliente

		String inputLine, outputLine;
		StringBuilder request = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			System.out.println("Message received from the client: " + inputLine);
			request.append(inputLine);
			if (!in.ready()) {
				break;
			}
		}
		String uriStr = request.toString().split(" ")[1];
		URI resourceURI = new URI(uriStr);
		outputLine = getResource(resourceURI);
		out.println(outputLine);

		out.close();
		in.close();
		clientSocket.close();
	}

	/**
	 * MiniFramework
	 * 
	 * @param resourceURI
	 * @return
	 */
	private String getComponentResource(URI resourceURI) {
		String response = "";
		String classPath = resourceURI.toString().substring(8);
		try {
			Class component = Class.forName(classPath);
			for (Method m : component.getDeclaredMethods()) { // Obtener métodos de component
				if (m.isAnnotationPresent(Service.class)) {
					response = m.invoke(null).toString(); // Convertir a string
					response = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html \r\n" + response;
				}
			}
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			Logger.getLogger(WebServerToFramework.class.getName()).log(Level.SEVERE, null, e);
			response = default404Response();
		}
		return "";
	}

	public String getResource(URI resourceURI) throws IOException {
		System.out.println("Received URI path: " + resourceURI.getPath());
		System.out.println("Received URI query: " + resourceURI.getQuery());

		Path htmlFile = Paths.get("public" + resourceURI.getPath());
		//File htmlFile = new File("target/classes/public" + resourceURI.getPath());
		//BufferedReader in = new BufferedReader(new FileReader(htmlFile));

		String response = "";
		Charset charset = Charset.forName("UTF-8");
		try(BufferedReader in = Files.newBufferedReader(htmlFile, charset)){
			String str, type;
			type = setMimeTypeContent(resourceURI.getPath());
			StringBuilder sb = new StringBuilder("HTTP/1.1 200 OK\r\n" + "Content-Type: " + type + "\r\n"); // Define MimeType of file

			while ((str = in.readLine()) != null) {
				sb.append(str + "\n");
			}
			response = sb.toString();
		}catch(IOException e) {
			System.err.format("IOException: %s%n", e);
			response = default404Response();
		};	

		return response;
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
				+ "<html>\n" + "	<head>\n" + "		<meta charset=\"UTF-8\">\n"
				+ "		<title>Title of the document</title>\n" + "	</head>\n" + "	<body>\n"
				+ "		My Web Site Space!!\n" + "	</body>\n" + "</html>\n";
		return outputLine;
	}
}
