import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendFile implements Runnable {

	String filename;
	Socket socket;

	public SendFile(String filename, int port, String adresse)
			throws UnknownHostException, IOException {
		this.filename = filename;
		socket = new Socket(adresse, port);
	}

	@Override
	public void run() {
		File file = new File(filename);
		int lu;
		char[] buff = new char[500];
		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			FileReader reader = new FileReader(file);

			while ((lu = reader.read(buff, 0, 500)) > 0) {
				pw.write(buff, 0, lu);
				pw.flush();
			}
			System.out.println("Envoye reussi");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
