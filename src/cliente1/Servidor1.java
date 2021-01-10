package cliente1;
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

public class Servidor1 {
	public static void main(String [] args) {
		ExecutorService pool=null;
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduzca el directorio de su servidor");
		String directorio = sc.nextLine();
		System.out.println("Introduzca el puerto donde funcionará su servidor");
		String puerto = sc.nextLine();
		sc.close();
		try (ServerSocket server = new ServerSocket(Integer.valueOf(puerto));) {
			pool = Executors.newCachedThreadPool();
			while (true) {
				try {
					Socket s = server.accept();
					AtenderPeticion1 ap = new AtenderPeticion1(s, new File(directorio));
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
