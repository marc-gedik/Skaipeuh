import  java.net.*;
import java.io.*;
import java.util.Scanner;


public class ThreadWRI implements Runnable{

    int PORT;
    Socket s; 

    public ThreadWRI(int p , Socket s){
	this.PORT = p;
	this.s=s;
    }

    public void run(){
	try{
	    String msg;
	    PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));

	    while(true){
		Scanner sc = new Scanner(System.in);
		msg = sc.nextLine();
		//	System.out.println("j'envoi ça :" + msg);
		pw.println(msg);
		pw.flush();	
	    }
	}
	catch (IOException e) {
	    System.out.println("threadWRI \n"+ e);
	}
    }
}


