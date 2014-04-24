import java.net.*; 


public class Emetteur {
    
    public static void main(String args[]) { 
	try { 
	    String msg = "HLO user machine port"; 
	    DatagramSocket ds = new DatagramSocket(); 
	    InetAddress ia = InetAddress.getByName("224.5.6.7"); 
	    DatagramPacket dp = 
		new DatagramPacket(msg.getBytes(), 
				   msg.getBytes().length,
				   ia,
				   61234); 
	    ds.send(dp); 
	    System.out.println("Envoi du message HLO"); 
	} catch(Exception e) { 
	    e.printStackTrace(); 
	}
    } 
}
