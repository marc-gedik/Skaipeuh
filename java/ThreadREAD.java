import  java.net.*;
import java.io.*;

public class ThreadREAD implements Runnable{	
   
    int PORT;
    Socket s;
   
    public ThreadREAD(int  p, Socket s){
	this.s=s;
	this.PORT=p;
    }

 
    public void run(){
	try{
	    InputStream bf = null;
	
	    bf = s.getInputStream();

	    byte[] b = new byte[508];
	    while(true){
		
		bf.read(b, 0, 4);
		if(b[0] == 'M' && b[1] == 'S' && b[2] == 'G'){
		    bf.read(b, 0, 4);
		    for(int i =0; i < 3; i++)
			System.out.print(":" + b[0] + " ");
		}
		
		//System.out.println("j'écoute");		
		System.out.println("");
	    }
	}
	catch (IOException e) {
	    System.out.println("Erreur ThreadREAD\n" + e);
	}
    }
}
