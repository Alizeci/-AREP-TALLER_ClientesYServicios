package co.edu.escuelaing.arep.networking.urlexercises;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebPageURLConnectionReader {

	public static void main(String[] args) throws IOException {
		// Crea el objeto que representa una URL
		URL siteURL = new URL("https://campusvirtual.escuelaing.edu.co/moodle/mod/feedback/view.php?id=115605");
		// Crea el objeto que URLConnection
		URLConnection urlConnection = siteURL.openConnection();

		// Obtiene los campos del encabezado y los almacena en un estructura Map
		Map<String, List<String>> headers = urlConnection.getHeaderFields();
		// Obtiene una vista del mapa como conjunto de pares <K,V> para poder navegarlo
		Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
		
		StringBuffer sb = new StringBuffer(); //armar el result para crear el archivo
		// Recorre la lista de campos e imprime los valores
		for (Map.Entry<String, List<String>> entry : entrySet) {
			String headerName = entry.getKey();
			// Si el nombre es nulo, significa que es la linea de estado
			if (headerName != null) {
				System.out.print(headerName + ":");
				sb.append(headerName + ":");
			}
			List<String> headerValues = entry.getValue();
			for (String value : headerValues) {
				System.out.print(value+"\n");
				sb.append(value+"\n");
			}
			
			System.out.println("");
			
			System.out.println("-------message-body------");
			
			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
				String inputLine = null;
				while ((inputLine = reader.readLine()) != null) {
					System.out.println(inputLine);
					sb.append(inputLine);
				}
			} catch (IOException x) {
				System.err.println(x);
			}
			saveInFile(sb.toString());
		}
	}

	/**
	 * Almacena en un archivo con el nombre resultado.html los datos recibidos.
	 * @param result
	 * @throws IOException
	 */
	private static void saveInFile(String result) throws IOException{
		String fileName = "result.html";
		String filePath = "/Users/aleja/eclipse-workspace/AREP/Networking/src/main/resources/public_html/"+fileName;
		File file = new File(filePath);
		BufferedWriter bw;
		if(file.exists()) {
		      bw = new BufferedWriter(new FileWriter(file));
		      bw.write(result);
		}else {
			bw = new BufferedWriter(new FileWriter(file));
		    bw.write("No existe el archivo");
		}
		bw.close();
	}
}