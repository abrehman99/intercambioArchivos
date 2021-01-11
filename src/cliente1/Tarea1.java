package cliente1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.TimerTask;

import utiles.InfoFichero;

public class Tarea1 extends TimerTask {
	private File directorio;

	public Tarea1(File directorio) {
		this.directorio = directorio;
	}

	public void run() {
		try (Socket client = new Socket("localhost", 6666);
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				ObjectInputStream inObject = new ObjectInputStream(client.getInputStream());
				ObjectOutputStream outObject = new ObjectOutputStream(
						new FileOutputStream(directorio.getName() + "\\.config.txt"));) {
			out.writeBytes("getTabla" + "\r\n");
			List<InfoFichero> lista = (List<InfoFichero>) inObject.readObject();
			outObject.writeObject(lista);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
