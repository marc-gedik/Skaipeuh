import  java.net.*;
import java.io.*;

class Clicli{

    public static void main(String[] args){
	int PORT = 0;
	if(args.length==1){
	    PORT=Integer.parseInt(args[0]);	   
	}
	else{
	    System.out.println("manque le port ");
	    System.exit(0);
	}

	try{
	    Socket s = new Socket("localhost" , PORT);
	    
	    ThreadWRI wri = new ThreadWRI(PORT,s);		
	    Thread t1 = new Thread(wri);
	    ThreadREAD read = new ThreadREAD(PORT, s);		
	    Thread t2 = new Thread(read);

	    t1.start();
	    t2.start();
	}
	catch(Exception e){
	    System.out.println("Erreur ClientTCP\n"+e);
	}
    }
}
