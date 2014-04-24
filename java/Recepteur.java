import java.net.*; 

public class Recepteur implements Runnable{
    
    String[] tabClients = new String[100];
    int indice = 0;
    String msgIAM;

    public Recepteur(String msg){
	this.msgIAM = msg;
    }


    public void afficheClientConnect(){
	int i;
	for(i=0; i<indice; i++)
	    System.out.println(tabClients[i]);
    }



    public static void reponseRecepteur(String rep){
	try{

	DatagramSocket ds = new DatagramSocket(); 
	InetAddress ia = InetAddress.getByName("224.5.6.7"); 
	DatagramPacket dp = new DatagramPacket(rep.getBytes(), 
					       rep.getBytes().length,
					       ia,
					       61234); 
	ds.send(dp); 
	} 
	catch(Exception e) { 
	    System.out.println("Erreur reponseRecepteur\n" +e );
	}
    }


    public void run() { 
	try { 

	    String msg;	  
	    byte [] buff = new byte[256]; 
	    InetAddress ia = InetAddress.getByName("224.5.6.7"); 
	    MulticastSocket ms = new MulticastSocket(61234); 
	    ms.joinGroup(ia); 
	    DatagramPacket dp = new DatagramPacket(buff, buff.length); 
	    while (true) { 
		ms.receive(dp); 
	        msg = new String(dp.getData() ,
				 0,
				 dp.getLength()); 
		//	System.out.println("Message recu:  "+ msg );

	
		if(msg.startsWith("HLO")){
		    System.out.println("Message recu:  "+ msg + 
				       "\nJ'envoie le message: " + msgIAM); 
		    tabClients[indice]=msg;
		    indice ++;
		    reponseRecepteur(msgIAM);
		}
	    

	    } 
	} 
	catch(Exception e) { 
	    System.out.println("Erreur Recepteur\n"+ e);
	}

    } 
}
