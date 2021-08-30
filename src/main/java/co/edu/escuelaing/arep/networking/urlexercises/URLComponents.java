package co.edu.escuelaing.arep.networking.urlexercises;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class URLComponents 
{
    public static void main( String[] args )
    {
        try {
			URL firstSite = new URL ("https://campusvirtual.escuelaing.edu.co/moodle/mod/feedback/view.php?id=115605");
			System.out.println( "La URL es: "+ firstSite.toString());
			System.out.println( "El host es: "+ firstSite.getHost());
			System.out.println( "El puerto es: "+ firstSite.getPort());
			System.out.println( "El authority es: "+ firstSite.getAuthority());
			System.out.println( "El archivo es: "+ firstSite.getFile());
			System.out.println( "El path: "+ firstSite.getPath());
			System.out.println( "El protocolo es: "+ firstSite.getProtocol());
			System.out.println( "La query es: "+ firstSite.getQuery());
			System.out.println( "La referencia es: "+ firstSite.getRef()); //Si tiene un Tag
		} catch (MalformedURLException e) {
			Logger.getLogger(URLComponents.class.getName()).log(Level.SEVERE, null, e);
		}
    }
}
