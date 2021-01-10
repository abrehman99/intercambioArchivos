package cliente1;

import java.io.File;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;

public class Actualizador1 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduzca directorio donde se descargará la tabla actualizada");
		String direc = sc.nextLine();
		File directorio = new File(direc);
		Calendar init = Calendar.getInstance();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new Tarea1(directorio), init.getTime(), 30000);
	}

}
