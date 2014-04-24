import java.net.*; 


public class Emetteur {
    String type;
    String user;
    String machine;
    String port;

    public Emetteur(String type, String user, String machine, String port){
	this.type = type;
	this.user=user;
	this.machine=machine;
	this.port=port;
    }


    public void lance() { 
	try { 
	    String msg = type + " "+ user + " " + machine + " " +port; 
	    DatagramSocket ds = new DatagramSocket(); 
	    InetAddress ia = InetAddress.getByName("224.5.6.7"); 
	    DatagramPacket dp = 
		new DatagramPacket(msg.getBytes(), 
				   msg.getBytes().length,
				   ia,
				   61234); 
	    ds.send(dp); 
	    System.out.println(msg); 
	} catch(Exception e) { 
	    e.printStackTrace(); 
	}
    } 
}
