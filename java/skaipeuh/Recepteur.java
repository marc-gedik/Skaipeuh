package skaipeuh;

import java.net.*;

public class Recepteur implements Runnable {
	TabUser tab = new TabUser();
	private String IAM, BYE, BAN;
	private String IPgroup;
	private int PORTgroup;

	public Recepteur(String IAM, String BYE, String BAN, String IPgroup,
			int PORTgroup) {
		this.IAM = IAM;
		this.BYE = BYE;
		this.BAN = BAN;
		this.IPgroup = IPgroup;
		this.PORTgroup = PORTgroup;
	}

	public void run() {
		try {
			byte[] buff = new byte[256];
			InetAddress ia = InetAddress.getByName(IPgroup);
			MulticastSocket ms = new MulticastSocket(PORTgroup);
			ms.joinGroup(ia);
			DatagramPacket dp = new DatagramPacket(buff, buff.length);

			String msg;
			boolean flag = true;
			InetAddress bannisseur1 = null;
			InetAddress bannisseur2 = null;
			Emetteur emetteur = new Emetteur(IPgroup, PORTgroup);

			tab.afficheUserConnect(true);

			while (true) {
				ms.receive(dp);
				msg = new String(dp.getData(), 0, dp.getLength());

				if (!(msg.substring(3).equals(IAM.substring(3)))) {

					if (msg.startsWith("HLO")) {
						flag = false;
						// System.out.println("HLO recu j'envoie: " + IAM);
						tab.addUser(msg);
						emetteur.lance(IAM);
						tab.afficheEvent(msg.substring(4, 12)
								+ " s'est connecte(e)");
					}

					if (msg.startsWith("IAM") && flag) {
						tab.addUser(msg);
						tab.afficheUserConnect(true);

					}

					if (msg.startsWith("RFH")) {
						tab.clearTabUser();
						flag = true;
						emetteur.lance(IAM);
					}

					if (msg.startsWith("BYE") && !(msg.equals(BYE))) {
						System.out.println("Message recu:  " + msg);
						tab.removeUser(msg.substring(4, 12));
						tab.afficheEvent(msg.substring(4, 12)
								+ " s'est deconnecte(e)");

					}

					if (msg.equals(BAN)) {
						System.out.println("Message recu:  " + msg);
						if (bannisseur1 == null) {
							bannisseur1 = dp.getAddress();
						} else {
							bannisseur2 = dp.getAddress();
							if (!(bannisseur2.equals(bannisseur1))) {
								emetteur.lance(BYE);
								System.exit(0);
							}
						}
					}
				}
			}
		}

		catch (Exception e) {
			System.out.println("Erreur Recepteur\n" + e);
		}

	}
}
