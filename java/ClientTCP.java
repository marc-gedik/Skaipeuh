import java.net.*;

public class ClientTCP implements Runnable {

	int PORT;

	public ClientTCP(int p) {
		this.PORT = p;
	}

	public void run() {

		try {
			Socket s = new Socket("localhost", PORT);

			ThreadWRI wri = new ThreadWRI(s);
			MyScanner.setClient(wri);
			ThreadREAD read = new ThreadREAD(s);
			MyScanner.setFileAccepter(read);
			Thread t2 = new Thread(read);

			t2.start();
		} catch (Exception e) {
			System.out.println("Erreur ClientTCP\n" + e);
		}
	}
}
