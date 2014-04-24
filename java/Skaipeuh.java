import java.net.*;
import java.io.*;
import java.util.Scanner;

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
	   
	    if(args.length==1){
		PORT1=Integer.parseInt(args[0]);	   
	    }
	    else{
		System.out.println("manque les ports ");
		System.exit(0);
	    }	
	
	    localeAdresse = InetAddress.getLocalHost();
	    machine = localeAdresse.getHostAddress();
	    user = Bourrage.bourrageUser("safir");
	    machine = Bourrage.bourrageMachine(machine);
	    port = Bourrage.bourragePort(PORT1+"");


	    IAM = "IAM "+ user + " " + machine + " " +port;
	    recepteur = new Recepteur(IAM);
	    t1 = new Thread(recepteur);
	    t1.start();


	    emetteur = new Emetteur("HLO", user, machine, port );
	    emetteur.lance();

	    serveur = new ServeurTCP(PORT1);
	    t2 = new Thread(serveur);
	    t2.start();       

	    String cmd;
	    boolean flag= true;
	    BufferedInputStream text = new BufferedInputStream(System.in);
	    Scanner sc = new Scanner(System.in);
	    while(true){
		if(text.available()>0){
		    cmd = sc.nextLine();
		    if(cmd.startsWith("BYE")){
			emetteur = new Emetteur("BYE", user);
			emetteur.lance();
			System.exit(0);
		    }
		
		    if(ServeurTCP.accept && flag){
			client = new ClientTCP(Integer.parseInt(port)); 
			t3 = new Thread(client);
			t3.start();
			flag=false;
		    }


		    if(cmd.startsWith("WHO")){
			recepteur.afficheClientConnect();
		    }

		}
	    }
	}
	    catch (Exception e) {
		System.out.println("Erreur while Skype\n"+e);
	    }
	}
    }

