import java.net.*;
import java.io.*;

class ServeurTCP implements Runnable {

	int port;
	static boolean running = true;

	public ServeurTCP(int p) {
		this.port = p;
	}

	synchronized public void run() {
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				ServeurTCP.running = true;
				
				Socket sRcv, sSent;
				sRcv = ss.accept();
				System.out.println("Une personne souhaite vous parler");

				Skaipeuh.lanceClient("localhost", port);
				
				sSent = ss.accept();
				System.out.println("Connection etablie");

				ServeurThread read = new ServeurThread(sSent, sRcv);
				Thread t1 = new Thread(read);
				ServeurThread wri = new ServeurThread(sRcv, sSent);
				Thread t2 = new Thread(wri);

				t1.start();
				t2.run();
			}
		} catch (IOException e) {
			System.out.println("Erreur ServeurTCP\n" + e);
			System.exit(-1);
		}
	}
}
