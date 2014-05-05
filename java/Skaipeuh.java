import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Skaipeuh{


    public static void main(String[] args){
	try {
	    String user = "";
	    String port = "";
	    String IPgroup = "224.5.6.7";
	    int PORTgroup = 9876;
	    
	    String machine, cmd;
	    String IAM, HLO, BYE, BAN;
	    InetAddress localeAdresse;	
	    Scanner sc;   

	    Recepteur recepteur;
	    Emetteur emetteur;
	   
	    ServeurTCP serveur;
	    Thread t1, t2;

	   
	    if(args.length==2){
		user=args[0];
		port=args[1];
				
	    }
	    else{
		System.out.println("Probleme avec les arguments ");
		System.exit(0);
	    }	
	
	    localeAdresse = InetAddress.getLocalHost();
	    machine = localeAdresse.getHostAddress();
	    user = Bourrage.bourrageUser(user);
	    machine = Bourrage.bourrageMachine(machine);
	    port = Bourrage.bourrage(port, 5, "0");

	    IAM = "IAM "+user+" "+machine+" "+port;
	    HLO = "HLO "+user+" "+machine+" "+port;
	    BYE = "BYE "+user;
	    BAN = "BAN "+user;
	   
	    recepteur = new Recepteur(IAM, BYE, BAN, IPgroup, PORTgroup);
	    t1 = new Thread(recepteur);
	    t1.start();

	    emetteur = new Emetteur(IPgroup, PORTgroup);
	    emetteur.lance(HLO);

	    serveur = new ServeurTCP(Integer.parseInt(port));
	    t2 = new Thread(serveur);
	    t2.start();       

	    sc = new Scanner(System.in);
	    recepteur.tab.afficheUserConnect(true);
	    while(true){
		cmd = sc.nextLine();

		if(cmd.equals("BYE")){
		    emetteur.lance(BYE);
		    System.exit(0);
		}
		

		if(cmd.equals("RFH")){
		    emetteur.lance("RFH");
		}
		    	    
		if(cmd.startsWith("BAN")){
		    BAN = cmd.substring(4);
		    BAN = "BAN "+ Bourrage.bourrageUser(BAN);
		    emetteur.lance(BAN);
		}
		   
		if(cmd.equals("AFF")){
		    recepteur.tab.afficheUserConnect(true);
		}

		if(cmd.startsWith("TCP")){
		    String portUser= (recepteur.tab.returnInfoUser(Integer.parseInt(cmd.substring(4)))).substring(29);
		    lanceClient(Integer.parseInt(portUser));
		}
		
		if(ServeurTCP.accept==true){
		    lanceClient(Integer.parseInt(port));
		}


	    }
	}
	catch (Exception e) {
	    System.out.println("Erreur Skaipeuh\n"+e);
	}
    }

    static void lanceClient(int port){
	try{
	    Socket s = new Socket("localhost" , port);

	    ThreadWRI wri = new ThreadWRI(port,s);		
	    Thread t1 = new Thread(wri);
	    ThreadREAD read = new ThreadREAD(port, s);		
	    Thread t2 = new Thread(read);
	    t1.start();
	    t2.run();
	    t1.join();
	    s.close();
	    ServeurTCP.accept=false;
	    System.out.println("false");
	}
	catch(Exception e){
	    System.out.println("Erreur ClientTCP\n"+e);
	}
    }

}


