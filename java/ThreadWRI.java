import java.net.*;
import java.io.*;

public class ThreadWRI {

	private Socket s;
	private PrintWriter pw;
	private BufferedReader bf;
	private File file;
	private String filename;
	private String adresse;
	boolean fichierAccepter;

	public ThreadWRI(Socket s, String adresse) {
		this.s = s;
		this.adresse = adresse;
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
			ServeurTCP.running = false;
			close();
		}

		if (msg.startsWith("FIL") && msg.length() > 4) {
			filename = msg.substring(4);
			file = new File(filename);

			if (file.isFile()) {
				System.out.println("Sur quel port envoyer ?");
				MyScanner.toFile();
			} else {
				System.out.println("Le fichier " + msg.substring(4)
						+ " n'existe pas");
			}

		} else {
			taille = String.valueOf(msg.length());
			send = "MSG " + Bourrage.leftBourrage(taille, 3, "0") + " " + msg;
			pw.print(send);
			pw.flush();
		}
	}

	public void readFile(String port) {
		char[] buff = new char[3];
		String filenameBourrer;
		try {
			filenameBourrer = Bourrage.rightBourrage(filename, 40, " ");
			port = Bourrage.leftBourrage(port, 5, "0");

			pw.print("FIL " + file.length() + " " + filenameBourrer + " "
					+ port);
			pw.flush();

			synchronized (this) {
				wait();
			}

			if (fichierAccepter) {
				SendFile sendFile = new SendFile(filename,
						Integer.parseInt(port), adresse);
				new Thread(sendFile).start();
			} else
				System.out.println("l'echange a ete refuse");
		} catch (NumberFormatException e) {
			System.out.println("Il faut un nombre");
		} catch (IOException e) {
			System.out.println("Io exception");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			fichierAccepter = false;
		}
		MyScanner.toClient();
	}

	private void close() {
		System.out.println("Conversation terminee");
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
