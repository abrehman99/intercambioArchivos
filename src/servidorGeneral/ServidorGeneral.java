package servidorGeneral;
import cliente1.*;
import cliente2.*;
import hash.*;
import utiles.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorGeneral {
	public static void main(String[] args) {
		List<InfoFichero> tabla = new ArrayList<InfoFichero>();
		ExecutorService pool=null;
		try (ServerSocket server = new ServerSocket(6666);) {
			pool = Executors.newCachedThreadPool();
			while (true) {
				try {
					Socket s = server.accept();
					AtenderPeticion ap = new AtenderPeticion(s, "", tabla);
					pool.execute(ap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(!pool.isShutdown()) {
				pool.shutdown();
			}
		}
	}

}
