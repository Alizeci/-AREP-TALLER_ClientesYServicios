package co.edu.escuelaing.arep.networking.urlexercises;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebPageURLReader {

	public static void main(String[] args) throws IOException {
		URL google = new URL("https://campusvirtual.escuelaing.edu.co/moodle/mod/feedback/view.php?id=115605"); //http://localhost:4567/data?st=FB
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(google.openStream()))) {
			String inputLine = null;
			while ((inputLine = reader.readLine()) != null) {
				System.out.println(inputLine);
			}
		} catch (IOException x) {
			System.err.println(x);
		}
	}
}