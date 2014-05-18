public class TabUser {

	String[] tabUser = new String[100];
	int nbEle;

	public TabUser() {
		this.tabUser = new String[100];
		this.nbEle = 0;
	}

	public String returnInfoUser(int indice) {
		if (indice <= nbEle)
			return tabUser[indice];
		return null;
	}

	public void afficheUserConnect(boolean erase) {
		int i;
		 if(erase)
			 System.out.print("\033c");

		if (nbEle == 0) {
			System.out.println("Personne n'est connectee");
		} else {
			System.out.println("       - Liste des personnes - \n");
			System.out.println("   |   nom   |    adresse    | port ");
			System.out.println("---+---------+---------------+------");

			for (i = 0; i < nbEle; i++) {
				System.out.println(Bourrage.leftBourrage(i + "", 2, " ")
						+ "   " + tabUser[i].substring(4));
			}
			System.out.println("---+---------+---------------+------\n");
		}

		System.out.println("- BYE pour se deconnecter");
		System.out.println("- RFH pour rafraichir\n");

	}

	public void afficheEvent(String msg) {
		System.out.print("\033c");
		System.out.println("##################################");
		System.out.println(msg);
		System.out.println("##################################\n");
		this.afficheUserConnect(false);
	}

	public void clearTabUser() {
		int i;
		for (i = 0; i < nbEle; i++)
			tabUser[i] = "";
		nbEle = 0;

	}

	public void addUser(String user) {
		if (tabUser.length == nbEle) {
			int i;
			String[] tmp = this.tabUser;
			tabUser = new String[2 * nbEle];
			for (i = 0; i < tmp.length; i++) {
				tabUser[i] = tmp[i];
			}
			tabUser[i + 1] = user;
			nbEle++;
		} else {
			tabUser[nbEle] = user;
			nbEle++;
		}
	}

	public void removeUser(String user) {
		int i, j;
		if (nbEle > 0) {
			for (i = 0; i < nbEle; i++) {
				if (user.equals(tabUser[i].substring(4, 12))) {
					nbEle--;
					break;
				}
			}
			for (j = i; j < nbEle; j++) {
				tabUser[j] = tabUser[j + 1];
			}
		}

		else {
			System.out.println("tabUser vide");
		}
	}
}
