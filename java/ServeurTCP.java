import  java.net.*;
import java.io.*;


class ServeurTCP implements Runnable{

    int PORT;
    static boolean accept = false;

    public ServeurTCP(int p){
	this.PORT = p; 
    }

    public void run(){
	try{
	    ServerSocket ss = new ServerSocket(PORT);
	    Socket sRcv, sSent;
	    sRcv= ss.accept();
	    accept=true;
	    System.out.println("Une personne souhaite vous parler");
	   

	    ClientTCP client = new ClientTCP(Integer.parseInt(PORT+"")); 
	    Thread  t3 = new Thread(client);
	    t3.start();
	    sSent= ss.accept();
	    System.out.println("Connection etablie");



	    ServeurThread read = new ServeurThread(sSent, sRcv);		
	    Thread t1 = new Thread(read);       
	    ServeurThread wri = new ServeurThread(sRcv, sSent);		
	    Thread t2 = new Thread(wri);

	    t1.start();
	    t2.start();
	} 
	catch (IOException e) {
	    System.out.println("Erreur ServeurTCP\n" + e);
	}	
    }
}













/*	text=msg.split(" ");
	if(text[0].equals("GET") && (text[1].charAt(0))=='/' && text[2].startsWith("HTTP/1.")){
	pw.print(message200);
	pw.flush();
			
	}
	       

	while(true){
	msg = br.readLine();

	if(msg.length()==0){
	System.out.println("Ligne vide");

	break;
	}
	}
*/
