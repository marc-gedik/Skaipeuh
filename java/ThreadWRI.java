import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ThreadWRI implements Runnable {

	Socket s;

	public ThreadWRI(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			String msg, send, taille;
			Scanner sc = new Scanner(System.in);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					s.getOutputStream()));
			while (ServeurTCP.running) {
				msg = sc.nextLine();
				taille = String.valueOf(msg.length());
				send = "MSG " + Bourrage.bourrage(taille, 3, "0") + " " + msg;
				pw.print(send);
				pw.flush();
				if (msg.equals("CLO")) {
					ServeurTCP.running = false;
					s.close();
				}
			}
		} catch (IOException e) {
			System.out.println("threadWRI \n" + e);
		}
		System.out.println("Thread WRI fini");
	}
}
