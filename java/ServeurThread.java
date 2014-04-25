import  java.net.*;
import java.io.*;
import java.util.Scanner;




public class ServeurThread implements Runnable{

    Socket s1;
    Socket s2;
    
    public ServeurThread(Socket s1, Socket s2 ){
	this.s1=s1;
	this.s2=s2;
    }
    
    
    public void run(){
	try{

	    BufferedReader br;
	    PrintWriter pw;
	    String msg;
	    br = new BufferedReader(new InputStreamReader(s1.getInputStream()));
	    pw = new PrintWriter(new OutputStreamWriter(s2.getOutputStream()));
	    
	    while(true){
		System.out.println("Serveur en attente ...");
		msg = br.readLine();
		//	System.out.println(msg);	        
		pw.println(msg);
		pw.flush();
	    }
	}
	catch(java.io.IOException e){
	    System.out.println("Erreur ServeurThread\n"+e);
	}
    }
}
