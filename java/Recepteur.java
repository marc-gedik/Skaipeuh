import java.net.*; 

public class Recepteur implements Runnable{
    TabUser tab = new TabUser();
    String msgIAM;
    String user;

    public Recepteur(String msg){
	this.msgIAM = msg;
	this.user=msgIAM.substring(4, 12);
    }


    public static void reponseRecepteur(String rep){
	try{

	    DatagramSocket ds = new DatagramSocket(); 
	    InetAddress ia = InetAddress.getByName("224.5.6.7"); 
	    DatagramPacket dp = new DatagramPacket(rep.getBytes(), 
						   rep.getBytes().length,
						   ia,
						  9876); 
	    ds.send(dp); 
	} 
	catch(Exception e) { 
	    System.out.println("Erreur reponseRecepteur\n" +e );
	}
    }


    public void run() { 
	try { 
	    String msg;
	    String tmp="";
	    byte [] buff = new byte[256]; 
	    InetAddress ia = InetAddress.getByName("224.5.6.7"); 
	    MulticastSocket ms = new MulticastSocket(9876); 
	    ms.joinGroup(ia); 
	    DatagramPacket dp = new DatagramPacket(buff, buff.length);
	    boolean flag1= true;
	    InetAddress bannisseur1 =null;
	    InetAddress bannisseur2 =null;

	    while (true) { 
		ms.receive(dp); 
		msg = new String(dp.getData(), 0, dp.getLength());

		if(!(msg.substring(3).equals(msgIAM.substring(3)))){
						
		    if(msg.startsWith("HLO")){
			flag1=false;

			System.out.println("HLO reçu j'envoie: " + msgIAM); 
			tab.addUser(msg);
			reponseRecepteur(msgIAM);
		    }			



		
		    if(msg.startsWith("IAM") && flag1){	    
			System.out.println(msg);
			tab.addUser(msg);
		    }
		  


	
		    if(msg.startsWith("RFH")){	    
			tab.clearTabUser();
			System.out.println("coucou");
			flag1=true;
			reponseRecepteur(msgIAM);  
		    }




		    if(msg.startsWith("BYE")){
			System.out.println("Message recu:  "+ msg); 
			tab.removeUser(msg.substring(4, 12));
		    }
		   



		    if(msg.equals("BAN "+ user)){
			System.out.println("Message recu:  "+ msg); 
			if(bannisseur1==null){
			    bannisseur1=dp.getAddress();
			}
			else{
			    bannisseur2=dp.getAddress();
			    if((bannisseur2.equals(bannisseur1))){
				reponseRecepteur("BYE "+user);
				System.exit(0);
			    }
			}
		    }   
		}
	    }
	}
	
	catch(Exception e) { 
	    System.out.println("Erreur Recepteur\n"+ e);
	}

    } 
}
