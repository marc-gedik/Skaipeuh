import  java.net.*;
import java.io.*;

public class ClientTCP implements Runnable{

    int PORT;

    public ClientTCP(int p){
	this.PORT=p;
    }


    public void run(){

	try{
	    Socket s = new Socket("localhost" , PORT);

	    ThreadWRI wri = new ThreadWRI(s);		
	    Thread t1 = new Thread(wri);
	    ThreadREAD read = new ThreadREAD(s);		
	    Thread t2 = new Thread(read);

	    t1.start();
	    t2.start();
	}
	catch(Exception e){
	    System.out.println("Erreur ClientTCP\n"+e);
	}
    }
}


    






/*
  while(true){
  try{
  Socket s = new Socket( "localhost" , PORT);
  Scanner sc = new Scanner(System.in);
  String st = sc.next();
  PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));
  pw.println(st);
  pw.flush();

  BufferedReader bf = new BufferedReader (new InputStreamReader(s.getInputStream()));
  System.out.println(bf.readLine());
  }
  catch(UnknownHostException e ) {
  System.out.println("UnknownHostException");
  }
  catch(IOException e ) {
  System.out.println("IOException Client");
	
  }
  }*/

