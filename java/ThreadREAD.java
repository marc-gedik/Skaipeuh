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
	    BufferedReader bf = new BufferedReader (new InputStreamReader(s.getInputStream()));
	    while(true){
		//System.out.println("j'écoute");		
		System.out.println(bf.readLine());
	    }
	}
	catch (IOException e) {
	    System.out.println("ThreadREAD\n" + e);
	}
    }
}
