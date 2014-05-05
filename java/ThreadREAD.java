import java.net.*;
import java.io.*;

public class ThreadREAD implements Runnable {

	int PORT;
	Socket s;

	public ThreadREAD(Socket s) {
		this.s = s;
	}

	public void run() {
		try {

			char[] buff = new char[508];
			BufferedReader br;
			int len;
			String taille;
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (ServeurTCP.running) {
				try {
					br.read(buff, 0, 3);
				} catch (SocketException e) {
				}
				if (buff[0] == 'M' && buff[1] == 'S' && buff[2] == 'G') {
					br.read();
					br.read(buff, 0, 4);
					taille = Character.toString(buff[0])
							+ Character.toString(buff[1])
							+ Character.toString(buff[2]) + "";
					len = Integer.parseInt(taille);
					br.read(buff, 0, len);
					System.out.print("↳ ");
					for (int i = 0; i < len; i++)
						System.out.print(buff[i]);

					if (len > 2 && buff[0] == 'C' && buff[1] == 'L'
							&& buff[2] == 'O') {
						ServeurTCP.running = false;
						s.close();
					}

				}
				System.out.println("");
			}
		} catch (IOException e) {
			System.out.println("Erreur ThreadREAD\n" + e);
		}
	}
}
