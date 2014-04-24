
import java.net.*;


public class Skaipeuh{


    public static void main(String[] args){
	try {
	    int PORT1=0;
	    int PORT2=0;
	    String user, machine, port, IAM;
	    Recepteur recepteur;
	    Emetteur emetteur;
	    InetAddress localeAdresse;
	    ClientTCP client;
	    ServeurTCP serveur;
	    Thread t1, t2, t3;
	    if(args.length==2){
		PORT1=Integer.parseInt(args[0]);	   
		PORT2=Integer.parseInt(args[1]);	  
	    }
	    else{
		System.out.println("manque les ports ");
		System.exit(0);
	    }


	
	
	    localeAdresse = InetAddress.getLocalHost();
	    machine = localeAdresse.getHostAddress();
	    user = Bourrage.bourrageUser("safir");
	    machine = Bourrage.bourrageMachine(machine);
	    port = Bourrage.bourragePort("345");

	    IAM = "IAM "+ user + " " + machine + " " +port;
	    recepteur = new Recepteur(IAM);
	    t1 = new Thread(recepteur);
	    t1.start();

	    emetteur = new Emetteur("HLO", user, machine, port );
	    emetteur.lance();

	    serveur = new ServeurTCP(PORT1);
	    client = new ClientTCP(PORT2); 

	    t2 = new Thread(serveur);
	    t3 = new Thread(client);
	    t2.start();       
	    t3.start();
 
	} 
	catch (Exception e) {
	    System.out.println("Erreur Skype\n"+e);
	}
    }
}

