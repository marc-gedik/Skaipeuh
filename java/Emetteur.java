import java.net.*; 


public class Emetteur {
    String type;
    String user;
    String machine;
    String port;
    String msg;

    public Emetteur(String type, String user, String machine, String port){
	this.type = type;
	this.user=user;
	this.machine=machine;
	this.port=port;
	this.msg = type + " "+ user + " " + machine + " " +port; 

    }


    public Emetteur(String type, String user){
	this.type = type;
	this.user=user;
	this.msg = type + " "+ user; 

    }


    public Emetteur(String type){
	this.type = type;
	this.msg = type ; 

    }

    public void lance() { 
	try { 
	    DatagramSocket ds = new DatagramSocket(); 
	    InetAddress ia = InetAddress.getByName("224.5.6.7"); 
	    DatagramPacket dp = 
		new DatagramPacket(msg.getBytes(), 
				   msg.getBytes().length,
				   ia,
				   9876); 
	    ds.send(dp); 
	    //System.out.println(msg); 
	} catch(Exception e) { 
	    e.printStackTrace(); 
	}
    } 
}
