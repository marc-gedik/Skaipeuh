import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFIL implements Runnable {

	int PORT;
	Socket s;
	File fichier;
	
	public ClientFIL(int p, Socket s ,File fichier) {
		this.PORT = p;
		this.s=s;
		this.fichier=fichier;
	}

	public void run() {

		try {
			String send;
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			pw.print("salut");
			pw.flush();
			System.out.println("fichier envoyé");
		} catch (Exception e) {
			System.out.println("Erreur ClientFIL\n" + e);
		}
	}
}
