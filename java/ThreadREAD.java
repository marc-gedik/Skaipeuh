import java.net.*;
import java.io.*;

public class ThreadREAD implements Runnable {

	Socket s;
	private boolean fileAccepted;
	private ThreadWRI wri;

	public ThreadREAD(Socket s, String adresse, ThreadWRI wri) {
		this.s = s;
		this.wri = wri;
	}

	public void run() {
		try {

			char[] buff = new char[512];
			char c;
			BufferedReader br;
			PrintWriter pw;
			int port, len, fileSize;
			String taille, filename;
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

			while (ServeurTCP.running) {
				try {
					br.read(buff, 0, 3);
				} catch (SocketException e) {
					System.out.println("Erreur read prefixe du message\n"+e);
				}

				if (buff[0] == 'M' && buff[1] == 'S' && buff[2] == 'G') {
					br.read();
					br.read(buff, 0, 4);
					taille = Character.toString(buff[0])
							+ Character.toString(buff[1])
							+ Character.toString(buff[2]) + "";
					len = Integer.parseInt(taille);
					br.read(buff, 0, len);
					System.out.print(">> ");

					for (int i = 0; i < len; i++)
						System.out.print(buff[i]);
					System.out.println("");
				} 
			
				else if (buff[0] == 'C' && buff[1] == 'L' && buff[2] == 'O') {
					System.out.println("Conversation terminee");
					ServeurTCP.running = false;
					s.close();
					MyScanner.toMain();
				} 
				
				
				else if (buff[0] == 'F' && buff[1] == 'I' && buff[2] == 'L') {
					br.read();
					taille = "";
					while ((c = (char) br.read()) != ' ' && c != -1)
						taille += c;
					fileSize = Integer.parseInt(taille);
					br.read(buff, 0, 40);
					filename = "";
					for (int i = 0; i < 40; i++)
						filename += buff[i];
					
					br.read();
					br.read(buff, 0, 5);
					port = Integer.parseInt(buff[0] + "" + buff[1] + ""
							+ buff[2] + "" + buff[3] + "" + buff[4]);
					
					filename = filename.trim();
					
					System.out.println("Accepter l'echange " + filename + " (" + fileSize + ")?(y/n)");
					MyScanner.toFileAccept();
					
					synchronized (this) {
						wait();
					}
					
					if (fileAccepted) {
						ReceveFile receveFile = new ReceveFile(port, filename, fileSize);
						new Thread(receveFile).start();
						
						System.out.println("L'echange va etre effectuer");
						pw.print("ACK");
					}
					
					else {
						System.out.println("Vous avez refuse le fichier");
						pw.print("NAK");
					}
					pw.flush();
				} else if (buff[0] == 'A' && buff[1] == 'C' && buff[2] == 'K') {
					fichierAccepter(true);
				} else if (buff[0] == 'N' && buff[1] == 'A' && buff[2] == 'K') {
					fichierAccepter(false);
				}
			}
		} catch (IOException e) {
			System.out.println("Erreur ThreadREAD\n" + e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void fichierAccepter(boolean b) {
		System.out.println("Fichier " + (b ? "acceptee" : "refusee"));
		ThreadWRI.fichierAccepter = b;
		synchronized (wri) {
			wri.notify();
		}
	}

	public void fileAccept(String s) {
		if (s.equals("y"))
			fileAccepted = true;
		else
			fileAccepted = false;
		synchronized (this) {
			notify();
		}
		MyScanner.toClient();
	}
}
