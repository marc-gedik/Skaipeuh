import java.net.*; 

public class Recepteur{
    
    public static void main(String args[]) { 
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
		System.out.println("Message recu:  "+ msg); 
	    } 
	} catch(Exception e) { 
	    e.printStackTrace(); 
	}

    } 
}
