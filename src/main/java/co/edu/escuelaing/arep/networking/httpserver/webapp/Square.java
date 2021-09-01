package co.edu.escuelaing.arep.networking.httpserver.webapp;

import co.edu.escuelaing.arep.networking.httpserver.myspring.Service;

public class Square {
	
	@Service
	public Double square() {
		return 2.0 * 2.0;
	}

}
