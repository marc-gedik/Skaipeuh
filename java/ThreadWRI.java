import  java.net.*;
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



    public  void myWritter(String msg, Socket s){
	try{
	    
	    msg= "MSG " + "005 "  + msg;
	    byte[] tab =  msg.getBytes();
	    OutputStream oos = new ObjectOutputStream(s.getOutputStream());
	    oos.write(tab);
	    oos.flush();
	}
	catch(Exception e){
	    System.out.println("erreur writter");
	}

    }



    public void run(){
	try{
	    String msg;
	    PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));

	    while(true){
		Scanner sc = new Scanner(System.in);
		msg = sc.nextLine();
			myWritter(msg,s);

		//	System.out.println("j'envoi ça :" + msg);
		//	pw.print(msg);
		//	pw.flush();
	    }
	}
	catch (IOException e) {
	    System.out.println("threadWRI \n"+ e);
	}
    }




}




