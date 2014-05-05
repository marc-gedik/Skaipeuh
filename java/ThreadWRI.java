import java.net.*;
import java.io.*;
import java.lang.*;
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
	    String msg, send, taille;
	    OutputStream oos = new ObjectOutputStream(s.getOutputStream());
	    Scanner sc = new Scanner(System.in);
	    PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));
	    boolean running=true;
	    while(running){
		msg = sc.nextLine();
		taille= String.valueOf(msg.length());
		send= "MSG " + Bourrage.bourrage(taille, 3, "0") + " " + msg;
		pw.print(send);
		pw.flush();
		if(msg.equals("CLO")){		
		    running=false;
		    s.close();
		}
	    }
	}
	catch (IOException e) {
	    System.out.println("threadWRI \n"+ e);
	}
    }
}




