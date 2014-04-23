import  java.net.*;
import java.io.*;
import java.util.Scanner;



public class ThreadWRI implements Runnable{


    int PORT;
    public ThreadWRI(int a){
	this.PORT = a;
    }

    public void run(){
	try{
	    String msg;
	    Socket s = new Socket( "localhost" , PORT);
	    PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));

	    while(true){
		    Scanner sc = new Scanner(System.in);
		    msg = sc.next();
		    System.out.println("j'essai d'envoyer ça :" + msg);
		    pw.println(msg);
		    pw.flush();
		    System.out.println("message envoyé");
		
	    }

	}
	catch (IOException e) {
	    System.out.println("threadWRI \n"+ e);
	}
    }
}


