import java.util.Scanner;

public class MyScanner implements Runnable {

	public static final int SKAIPEUH = 0, CLIENT = 1, FILE = 2, FILE_ACCEPT = 3;
	private static int where;
	private static Skaipeuh skaipeuh;
	private static ThreadWRI client;
	private static ThreadREAD fileAccepter;
	
	public MyScanner(Skaipeuh skaipeuh) {
		MyScanner.skaipeuh = skaipeuh;
		run();
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		String s;

		while (true) {
			s = sc.nextLine();
			switch (where) {
			case SKAIPEUH:
				skaipeuh.lance(s);
				break;
			case CLIENT:
				client.write(s);
				break;
			case FILE:
				client.readFile(s);
				break;
			case FILE_ACCEPT:
				fileAccepter.fileAccept(s);
			}
		}
	}

	public static void toFile() {
		where = FILE;
	}

	public static void toClient() {
		where = CLIENT;
	}

	public static void toMain() {
		where = SKAIPEUH;
	}

	public static void setClient(ThreadWRI wri) {
		toClient();
		client = wri;
	}

	public static void setFileAccepter(ThreadREAD read){
		fileAccepter = read;
	}
	
	public static void toFileAccept() {
		where = FILE_ACCEPT;
	}
}
