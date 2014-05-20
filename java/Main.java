import skaipeuh.Skaipeuh;

public class Main {
	public static void main(String[] args) {
		if (args.length == 2) {
			new Skaipeuh(args[0], args[1]);

		} else {
			System.out.println("Probleme avec les arguments ");
			System.exit(0);
		}
	}
}
