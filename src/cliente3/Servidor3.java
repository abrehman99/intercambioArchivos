package cliente3;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utiles.InfoFichero;

public class Servidor3 {
	public static void main(String [] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduzca el directorio de su servidor");
		String directorio = sc.nextLine();
		System.out.println("Introduzca el puerto donde funcionará su servidor");
		String puerto = sc.nextLine();
		sc.close();
		ExecutorService pool=null;
		try (ServerSocket server = new ServerSocket(Integer.valueOf(puerto));) {
			pool = Executors.newCachedThreadPool();
			while (true) {
				try {
					Socket s = server.accept();
					AtenderPeticion3 ap = new AtenderPeticion3(s, new File(directorio));
					pool.execute(ap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(pool!=null) {
				System.out.println("cierra pool");
				pool.shutdown();
			}
		}
	}

}
