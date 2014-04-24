import  java.net.*;
import java.io.*;

class ServeurTCP{

    public static void main(String[] args){
	int PORT=0;	
	if(args.length==1){
	    PORT=Integer.parseInt(args[0]);
	}
	else{
	    System.out.println("manque les ports ");
	    System.exit(0);
	}

	try{
	    ServerSocket ss = new ServerSocket(PORT);
	    Socket sRcv, sSent;
	    sSent= ss.accept();
	    sRcv= ss.accept();

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