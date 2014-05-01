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
	    String msg;
	    OutputStream oos = new ObjectOutputStream(s.getOutputStream());
	    Scanner sc = new Scanner(System.in);
	    PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));
	 
	    while(true){
		msg = sc.nextLine();
		
		msg= "MSG " + Bourrage.bourrage(String.valueOf(msg.length()), 3, "0") + " " + msg;
		System.out.println("envoit: "+ msg);
		pw.print(msg);
		pw.flush();
	    }
	}
	catch (IOException e) {
	    System.out.println("threadWRI \n"+ e);
	}
    }
}




