import java.net.*;
import java.io.*;

class ServeurFIC implements Runnable {

	int port;
	static boolean running = true;
	int fileSize;
	String filename;
	Socket s;

	public ServeurFIC(int p, int fileSize, String filename, Socket s) {
		this.port = p;
		this.fileSize = 100;
		this.filename = filename;
		this.s=s;
	}

	synchronized public void run() {
		try {
			char[] buff = new char[512];
			File fichier = new File("./downloads/"+filename);
			System.out.println("Début du telechargement ...");
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			FileWriter writer = new  FileWriter(fichier,false);

			while (fileSize>0){
				System.out.println("ok");
				//br.read(buff, 0, 5);
				
				writer.write("bonjour");

				for (int i = 0; i < 10; i++){
				//	System.out.print(buff[i]);
				}
				System.out.println("");
				fileSize--;
			}
			System.out.println("telechargement termine");




		} catch (IOException e) {
			System.out.println("Erreur ServeurFIC\n" + e);
			System.exit(-1);
		}
	}
}
