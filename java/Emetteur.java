import java.net.*; 


public class Emetteur {
    
    String IPgroup;
    int PORTgroup;
    
    public Emetteur(String IPgroup, int PORTgroup){
	this.IPgroup = IPgroup;
	this.PORTgroup = PORTgroup;
    }

    public void lance(String msg) { 
	try { 
	    DatagramSocket ds = new DatagramSocket(); 
	    InetAddress ia = InetAddress.getByName(IPgroup); 
	    DatagramPacket dp = 
		new DatagramPacket(msg.getBytes(), 
				   msg.getBytes().length,
				   ia,
				   PORTgroup); 
	    ds.send(dp); 
	    //System.out.println(msg); 
	} catch(Exception e) { 
	    e.printStackTrace(); 
	}
    } 
}
