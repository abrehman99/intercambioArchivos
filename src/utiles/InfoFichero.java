package utiles;
import cliente1.*;
import cliente2.*;
import hash.*;
import servidorGeneral.*;
import java.io.File;
import java.io.Serializable;
import java.util.List;

public class InfoFichero implements Serializable{
	private String id, nombre;
	private Long tam;
	private List<Usuario> listaUsuarios;
	private static final long serialVersionUID = 111L;
	public InfoFichero(File fichero, String nombre, Long tam, List<Usuario> listaUsuarios) {
		this.id = hash.HashSHA256.getHash(fichero);
		this.nombre = nombre;
		this.tam = tam;
		this.listaUsuarios = listaUsuarios;
	}
	public InfoFichero(File fichero, List<Usuario> listaUsuarios) {
		this.id = hash.HashSHA256.getHash(fichero);
		this.nombre = fichero.getName();
		this.tam = fichero.length();
		this.listaUsuarios = listaUsuarios;
		
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Long getTam() {
		return tam;
	}
	public void setTam(Long tam) {
		this.tam = tam;
	}
	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}
	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	public String getId() {
		return id;
	}
	public void desconectarUsuario(Usuario usuario) {
		System.out.println(this.id);
		this.listaUsuarios.remove(usuario);
	}
	public void addUsuario(Usuario usuario) {
		this.listaUsuarios.add(usuario);
	}
	public boolean equals(Object info) {
		System.out.println("en equals");
		boolean iguales=false;
		if (this == info) {
			return true;
		}
		if (!(info instanceof InfoFichero)) {
			return false;
		}
		InfoFichero infoFichero = (InfoFichero) info;
		iguales = this.id.equals(infoFichero.id);
		System.out.println("id this: " + this.id);
		System.out.println("id param: " + infoFichero.id);
		System.out.println("iguales?: " + iguales);
		return iguales;
	}
	public void mostrar() {
		System.out.println("id: " + this.id);
		System.out.println("nombre: " + this.nombre);
		System.out.println("tamaño: " + this.tam);
		System.out.println("listaUsuarios: ");
		for(Usuario usuario: this.listaUsuarios) {
			usuario.mostrar();
		}
	}
	
}
