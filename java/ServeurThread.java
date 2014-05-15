import java.net.*;
import java.io.*;

public class ServeurThread implements Runnable {

	Socket s1;
	Socket s2;

	public ServeurThread(Socket s1, Socket s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	public void run() {
		try {
			char[] buff = new char[508];
			BufferedReader br;
			PrintWriter pw;
			int len;
			br = new BufferedReader(new InputStreamReader(s1.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(s2.getOutputStream()));
			boolean running = true;
			while (running) {
				len = br.read(buff, 0, buff.length);
				if (len > 2 && buff[0] == 'C' && buff[1] == 'L'
						&& buff[2] == 'O') {
					pw.write(buff, 0, len);
					pw.flush();
					running = false;
				} else {
					pw.write(buff, 0, len);
					pw.flush();
				}
			}
			System.out.println("fin serveurhtread");
		} catch (java.io.IOException e) {
			System.out.println("Erreur ServeurThread\n" + e);
		}
		catch (Exception e) {
			System.out.println("Erreur");
		}
	}
}
