package cliente1;

import servidorGeneral.*;
import cliente2.*;
import hash.*;
import utiles.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AtenderPeticion1 implements Runnable {
	private Socket s;
	private File directorio;

	public AtenderPeticion1(Socket s, File d) {
		this.s = s;
		this.directorio = d;
	}

	@Override
	public void run() {
		String fichero = null, id, opcion;
		BufferedReader br;
		try (DataOutputStream out = new DataOutputStream(this.s.getOutputStream());) {
			br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
			id = br.readLine();
			for (int i = 0; i < this.directorio.listFiles().length; i++) {
				if (hash.HashSHA256.getHash(this.directorio.listFiles()[i]).equals(id)) {
					fichero = this.directorio.listFiles()[i].getName();
					break;
				}
			}
			if (fichero == null) {
				out.writeBytes("KO"+"\r\n");
			} else {
				out.writeBytes("OK" + "\r\n");
				FileInputStream in = new FileInputStream(this.directorio.getName() + "\\" + fichero);
				// enviar un fichero a los clientes
				int byteLeido = in.read();
				while (byteLeido != -1) {
					out.write(byteLeido);
					byteLeido = in.read();
				}
				in.close();
				br.close();
			}
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
