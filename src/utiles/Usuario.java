package utiles;
import cliente1.*;
import cliente2.*;
import hash.*;
import servidorGeneral.*;

import java.io.File;
import java.io.Serializable;

public class Usuario implements Serializable{
	private String host, puerto;
	private File directorio;
	private static final long serialVersionUID = 111L;

	public Usuario(String host, String puerto, File directorio) {
		this.host = host;
		this.puerto = puerto;
		this.directorio = directorio;
	}

	public Usuario(String puerto) {
		this.host = "localhost";
		this.puerto = puerto;
		if(!new File("Compartidos").isDirectory()) {
			new File("Compartidos").mkdir();
		}
		this.directorio = new File("Compartidos");
	}
	public Usuario(String puerto, File directorio) {
		this.host = "localhost";
		this.puerto = puerto;
		this.directorio = directorio;
	}

	public boolean equals(Object otroUsuario) {
		if (this == otroUsuario) {
			return true;
		}
		if (!(otroUsuario instanceof Usuario)) {
			return false;
		}
		Usuario usuario = (Usuario) otroUsuario;
		return this.host.equals(usuario.getHost()) && this.puerto.equals(usuario.getPuerto());
	}
	public void mostrar() {
		System.out.println("host: " + this.host + " -- puerto: " + this.puerto);
	}

	public String getHost() {
		return host;
	}

	public String getPuerto() {
		return puerto;
	}
	public File getDirectorio() {
		return this.directorio;
	}

}
