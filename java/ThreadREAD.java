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
	    
	    char[] buff = new char[508];
	    BufferedReader br;
	    int len;
	    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    
	    while(true){
		
		br.read(buff, 0, 3);
		if(buff[0] == 'M' && buff[1] == 'S' && buff[2] == 'G'){
		    br.read();
		    br.read(buff, 0, 3);
		    len = Integer.parseInt(buff[0] + buff[1] + buff[2] + "");
		    br.read(buff, 0, len);
		    for(int i = 0; i < len; i++)
			System.out.print(buff[i]);
		    System.out.println("");
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
