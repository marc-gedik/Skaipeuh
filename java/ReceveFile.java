import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceveFile implements Runnable {

	private int port;
	private String filename;
	private int fileSize;

	public ReceveFile(int port, String filename, int fileSize) {
		this.port = port;
		this.filename = filename;
		this.fileSize = fileSize;
	}

	@Override
	public void run() {
		BufferedReader bf;
		FileWriter fileWriter;
		File f;
		String dir;
		String[] tab, mot, racine;
		char[] buff = new char[500];
		int i = 0;
		int j = 1;
		try {
			ServerSocket serverSocket;
			serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();

			tab = filename.split("/");
			filename = tab[tab.length - 1];
			dir = System.getProperty("user.home");
			f = new File(dir + "/Telechargements");
			if (f.exists() && f.isDirectory())

				filename = dir + "/Telechargements/" + filename;
			else
				filename = dir + "/" + filename;

			// f = new File(filename);
			while (new File(filename).exists()) {
				mot = filename.split("\\.");
				racine = mot[0].split("\\(\\d{0,}\\)");

				filename = racine[0] + "(" + j + ").";
				for (int k = 1; k < mot.length; k++)
					filename += mot[k] + ".";

				j++;
				filename = filename.substring(0, filename.length() - 1);
			}
			f = new File(filename);
			filename = f.getAbsolutePath();

			fileWriter = new FileWriter(f);
			bf = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			while (fileSize > 0) {
				i = bf.read(buff, 0, 500);
				if (i == -1)
					break;
				fileWriter.write(buff, 0, i);
				fileSize -= i;
			}
			bf.close();
			fileWriter.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fichier enregistre a l'emplacement : " + filename);
	}
}
