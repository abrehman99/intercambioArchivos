package servidorGeneral;

import cliente1.*;
import cliente2.*;
import hash.*;
import utiles.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AtenderPeticion implements Runnable {
	private Socket s;
	private String opcion;
	private List<InfoFichero> tabla;

	public AtenderPeticion(Socket s, String opcion, List<InfoFichero> tabla) {
		this.s = s;
		this.opcion = opcion;
		this.tabla = tabla;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId());
		String opcion = this.opcion, nombre, tam, puerto, nombreDirectorio, id;
		boolean encontrado = false;
		Long tamL;
		int numFicheros;
		BufferedReader br;
		BufferedWriter bw;
		try (InputStream in = new DataInputStream(this.s.getInputStream());
				DataOutputStream out = new DataOutputStream(this.s.getOutputStream());
				ObjectOutputStream outObject = new ObjectOutputStream(this.s.getOutputStream());) {
			br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream()));
			opcion = br.readLine();
			System.out.println(opcion);
			if (opcion.equals("connect") || opcion.equals("getTabla")) {

				// conectar usuario primera vez
				
				// añadir a tabla los ficheros que tiene este nuevo cliente
				if (opcion.equals("connect")) {
					puerto = br.readLine();
					nombreDirectorio = br.readLine();
					numFicheros = Integer.valueOf(br.readLine());
					for (int i = 0; i < numFicheros; i++) {
						nombre = br.readLine();
						Usuario usu = new Usuario(puerto, new File(nombreDirectorio));
						id = hash.HashSHA256.getHash(new File(usu.getDirectorio().getName() + "\\" + nombre));
						int j = 0;
						while (j < tabla.size() && !encontrado) {
							InfoFichero info = tabla.get(j);
							if (info.getId().equals(id)) {
								encontrado = true;
								if (!info.getListaUsuarios().contains(usu)) {
									info.addUsuario(usu);
								}
							}
							j++;
						}
						if (!encontrado) {
							List<Usuario> listaUsuarios = new ArrayList<Usuario>();
							listaUsuarios.add(usu);
							InfoFichero fila = new InfoFichero(new File(usu.getDirectorio().getName() + "\\" + nombre),
									listaUsuarios);
							tabla.add(new InfoFichero(new File(usu.getDirectorio().getName() + "\\" + nombre),
									listaUsuarios));
						}
						encontrado = false;
					}
				}

				// devolver la tabla actualizada completa al usuario
				outObject.writeObject(tabla);
//				for(InfoFichero info: tabla) {
//					out.writeBytes(info.getId()+"\r\n");
//					out.writeBytes(info.getNombre() + "\r\n");
//					out.writeBytes(info.getListaUsuarios().size()+"\r\n");
//					for(Usuario usuario: info.getListaUsuarios()) {
//						out.writeBytes(usuario+"\r\n");
//					}
//				}

			} else if (opcion.equals("disconnect")) {
				puerto = br.readLine();
				System.out.println("puerto: " + puerto);
				Usuario usu = new Usuario(puerto);
				List<InfoFichero> borrar = new ArrayList<>();
				for (Iterator<InfoFichero> iterator = this.tabla.iterator(); iterator.hasNext();) {
					InfoFichero info = iterator.next();
					if (info.getListaUsuarios().size() == 1 && info.getListaUsuarios().get(0).equals(usu)) {
						borrar.add(info);
					}else {
						info.desconectarUsuario(usu);
					}
				}
				this.tabla.removeAll(borrar);
				System.out.println("mostrando despues de desconectar");
				for (InfoFichero info : this.tabla) {
					info.mostrar();
				}
			} else if (opcion.equals("update")) {
				// actualizar info
				puerto = br.readLine();
				id = br.readLine();
				if (puerto != null && !puerto.isEmpty()) {
					for (InfoFichero info : this.tabla) {
						if (info.getId().equals(id)) {
							Usuario usu = new Usuario(puerto);
							if (!info.getListaUsuarios().contains(usu)) {
								info.addUsuario(usu);
							}
							break;
						}
					}
				} else {
					System.out.println("en servidor no llega puerto bien");
				}
				// outObject.writeObject(this.tabla);
			} else {
				System.out.println("en opcion else");
				for (InfoFichero info : tabla) {
					info.mostrar();
				}
			}
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
