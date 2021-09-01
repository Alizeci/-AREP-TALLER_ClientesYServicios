package co.edu.escuelaing.arep.networking.httpserver.webapp;

import co.edu.escuelaing.arep.networking.httpserver.myspring.Service;

public class Square {
	
	@Service(uri="/square")
	public static Double square() {
		return 2.0 * 2.0;
	}

}
