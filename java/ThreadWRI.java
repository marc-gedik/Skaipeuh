import java.net.*;
import java.io.*;

public class ThreadWRI {

	private Socket s;
	private PrintWriter pw;
	private BufferedReader bf;
	private File file;
	private String filename;

	public ThreadWRI(Socket s) {
		this.s = s;
		try {
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String msg) {
		String send, taille;
		if (msg.equals("CLO")) {
			pw.print("CLO");
			pw.flush();
			close();
		}
		if (msg.startsWith("FIL") && msg.length() > 4) {
			filename = msg.substring(4);
			file = new File(filename);
			if (file.isFile()) {
				System.out.println("Sur quel port envoyer ?");
				MyScanner.toFile();
			} else
				System.out.println("Le fichier " + msg.substring(4)
						+ " n'existe pas");
		} else {
			taille = String.valueOf(msg.length());
			send = "MSG " + Bourrage.bourrage(taille, 3, "0") + " " + msg;
			pw.print(send);
			pw.flush();
		}
	}

	public void readFile(String msg) {
		int port;
		char[] buff = new char[3];

		try {
			port = Integer.parseInt(msg);
			//TODO bourrer le nom du fichier et le numero de port
			pw.print("FIL " + file.length() + " " + filename + " " + port);
			pw.flush();
			//TODO petit bug le print ne se fait pas
			bf.read(buff, 0, 3);
			if (buff[0] == 'A' && buff[1] == 'C' && buff[2] == 'K') {
				System.out.println("TODO envoyer le fichier");
				// envoyer le fichier
			} else
				System.out.println("l'echange a ete refuse");
		} catch (NumberFormatException e) {
			System.out.println("Il faut un nombre");
		} catch (IOException e) {
			e.printStackTrace();
		}
		MyScanner.toClient();
	}

	private void close() {
		ServeurTCP.running = false;
		MyScanner.toMain();
		try {
			pw.close();
			bf.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
