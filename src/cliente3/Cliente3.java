package cliente3;

import servidorGeneral.*;
import cliente2.*;
import hash.*;
import utiles.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Cliente3 {
	public static void main(String[] args) {
		String opcion, s, idDescarga, nombreLocal;
		File directorio = null;

		boolean conectado = false;
		Scanner sc = new Scanner(System.in);
		System.out.println("Pulse 1 para conectar con el servidor general");
		System.out.println("Pulse 2 para desconectar");
		System.out.println("Pulse 3 para descargar un archivo");
		System.out.println("Pulse 0 para salir");
		opcion = sc.nextLine();
		System.out.println(opcion);
//		while (!opcion.equals("0")) {
		if (opcion.equals("1")) {
			String puerto;
			conectado = true;
			System.out.println("Introduzca nombre del directorio");
			s = sc.nextLine();
			if (s.isBlank()) {
				s = "Compartidos2";
			}
			directorio = new File(s);
			System.out.println("Introduzca el puerto donde funciona su servidor");
			puerto = sc.nextLine();
			try (Socket client = new Socket("localhost", 6666);
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					ObjectInputStream inObject = new ObjectInputStream(client.getInputStream());
					ObjectOutputStream outObject = new ObjectOutputStream(
							new FileOutputStream(directorio.getName() + "\\.config.txt"));) {
				out.writeBytes("connect" + "\r\n");
				out.writeBytes(puerto + "\r\n");
				out.writeBytes(directorio.getName() + "\r\n");
				out.writeBytes(directorio.listFiles().length + "\r\n");
				// enviar todos los ficheros
				int n = 0;
				byte[] buf = new byte[4092];
				for (int i = 0; i < directorio.listFiles().length; i++) {
					File fichero = directorio.listFiles()[i];
					String nombre = fichero.getName();
					out.writeBytes(nombre + "\r\n");
				}
				// recibe tabla
				List<InfoFichero> lista = (List<InfoFichero>) inObject.readObject();
				for (InfoFichero info : lista) {
					info.mostrar();
				}
				outObject.writeObject(lista);
			} catch (IOException e) {
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (opcion.equals("2")) {
			String puerto;
			System.out.println("Introduzca su puerto: ");
			puerto = sc.nextLine();
			try (Socket client = new Socket("localhost", 6666);
					DataOutputStream out = new DataOutputStream(client.getOutputStream());) {
				out.writeBytes("disconnect" + "\r\n");
				out.writeBytes(puerto + "\r\n");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (opcion.equals("3")) {
			//1. PEDIR DIRECTORIO, PUERTO Y NOMBRE FICHERO
			//2. ESTABLECER CONEXION
			//3. MOSTRAR TABLA Y PEDIR ID ARCHIVO A DESCARGAR
			//4. ALGORITMO PARA DESCARGAR EL ARCHIVO
			
			//1. PEDIR DIRECTORIO, PUERTO Y NOMBRE FICHERO
			
			System.out.println("Introduzca el nombre de su directorio");
			directorio = new File(sc.nextLine());
			String puerto;
			System.out.println("Introduzca su puerto: ");
			puerto = sc.nextLine();
			System.out.println("Introduzca el nombre que tendrá el fichero en local");
			nombreLocal = sc.nextLine();
			String puertoDestino = "";
			//2. ESTABLECER CONEXION
			try (Socket client = new Socket("localhost", 6666);
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					FileOutputStream outa = new FileOutputStream(directorio + "\\" + nombreLocal);
					FileInputStream streamIn = new FileInputStream(directorio + "\\.config.txt");
					ObjectInputStream objectIn = new ObjectInputStream(streamIn);) {
				//3. MOSTRAR TABLA Y PEDIR ID ARCHIVO A DESCARGAR
				List<InfoFichero> lista = (List<InfoFichero>) objectIn.readObject();
				System.out.println("Mostrando ids disponibles: ");
				for (InfoFichero info : lista) {
					info.mostrar();
				}
				System.out.println("Introduzca el id del fichero a descargar");
				idDescarga = sc.nextLine();
				InfoFichero infoDescarga = null;
				for (InfoFichero info : lista) {
					if (info.getId().equals(idDescarga)) {
						infoDescarga = info;
					}
				}
				int i = 0;

				boolean descargado = false;
				//4. ALGORITMO PARA DESCARGAR EL ARCHIVO
				// Se recorre la lista de usuarios del archivo elegido y se usan los puertos en orden de aparición en
				// 	la lista, si no se puede establecer conexion o si no se confirma la descarga se vuelve a intentar con el
				//	  siguiente puerto disponible
				while (i < infoDescarga.getListaUsuarios().size() && puertoDestino.equals("") && !descargado) {
					puertoDestino = infoDescarga.getListaUsuarios().get(i).getPuerto();
					try {
						Socket clientt2 = new Socket("localhost", Integer.valueOf(puertoDestino));
						DataInputStream in = new DataInputStream(clientt2.getInputStream());
						DataOutputStream outB = new DataOutputStream(clientt2.getOutputStream());
						BufferedReader br = new BufferedReader(new InputStreamReader(clientt2.getInputStream()));
						outB.writeBytes(idDescarga + "\r\n");
						String respuesta = br.readLine();
						System.out.println("respuesta: " + respuesta);
						if (respuesta.equals("OK")) {
							int byteLeido = in.read();
							while (byteLeido != -1) {
								outa.write(byteLeido);
								byteLeido = in.read();
							}
							out.writeBytes("update" + "\r\n");
							out.writeBytes(puerto + "\r\n");
							out.writeBytes(idDescarga + "\r\n");
							descargado = true;
						} else {
							System.out.println("error fichero");
							puertoDestino = "";
						}
					} catch (IOException e) {
						System.out.println("error");
						puertoDestino = "";
					}
					i++;
				}
				if (puertoDestino.equals("") || puertoDestino.equals(puerto)) {
					System.out.println("error al conectar al servidor auxiliar");
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Ejecucion terminada");
	}
}
